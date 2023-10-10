FROM openjdk:8u131-jdk
MAINTAINER bison

ARG JAR_FILE

ADD target/${JAR_FILE} /netty-websocket-test.jar

EXPOSE 8080

ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions","-XX:+UseCGroupMemoryLimitForHeap", "-jar","/netty-websocket-test.jar"]

# docker build --build-arg JAR_FILE=netty-websocket-test-1.0.0-SNAPSHOT-ark-biz.jar . -t 