##问题一：自动打包压缩js\css
解决方案：
1、需要安装nodeJS，版本为6.0+  
2、进入earth的webapp的static目录，执行命令`npm install`,然后执行命令`npm run dist`

###问题二：安装cucumber的eclipse插件
解决方案：
1、点击eclipse的按钮`help`->`install new software`,选择`add site`，填入`https://cucumber.io/cucumber-eclipse/update-site`  
2、按照提示选择安装即可

###问题三：电脑上无法运行打js、css压缩包命令
解决方案：
1、window电脑上执行`mvn -DskipCompress=false clean install －Pwindows`即可  
2、mac、linux电脑上执行`mvn -DskipCompress=false clean install`

####问题四: 整体跑`earth`项目的测试方法？

解决方案： 从终端进入到`earth`的项目下，  
执行命令`mvn clean test -DskipTests=false "-Dtest=*AllTestsOfEarth"`即可
