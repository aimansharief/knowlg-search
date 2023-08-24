#!/bin/bash
TAG=$1

docker rmi $(docker images -a | grep search-service | awk '{print $1":"$2}')

cd knowlg-search
sbt dist
cd ../
docker build -f build/search-service/Dockerfile -t search-service:${TAG} .
