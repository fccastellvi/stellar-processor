FROM gradle:6.1-jdk8 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle shadowJar

FROM openjdk:8
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/stellar-processor_2.11-1.0-SNAPSHOT.jar /app/stellar-processor_2.11-1.0-SNAPSHOT.jar
EXPOSE 443
ENTRYPOINT ["java", "-cp", "/app/stellar-processor_2.11-1.0-SNAPSHOT.jar", "com.facc.projects.stellar.kafka.KafkaStellarProducer"]