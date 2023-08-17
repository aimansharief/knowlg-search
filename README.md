# knowledge-search

Repository for Knowledge Search - 1.0
## Knowledge-search-jobs local setup
This readme file contains the instruction to set up and run the knowledge-search-jobs in local machine.

### System Requirements:

### Prerequisites:
* Java 11

### Prepare folders for database data and logs

```shell
mkdir -p ~/sunbird-dbs/es ~/sunbird-dbs/kafka
export sunbird_dbs_path=~/sunbird-dbs
```


### Elasticsearch database setup in docker:
```shell
docker run --name sunbird_es -d -p 9200:9200 -p 9300:9300 \
-v $sunbird_dbs_path/es/data:/usr/share/elasticsearch/data \
-v $sunbird_dbs_path/es/logs://usr/share/elasticsearch/logs \
-v $sunbird_dbs_path/es/backups:/opt/elasticsearch/backup \
 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:6.8.22

```
> --name -  Name your container (avoids generic id)
>
> -p - Specify container ports to expose
>
> Using the -p option with ports 7474 and 7687 allows us to expose and listen for traffic on both the HTTP and Bolt ports. Having the HTTP port means we can connect to our database with Neo4j Browser, and the Bolt port means efficient and type-safe communication requests between other layers and the database.
>
> -d - This detaches the container to run in the background, meaning we can access the container separately and see into all of its processes.
>
> -v - The next several lines start with the -v option. These lines define volumes we want to bind in our local directory structure so we can access certain files locally.
>
> --env - Set config as environment variables for Neo4j database
>

### Running kafka using docker:
1. Kafka stores information about the cluster and consumers into Zookeeper. ZooKeeper acts as a coordinator between them. we need to run two services(zookeeper & kafka), Prepare your docker-compose.yml file using the following reference.
```shell
version: '3'

services:
  zookeeper:
    image: 'wurstmeister/zookeeper:latest'
    container_name: zookeeper
    ports:
      - "2181:2181"    
    environment:
      - KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://127.0.0.1:2181     
    
  kafka:
    image: 'wurstmeister/kafka:2.11-1.0.1'
    container_name: kafka
    ports:
      - "9092:9092"
    environment:
      - KAFKA_BROKER_ID=1
      - KAFKA_LISTENERS=PLAINTEXT://:9092
      - KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://127.0.0.1:9092
      - KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181      
      - ALLOW_PLAINTEXT_LISTENER=yes
    depends_on:
      - zookeeper  
```
2. Go to the path where docker-compose.yml placed and run the below command to create and run the containers (zookeeper & kafka).
```shell
docker-compose -f docker-compose.yml up -d
```
3. To start kafka docker container shell, run the below command.
```shell
docker exec -it kafka sh
```
Go to path /opt/kafka/bin, where we will have executable files to perform operations(creating topics, running producers and consumers, etc).
Example:
```shell
kafka-topics.sh --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 1 --topic test_topic 
```

### Steps to start a job in debug or development mode using IntelliJ:
1. Navigate to downloaded repository folder (knowlg-search/search-job/) and run below command.
```shell
mvn clean install -DskipTests
``` 
2. Open the project in IntelliJ.
3. Navigate to the target job folder ( ../knowlg-search/search-job/search-indexer) and edit the 'pom.xml' to add below dependency.
```shell
<dependency>
  <groupId>org.apache.flink</groupId>
  <artifactId>flink-clients_${scala.version}</artifactId>
  <version>${flink.version}</version>
</dependency>
```
4. Comment "provided" scope from flink-streaming-scala_${scala.version} artifact dependency in the job's 'pom.xml'.
```shell
<dependency>
    <groupId>org.apache.flink</groupId>
    <artifactId>flink-streaming-scala_${scala.version}</artifactId>
    <version>${flink.version}</version>
<!--            <scope>provided</scope>-->
</dependency>
```
5. Comment the default flink StreamExecutionEnvironment in the job's StreamTask file ( SearchIndexerStreamTask.scala) and add code to create local StreamExecutionEnvironment.
```shell
//    implicit val env: StreamExecutionEnvironment = FlinkUtil.getExecutionContext(config)
      implicit val env: StreamExecutionEnvironment = StreamExecutionEnvironment.createLocalEnvironment()
```
6. Save cloud storage related environment variables in StreamTask environment variables.
7. Start all databases, zookeper and kafka containers in docker
8. Run the StreamTask (Normal or Debug)
9. Open a terminal, connect to kafka docker container and produce the target job topic.
```shell
docker exec -it kafka_container_id sh
kafka-console-producer.sh --broker-list kafka:9092 --topic sunbirddev.learning.graph.events
```


## Steps for running jobs in Flink locally:-
### Running flink :
1. Download the Apache flink
```shell
wget https://dlcdn.apache.org/flink/flink-1.12.7/flink-1.12.7-bin-scala_2.12.tgz
```
2. Extract the downloaded folder
```shell
tar xzf flink-1.12.7-bin-scala_2.12.tgz
```
3. Change the directory & Start the flink cluster.
```shell
cd flink-1.12.7
./bin/start-cluster.sh
```
4. Open web view to check jobmanager and taskmanager
```shell
localhost:8081
```

### Setting up Cloud storage connection:
Setup cloud storage specific variables as environment variables.
```shell
export cloud_storage_type=  #values can be 'aws' or 'azure'

For AWS Cloud Storage connectivity: 
export aws_storage_key=
export aws_storage_secret=
export aws_storage_container=

For Azure Cloud Storage connectivity:
export azure_storage_key=
export azure_storage_secret=
export azure_storage_container=

export content_youtube_apikey= #key to fetch metadata of youtube videos

```
### Running job in Flink:
1. Navigate to the required job folder (Example: ../knowlg-search/search-job/search-indexer) and run the below maven command to build the application.
```shell
mvn clean install -DskipTests
``` 
2. Start all databases, zookeper and kafka containers in docker
3. Start flink (if not started) and submit the job to flink. Example:
```shell
cd flink-1.12.7
./bin/start-cluster.sh
./bin/flink run -m localhost:8081 /user/test/workspace/knowlg-search/search-job/search-indexer/target/search-indexer-1.0.0.jar
```
4. Open a terminal, connect to kafka docker container and produce the target job topic.
```shell
docker exec -it kafka_container_id sh
kafka-console-producer.sh --broker-list kafka:9092 --topic sunbirddev.learning.graph.events
```

## Knowledge-search-api local setup 
This readme file contains the instruction to set up and run the content-service in local machine.

### System Requirements:

### Prerequisites:
* Java 11
* Docker, Docker Compose


## One step installation 

1. Go to Root folder (knowledge-search/search-api)
2. Run "local-setup.sh" file
``` shell
sh ./local-setup.sh
```
 
 This will install all the requied dcoker images & local folders for DB mounting.




## Manual steps to install all the dependents
Please follow the manual steps in [One step installation](#one-step-installation) is failed.

### Prepare folders for database data and logs

```shell
mkdir -p ~/sunbird-dbs/neo4j ~/sunbird-dbs/cassandra ~/sunbird-dbs/redis ~/sunbird-dbs/es ~/sunbird-dbs/kafka
export sunbird_dbs_path=~/sunbird-dbs
```

### Running kafka using docker:
1. Kafka stores information about the cluster and consumers into Zookeeper. ZooKeeper acts as a coordinator between them. we need to run two services(zookeeper & kafka), Prepare your docker-compose.yml file using the following reference.
```shell
version: '3'

services:
  zookeeper:
    image: 'wurstmeister/zookeeper:latest'
    container_name: zookeeper
    ports:
      - "2181:2181"    
    environment:
      - KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://127.0.0.1:2181     
    
  kafka:
    image: 'wurstmeister/kafka:2.12-1.0.1'
    container_name: kafka
    ports:
      - "9092:9092"
    environment:
      - KAFKA_BROKER_ID=1
      - KAFKA_LISTENERS=PLAINTEXT://:9092
      - KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://127.0.0.1:9092
      - KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181      
      - ALLOW_PLAINTEXT_LISTENER=yes
    depends_on:
      - zookeeper  
```
2. Go to the path where docker-compose.yml placed and run the below command to create and run the containers (zookeeper & kafka).
```shell
docker-compose -f docker-compose.yml up -d
```
3. To start kafka docker container shell, run the below command.
```shell
docker exec -it kafka sh
```
Go to path /opt/kafka/bin, where we will have executable files to perform operations(creating topics, running producers and consumers, etc).
Example:
```shell
kafka-topics.sh --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 1 --topic test_topic 
```

1. Go to the path: /knowledge-search/search-api and run the below maven command to build the application.
```shell
mvn clean install -DskipTests
```
2. Go to the path: /knowlg-search/search-api/knowlg-search and run the below maven command to run the netty server.
```shell
sbt clean compile
sbt searchService/run
```
3. Using the below command we can verify whether the databases(elasticSearch) connection is established or not. If all connections are good, health is shown as 'true' otherwise it will be 'false'.
```shell
curl http://localhost:9000/health
```

## Knowledge-search-jobs local setup
This readme file contains the instruction to set up and run the knowledge-search-jobs in local machine.

### System Requirements:

### Prerequisites:
* Java 11

### Prepare folders for database data and logs

```shell
mkdir -p ~/sunbird-dbs/es ~/sunbird-dbs/kafka
export sunbird_dbs_path=~/sunbird-dbs
```


### Elasticsearch database setup in docker:
```shell
docker run --name sunbird_es -d -p 9200:9200 -p 9300:9300 \
-v $sunbird_dbs_path/es/data:/usr/share/elasticsearch/data \
-v $sunbird_dbs_path/es/logs://usr/share/elasticsearch/logs \
-v $sunbird_dbs_path/es/backups:/opt/elasticsearch/backup \
 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:6.8.22

```
> --name -  Name your container (avoids generic id)
>
> -p - Specify container ports to expose
>
> Using the -p option with ports 7474 and 7687 allows us to expose and listen for traffic on both the HTTP and Bolt ports. Having the HTTP port means we can connect to our database with Neo4j Browser, and the Bolt port means efficient and type-safe communication requests between other layers and the database.
>
> -d - This detaches the container to run in the background, meaning we can access the container separately and see into all of its processes.
>
> -v - The next several lines start with the -v option. These lines define volumes we want to bind in our local directory structure so we can access certain files locally.
>
> --env - Set config as environment variables for Neo4j database
>

### Running kafka using docker:
1. Kafka stores information about the cluster and consumers into Zookeeper. ZooKeeper acts as a coordinator between them. we need to run two services(zookeeper & kafka), Prepare your docker-compose.yml file using the following reference.
```shell
version: '3'

services:
  zookeeper:
    image: 'wurstmeister/zookeeper:latest'
    container_name: zookeeper
    ports:
      - "2181:2181"    
    environment:
      - KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://127.0.0.1:2181     
    
  kafka:
    image: 'wurstmeister/kafka:2.11-1.0.1'
    container_name: kafka
    ports:
      - "9092:9092"
    environment:
      - KAFKA_BROKER_ID=1
      - KAFKA_LISTENERS=PLAINTEXT://:9092
      - KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://127.0.0.1:9092
      - KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181      
      - ALLOW_PLAINTEXT_LISTENER=yes
    depends_on:
      - zookeeper  
```
2. Go to the path where docker-compose.yml placed and run the below command to create and run the containers (zookeeper & kafka).
```shell
docker-compose -f docker-compose.yml up -d
```
3. To start kafka docker container shell, run the below command.
```shell
docker exec -it kafka sh
```
Go to path /opt/kafka/bin, where we will have executable files to perform operations(creating topics, running producers and consumers, etc).
Example:
```shell
kafka-topics.sh --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 1 --topic test_topic 
```

### Steps to start a job in debug or development mode using IntelliJ:
1. Navigate to downloaded repository folder (knowlg-search/search-job/) and run below command.
```shell
mvn clean install -DskipTests
``` 
2. Open the project in IntelliJ.
3. Navigate to the target job folder ( ../knowlg-search/search-job/search-indexer) and edit the 'pom.xml' to add below dependency.
```shell
<dependency>
  <groupId>org.apache.flink</groupId>
  <artifactId>flink-clients_${scala.version}</artifactId>
  <version>${flink.version}</version>
</dependency>
```
4. Comment "provided" scope from flink-streaming-scala_${scala.version} artifact dependency in the job's 'pom.xml'.
```shell
<dependency>
    <groupId>org.apache.flink</groupId>
    <artifactId>flink-streaming-scala_${scala.version}</artifactId>
    <version>${flink.version}</version>
<!--            <scope>provided</scope>-->
</dependency>
```
5. Comment the default flink StreamExecutionEnvironment in the job's StreamTask file ( SearchIndexerStreamTask.scala) and add code to create local StreamExecutionEnvironment.
```shell
//    implicit val env: StreamExecutionEnvironment = FlinkUtil.getExecutionContext(config)
      implicit val env: StreamExecutionEnvironment = StreamExecutionEnvironment.createLocalEnvironment()
```
6. Save cloud storage related environment variables in StreamTask environment variables.
7. Start all databases, zookeper and kafka containers in docker
8. Run the StreamTask (Normal or Debug)
9. Open a terminal, connect to kafka docker container and produce the target job topic.
```shell
docker exec -it kafka_container_id sh
kafka-console-producer.sh --broker-list kafka:9092 --topic sunbirddev.learning.graph.events
```


## Steps for running jobs in Flink locally:-
### Running flink :
1. Download the Apache flink
```shell
wget https://dlcdn.apache.org/flink/flink-1.12.7/flink-1.12.7-bin-scala_2.12.tgz
```
2. Extract the downloaded folder
```shell
tar xzf flink-1.12.7-bin-scala_2.12.tgz
```
3. Change the directory & Start the flink cluster.
```shell
cd flink-1.12.7
./bin/start-cluster.sh
```
4. Open web view to check jobmanager and taskmanager
```shell
localhost:8081
```

### Setting up Cloud storage connection:
Setup cloud storage specific variables as environment variables.
```shell
export cloud_storage_type=  #values can be 'aws' or 'azure'

For AWS Cloud Storage connectivity: 
export aws_storage_key=
export aws_storage_secret=
export aws_storage_container=

For Azure Cloud Storage connectivity:
export azure_storage_key=
export azure_storage_secret=
export azure_storage_container=

export content_youtube_apikey= #key to fetch metadata of youtube videos

```
### Running job in Flink:
1. Navigate to the required job folder (Example: ../knowlg-search/search-job/search-indexer) and run the below maven command to build the application.
```shell
mvn clean install -DskipTests
``` 
2. Start all databases, zookeper and kafka containers in docker
3. Start flink (if not started) and submit the job to flink. Example:
```shell
cd flink-1.12.7
./bin/start-cluster.sh
./bin/flink run -m localhost:8081 /user/test/workspace/knowlg-search/search-job/search-indexer/target/search-indexer-1.0.0.jar
```
4. Open a terminal, connect to kafka docker container and produce the target job topic.
```shell
docker exec -it kafka_container_id sh
kafka-console-producer.sh --broker-list kafka:9092 --topic sunbirddev.learning.graph.events
```