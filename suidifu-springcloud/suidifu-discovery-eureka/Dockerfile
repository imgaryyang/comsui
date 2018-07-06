FROM openjdk:8-jre-alpine3.7
MAINTAINER louguanyang <louguanyang@hzsuidifu.com>
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} ROOT.jar
ENTRYPOINT ["java", "-jar","ROOT.jar"]