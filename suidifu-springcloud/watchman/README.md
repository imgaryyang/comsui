init project

## Sonar 执行测试覆盖率
```
1. mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install -Dmaven.test.failure.ignore=true
2. mvn sonar:sonar
```