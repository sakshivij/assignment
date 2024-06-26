## Running

```
    mvn clean
    mvn install
    mvn package
    java -javaagent:target/dependencies/agent-1.0-SNAPSHOT.jar -Ddebug -jar target/dependencies/application-1.0-SNAPSHOT.jar
```