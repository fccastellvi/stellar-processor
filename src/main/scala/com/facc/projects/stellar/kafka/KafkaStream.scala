package com.facc.projects.stellar.kafka

import java.time.Duration
import java.util.Properties

import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream._
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import com.facc.projects.stellar.model.Config.kafkaHostname
import com.facc.projects.stellar.model.StellarVTE
import org.json4s.{DefaultFormats, jackson}

object KafkaStream extends App {

  //Example: https://github.com/confluentinc/kafka-streams-examples/blob/5.5.0-post/src/main/scala/io/confluent/examples/streams/WordCountScalaExample.scala

  implicit val serialization = jackson.Serialization
  implicit val formats = DefaultFormats

  import org.apache.kafka.streams.scala.Serdes._
  import org.apache.kafka.streams.scala.ImplicitConversions._

  val config: Properties = {
    val p = new Properties()
    p.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-scala-application")
    val bootstrapServers = s"$kafkaHostname:9092"
    p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
    p
  }

  val builder = new StreamsBuilder()
  val vtes: KStream[String, String] = builder.stream[String, String]("stellar-transactions")

  val parsedVTE = vtes.mapValues(serialization.read[StellarVTE](_))

  parsedVTE.foreach((x, y) => println(s"Key $x with value ${y.toString}"))

  parsedVTE.mapValues(serialization.write(_)).to("stellar-aggregations")

  val streams: KafkaStreams = new KafkaStreams(builder.build(), config)

  streams.cleanUp()

  streams.start()

  sys.ShutdownHookThread {
    streams.close(Duration.ofSeconds(10))
  }

}
