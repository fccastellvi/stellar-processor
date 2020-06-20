package com.facc.projects.stellar.http

import java.util.concurrent.TimeUnit

import cats.effect.{ContextShift, IO, Resource, Timer}
import org.http4s.{BasicCredentials, Headers, MediaRange, Method, Request, Uri, UrlForm}
import org.http4s.client.Client
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.headers.{Accept, Authorization}
import org.http4s.circe.jsonOf

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.global
import scala.concurrent.duration.FiniteDuration
import io.circe.generic.auto._
import cats.implicits._
import com.facc.projects.stellar.model.AuthResponse

object RestScalaClient {

  val defaultDuration = FiniteDuration(2, TimeUnit.SECONDS)
  implicit val timer: Timer[IO] = IO.timer(ExecutionContext.global)

  val defaultIdleTimeout = FiniteDuration(10, TimeUnit.SECONDS)

  implicit val cs: ContextShift[IO] = IO.contextShift(global)

  def withIOClient: Resource[IO, Client[IO]] = {
    BlazeClientBuilder[IO](scala.concurrent.ExecutionContext.global)
      .withResponseHeaderTimeout(15 seconds)
      .withRequestTimeout(15 seconds)
      .withCheckEndpointAuthentication(false)
      .resource
  }

  def login(uri: Uri, credentials: BasicCredentials, retries: Int = 3, client: Client[IO]): IO[AuthResponse] = {
    val loginRequest = {
      Request[IO](
        method = Method.POST,
        uri = uri,
        headers = Headers(Authorization(credentials), Accept(MediaRange.`application/*`))
      ).withBody(UrlForm("grant_type" -> "client_credentials"))
    }
    val token = client.expect[AuthResponse](loginRequest)(jsonOf[IO, AuthResponse])
    retryWithBackoff(token, defaultDuration, retries)
  }

  def getRequest[T: io.circe.Decoder](uri: Uri,
                                      retries: Int = 3,
                                      client: Client[IO]): IO[Option[T]] = {
    val getBlockDetails = {
      Request[IO](
        method = Method.GET,
        uri = uri,
        headers = Headers(Accept(MediaRange.`application/*`))
      )
    }
    val detailedBlock = client.expectOption[T](getBlockDetails)(jsonOf[IO, T])
    retryWithBackoff(detailedBlock, defaultDuration, retries)
  }

  def retryWithBackoff[A](ioa: IO[A], initialDelay: FiniteDuration, maxRetries: Int)
                         (implicit timer: Timer[IO]): IO[A] = {

    ioa.handleErrorWith { error =>
      if (maxRetries > 0)
        IO.sleep(initialDelay) *> retryWithBackoff(ioa, initialDelay * 2, maxRetries - 1)
      else
        IO.raiseError(error)
    }
  }
}
