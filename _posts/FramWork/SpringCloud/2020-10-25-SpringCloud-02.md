---
layout: post
title: "SpringCloud-02"
subtitle: "「Feign、Gateway、Bus」"
author: "月明"
date:  2020-10-25 23:56:00
header-img: "assets/background8.png"
header-mask: 0.3
tags:
  - FrameWork
  - SpringCloud
---

# SpringCloud

## Feign

> Feign（伪装）可以把Rest的请求进行隐藏，伪装成类似SpringMVC的Controller一样。你不用再自己拼接url，拼接参数等等操作，一切都交给Feign去做。

### 快速入门

在 consumer项目的 pom.xml 文件中添加如下依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

#### Feign的客户端

在 consumer中编写如下Feign客户端接口类

`@FeignClient`

```java
@FeignClient("user-service")
public interface UserClient {
    @GetMapping("/user/{id}")
    User queryById(@PathVariable("id") Long id);
}
```

* 首先这是一个`接口`，Feign会通过`动态代理`，帮我们生成实现类。这点跟mybatis的mapper很像
* @FeignClient ，声明这是一个`Feign客户端`，同时通过 `value `属性`指定服务名称`
* 接口中的定义方法，完全采用SpringMVC的注解，Feign会`根据注解`帮我们生成`URL`，并访问获取结果
* @GetMapping中的`/user`，请不要忘记；因为Feign需要拼接可访问的地址

编写新的控制器类 ConsumerFeignController ，使用UserClient访问：

```java
@RestController
@RequestMapping("/cf")
public class ConsumerFeignController {
    @Autowired
    private UserClient userClient;
    @GetMapping("/{id}")
    public User queryById(@PathVariable Long id){
        return userClient.queryById(id);
    }
}
```

####  开启Feign功能

`@EnableFeignClients`//开启Feign功能

在 ConsumerApplication 启动类上，添加注解，开启Feign功能

```jav
@SpringCloudApplication
@EnableFeignClients//开启Feign功能
public class ConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
```

Feign中已经自动集成了Ribbon负载均衡，因此不需要自己定义RestTemplate进行负载均衡的配置。

Fegin内置的ribbon默认设置了请求超时时长，默认是1000，我们可以通过手动配置来修改这个超时时长：

```yaml
ribbon:
  ReadTimeout: 2000 # 读取超时时长
  ConnectTimeout: 1000 # 建立链接的超时时长
```

因为ribbon内部有重试机制，一旦超时，会自动重新发起请求。如果不希望重试，可以添加配置：
修改 consumer\src\main\resources\application.yml  添加如下配置：

```yaml
ribbon:
  ConnectTimeout: 1000 # 连接超时时长
  ReadTimeout: 2000 # 数据通信超时时长
  MaxAutoRetries: 0 # 当前服务器的重试次数
  MaxAutoRetriesNextServer: 0 # 重试多少次服务
  OkToRetryOnAllOperations: false # 是否对所有的请求方式都重试
```

#### Hystrix支持(了解)

Feign默认也有对Hystrix的集成：

只不过，默认情况下是关闭的。需要通过下面的参数来开启；
修改 consumer\src\main\resources\application.yml  添加如下配置：

```yaml
feign:
  hystrix:
    enabled: true # 开启Feign的熔断功能
```

Feign中的Fallback配置不像Ribbon中那样简单了。

1. 首先，要定义一个类，实现刚才编写的UserFeignClient，作为fallback的处理类

   ```java
   @Component
   public class UserClientFallback implements UserClient {
       @Override
       public User queryById(Long id) {
           User user = new User();
           user.setId(id);
           user.setName("用户异常");
           return user;
       }
   }
   ```

2. 然后在UserFeignClient中，指定刚才编写的实现类

   ```java
   @FeignClient(value = "user-service", fallback = 				UserClientFallback.class)
   public interface UserClient {
       @GetMapping("/user/{id}")
       User queryById(@PathVariable("id") Long id);
   }
   ```

#### 请求压缩(了解)

Spring Cloud Feign 支持对请求和响应进行`GZIP压缩`，以减少通信过程中的性能损耗。通过下面的参数即可开启请求与响应的压缩功能

```yaml
feign:
  compression:
    request:
      enabled: true # 开启请求压缩
    response:
      enabled: true # 开启响应压缩
```

同时，我们也可以对请求的数据类型，以及触发压缩的大小下限进行设置：

```yaml
feign:
  compression:
    request:
      enabled: true # 开启请求压缩
      mime-types: text/html,application/xml,application/json # 设置压缩的数据类型
      min-request-size: 2048 # 设置触发压缩的大小下限
```

注：上面的数据类型、压缩大小下限均为默认值。

#### 日志级别(了解)

通过 logging.level.xx=debug 来设置日志级别。然而这个对Fegin客户端而言不会产生效果。因为@FeignClient 注解修改的客户端在被代理时，都会创建一个新的Fegin.Logger实例。我们需要额外指定这个日志的级别才可以。

1. 在 consumer-demo 的配置文件中设置io.moomin包下的日志级别都为 debug

   修改 consumer-demo\src\main\resources\application.yml  添加如下配置：

   ```yaml
   logging:
     level:
       io.moomin: debug
   ```

2. 在 consumer-demo 编写FeignConfig配置类，定义日志级别

   ```java
   @Configuration
   public class FeignConfig {
       @Bean
       Logger.Level feignLoggerLevel(){
           //记录所有请求和响应的明细，包括头信息、请求体、元数据
           return Logger.Level.FULL;
       }
   }
   ```

   这里指定的Level级别是FULL，Feign支持4种级别：

   * `NONE`：不记录任何日志信息，这是默认值
   * `BASIC`：仅记录请求的方法，URL以及响应状态码和执行时间
   * `HEADERS`：在BASIC的基础上，额外记录了请求和响应的头信息
   * `FULL`：记录所有请求和响应的明细，包括头信息、请求体、元数据

3. 在 consumer-demo 的 UserClient 接口类上的@FeignClient注解中指定配置类：

   ```java
   @FeignClient(value = "user-service", fallback = 						UserClientFallback.class,
   configuration = FeignConfig.class)
   public interface UserClient {
       @GetMapping("/user/{id}")
       User queryById(@PathVariable Long id);
   }
   ```

   重启项目，访问：http://localhost:8080/cf/8 ；即可看到每次访问的日志

##  Spring Cloud Gateway网关

> Spring Cloud Gateway是Spring官网基于Spring 5.0、 Spring Boot 2.0、Project Reactor等技术开发的网关服务。
> Spring Cloud Gateway基于Filter链提供网关基本功能：`安全、监控／埋点、限流`等。Spring Cloud Gateway为微服务架构提供简单、有效且统一的`API路由管理`方式。Spring Cloud Gateway是替代Netflix Zuul的一套解决方案。

Spring Cloud Gateway组件的核心是一系列的`过滤器`，通过这些过滤器可以将客户端发送的`请求转发（路由）`到对应的微服务。 Spring Cloud Gateway是加在整个微服务`最前沿`的`防火墙`和`代理器`，隐藏微服务`结点IP端口`信息，从而加强安全保护。Spring Cloud Gateway本身也是一个微服务，需要`注册到Eureka`服务注册中心。

网关的核心功能是：`过滤`和`路由`

![](https://pic.downk.cc/item/5fe4d8c33ffa7d37b37a0b4c.jpg)

不管是来自于客户端（PC或移动端）的请求，还是服务内部调用。一切对服务的请求都可经过网关，然后再由网关来实现 鉴权、动态路由等等操作。Gateway就是我们服务的统一入口。

### 核心概念

* `路由（route）`：路由信息的组成：由一个ID、一个目的URL、一组断言工厂、一组Filter组成。如果路由断言为真，说明`请求URL和配置路由`匹配。
* `断言（Predicate）`：Spring Cloud Gateway中的断言函数输入类型是Spring 5.0框架中的ServerWebExchange。Spring Cloud Gateway的断言函数允许开发者去定义`匹配`来自于Http `Request`中的`任何信息`比如请求头和参数。
* `过滤器（Filter）`：一个标准的Spring WebFilter。 Spring Cloud Gateway中的Filter分为两种类型的Filter，分别是`Gateway Filter`和`Global Filter`。过滤器Filter将会对请求和响应进行修改处理。

### 快速入门

打开 pom.xml  文件修改为如下：

```xml
<dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
    </dependencies>
```

####  编写启动类

io.moomin.gateway.GatewayApplication 启动类

```java
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
```

#### 编写配置

application.yml 文件

```yaml
server:
  port: 10010
spring:
  application:
    name: api-gateway
eureka:
  client:
    service-url:
      defaultZone: http://127.0.0.1:10086/eureka
  instance:
    prefer-ip-address: true
```

#### 编写路由规则

需要用网关来代理 user-service 服务，先看一下控制面板中的服务状态：

![](https://pic.downk.cc/item/5fe4dad83ffa7d37b37d4194.jpg)

修改application.yml 文件为：

```yaml
server:
  port: 10010
spring:
  application:
    name: api-gateway
  cloud:
    gateway:
      routes:
        # 路由id，可以随意写
        - id: user-service-route
          # 代理的服务地址
          uri: http://127.0.0.1:9091
          # 路由断言，可以配置映射路径
          predicates:
            - Path=/user/**
eureka:
  client:
    service-url:
      defaultZone: http://127.0.0.1:10086/eureka
  instance:
    prefer-ip-address: true
```

将符合 Path  规则的一切请求，都代理到 uri 参数指定的地址

我们将路径中包含有 /user/** 开头的请求，代理到http://127.0.0.1:9091

访问的路径中需要加上配置规则的映射路径，我们访问：http://localhost:10010/user/8

### 面向服务的路由

> 应该根据服务的名称，去Eureka注册中心查找 服务对应的所有`实例列表`，然后进行`动态路由`！

#### 修改映射配置，通过服务名称获取

可以从Eureka获取服务的地址信息。

修改 moomin-gateway\src\main\resources\application.yml 文件如下：

```yaml
server:
  port: 10010
spring:
  application:
    name: api-gateway
  cloud:
    gateway:
      routes:
        # 路由id，可以随意写
        - id: user-service-route
          # 代理的服务地址；lb表示从eureka中获取具体服务
          uri: lb://user-service
          # 路由断言，可以配置映射路径
          predicates:
            - Path=/user/**
eureka:
  client:
    service-url:
      defaultZone: http://127.0.0.1:10086/eureka
  instance:
    prefer-ip-address: true
```

路由配置中uri所用的`协议为lb`时（以uri: lb://user-service为例），gateway将使用 `LoadBalancerClient`把user-service通过`eureka解析为实际的主机和端口`，并进行ribbon负载均衡。

### 路由前缀

#### 添加前缀

通过 `PrefixPath=/xxx `来指定了路由要添加的前缀。

在gateway中可以通过配置路由的过滤器PrefixPath，实现映射路径中地址的添加；修改 gateway\src\main\resources\application.yml 文件：

```yaml
server:
  port: 10010
spring:
  application:
    name: api-gateway
    cloud:
    gateway:
      routes:
        # 路由id，可以随意写
        - id: user-service-route
          # 代理的服务地址；lb表示从eureka中获取具体服务
          uri: lb://user-service
          # 路由断言，可以配置映射路径
          predicates:
            - Path=/**
          filters:
            # 添加请求路径的前缀
            - PrefixPath=/user
eureka:
  client:
    service-url:
      defaultZone: http://127.0.0.1:10086/eureka
  instance:
    prefer-ip-address: true
```

* PrefixPath=/user  http://localhost:10010/8 --》http://localhost:9091/user/8
* PrefixPath=/user/abc  http://localhost:10010/8 --》http://localhost:9091/user/abc/8

####  去除前缀

在gateway中可以通过配置路由的过滤器`StripPrefix`，实现映射路径中地址的去除；

```yaml
server:
  port: 10010
spring:
  application:
    name: api-gateway
  cloud:
    gateway:
      routes:
        # 路由id，可以随意写
        - id: user-service-route
          # 代理的服务地址；lb表示从eureka中获取具体服务
          uri: lb://user-service
          # 路由断言，可以配置映射路径
          predicates:
            - Path=/api/user/**
          filters:
            # 表示过滤1个路径，2表示两个路径，以此类推
            - StripPrefix=1
eureka:
  client:
    service-url:
      defaultZone: http://127.0.0.1:10086/eureka
  instance:
    prefer-ip-address: true
```

通过 StripPrefix=1 来指定了路由要去掉的前缀个数。如：路径 /api/user/1 将会被代理到 /user/1 。

* StripPrefix=1  http://localhost:10010/api/user/8 --》http://localhost:9091/user/8
* StripPrefix=2  http://localhost:10010/api/user/8 --》http://localhost:9091/8

### 过滤器

> Gateway作为网关的其中一个重要功能，就是实现`请求的鉴权`。而这个动作往往是通过网关提供的过滤器来实现的。路由前缀的功能也是使用过滤器实现的。

Gateway自带过滤器有几十个，常见自带过滤器有：

| 过滤器名称             | 说明                         |
| ---------------------- | ---------------------------- |
| `AddRequestHeader`     | 对匹配上的请求加上Header     |
| `AddRequestParameters` | 对匹配上的请求路由添加参数   |
| `AddResponseHeader`    | 对从网关返回的响应添加Header |
| `StripPrefix`          | 对匹配上的请求路径去除前缀   |

#### 配置全局默认过滤器

这些自带的过滤器可以和使用 路由前缀 的用法类似，也可以将这些过滤器配置成不只是针对某个路由；而是可以对`所有路由`生效，也就是配置默认过滤器：

```yaml
server:
  port: 10010
spring:
  application:
    name: api-gateway
  cloud:
    gateway:
      # 默认过滤器，对所有路由生效
      default-filters:
        # 响应头过滤器，对输出的响应设置其头部属性名称为X-Response-Default-MyName，值为itcast；
如果有多个参数多则重写一行设置不同的参数
        - AddResponseHeader=X-Response-Default-MyName, itcast
      routes:
        # 路由id，可以随意写
        - id: user-service-route
          # 代理的服务地址；lb表示从eureka中获取具体服务
          uri: lb://user-service
          # 路由断言，可以配置映射路径
          predicates:
            - Path=/api/user/**
          filters:
            # 表示过滤1个路径，2表示两个路径，以此类推
            - StripPrefix=1
```

#### 过滤器类型

*  局部过滤器：通过 spring.cloud.gateway.routes.filters  配置在具体路由下，只作用在当前路由上；自带的过滤器都可以配置或者自定义按照自带过滤器的方式。如果配置spring.cloud.gateway.default-filters 上会对所有路由生效也算是全局的过滤器；但是这些过滤器的实现上都是要实现GatewayFilterFactory接口。
*  全局过滤器：不需要在配置文件中配置，作用在所有的路由上；实现 GlobalFilter  接口即可。

#### 执行生命周期

Spring Cloud Gateway 的 Filter 的生命周期也类似Spring MVC的拦截器有两个：“pre” 和 “post”。“pre”和 “post” 分别会在请求被执行前调用和被执行后调用。

![](https://pic.downk.cc/item/5fe552db3ffa7d37b3dfe988.jpg)

这里的 pre  和 post  可以通过过滤器的 GatewayFilterChain 执行filter方法前后来实现

#### 使用场景

* 请求鉴权：一般 GatewayFilterChain 执行filter方法前，如果发现没有访问权限，直接就返回空。
* 异常处理：一般 GatewayFilterChain 执行filter方法后，记录异常并返回。
* 服务调用时长统计： GatewayFilterChain 执行filter方法前后根据时间统计。

###  自定义过滤器

> 在application.yml中对某个路由配置过滤器，该过滤器可以在控制台输出配置文件中指定名称的请求参数的值。

#### 编写过滤器

在gateway工程编写过滤器工厂类MyParamGatewayFilterFactory

```java
@Component
public class MyParamGatewayFilterFactory extends 
AbstractGatewayFilterFactory<MyParamGatewayFilterFactory.Config> {
    public static final String PARAM_NAME = "param";
    public MyParamGatewayFilterFactory() {
        super(Config.class);
    }
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(PARAM_NAME);
    }
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (request.getQueryParams().containsKey(config.param)) {
                request.getQueryParams().get(config.param)
                        .forEach(value -> System.out.printf("----------局部过滤器-----%s 
= %s-----",
                                config.param, value));
            }
            return chain.filter(exchange);
        };
    }
    public static class Config {
        private String param;
        public String getParam() {
            return param;
        }
        public void setParam(String param) {
            this.param = param;
        }
    }
}
```

#### 修改配置文件

在gateway工程修改 gateway\src\main\resources\application.yml 配置文件

```yaml
server:
port: 10010
spring:
  application:
    name: api-gateway
  cloud:
    gateway:
      routes:
        # 路由id，可以随意写
        - id: user-service-route
          # 代理的服务地址；lb表示从eureka中获取具体服务
          uri: lb://user-service
          # 路由断言，可以配置映射路径
          predicates:
            - Path=/api/user/**
          filters:
            # 表示过滤1个路径，2表示两个路径，以此类推
            - StripPrefix=1
            # 自定义过滤器
            - MyParam=name
eureka:
  client:
    service-url:
      defaultZone: http://127.0.0.1:10086/eureka
  instance:
    prefer-ip-address: true
```

`自定义过滤器的命名应该为：***GatewayFilterFactory`

####  自定义全局过滤器

> 模拟一个登录的校验。基本逻辑：如果请求中有token参数，则认为请求有效，放行

编写全局过滤器类MyGlobalFilter

```java
@Component
public class MyGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("-----------------全局过滤器MyGlobalFilter-------------------
--");
        String token = exchange.getRequest().getQueryParams().getFirst("token");
        if (StringUtils.isBlank(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }
    @Override
    public int getOrder() {
        //值越小越先执行
        return 1;
    }
}
```

### 负载均衡和熔断（了解）

Gateway中默认就已经集成了Ribbon负载均衡和Hystrix熔断机制。但是所有的超时策略都是走的默认值，比如熔断超时时间只有1S，很容易就触发了。因此建议手动进行配置：

```yaml
hystrix:
  command:
    default:
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 6000
ribbon:
  ConnectTimeout: 1000
  ReadTimeout: 2000
  MaxAutoRetries: 0
  MaxAutoRetriesNextServer: 0
```

### Gateway跨域配置

> 一般网关都是所有微服务的统一入口，必然在被调用的时候会出现跨域问题。
>
> `跨域`：在js请求访问中，如果访问的地址与当前服务器的域名、ip或者端口号`不一致`则称为跨域请求。若不解决则不能获取到对应地址的返回结果。
>
> 如：从在http://localhost:9090中的js访问 http://localhost:9000的数据，因为端口不同，所以也是跨域请求。

在访问Spring Cloud Gateway网关服务器的时候，出现跨域问题的话；可以在网关服务器中通过配置解决，允许哪些服务是可以跨域请求的；具体配置如下：

```yaml
spring:
  cloud:
    gateway:
      globalcors:
        corsConfigurations:
          '[/**]':
            #allowedOrigins: * # 这种写法或者下面的都可以，*表示全部
            allowedOrigins:
            - "http://docs.spring.io"
            allowedMethods:
            - GET
```

上述配置表示：可以允许来自 http://docs.spring.io 的get请求方式获取服务数据。

`allowedOrigins` 指定允许访问的服务器地址，如：http://localhost:10000 也是可以的。

`'[/**]'` 表示对所有访问到网关服务器的请求地址

### Gateway的高可用（了解）

启动多个Gateway服务，自动注册到Eureka，形成集群。如果是服务内部访问，访问Gateway，自动负载均衡，没问题。
但是，Gateway更多是外部访问，PC端、移动端等。它们无法通过Eureka进行负载均衡，那么该怎么办？
此时，可以使用其它的服务网关，来对Gateway进行代理。比如：Nginx

### Gateway与Feign的区别

* Gateway 作为整个应用的流量入口，接收所有的请求，如PC、移动端等，并且将不同的请求转发至不同的处理微服务模块，其作用可视为nginx；大部分情况下用作权限鉴定、服务端流量控制
* Feign 则是将当前微服务的部分服务接口暴露出来，并且主要用于各个微服务之间的服务调用

## Spring Cloud Config分布式配置中心

> 在分布式系统中，由于服务数量非常多，配置文件分散在不同的微服务项目中，管理不方便。为了方便配置文件集中管理，需要分布式配置中心组件。在Spring Cloud中，提供了Spring Cloud Config，它支持配置文件放在配置服务的本地，也支持放在远程Git仓库（GitHub、码云）。
>
> 配置中心本质上也是一个微服务，同样需要注册到Eureka服务注册中心！

![](https://pic.downk.cc/item/5fe554ec3ffa7d37b3e332c2.jpg)

###  Git配置管理

####  创建配置文件

在新建的仓库中创建需要被统一配置管理的配置文件。

配置文件的命名方式: {application}-{profile}.yml 或 {application}-{profile}.properties

application为应用名称
profile用于区分开发环境，测试环境、生产环境等

如user-dev.yml，表示用户微服务开发环境下使用的配置文件。

创建 user-dev.yml

```yaml
server:
  port: ${port:9091}
spring:
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/springcloud
    username: root
    password: root
  application:
    #应用名
    name: user-service
mybatis:
  type-aliases-package: com.itheima.user.pojo
eureka:
  client:
    service-url:
      defaultZone: http://127.0.0.1:10086/eureka
  instance:
  ip-address: 127.0.0.1
    prefer-ip-address: true
    lease-expiration-duration-in-seconds: 90
    lease-renewal-interval-in-seconds: 30
```

####  搭建配置中心微服务

pom.xml

```xml
<dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
        </dependency>
    </dependencies>
```

####  启动类

config-server 的启动类

```java
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
```

#### 配置文件

 config-server 的配置文件

```yaml
server:
  port: 12000
spring:
  application:
    name: config-server
  cloud:
    config:
      server:
        git:
          uri: https://gitee.com/autumnmoonming/moomin-config.git
eureka:
  client:
    service-url:
      defaultZone: http://127.0.0.1:10086/eureka
```

注意上述的 spring.cloud.config.server.git.uri 则是在码云创建的仓库地址；

#### 启动测试

启动eureka注册中心和配置中心；然后访问http://localhost:12000/user-dev.yml ，查看能否输出在码云存储管理的user-dev.yml文件。并且可以在gitee上修改user-dev.yml然后刷新上述测试地址也能及时到最新数据。

### 获取配置中心配置

完成了`配置中心`微服务的搭建，改造一下用户微服务 user-service ，配置文件信息不再由微服务项目提供，而是`从配置中心获取`。如下对 user-service 工程进行改造

#### 添加依赖

pom.xml

```xml
<dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
            <version>2.1.1.RELEASE</version>
        </dependency>
```

#### 修改配置

删除 user-service 工程的 user-service\src\main\resources\application.yml 文件

创建 user-service 工程 user-service\src\main\resources\bootstrap.yml 配置文件

```yaml
spring:
  cloud:
    config:
      # 与远程仓库中的配置文件的application保持一致
      name: user
      # 远程仓库中的配置文件的profile保持一致
      profile: dev
      # 远程仓库中的版本保持一致
      label: master
      discovery:
        # 使用配置中心
        enabled: true
        # 配置中心服务id
        service-id: config-server
eureka:
  client:
    service-url:
      defaultZone: http://127.0.0.1:10086/eureka
```

`bootstrap.yml`文件也是Spring Boot的默认配置文件，而且其加载的时间相比于application.yml`更早`。application.yml和bootstrap.yml虽然都是Spring Boot的默认配置文件，但是定位却不相同。bootstrap.yml可以理解成`系统级别`的一些参数配置，这些参数一般是不会变动的。application.yml 可以用来定义`应用级`别的参数，如果搭配 spring cloud config 使用，application.yml 里面定义的文件可以实现`动态替换`。

总结就是，bootstrap.yml文件相当于项目启动时的`引导`文件，内容相对固定。application.yml文件是微服务的一些`常规`配置参数，变化比较频繁。

#### 启动测试

启动注册中心 eureka-server 、配置中心 config-server 、用户服务 user-service ，如果启动没有报错其实已经使用上配置中心内容，可以到注册中心查看，也可以检验 user-service 的服务。

## Spring Cloud Bus服务总线

> 完成了将微服务中的配置文件集中存储在远程Git仓库，并且通过配置中心微服务从Git仓库拉取配置文件，当用户微服务`启动`时会连接配置中心获取配置信息从而启动用户微服务。更新Git仓库中的配置文件，想在不重启微服务的情况下更新配置， 可以使用Spring Cloud Bus来`实现配置的自动更新`。
>
> 需要注意的是Spring Cloud Bus底层是基于RabbitMQ实现的，默认使用本地的消息队列服务，所以需要提前启动本地RabbitMQ服务（安装RabbitMQ以后才有）

Spring Cloud Bus是用轻量的`消息代理`将分布式的节点连接起来，可以用于广播`配置文件的更改`或者`服务的监控管理`。也就是`消息总线`可以为微服务做监控，也可以实现应用程序之间相互通信。 Spring Cloud Bus可选的消息代理有RabbitMQ和Kafka。

![](https://pic.downk.cc/item/5fe557bc3ffa7d37b3e8100d.jpg)

### 改造配置中心

 在 config-server 项目的pom.xml文件中加入Spring Cloud Bus相关依赖

```xml
<dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-bus</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-stream-binder-rabbit</artifactId>
        </dependency>
```

在 config-server 项目修改application.yml文件如下

```yaml
server:
  port: 12000
spring:
  application:
    name: config-server
  cloud:
    config:
      server:
        git:
          uri: https://gitee.com/liaojianbin/heima-config.git
  # rabbitmq的配置信息；如下配置的rabbit都是默认值，其实可以完全不配置
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
eureka:
  client:
    service-url:
      defaultZone: http://127.0.0.1:10086/eureka
management:
  endpoints:
    web:
      exposure:
        # 暴露触发消息总线的地址
        include: bus-refresh
        rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
eureka:
  client:
    service-url:
      defaultZone: http://127.0.0.1:10086/eureka
management:
  endpoints:
    web:
      exposure:
        # 暴露触发消息总线的地址
        include: bus-refresh
```

### 改造用户服务

在用户微服务 user-service 项目的pom.xml中加入Spring Cloud Bus相关依赖

```xml
<dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-bus</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-stream-binder-rabbit</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
```

修改 user-service 项目的bootstrap.yml如下：

```yaml
spring:
  cloud:
    config:
      # 与远程仓库中的配置文件的application保持一致
      name: user
      # 远程仓库中的配置文件的profile保持一致
      profile: dev
      # 远程仓库中的版本保持一致
      label: master
      discovery:
        # 使用配置中心
        enabled: true
        # 配置中心服务id
        service-id: config-server
  # rabbitmq的配置信息；如下配置的rabbit都是默认值，其实可以完全不配置
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
eureka:
  client:
    service-url:
      defaultZone: http://127.0.0.1:10086/eureka
```

 改造用户微服务 user-service 项目的UserController

`@RefreshScope`//刷新配置

### 测试

> 1. Postman或者RESTClient是一个可以模拟浏览器发送各种请求（POST、GET、PUT、DELETE等）的工具
> 2. 请求地址http://127.0.0.1:12000/actuator/bus-refresh中 /actuator是固定的，/bus-refresh对应的是配置
>    中心config-server中的application.yml文件的配置项include的内容
> 3. 请求http://127.0.0.1:12000/actuator/bus-refresh地址的作用是访问配置中心的消息总线服务，消息总线服务接收到请求后会向消息队列中发送消息，各个微服务会监听消息队列。当微服务接收到队列中的消息后，
>    会重新从配置中心获取最新的配置信息。

依次启动注册中心 eureka-server 、配置中心 config-server 、用户服务 user-service
访问用户微服务http://localhost:9091/user/8；查看IDEA控制台输出结果

修改Git仓库中配置文件 user-dev.yml 的 test.name 内容
使用Postman或者RESTClient工具发送POST方式请求访问地址http://127.0.0.1:12000/actuator/bus-refresh

访问用户微服务系统控制台查看输出结果

