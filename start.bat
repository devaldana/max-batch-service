docker network create devspods.com
docker run --rm -d --name mysql -e MYSQL_ROOT_PASSWORD=E3ZpsFw78QHQGat6 -p 3306:3306 --net devspods.com mysql:8.0.26
docker build -t batch-base-img .
docker run --rm -it -v %1:/app -w /app --name max-batch-service --net devspods.com batch-base-img