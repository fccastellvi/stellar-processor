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
    val typesafeConfig =
      ConfigFactory
        .load
        .getConfig("config")
        .as[Config]
        .fold(
          error => throw new Exception(s"Failed parsing config: ${error.getMessage}"),
          x => x
        )

//    } match {
//      case Success(value) => value
//      case Failure(exception) => throw new Exception(s"Failed parsing config: ${exception.getMessage}")
//    }
    typesafeConfig
  }

  val config: Config = getConfig
  val stellarUrl: String = config.stellar.url
  lazy val startBlock = sys.env.getOrElse("START_HEIGHT", throw new Exception("Please define START_HEIGHT env variable")).toLong
  lazy val topic = sys.env.getOrElse("TOPIC", throw new Exception("Please define TOPIC env variable"))
  lazy val kafkaHostname = sys.env.getOrElse("KAFKA_HOST", throw new Exception("Please define the kafka host env variable"))


  val baseUrl: Uri = Uri.fromString(stellarUrl).right.getOrElse(throw new Exception("Wrong url. Correct it"))

}