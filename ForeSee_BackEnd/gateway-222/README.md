# ForeSee路由网关模块

## 模块介绍

本模块主要用于发现注册在Eureka的数据访问模块，将触发端的请求转发给ForeSee的数据访问模块，同时将响应结果返回给触发端。

### 模块结构

```lua
├── gateway-222
    ├── src
        ├── main
            ├── java  -- java源文件
                ├── com.ForeSee.ForeSee
                    ├── GatewayMain.java  -- 启动类
            ├── resource  -- 其他文件
                ├── application.yml  -- 主配置文件，用于指定哪个配置文件
                ├── application-dev.yml -- 开发环境配置文件
                ├── application-prod.yml -- 生产环境配置文件
        ├── test  -- 测试文件
	├── pom.xml  -- Maven依赖文件
```
### 配置说明

主配置文件

```yaml
server:
  port: 6666 # 路由网关占用的端口号

spring:
  profiles:
      active: prod  # 指定使用的配置文件，本地测试使用dev文件，服务器部署使用prod文件
  application:
     name: springcloud-gateway # 路由网关的服务名，可以有多个路由网关实例共享服务名
     # 目前只有一个路由网关实例
  cloud:
     gateway:
       discovery:
         locator:
           enabled: true # 发现服务注册中心的微服务，使用服务名把请求转发给对应的微服务
           # 前提是路由网关也要注册进服务注册中心
       routes:
         - id: springcloud-foresee1 # 路由转发关系的id，要求唯一，通常与要转发给的微服务名称有关
           uri: lb://springcloud-foresee # 接收路由网关转发的请求的微服务名称
           # 路由网关会进行负载均衡，把请求转发到较为空闲的微服务实例，进行请求的处理
           predicates: # 指定需要匹配的条件，匹配成功后路由网关才会将请求转发给对应的微服务
             - Path=/companyInfo/** # 指定请求的路径
             - Method=GET # 指定请求的方法
        # 121发送给路由网关get请求且请求路径是/companyInfo/300433时
        # 路由网关会把请求转发给服务名为springcloud-foresee的微服务实例
        # 转发出去的请求也是get请求，路径是/companyInfo/300433
        
         - id: springcloud-foresee2
           uri: lb://springcloud-foresee
           predicates:
             - Path=/allNotice/**
             - Method=GET
        # 121发送给路由网关get请求且请求路径是/allNotice/300433/1时
        # 路由网关会把请求转发给服务名为springcloud-foresee的微服务实例
        # 转发出去的请求也是get请求，路径是/allNotice/300433/1
        
         - id: springcloud-foresee3
           uri: lb://springcloud-foresee
           predicates:
             - Path=/allNews/**
             - Method=GET
        # 121发送给路由网关get请求且请求路径是/allNews/300433/1时
        # 路由网关会把请求转发给服务名为springcloud-foresee的微服务实例
        # 转发出去的请求也是get请求，路径是/allNews/300433/1
        
         - id: springcloud-foresee4
           uri: lb://springcloud-foresee
           predicates:
             - Path=/allInfo/**
             - Method=GET
        # 121发送给路由网关get请求且请求路径是/allInfo/300433时
        # 路由网关会把请求转发给服务名为springcloud-foresee的微服务实例
        # 转发出去的请求也是get请求，路径是/allInfo/300433
        
         - id: springcloud-foresee5
           uri: lb://springcloud-foresee
           predicates:
             - Path=/industryInfo/**
             - Method=GET
        # 121发送给路由网关get请求且请求路径是/industryInfo/300433时
        # 路由网关会把请求转发给服务名为springcloud-foresee的微服务实例
        # 转发出去的请求也是get请求，路径是/industryInfo/300433

         - id: springcloud-foresee6
           uri: lb://springcloud-foresee
           predicates:
             - Path=/industryReports/**
             - Method=GET   
        # 121发送给路由网关get请求且请求路径是/industryReports/300433/1时
        # 路由网关会把请求转发给服务名为springcloud-foresee的微服务实例
        # 转发出去的请求也是get请求，路径是/industryReports/300433/1
```

开发环境配置文件：用于本地测试

```yaml
eureka: # 路由网关为了发现服务注册中心的微服务，转发请求给对应的微服务，需要注册进服务注册中心
  client: # 路由网关作为eureka客户端
    service-url:
      defaultZone: http://localhost:8888/eureka/ # 注册到服务注册中心中
  instance:
    instance-id: springcloud-gateway-6666 # 实例id，要求唯一，用于区分同一微服务下的不同实例
    prefer-ip-address: true # 使用ip地址注册到服务注册中心
    ip-address: 127.0.0.1 # 实例所在的ip地址
    # 运行在127.0.0.1:6666上的路由网关被注册到127.0.0.1:8888的服务注册中心中
    # 可以将来自127.0.0.1:8288的请求转发给服务注册中心的微服务
```

生产环境配置文件：用于服务器部署

```yaml
eureka:  # 路由网关负责把121上的请求转发给192.所以需要ping通121和192，因此路由网关放在222上
  client:
    service-url:
       defaultZone: http://222.200.184.74:8888/eureka/ # 注册到服务注册中心中
  instance:
    instance-id: springcloud-gateway-6666 # 实例id
    prefer-ip-address: true # 注册时使用ip地址注册
    ip-address: 222.200.184.74 # 实例所在的ip地址
    # 运行在222.200.184.74:6666上的路由网关被注册到222.200.184.74:8888的服务注册中心
    # 可以将来自客户端121.46.19.26:8288的请求转发给服务注册中心的微服务
```

