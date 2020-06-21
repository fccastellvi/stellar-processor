package com.facc.projects.stellar

import cats.effect._
import com.facc.projects.stellar.http.RestScalaClient._
import com.facc.projects.stellar.rpc.StellarRpc

object StellarProcessor extends App {

  val startBlock = 696962

  val stellarResponse = withIOClient.use{client =>
    val stellarRpc = StellarRpc(client)
   stellarRpc
      .getStreamFrom(startBlock)
      .evalMap(x => IO(println(x)))
      .compile
      .toVector
  }.unsafeRunSync()
}
