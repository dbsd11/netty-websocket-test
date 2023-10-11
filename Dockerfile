FROM alpine

RUN apk add openjdk8

MAINTAINER bison

ARG JAR_FILE

ADD target/${JAR_FILE} /netty-websocket-test.jar

COPY ./modules /modules

RUN find /modules -type f -name '*ark-biz.jar'|xargs -I file_name cp file_name / && rm -rf /modules

EXPOSE 443

ENV ssl_enabled=false

ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions","-XX:+UseCGroupMemoryLimitForHeap", "-jar","/netty-websocket-test.jar"]

# docker build --build-arg JAR_FILE=netty-websocket-test-1.0.0-SNAPSHOT-ark-biz.jar . -t 