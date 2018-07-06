### 在SpringBoot中引用canal-core

```
@Import({ ImportCanalDependencies.class })
@SpringBootApplication
```

### 加入以下内容到spring配置文件中

```
canal.server.host=192.168.1.138
canal.server.port=22222
canal.server.destination=test1
canal.server.username=
canal.server.password=
# .*\\..* 则监控所有表更改
canal.server.subscribe=canal_test.journal_voucher,canal_test.cash_flow
#每次获取数据库更改记录数目
canal.server.batch-size=1000
#间隔多少毫秒获取一次更改
canal.server.fixed-delay=3000
```

### 监控更新处理类(组件名字必须以表名)

```
@Component("journal_voucher")
public class JournalVoucher extends AbstractRowEvents{}

@Component("cash_flow")
public class CashFlow extends AbstractRowEvents{}
```