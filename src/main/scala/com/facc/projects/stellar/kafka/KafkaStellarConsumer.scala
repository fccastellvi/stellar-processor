package com.facc.projects.stellar.kafka


import java.util

import org.apache.kafka.clients.consumer.KafkaConsumer
import java.util.Properties
import scala.collection.JavaConverters._

case class KafkaStellarConsumer(kafkaConsumer: KafkaConsumer[String, String]) {
  def consumeFromKafka(topic: String): Unit = {
    kafkaConsumer.subscribe(util.Arrays.asList(topic))
    while (true) {
      val record = kafkaConsumer.poll(1000).asScala
      record.foreach(x => println(x.value()))
    }
  }
}

object KafkaStellarConsumer {

  def getKafkaConsumerClient: KafkaConsumer[String, String] = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("auto.offset.reset", "earliest")
    props.put("group.id", "consumer-group")

    new KafkaConsumer[String, String](props)
  }

  def main(args: Array[String]): Unit = {
    //    TOPIC=stellar-transactions
    val topic = sys.env.getOrElse("TOPIC", throw new Exception("Please define TOPIC env variable"))
    val kafkaConsumerClient = getKafkaConsumerClient
    KafkaStellarConsumer(kafkaConsumerClient).consumeFromKafka(topic)
    kafkaConsumerClient.close()
  }
}

