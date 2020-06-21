package com.facc.projects.stellar.model

import com.typesafe.config.ConfigFactory
import io.circe.config.syntax._
import io.circe.generic.auto._
import org.http4s.Uri

import scala.util.{Failure, Success, Try}

case class Config(kafka: KafkaConfig, stellar: StellarConfig)

case class StellarConfig(url: String)

case class KafkaConfig(stellarTxTopic: String)


object Config {
  def getConfig: Config = {
    val typesafeConfig = Try {
      ConfigFactory
        .load
        .getConfig("config")
        .as[Config]
        .right.get
    } match {
      case Success(value) => value
      case Failure(exception) => throw new Exception(s"Failed parsing config: ${exception.getMessage}")
    }
    typesafeConfig
  }

  val config: Config = getConfig
  val stellarUrl: String = config.stellar.url
  val stellarTxTopic: String = config.kafka.stellarTxTopic

  val baseUrl: Uri = Uri.fromString(stellarUrl).right.getOrElse(throw new Exception("Wrong url. Correct it"))

}