#!/usr/bin/env bash

ROOT_DIR=$(git rev-parse --show-toplevel)

source ${ROOT_DIR}/bin/env.sh

#Download the zip file for confluence platform: curl -O http://packages.confluent.io/archive/5.5/confluent-5.5.0-2.11.zip and place it to /root/project/kafka/confluent-5.5.0

# Start ZooKeeper.  Run this command in its own terminal.
nohup $KAFKA_HOME/bin/zookeeper-server-start -daemon $KAFKA_HOME/etc/kafka/zookeeper.properties > $KAFKA_HOME/logs/zookeper_nohup.log &

#Sleep a while till zookeper is up
sleep 5

# Start Kafka.  Run this command in its own terminal
nohup ${KAFKA_HOME}/bin/kafka-server-start -daemon $KAFKA_HOME/etc/kafka/server.properties > $KAFKA_HOME/logs/kafka_nohup.log &

#Creating stellar-transactions topic
sh $ROOT_DIR/bin/kafka/createTopic.sh