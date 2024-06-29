## Start using maven

```
mvn clean
mvn package
java -javaagent:agent/target/agent-1.0-SNAPSHOT.jar -Ddebug -jar application/target/application-1.0-SNAPSHOT.jar
```

## Start using docker

```
docker-compose up
```

Add --build to rebuild the docker image
