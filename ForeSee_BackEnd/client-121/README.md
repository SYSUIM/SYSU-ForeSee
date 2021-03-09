# ForeSee触发模块

## 模块介绍

本模块主要用于接收来自前端的请求，将请求转发至ForeSee数据访问模块，收到检索结果后把结果返回给前端。

### 模块结构

```lua
├── client-222
    ├── src
        ├── main
            ├── java                       -- java源文件
                ├── com.ForeSee.ForeSee
                    ├── controller         -- controller层代码，接收前端的请求并发送请求给数据访问模块
                    ├── ClientMain.java    -- 启动类
            ├── resource                   -- 其他文件
                ├── application.yml        -- 主配置文件，用于指定使用哪个配置文件
                ├── application-dev.yml    -- 开发环境配置文件
                ├── application-prod.yml   -- 生产环境配置文件
        ├── test                           -- 测试文件
	├── pom.xml                              -- Maven依赖文件
```



### 接口文档

共有13个接口。

* 接口样式见 /client-121/src/main/java/com/ForeSee/ForeSee/controller/InfoController.java
* 返回数据样式见 /client-121/src/main/resources/FrontEndData

### 配置说明

主配置文件

```yaml
spring:
  profiles:
    active: prod  # 指定配置文件，本地测试使用dev文件，服务器部署使用prod文件
  application:
    name: springcloud-client # 服务名，发送请求给微服务处理的服务消费者
```

开发环境配置文件：用于本地测试

```yaml
server:
  port: 8288 # 占用端口
  servlet:
    context-path: /ForeSee # 前端发送请求时需要加上/ForeSee前缀
  # 本次测试发送请求的路径:127.0.0.1:8288/ForeSee/
http:
  REST_URL_PREFIX: http://127.0.0.1:6666 # 把请求转发给对应路径的路由网关
  # 前端请求127.0.0.1:8288/ForeSee/companyInfo/300433
  # 转发给路由网关的请求127.0.0.1:6666/companyInfo/300433
  # 转发给微服务的请求127.0.0.1:8287/companyInfo/300433
```

生产环境配置文件：用于服务器部署

```yaml
server:
  port: 8288 # 占用端口
  servlet:
    context-path: /ForeSee # 前端发送请求时需要加上/ForeSee前缀
  # 服务器部署发送请求的路径:121.46.19.26:8288/ForeSee/
http:
  REST_URL_PREFIX: http://222.200.184.74:6666 # 把请求转发给对应路径的路由网关
  # 前端请求121.46.19.26:8288/ForeSee/companyInfo/300433
  # 转发给路由网关的请求222.200.184.125:6666/companyInfo/300433
  # 转发给微服务的请求192.168.1.103:8288/companyInfo/300433
```

