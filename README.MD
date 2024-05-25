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

**注意每次部署修改jwt.secret-key否则可能因为以前平台签发的票据导致越权！！！**

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

## nginx 参考配置

```
        location /c1ctf/api/ {
                proxy_pass http://192.168.100.10:8180/c1ctf/api/;
                include /etc/nginx/proxy_params;
        }

        location = /cas/login {
                proxy_pass http://192.168.100.10:3002/cas_login.html;
                proxy_redirect http://192.168.100.10/ /;
                include /etc/nginx/proxy_params;
        }

```