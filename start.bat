SET IMAGE_NAME=max-batch-service
SET CONTAINER_NAME=max-batch-service
docker network create devspods.com
docker run --rm -d --name mysql -e MYSQL_ROOT_PASSWORD=E3ZpsFw78QHQGat6 -p 3306:3306 --net devspods.com mysql:8.0.26
docker build -t %IMAGE_NAME% .
docker run --rm -it -v %1:/app -w /app --name %CONTAINER_NAME% --net devspods.com %IMAGE_NAME%