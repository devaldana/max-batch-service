#!/bin/sh
sudo docker rm -f max-batch-service
sudo docker rm -f mysql 
sudo docker network create devspods.com 
sudo docker run --rm -d --name mysql -e MYSQL_ROOT_PASSWORD=E3ZpsFw78QHQGat6 -p 3306:3306 --net devspods.com mysql:8.0.26 && \
sudo docker build -t batch-base-img . && \
sudo docker run --rm -it -v $1:/app -w /app --name max-batch-service --net devspods.com batch-base-img
