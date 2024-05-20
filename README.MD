# c1ctf-backend
## 运行环境

Java SDK 11 / OpenJDK 11 (jdk11-corretto)

## 配置文件

/src/main/resources/application.yml

修改当当前配置为prod或prod-test

```
spring:
  profiles:
    active: dev
```

注意修改redis、mysql和邮箱配置

## 编译
```
mvn package
```

然后将`c1ctfplatform.war`放至tomcat的webapp目录下

后端地址为

```
http://tomcat-ip:default-port-8080/c1ctfplatform/api
```

可访问以下网址测试服务是否正常

```
http://tomcat-ip:default-port-8080/c1ctfplatform/api/config/get_match_info
```
