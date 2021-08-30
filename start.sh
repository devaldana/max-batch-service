#!/bin/sh
IMAGE_NAME=maxbatchservice
CONTAINER_NAME=maxbatchservice
sudo docker rm -f $CONTAINER_NAME && \
sudo docker rm -f mysql && \
sudo docker network create devspods.com && \
sudo docker run --rm -d --name mysql -e MYSQL_ROOT_PASSWORD=E3ZpsFw78QHQGat6 -p 3306:3306 --net devspods.com mysql:8.0.26 && \
sudo docker build -t $IMAGE_NAME . && \
sudo docker run --rm -it -v $1:/app -w /app --name $CONTAINER_NAME --net devspods.com $IMAGE_NAME
