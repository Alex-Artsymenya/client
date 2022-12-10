# Client-server Application

## Start Server with Database

```shell
docker-compose build
docker-compose up
```

## Start Client

```shell
cd client
mvn clean package
java -jar target/client-1.0-SNAPSHOT.jar
```
