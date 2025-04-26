Tasks:
Apply checks or constraints on fields, include multiple & custom field classes
Add kafka testcontainers
kafka with 3 brokers

Start & Stop Zookeeper:2181
-- C:\kafka_2.13-3.8.1\bin\windows>zookeeper-server-start.bat ../../config/zookeeper.properties
-- C:\kafka_2.13-3.8.1\bin\windows>zookeeper-server-stop.bat

Start & Stop Kafka Server/Broker:9092
-- C:\kafka_2.13-3.8.1\bin\windows>kafka-server-start.bat ../../config/server.properties
-- C:\kafka_2.13-3.8.1\bin\windows>kafka-server-stop.bat

Create new topic: default partition 1, replication factor is 3
-- bin\windows\kafka-topics.bat --create --topic user-topic --bootstrap-server localhost:9092
-- bin\windows\kafka-topics.bat --bootstrap-server localhost:9092 --list/--describe --topic user-topic

Produce new topic
-- bin\windows\kafka-console-producer.bat --topic user-topic --bootstrap-server localhost:9092
-- bin\windows\kafka-console-producer.bat --topic user-topic --broker-list localhost:9092,localhost9093

Consuming message
-- bin\windows\kafka-console-consumer.bat --topic user-topic --from-beginning --bootstrap-server localhost:9092

Send CSV File data to kafka
-- bin/kafka-console-producer --broker-list localhost:9092 --topic NewTopic1 <bin/customers.csv

[//]: # (_customer_offset) : stores date about offset of each consumer for each server

Notes:
-- 1 broker, 1 prod, 1 consumer, 1 topic with 3 partition & RF 1, group coordinator decides where data will go but first in partition 0 always then partition 1, (consuming in round-robin fashion sometimes)
-- we can run after kafka 2.8 without zookeeper (after 4.0 zookeeper removed), as we can store data in server in topic partition.
https://www.youtube.com/watch?v=k_JbBYjZ6G4&list=PLVz2XdJiJQxwpWGoNokohsSW2CysI6lDc&index=5
-- https://github.com/Java-Techie-jt/kafka-installation
### Run Docker Compose
```docker-compose -f docker-compose.yml up -d```
### Move into Kafka container
```docker exec -it <kafka_conatiner_id> /bin/sh``` or ```docker exec -it kafka /bin/sh```
### Go inside kafka installation folder
```cd /opt/kafka_<version>/bin```
### Create Kafka topic
```kafka-topics.sh --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 1 --topic quickstart```
### Start Producer app (CLI)
```kafka-console-producer.sh --topic quickstart --bootstrap-server localhost:9092```
### Start consumer app (CLI)
```kafka-console-consumer.sh --topic quickstart --from-beginning --bootstrap-server localhost:9092```
### Del records from partition
-- kafka-delete-records --bootstrap-server localhost:9092 --offset-json-file delete-records.json
-- kafka-topics.sh --bootstrap-server localhost:9092 --topic customer --delete

-- in spring boot if no NewTopic config, default is partition 1 & RF 1. if we send data in 4 partition we can't control how much data will go in which partition, some might be empty (zookeeper decides)
-- if we want data in order then use only one partition. (how to send & read data from single partition? and how to define read from specific offset?)
-- multiple consumer, use command line args to define diff group-ids. we can interchange producer-consumer apps?
-- one consumer for 3 partition is not good for better throughput, & also if all partition assigned to each consumer then extra consumer will be idle (until another consumer dies, consumer rebalancing)
-- Bydefault SERDE is byte arr or String.class so we need to specify Json for sending custom objects to topics
