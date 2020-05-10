#!/usr/bin/env bash

source ../env.sh

$KAFKA_HOME/bin/kafka-topics --delete --topic streams-plaintext-input --bootstrap-server localhost:9092