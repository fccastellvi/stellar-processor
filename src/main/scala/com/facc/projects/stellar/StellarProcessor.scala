package com.facc.projects.stellar

import com.facc.projects.stellar.http.RestScalaClient._
import com.facc.projects.stellar.kafka.KafkaProducer._
import com.facc.projects.stellar.rpc.StellarRpc

object StellarProcessor extends App {

  val startBlock = 696962

  val stellarResponse = withIOClient.use { client =>
    val stellarRpc = StellarRpc(client)
    stellarRpc
      .getStreamFrom(startBlock)
      .evalMap(writeToKafkaWrapper)
      .compile
      .toVector
  }.unsafeRunSync()
}
