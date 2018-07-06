#! /bin/sh
# 解决微服务项目依赖config-server启动太慢，导致微服务项目无法启动
sleep 10 && java  -Duser.timezone=GMT+08 -jar ROOT.jar --spring.config.location=/config/bootstrap.properties
