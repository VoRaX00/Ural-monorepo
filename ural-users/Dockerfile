FROM mirror.gcr.io/library/eclipse-temurin:21-jre-alpine

COPY ural-users-service/target/*.jar service.jar

EXPOSE 8080

CMD exec java $JAVA_OPTS -jar service.jar
