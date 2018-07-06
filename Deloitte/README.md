### 架构说明

```
canal负责监控需要对账的三张表cash_flow,t_remittance_plan_exec_log,t_remittance_refund_bill并实时同步到pika中；
pika负责实时计算对账结果，并把对账结果实时同步回数据库remittance_audit_result表中
```

### 相关文档

```
pika：
https://github.com/Qihoo360/pika/blob/master/README_CN.md

canal：
https://github.com/alibaba/canal/wiki：
```


### 修改/etc/mysql/my.cnf配置：

```
[mysqld]
log-bin=mysql-bin #添加这一行就ok
binlog-format=ROW #选择row模式
server_id=1 #配置mysql replaction需要定义，不能和canal的slaveId重复
```

### 创建mysql slave的相关权限并重启

```
CREATE USER canal IDENTIFIED BY '123456';  
GRANT SELECT, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'canal'@'%';
FLUSH PRIVILEGES;

service mysql restart;
```

### 下载canal_deployer并配置

```
wget https://github.com/alibaba/canal/releases/download/canal-1.0.22/canal.deployer-1.0.22.tar.gz
```

###  解压后修改 conf/example/instance.properties

```
## mysql serverId
canal.instance.mysql.slaveId = 1234

# db info username/password，需要改成自己的数据库信息
canal.instance.master.address = 127.0.0.1:3306 
canal.instance.master.journal.name = 
canal.instance.master.position = 
canal.instance.master.timestamp = 
 ... 
canal.instance.dbUsername = canal  
canal.instance.dbPassword = 123456
canal.instance.defaultDatabaseName = canal_test
canal.instance.connectionCharset = UTF-8

# table regex
canal.instance.filter.regex = canal_test.cash_flow,canal_test.t_remittance_plan_exec_log,canal_test.t_remittance_refund_bill
```

###  启动canal_server

```
sh bin/startup.sh
```

###  查看日志

```
tail -f  logs/canal/canal.log
```

###  具体instance的日志

```
tail -f  logs/example/example.log
```

###  关闭canal_server

```
sh bin/stop.sh
或
ps -ef|grep canal
kill -9 xxx
rm -rf bin/canal.pid
```

### 启动本项目

```
服务启动：
sudo ln -s ~/_bak/deloitte/deloitte-1.0.1.jar /etc/init.d/deloitte
（如果出现Unable to find Java，where java, 然后ln -s /path/to/java /usr/bin/java）

启动
sudo /etc/init.d/deloitte start --spring.profiles.active=dev --spring.config.location=~/_bak/canal --spring.config.name=deloitte

日志
stdout logs：
tail -f /var/log/deloitte.log

log4j2 logs：
tail -f ~/_bak/deloitte/logs/deloitte/deloitte.log

停止
sudo /etc/init.d/deloitte stop

```