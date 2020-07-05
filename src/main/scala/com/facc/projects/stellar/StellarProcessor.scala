package com.facc.projects.stellar

import cats.effect.IO
import com.facc.projects.stellar.http.RestScalaClient._
import com.facc.projects.stellar.kafka.KafkaProducer._
import com.facc.projects.stellar.rpc.StellarRpc

object StellarProcessor extends App {

  //696962
  val startBlock = sys.env.getOrElse("START_HEIGHT", throw new Exception("Please define START_HEIGHT env variable")).toLong

  println(s"Start height of: $startBlock")

  val stellarResponse = withIOHttpClient.use { client =>
    val stellarRpc = StellarRpc(client)
    stellarRpc
      .getStreamFrom(startBlock)
      .evalMap(writeVteToKafka)
      .compile
      .toVector
  }.handleErrorWith{
    case e:Exception => IO(println(s"Failed to write to Kafka: ${e.getMessage}"))
  }.unsafeRunSync()
}
