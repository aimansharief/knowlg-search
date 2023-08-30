# FROM --platform=linux/x86_64 maven:3.6.0-jdk-11-slim as search-indexer-build
# COPY . /app
# RUN mvn clean install -DskipTests -f /app/pom.xml 

FROM flink:1.12.0-java11

COPY search-indexer/target/search-indexer-1.0.0.jar /opt/flink/lib
COPY jobs-core/target/jobs-core-1.0.0.jar /opt/flink/lib

USER flink