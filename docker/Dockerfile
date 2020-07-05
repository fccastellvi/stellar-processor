FROM openjdk:8 AS kafka-stellar-producer
COPY ./build/libs/stellar-processor_2.11-1.0-SNAPSHOT.jar /usr/app/
WORKDIR /usr/app
EXPOSE 443
ENTRYPOINT ["java", "-cp", "stellar-processor_2.11-1.0-SNAPSHOT.jar", "com.facc.projects.stellar.kafka.KafkaStellarProducer"]

FROM openjdk:8 AS kafka-stellar-consumer
COPY ./build/libs/stellar-processor_2.11-1.0-SNAPSHOT.jar /usr/app/
WORKDIR /usr/app
EXPOSE 443
ENTRYPOINT ["java", "-cp", "stellar-processor_2.11-1.0-SNAPSHOT.jar", "com.facc.projects.stellar.kafka.KafkaStellarConsumer"]