# dockerfile-maven插件使用

### 一、插件引入说明

为了给微服务改造后期的Docker自动化运维做准备，我们废弃了之前采用的shell脚本手动上传Docker镜像的方式。新的Docker镜像自动打包上传方式采用了`spotify`的docker maven插件。新的方式只需要开发人员在编写代码的时候，在对应的微服务的POM文件中添加相关依赖并创建一个Dockerfile，就可以完成插件集成。不需要运维同事再手工执行shell脚本打包镜像。

### 插件集成（修改POM文件）

1. 在`pom.xml`的build节点下添加以下插件：

```xml
<!-- dockerfile-maven-plugin -->
    <build>
        <plugins>
            
            <!-- dockerfile-maven-plugin -->
            <plugin>
                <groupId>com.spotify</groupId>
                <artifactId>dockerfile-maven-plugin</artifactId>
                <version>${dockerfile-maven-plugin.version}</version>
                <executions>
                    <execution>
                        <id>default</id>
                        <phase>package</phase>
                        <goals>
                            <goal>build</goal>
                            <goal>push</goal>
                            <goal>tag</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <force>true</force>
                    <repository>${docker.repository}/${docker.image.prefix}/${project.artifactId}</repository>
                    <tag>${project.version}</tag>
                    <buildArgs>
                        <JAR_FILE>target/${project.build.finalName}.jar</JAR_FILE>
                    </buildArgs>
                </configuration>
            </plugin>
            
        </plugins>
    </build>
```

> `${dockerfile-maven-plugin.version}`:插件版本：1.3.6
> `${docker.repository}`:Docker私服地址：120.26.102.180:5000   
> `${docker.image.prefix}`:Docker镜像统一前缀：suidifu   
> 以上配置都在父项目的`pom.xml`中

### 二、生成Dockerfile文件

在微服务模块的根目录新建 `Dockerfile` 文件，Dockerfile 路径跟 `pom.xml`文件一致。

文件模版内容如下：

```dockerfile
FROM openjdk:8-jre-alpine3.7
MAINTAINER louguanyang <louguanyang@hzsuidifu.com>
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} /app.jar
ENTRYPOINT ["java", "-jar","/app.jar"]
```

### 三、运行

在 suidifu-springcloud 项目下执行以下操作
`mvn clean install -DskipTests=true`

### 四、插件其它maven操作命令

|maven操作|备注|
|:----:|:---:|
dockerfile.skip|禁用整个dockerfile插件; 所有的goals都失效。
dockerfile.build.skip|禁用 docker build 功能。
dockerfile.tag.skip|禁用 docker tag 功能。
dockerfile.push.skip|禁用 docker push 功能。

范例：
> mvn clean package -Ddockerfile.skip

