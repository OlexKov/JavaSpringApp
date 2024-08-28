mvn -v

mvn package

mvn clean install

java -jar target/spring-pv221.jar

```
docker build -t JavaSpringApp . 
docker run -it --rm -p 5085:8080 --name facebook_container facebook-api
docker run -d --restart=always --name facebook_container -p 5085:8080 facebook-api
docker run -d --restart=always -v d:/volumes/api/images:/app/images -v d:/volumes/api/EmailTemplates:/app/EmailTemplates --name facebook_container -p 5085:8080 facebook-api
docker run -d --restart=always -v /volumes/api/images:/app/images -v /volumes/api/EmailTemplates:/app/EmailTemplates --name facebook_container -p 5085:8080 facebook-api
docker ps -a
docker stop facebook_container
docker rm facebook_container

docker images --all
docker rmi facebook-api

docker login
docker tag facebook-api:latest novakvova/facebook-api:latest
docker push novakvova/facebook-api:latest

docker pull novakvova/facebook-api:latest
docker ps -a
docker run -d --restart=always --name facebook_container -p 5085:8080 novakvova/facebook-api


docker pull novakvova/facebook-api:latest
docker images --all
docker ps -a
docker stop facebook_container
docker rm facebook_container
docker run -d --restart=always --name facebook_container -p 5085:8080 novakvova/facebook-api
```