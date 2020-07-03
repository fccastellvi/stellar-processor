COPY ./build/libs/stellar-processor_2.11-1.0-SNAPSHOT.jar /usr/app/
WORKDIR /usr/app
EXPOSE 443
ENTRYPOINT ["java", "-jar", "stellar-processor_2.11-1.0-SNAPSHOT.jar"]