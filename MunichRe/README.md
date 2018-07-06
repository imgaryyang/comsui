### 打包启动说明

```
依赖canal-core；

打包：mvn clean package；

服务方式启动：
ln -s ~/xxx/munichre-1.0.0-SNAPSHOT.jar /etc/init.d/munichre

cp main/resources/application-prod.properties ~/xxx/

修改application-prod.properties里面的相应配置并启动：

sudo /etc/init.d/munichre start --spring.profiles.active=prod --spring.config.location=~/xxx/

停止：
sudo /etc/init.d/munichre stop
```

### canal配置说明：

```
1，cd 到canal安装根目录；
2，停止canal：./bin/stop.sh
3，在conf目录下copy 一份 example 并重命名为munichre；
4，cd conf/munichre，删除meta文件，修改instance.properties里canal.instance.mysql.slaveId值加1即可；
5，cd conf目录，修改canal.properties里canal.destinations=example,munichre即可；
6，启动canal：./bin/startup.sh，查看日志：tail -f logs/munichre/munichre.log
```
 