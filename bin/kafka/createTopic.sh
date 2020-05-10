#!/usr/bin/env bash

source ../env.sh

# Create the input topic
$KAFKA_HOME/bin/kafka-topics --create \
      --bootstrap-server localhost:9092 \
      --replication-factor 1 \
      --partitions 1 \
      --topic stellar-transactions

$KAFKA_HOME/bin/kafka-topics --list --bootstrap-server localhost:9092
