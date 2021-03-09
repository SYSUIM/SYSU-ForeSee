# ForeSee服务注册中心

## 模块介绍

本模块充当服务注册中心，将Foresee的数据访问模块和路由网关模块注册到本模块，使得路由网关模块可以发现数据访问模块并将触发端的请求转发给数据访问模块。

### 模块结构

```lua
├── eurekaServer-222
    ├── src
        ├── main
            ├── java                           -- java源文件
                ├── com.ForeSee.ForeSee
                    ├── eurekaServerMain.java  -- 启动类
            ├── resource                       -- 其他文件
                ├── application.yml            -- 主配置文件，用于指定使用哪个配置文件
                ├── application-dev.yml        -- 开发环境配置文件
                ├── application-prod.yml       -- 生产环境配置文件
        ├── test                               -- 测试文件
	├── pom.xml                                  -- Maven依赖文件
```

### 配置说明 

主配置文件

```yaml
server:
  port: 8888  # 服务注册中心占用的端口号

spring:
  profiles:
    active: prod  # 指定使用的配置文件，本地测试时使用dev文件，服务器部署使用prod文件
  application:
    name: springcloud-eureka-server # 服务注册中心的服务名
```

开发环境配置文件：用于本地测试

```yaml
eureka: # eureka是C-S模式，eureka服务器端担任服务注册中心，eureka客户端把微服务注册到服务注册中心	
   instance:  # 多个eureka服务器端可以组成服务注册中心集群，每个eureka服务器端都是一个服务注册中心实例，共享相同的服务名
   　# 目前只配置了一个eureka服务器端，服务注册中心只有一个实例
   　instance-id: onlyEurekaServer # 服务注册中心的实例名
     ip-address: 127.0.0.1 #　服务注册中心实例所在的ip地址
     lease-expiration-duration-in-seconds: 90 # 服务失效时间，微服务超过90s没有向服务注册中心发送心跳，就会被从服务列表中剔除
     lease-renewal-interval-in-seconds: 30 # 服务续约频率，微服务需要每30s至少向服务注册中心发送一次心跳，告知对方微服务在正常运行
   client:
     register-with-eureka: false # eureka服务器端不需要向服务注册中心注册自己
     fetch-registry: false # eureka服务器端不需要从服务注册中心获取注册信息
     service-url: 
       defaultZone: http://localhost:8888/eureka/ # 服务注册中心的地址，可以使用localhost:8888访问服务注册中心的控制台，查看微服务状态
```

生产环境配置文件：用于服务器部署

```yaml
eureka: 
  instance:
  # 222可以ping通121和192，方便121和192上的微服务注册，所以服务注册中心放在222上
  # 目前在linux集群上只配置了一个服务注册中心实例
    instance-id: onlyEurekaServer # 服务注册中心的实例名
    ip-address: 222.200.184.74 # 服务注册中心实例所在的ip地址
    lease-expiration-duration-in-seconds: 90 # 服务失效时间
    lease-renewal-interval-in-seconds: 30 # 服务续约频率
  client:
    register-with-eureka: false # eureka服务器端不需要向服务注册中心注册自己
    fetch-registry: false # eureka服务器端不需要从服务注册中心获取注册信息
    service-url:
      defaultZone: http://222.200.184.74:8888/eureka/ # 服务注册中心的地址，可以使用222.200.184.74:8888访问服务注册中心的控制台，查看微服务状态
```





