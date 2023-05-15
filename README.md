# 铜锁训练营作品提交仓库-基于Netty的reactive server

利用spring boot webflux + netty reactive 搭建高性能的http/websocket服务。支持ssl加密， websocket协议支持protobuf 消息，后续http协议支持http2.0。

## 设计

1. tls
   * TLS(v1.2/v1.3) ，支持国密加解密算法(基于tongsuo provider实现，目前仅做了单向认证)
     具体配置见这几个文件：

     ```
     group.bison.netty.websocket.config.SslConfiguration.java
     org.springframework.boot.web.embedded.netty.SslServerCustomizer.java
     src/main/resources/application.yml
     ```
2. protobuf
3. http2.0(开发中)
4. 业务biz代码热加载

## 使用

编译打包：

```
mvn clean package -DskipTests
```

启动

```
java -jar target/netty-websocket-test*.jar
```

依赖服务项

* 针对jdk1.8或以下的 jre security 配置修改

  `echo 'crypto.policy=unlimited' >> ${JAVA_HOME}/jre/lib/security/java.security`

测试

1. 测试https方式请求请求

   `mvn test -Dtest=group.bison.netty.websocket.tests.HttpClientTest`
2. 测试wss方式请求服务

   `mvn test -Dtest=group.bison.netty.websocket.tests.WebsocketClientTest`

>>>>>>> e333587 (add README)
>>>>>>>
>>>>>>
>>>>>
>>>>
>>>
>>
