#!/usr/bin/env bash

ROOT_DIR=$(git rev-parse --show-toplevel)

source ${ROOT_DIR}/bin/env.sh

$KAFKA_HOME/bin/kafka-topics --list --bootstrap-server localhost:9092