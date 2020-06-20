package com.facc.projects.stellar

import cats.effect._
import com.facc.projects.stellar.http.RestScalaClient._
import com.facc.projects.stellar.model.PaymentResponse
import io.circe.generic.auto._
import org.http4s._
import org.http4s.circe.jsonOf

object StellarProcessor extends App {

  import com.facc.projects.stellar.model.Config._

  import scala.concurrent.ExecutionContext.global
  implicit val cs: ContextShift[IO] = IO.contextShift(global)
  implicit val timer: Timer[IO] = IO.timer(global)

  val baseUri = Uri.fromString(stellarUrl).right.getOrElse(throw new Exception("Wrong url. Correct it"))

  val endpoint = baseUri.withPath("/ledgers/696962/payments")

  val stellarResponse = withIOClient.use{client =>
    client.expect[PaymentResponse](endpoint)(jsonOf[IO, PaymentResponse])
  }.unsafeRunSync()

  println(stellarResponse)

}
