package com.facc.projects.stellar.kafka

import java.util.Properties

import cats.effect.IO
import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}
import org.json4s.{DefaultFormats, jackson}

case class KafkaStellarProducer(kafkaProducer: KafkaProducer[String, String]) {

  implicit val serialization = jackson.Serialization
  implicit val formats = DefaultFormats

  def writeToKafka[V <: AnyRef](topic: String, key: String, message: V): IO[Unit] = {
    val jsonMessage = serialization.write[V](message)
    val record = new ProducerRecord[String, String](topic, key, jsonMessage)
    IO {
      kafkaProducer.send(record, new Callback() {
        def onCompletion(metadata: RecordMetadata, e: Exception) {
          if (e != null)
            e.printStackTrace();
          else
            println(s"The offset of the record we just sent is: ${metadata.offset} with json message: ${jsonMessage}")
        }
      }
      )
    }
  }
}

object KafkaStellarProducer {

  def getKafkaClient: KafkaProducer[String, String] = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    new KafkaProducer[String, String](props)
  }

  def main(args: Array[String]): Unit = {
    case class ValueTransferEvent(from: String, to: String, value: Int)
    val message = Seq(ValueTransferEvent("A", "B", 2), ValueTransferEvent("B", "C", 3))
    val kafkaClient = getKafkaClient
    KafkaStellarProducer(kafkaClient).writeToKafka("test", "test", message)
  }
}
