package com.facc.projects.stellar

import cats.effect._
import com.facc.projects.stellar.http.RestScalaClient._
import com.facc.projects.stellar.rpc.StellarRpc

object StellarProcessor extends App {

  import scala.concurrent.ExecutionContext.global
  implicit val cs: ContextShift[IO] = IO.contextShift(global)
  implicit val timer: Timer[IO] = IO.timer(global)

  val startBlock = 696962

  println(s"Getting payments for block $startBlock")

  val stellarResponse = withIOClient.use{client =>
    val stellarRpc = StellarRpc(client)
   stellarRpc
      .getStreamFrom(startBlock)
      .evalMap(x => IO(println(x)))
      .compile
      .toVector
  }.unsafeRunSync()
}
