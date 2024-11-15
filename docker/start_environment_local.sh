#!/usr/bin/env bash
docker-compose up -d
#docker stop zookeeper-local
##docker rm zookeeper-local
#docker stop kafka-local
##docker rm kafka-local
#docker run -d --rm -e ALLOW_ANONYMOUS_LOGIN=yes -p 2181:2181 --name zookeeper-local bitnami/zookeeper:latest
#docker run -d --rm -p 9092:9092 -e KAFKA_ZOOKEEPER_CONNECT=localhost:2181 \
#    -e ALLOW_PLAINTEXT_LISTENER=yes \
#    -e KAFKA_LISTENERS=SASL_PLAINTEXT://:9092 \
#    -e KAFKA_ADVERTISED_LISTENERS=SASL_PLAINTEXT://:9092 \
#    -e KAFKA_BROKER_USER=cautelar \
#    -e KAFKA_BROKER_PASSWORD=teste \
#    --network host --name kafka-local bitnami/kafka:latest
