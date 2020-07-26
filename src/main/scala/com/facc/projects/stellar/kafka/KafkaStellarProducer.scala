package com.facc.projects.stellar.kafka

import java.util.Properties

import cats.effect.IO
import com.facc.projects.stellar.http.RestScalaClient.withIOHttpClient
import com.facc.projects.stellar.rpc.StellarRpc
import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}
import org.json4s.{DefaultFormats, jackson}
import com.facc.projects.stellar.model.Config.kafkaHost

case class KafkaStellarProducer(kafkaProducer: KafkaProducer[String, String]) {

  implicit val serialization = jackson.Serialization
  implicit val formats = DefaultFormats

  def writeToKafka[V <: AnyRef](topic: String, key: String, message: V): IO[Unit] = {
    val jsonMessage = serialization.write[V](message)
    val record = new ProducerRecord[String, String](topic, key, jsonMessage)
    IO {
      kafkaProducer.send(record, new Callback() {
        def onCompletion(metadata: RecordMetadata, e: Exception) {
          if (e != null)
            e.printStackTrace();
          else
            println(s"The offset of the record we just sent is: ${metadata.offset} with json message: ${jsonMessage}")
        }
      }
      )
    }
  }
}

object KafkaStellarProducer {

  def getKafkaProducerClient: KafkaProducer[String, String] = {
    val props = new Properties()
    props.put("bootstrap.servers", s"$kafkaHost:9092")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    new KafkaProducer[String, String](props)
  }

  def main(args: Array[String]): Unit = {
    //696962
    val startBlock = sys.env.getOrElse("START_HEIGHT", throw new Exception("Please define START_HEIGHT env variable")).toLong
    val topic = sys.env.getOrElse("TOPIC", throw new Exception("Please define TOPIC env variable"))

    println(s"Putting records into topic $topic with a start height of: $startBlock")

    val kafkaProducerClient = getKafkaProducerClient

    withIOHttpClient.use { client =>
      val stellarRpc = StellarRpc(client)
      stellarRpc
        .getStreamFrom(startBlock)
        .evalMap(vte => KafkaStellarProducer(kafkaProducerClient).writeToKafka("stellar-transactions", vte.transaction_hash, vte))
        .compile
        .toVector
    }.handleErrorWith {
      case e: Exception => IO(println(s"Failed to write to Kafka: ${e.getMessage}"))
    }.unsafeRunSync()

    kafkaProducerClient.close()
  }
}
