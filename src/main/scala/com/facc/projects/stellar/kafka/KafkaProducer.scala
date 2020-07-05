package com.facc.projects.stellar.kafka

import java.util.Properties

import cats.effect.IO
import com.facc.projects.stellar.model.Config.stellarTxTopic
import com.facc.projects.stellar.model.VTE
import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}
import org.json4s.{DefaultFormats, jackson}

import scala.collection.JavaConverters._

object KafkaProducer {
  def main(args: Array[String]): Unit = {

    case class ValueTransferEvent(from: String, to: String, value: Int)
    val message = Seq(ValueTransferEvent("A", "B", 2), ValueTransferEvent("B", "C", 3))
    writeToKafka("test", "test", message)
  }

  def writeToKafka[T <: AnyRef](topic: String, key: String, message: T): Unit = {

    implicit val serialization = jackson.Serialization
    implicit val formats = DefaultFormats

    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val producer = new KafkaProducer[String, String](props)
    val jsonMessage = serialization.write[T](message)
    val record = new ProducerRecord[String, String](topic, key, jsonMessage)

    producer.send(record, new Callback() {
      def onCompletion(metadata: RecordMetadata, e: Exception) {
        if (e != null) {
          e.printStackTrace();
        } else {
          println(s"The offset of the record we just sent is: ${metadata.offset} with json message: ${jsonMessage}")
        }
      }
    }
    )
    producer.close()
  }

  def writeVteToKafka(vte: VTE): IO[Unit] = {
    IO(writeToKafka(stellarTxTopic, vte.transaction_hash, vte))
  }

}
