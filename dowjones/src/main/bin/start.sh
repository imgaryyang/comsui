#!/bin/sh
echo -------------------------------------------
echo Start Server
echo -------------------------------------------
# 设置项目代码路径
export CODE_HOME="/data/webapps/dowjones"
# 设置依赖路径
export CLASSPATH="$CODE_HOME/WEB-INF/classes:$CODE_HOME/WEB-INF/lib/*"
# 日志路径
export LOG_PATH="/data/webapps/dowjones/logs"
mkdir -p $LOG_PATH
# java可执行文件位置
export _EXECJAVA="$JAVA_HOME/bin/java"
# JVM启动参数
export JAVA_OPTS="-server -Xms512m -Xmx1024m "
# 服务端端口、上下文、项目根配置
export SERVER_INFO="-Dserver.port=8000 -Dserver.contextPath=/dowjones -Dserver.docBase=$CODE_HOME"
# 启动类
export MAIN_CLASS=com.suidifu.dowjones.Dowjones

nohup $_EXECJAVA $JAVA_OPTS -classpath $CLASSPATH $SERVER_INFO $MAIN_CLASS >$LOG_PATH/dowjones.log  2>&1 &