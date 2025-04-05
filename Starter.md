# How to run the application

Before starting the application run:
```
docker run -d --publish 3306:3306 --name sakila restsql/mysql-sakila
```
With the above command you can use the "sakila" DB.

I'm using 'Java 23' and to run the application use:
```
MAVEN_OPTS="-Dnet.bytebuddy.experimental=true" ./mvnw quarkus:dev
```







