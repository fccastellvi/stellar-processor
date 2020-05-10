#!/usr/bin/env bash

ps -ef | egrep -ie "kafka|zookeper" | grep -v grep | awk '{print $2}' | xargs kill -9