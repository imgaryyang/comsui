FROM 120.26.102.180:5000/suidifu/openjdk-8-jre-alpine3.7:1.0.0

MAINTAINER wukai wukai@hzsudifu.com

RUN mkdir -p /data/webapps/

COPY ROOT.jar /data/webapps/

EXPOSE 8080

WORKDIR /data/webapps

CMD java -jar ROOT.jar --spring.config.location=/config/bootstrap.properties
