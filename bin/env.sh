#!/usr/bin/env bash

ROOT_DIR=$(git rev-parse --show-toplevel)
export KAFKA_HOME="$ROOT_DIR/kafka/confluent-5.5.0"