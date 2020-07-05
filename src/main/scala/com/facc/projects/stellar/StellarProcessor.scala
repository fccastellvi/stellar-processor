package com.facc.projects.stellar

import cats.effect.IO
import com.facc.projects.stellar.http.RestScalaClient._
import com.facc.projects.stellar.kafka.KafkaStellarProducer
import com.facc.projects.stellar.kafka.KafkaStellarProducer._
import com.facc.projects.stellar.rpc.StellarRpc

object StellarProcessor extends App {

  //696962
  val startBlock = sys.env.getOrElse("START_HEIGHT", throw new Exception("Please define START_HEIGHT env variable")).toLong
  val topic = sys.env.getOrElse("TOPIC", throw new Exception("Please define TOPIC env variable"))

  println(s"Putting records into topic $topic with a start height of: $startBlock")

  val kafkaProducerClient = getKafkaProducerClient

  val stellarResponse = withIOHttpClient.use { client =>
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
