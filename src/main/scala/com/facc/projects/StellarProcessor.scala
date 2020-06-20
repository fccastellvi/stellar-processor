package com.facc.projects

import cats.effect._
import com.facc.projects.model.StellarPaymentResponse
import io.circe.generic.auto._
import org.http4s._
import org.http4s.circe.jsonOf
import org.http4s.client.blaze.BlazeClientBuilder

object StellarProcessor extends App {

  import scala.concurrent.ExecutionContext.global
  implicit val cs: ContextShift[IO] = IO.contextShift(global)
  implicit val timer: Timer[IO] = IO.timer(global)

//  "https://horizon-testnet.stellar.org/ledgers/696963/payments"

  val baseUri = Uri.fromString("https://horizon-testnet.stellar.org")

  val endpoint = baseUri.right.get.withPath("/ledgers/696962/payments")

  val stellarResponse = BlazeClientBuilder[IO](global).resource.use { client =>
    client.expect[StellarPaymentResponse](endpoint)(jsonOf[IO, StellarPaymentResponse])
  }.unsafeRunSync()

//  println("printing")
  println(stellarResponse)


}
