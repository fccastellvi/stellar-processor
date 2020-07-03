package com.facc.projects.stellar

import com.facc.projects.stellar.http.RestScalaClient._
import com.facc.projects.stellar.kafka.KafkaProducer._
import com.facc.projects.stellar.rpc.StellarRpc

object StellarProcessor extends App {

  //696962
  val startBlock = System.getenv("START_HEIGHT").toLong

  println(s"Start height of: $startBlock")

  val stellarResponse = withIOHttpClient.use { client =>
    val stellarRpc = StellarRpc(client)
    stellarRpc
      .getStreamFrom(startBlock)
      .evalMap(writeVteToKafka)
      .compile
      .toVector
  }.unsafeRunSync()
}
