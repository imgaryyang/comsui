# 随地付jdk基础镜像
# 1. docker build -t 120.26.102.180:5000/suidifu/openjdk-8-jre-alpine3.7:1.0.0 .
# 2. docker push 120.26.102.180:5000/suidifu/openjdk-8-jre-alpine3.7:1.0.0

FROM openjdk:8-jre-alpine3.7

MAINTAINER wukai wukai@hzsudifu.com

RUN apk add -U tzdata

RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN echo "Asia/Shanghai" > /etc/timezone

RUN apk del tzdata

