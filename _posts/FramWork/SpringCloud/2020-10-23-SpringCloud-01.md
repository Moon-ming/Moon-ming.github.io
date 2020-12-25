---
layout: post
title: "SpringCloud-01"
subtitle: "「Eureka注册中心、Ribbon负载均衡、Hystrix熔断器」"
author: "月明"
date:  2020-10-23 19:30:00
header-img: "assets/background7.png"
header-mask: 0.3
tags:
  - FrameWork
  - SpringCloud
  - 雪崩问题
  - 服务熔断
---

# SpringCloud

> 微服务架构是使用一套小服务来开发单个应用的方式或途径，每个服务基于单一业务能力构建，运行在自己的进程中，并使用轻量级机制通信，通常是HTTP API，并能够通过自动化部署机制来独立部署。这些服务可以使用不同的编程语言实现，以及不同数据存储技术，并保持最低限度的集中式管理。
>
> API Gateway网关是一个服务器，是系统的唯一入口。为每个客户端提供一个定制的API。API网关核心是，所有的客户端和消费端都通过统一的网关接入微服务，`在网关层处理所有的非业务功能`。如它还可以具有其它职责，如`身份验证、监控、负载均衡、缓存、请求分片与管理、静态响应处理`。通常，网关提供RESTful/HTTP的方式访问服务。而服务端通过服务注册中心进行服务注册和管理。

![](https://pic.downk.cc/item/5fe55eea3ffa7d37b3f4aed4.jpg)

![](https://pic.downk.cc/item/5fe4483e3ffa7d37b377997b.jpg)

微服务的特点：

* 单一职责：微服务中每一个服务都对应唯一的业务能力，做到单一职责
* 微：微服务的服务拆分粒度很小，例如一个用户管理就可以作为一个服务。每个服务虽小，但“五脏俱全”。
* 面向服务：面向服务是说每个服务都要对外暴露Rest风格服务接口API。并不关心服务的技术实现，做到与平台和语言无关，也不限定用什么技术实现，只要提供Rest的接口即可。
* 自治：自治是说服务间互相独立，互不干扰
  * 团队独立：每个服务都是一个独立的开发团队，人数不能过多。
  * 技术独立：因为是面向服务，提供Rest接口，使用什么技术没有别人干涉
  * 前后端分离：采用前后端分离开发，提供统一Rest接口，后端不用再为PC、移动端开发不同接口
  * 数据库分离：每个服务都使用自己的数据源
  * 部署独立，服务间虽然有调用，但要做到服务重启不影响其它服务。有利于持续集成和持续交付。每个服
    务都是独立的组件，可复用，可替换，降低耦合，易维护

微服务架构与SOA都是对系统进行拆分；微服务架构基于SOA思想，可以把微服务当做去除了ESB的SOA。ESB是SOA架构中的中心总线，设计图形应该是星形的，而微服务是去中心化的分布式软件架构。两者比较类似，但其实也有一些差别：

| 功能     | SOA                  | 微服务                         |
| -------- | -------------------- | ------------------------------ |
| 组件大小 | 大块业务逻辑         | 单独任务或小块业务逻辑         |
| 耦合     | 通常松耦合           | 总是松耦合                     |
| 管理     | 着重中央管理         | 着重分散管理                   |
| 目标     | 确保应用能够交互操作 | 易维护、易扩展、更轻量级的交互 |

## 服务调用方式

常见的远程调用方式有以下2种：

* RPC：`Remote Produce Call`远程过程调用，RPC基于`Socket`，工作在会话层。自定义数据格式，速度快，效率高。早期的webservice，现在热门的dubbo，都是RPC的典型代表。
* Http：http其实是一种`网络传输协议`，基于TCP，工作在应用层，规定了数据传输的格式。现在客户端浏览器与服务端通信基本都是采用Http协议，也可以用来进行远程服务调用。缺点是消息封装臃肿，优势是对服务的提供和调用方没有任何技术限定，自由灵活，更符合微服务理念。

现在热门的Rest风格，就可以通过http协议来实现。

区别：RPC的机制是根据语言的API（language API）来定义的，而不是根据基于网络的应用来定义的。

如果公司全部采用Java技术栈，那么使用`Dubbo`作为微服务架构是一个不错的选择。

相反，如果公司的技术栈多样化，而且你更青睐Spring家族，那么Spring Cloud搭建微服务是不二之选。在项目中，会选择Spring Cloud套件，因此会使用`Http方式`来实现服务间调用。

## Http客户端工具

微服务选择了Http，那么我们就需要考虑自己来实现对请求和响应的处理。不过开源世界已经有很多的http客户端工具，能够帮助我们做这些事情，例如

* HttpClient
* OKHttp
* URLConnection

不过这些不同的客户端，API各不相同。而Spring也有对http的客户端进行封装，提供了工具类叫RestTemplate。

## Spring的RestTemplate

Spring提供了一个RestTemplate模板工具类，对基于`Http`的客户端进行了封装，并且实现了对象与json的序列化和反序列化，非常方便。RestTemplate并没有限定Http的客户端类型，而是进行了抽象，目前常用的3种都有支持：

* HttpClient
* OkHttp
* JDK原生的URLConnection（默认的）

可以在启动类位置注册，注册一个 RestTemplate 对象

`````````````````````java
@SpringBootApplication
public class HttpDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(HttpDemoApplication.class, args);
    }
    @Bean
    public RestTemplate restTemplate(){
    return new RestTemplate();
    }
}
`````````````````````

启动springboot项目，在项目中的测试类中直接 @Autowired 注入

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class RestTemplateTest {
    @Autowired
    private RestTemplate restTemplate;
    @Test
    public void test(){
        //如果要测试需要启动spring boot项目，以便获取数据
        String url = "http://localhost/user/8";
        User user = restTemplate.getForObject(url, User.class);
        System.out.println(user);
    }
}
```

通过RestTemplate的getForObject()方法，传递`url地址`及`实体类的字节码`

RestTemplate会自动发起请求，接收响应，并且帮我们对响应结果进行反序列化。

## SpringCloud

配置管理，服务发现，智能路由，负载均衡，熔断器，控制总线，集群状态等功能；协调分布式环境中各个系统，为各类服务提供模板性配置。其主要涉及的组件包括：

* Eureka：注册中心
* Zuul、Gateway：服务网关
* Ribbon：负载均衡
* Feign：服务调用
* Hystrix或Resilience4j：熔断器

![](https://pic.downk.cc/item/5fe4a14f3ffa7d37b321eafc.jpg)

Spring Cloud不是一个组件，而是许多组件的集合；它的版本命名比较特殊，是以A到Z的为首字母的一些单词（其实是伦敦地铁站的名字）组成

## 分布式服务面临的问题

* 服务管理

  * 如何自动注册和发现

    Eureka，负责管理、记录服务提供者的信息。服务调用者无需自己寻找服务，而是把自己的`需求`告诉Eureka，然后Eureka会把符合你需求的服务告诉你。

  * 如何实现状态监管

    同时，服务提供方与Eureka之间通过 `“心跳” 机制`进行监控，当某个服务提供方出现问题，Eureka自然会把它从服务列表中剔除。

    ![](https://pic.downk.cc/item/5fe4a4b63ffa7d37b3276e81.jpg)

    > * Eureka：就是服务注册中心（可以是一个集群），对外暴露自己的地址
    > * 提供者：启动后向Eureka注册自己信息（`地址，提供什么服务`）
    > * 消费者：向Eureka订阅服务，Eureka会将对应服务的所有提供者地址列表发送给消费者，并且定期更新
    > * 心跳(续约)：提供者定期通过http方式向Eureka刷新自己的状态

  * 如何实现动态路由

* 服务如何实现负载均衡

  实际环境中，往往会开启很多个 user-service 的集群。此时获取的服务列表中就会有多个，到底该访问哪一个呢？

  `Ribbon`是Netflix发布的负载均衡器，它有助于控制HTTP和TCP客户端的行为。为Ribbon配置服务提供者·后，RIbbon就可基于某种负载均衡算法，·服务消费者去请求。Ribbon默认为我们提供了很多的负载均衡算法，例如轮询、随机等。也可以实现自定义的负载均衡算法。

* 服务如何解决容灾问题

* 服务如何实现统一配置

## Eureka注册中心

### 搭建eureka-server

创建一个项目 eureka-server ，启动一个Eureka Server Application服务注册中心

pom.xml

```xml
<dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-						server</artifactId>
        </dependency>
    </dependencies>
```

#### 编写启动类

`@EnableEurekaServer`

EurekaServerApplication.java

```java
//声明当前应用为eureka服务
@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class);
    }
}
```

#### 编写配置文件

eureka-server\src\main\resources\application.yml

```yaml
server:
  port: 10086
spring:
  application:
    name: eureka-server # 应用名称，会在Eureka中作为服务的id标识（serviceId）
eureka:
  client:
    service-url: # EurekaServer的地址，现在是自己的地址，如果是集群，需要写其它Server的地址。
      defaultZone: http://127.0.0.1:10086/eureka
    register-with-eureka: false # 不注册自己
    fetch-registry: false #不拉取
```

#### 启动服务

启动 eureka-server  访问：http://127.0.0.1:10086

### 服务注册

注册服务，就是在服务上添加Eureka的客户端依赖，客户端代码会自动把服务注册到EurekaServer中。

#### 添加依赖

user-service中添加Eureka客户端依赖：

```xml
<!-- Eureka客户端 -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

#### 修改启动类

`@EnableDiscoveryClient `

在启动类上开启Eureka客户端功能

```java
@SpringBootApplication
@MapperScan("io.moomin.user.mapper")
@EnableDiscoveryClient // 开启Eureka客户端发现功能
public class UserServiceDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceDemoApplication.class, args);
    }
}
```

#### 修改配置文件

user-service\src\main\resources\application.yml配置文件

```yaml
server:
  port: 9091
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
  type-aliases-package: io.moomin.user.pojo
eureka:
  client:
    service-url:
      defaultZone: http://localhost:10086/eureka
```

> 添加了spring.application.name属性来指定应用名称，将来会作为服务的id使用。

#### 测试

重启 user-service 项目，访问Eureka监控页面

### 服务发现

方法与服务提供者类似，只需要在项目中添加EurekaClient依赖，就可以通过服务名称来获取信息了！

#### 添加依赖

```xml
<!-- Eureka客户端 -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

#### 修改启动类

```java
@SpringBootApplication
@EnableDiscoveryClient
public class ConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
```

#### 新增配置文件

consumer-demo\src\main\resources\application.yml配置文件

```yaml
server:
  port: 8080
spring:
  application:
    name: consumer-demo # 应用名称
eureka:
  client:
    service-url: # EurekaServer地址
      defaultZone: http://127.0.0.1:10086/eureka
```

#### 修改处理器

修改ConsumerController.java 

用`DiscoveryClient`类的方法，根据服务名称，获取服务实例。

```java
@RestController
@RequestMapping("/consumer")
public class ConsumerController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;
    @GetMapping("{id}")
    public User queryById(@PathVariable Long id){
        String url = "http://localhost:9091/user/" + id;
        //获取eureka中注册的user-service实例列表
        List<ServiceInstance> serviceInstanceList = 
			discoveryClient.getInstances("user-service");
        ServiceInstance serviceInstance = 										serviceInstanceList.get(0);
        url = "http://" + serviceInstance.getHost() + ":"
            + serviceInstance.getPort() + "/user/" + id;
        return restTemplate.getForObject(url, User.class);
    }
}
```

###  Eureka详解

Eureka架构中的三个核心角色：

1. 服务注册中心

   Eureka的`服务端应用`，提供服务注册和发现功能，就是eureka-server

2. 服务提供者

   提供服务的应用，可以是SpringBoot应用，也可以是其它任意技术实现，只要对外提供的是Rest风格服务即可。user-service

3. 服务消费者

   消费应用从注册中心获取服务列表，从而得知每个服务方的信息，知道去哪里调用服务方。consumer-demo

#### 高可用的Eureka Server

Eureka Server即服务的注册中心，事实上EurekaServer也可以是一个集群，形成高可用的Eureka中心。

##### 服务同步

多个Eureka Server之间也会互相注册为服务，

当服务提供者注册到Eureka Server集群中的某个节点时，该节点会把服务的信息`同步给集群中的每个节点`，从而实现数据同步。因此，无论客户端访问到Eureka Server集群中的任意一个节点，都可以获取到完整的服务列表信息。

而作为客户端，需要把信息注册到每个Eureka中：

![](https://pic.downk.cc/item/5fe4ad093ffa7d37b33524d8.jpg)

> 如果有三个Eureka，则每一个EurekaServer都需要注册到其它几个Eureka服务中

##### 搭建高可用的EurekaServer

> 所谓的高可用注册中心，其实就是把EurekaServer自己也作为一个服务，注册到其它EurekaServer上，这样多个EurekaServer之间就能互相发现对方，从而形成集群。

假设要搭建两台EurekaServer的集群，端口分别为：10086和10087

1. 修改原来的EurekaServer配置；修改 eureka-server\src\main\resources\application.yml  如下：

   ```yaml
   server:
     port: ${port:10086}
   spring:
     application:
       # 应用名称，会在eureka中作为服务的id(serviceId)
       name: eureka-server
   eureka:
     client:
       service-url:
         # eureka服务地址；如果是集群则是其它服务器地址，后面要加/eureka
         defaultZone: ${defaultZone:http://127.0.0.1:10086/eureka}
       # 是否注册自己，自身不提供服务所以不注册
       #register-with-eureka: false
       # 是否拉取服务
       #fetch-registry: false
   ```

   * 注意把register-with-eureka和fetch-registry修改为true或者注释掉
   * `${}`表示在jvm启动时候若能找到对应port或者defaultZone参数则使用，若无则使用后面的默认值
   * 把service-url的值改成了另外一台EurekaServer的地址，而不是自己

2. 另外一台在启动的时候可以指定端口port和defaultZone配置：

   修改原来的启动配置组件；在如下界面中的 VM options 中设置 `-DdefaultZone=http:127.0.0.1:10087/eureka`

   ![](https://pic.downk.cc/item/5fe4ae413ffa7d37b336f93b.jpg)

   设置 `-Dport=10087 -DdefaultZone=http:127.0.0.1:10086/eureka`

   ![](https://pic.downk.cc/item/5fe4ae5b3ffa7d37b33722cd.jpg)

3. 启动测试；同时启动两台eureka server

4. 客户端注册服务到集群

   因为EurekaServer不止一个，因此 user-service 项目注册服务或者 consumer-demo 获取服务的时候，service-url参数需要修改为如下：

   ```yaml
   eureka:
     client:
       service-url: # EurekaServer地址,多个地址以','隔开
         defaultZone: http://127.0.0.1:10086/eureka,http://127.0.0.1:10087/eureka
   ```

#### Eureka客户端

服务提供者要向EurekaServer注册服务，并且完成服务续约等工作。

##### 服务注册

服务提供者在启动时，会检测配置属性中的： eureka.client.register-with-erueka=true 参数是否正确，事实上默认就是true。如果值确实为true，则会向EurekaServer发起一个`Rest请求`，并携带自己的`元数据信息`，EurekaServer会把这些信息保存到一个双层Map结构中。

* 第一层Map的Key就是`服务id`，一般是配置中的 spring.application.name 属性
* 第二层Map的key是服务的`实例id`。一般host+ serviceId + port，例如： localhost:user-service:8081值则是服务的`实例对象`，也就是说一个服务，可以同时启动多个不同实例，形成集群。

默认注册时使用的是主机名或者localhost，如果想用ip进行注册，可以在 user-service 中添加配置如下：

```yaml
eureka:
  instance:
    ip-address: 127.0.0.1 # ip地址
    prefer-ip-address: true # 更倾向于使用ip，而不是host名
```

##### 服务续约

在注册服务完成以后，服务提供者会维持一个心跳（`定时`向EurekaServer发起Rest请求），告诉EurekaServer：“我还活着”。这个我们称为`服务的续约（renew）`；

有两个重要参数可以修改服务续约的行为，可以在 user-service  中添加如下配置项：

```yaml
eureka:
  instance:
    lease-expiration-duration-in-seconds: 90
    lease-renewal-interval-in-seconds: 30
```

* `lease-renewal-interval-in-seconds`：服务`续约`(renew)的间隔，默认为30秒
* `lease-expiration-duration-in-seconds`：服务`失效`时间，默认值90秒

##### 获取服务列表

当服务消费者启动时，会检测 `eureka.client.fetch-registry=true `参数的值，如果为true，则会从EurekaServer服务的列表拉取`只读备份`，然后`缓存在本地`。并且 每隔30秒 会重新拉取并更新数据。可以在 consumer项目中通过下面的参数来修改：

```yaml
eureka:
  client:
    registry-fetch-interval-seconds: 30
```

#### 失效剔除和自我保护

如下的配置都是在Eureka Server服务端进行：

##### 服务下线

当服务进行正常关闭操作时，它会触发一个`服务下线的REST请求`给Eureka Server，告诉服务注册中心：“我要下线了”。服务中心接受到请求之后，将该服务置为下线状态。

##### 失效剔除

有时我们的服务可能由于`内存溢出`或`网络故障`等原因使得服务不能正常的工作，而服务注册中心并未收到“服务下线”的请求。相对于服务提供者的“服务续约”操作，服务注册中心在启动时会创建一个定时任务，默认每隔一段时间（默认为`60秒`）将当前清单中`超时`（默认为90秒）`没有续约`的服务`剔除`，这个操作被称为失效剔除。

` eureka.server.eviction-interval-timer-in-ms` 参数对其进行修改，单位是毫秒

##### 自我保护

我们关停一个服务，很可能会在Eureka面板看到一条警告：

![](https://pic.downk.cc/item/5fe4b0363ffa7d37b33a16c7.jpg)

这是触发了Eureka的自我保护机制。当服务未按时进行心跳续约时，Eureka会统计服务实例最近15分钟心跳续约的比例是否低于了85%。在生产环境下，因为网络延迟等原因，心跳失败实例的比例很有可能超标，但是此时就把服务剔除列表并不妥当，因为服务可能没有宕机。Eureka在这段时间内不会剔除任何服务实例，直到网络恢复正常。生产环境下这很有效，保证了大多数服务依然可用，不过也有可能获取到失败的服务实例，因此`服务调用者必须做好服务的失败容错`。

关停自我保护

```yaml
eureka:
  server:
    enable-self-preservation: false # 关闭自我保护模式（缺省为打开）
```

## 负载均衡Ribbon

### 开启负载均衡

` @LoadBalanced `

因为Eureka中已经集成了Ribbon，所以我们无需引入新的依赖。

ConsumerApplication.java

```java
@Bean
@LoadBalanced
public RestTemplate restTemplate() {
    return new RestTemplate();
}
```

ConsumerController.java 调用方式，不再手动获取ip和端口，而是直接`通过服务名称`调用；

```java
@GetMapping("{id}")
public User queryById(@PathVariable("id") Long id){
    String url = "http://user-service/user/" + id;
    User user = restTemplate.getForObject(url, User.class);
    return user;
}
```

Ribbon默认的负载均衡策略是`轮询`。SpringBoot也帮提供了修改负载均衡规则的配置入口在consumer的配置文件中添加如下，就变成随机的了：

```yaml
user-service:
  ribbon:
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule
```

格式是：` {服务名称}.ribbon.NFLoadBalancerRuleClassName`

###  源码跟踪

有组件根据service名称，获取到了服务实例的ip和端口。因为 consumer使用的是RestTemplate，spring的负载均衡自动配置类 `LoadBalancerAutoConfiguration.LoadBalancerInterceptorConfig `会自动配置负载均衡拦截器（在spring-cloud-commons-**.jar包中的spring.factories中定义的自动配置类）， 它就是`LoadBalancerInterceptor` ，这个类会在对RestTemplate的请求进行拦截，然后从Eureka根据`服务id获取服务列`表，随后利用负载均衡算法得到`真实的服务地址信息`，替换服务id。

## 熔断器Hystrix

> Hystrix 在英文里面的意思是 豪猪，它的logo 是一头豪猪，它在微服务系统中是一款提供`保护机制`的组件，和eureka一样也是由netflix公司开发。
>
> Hystrix是Netflix开源的一个`延迟`和`容错`库，用于隔离访问远程服务、第三方库，防止出现级联失败。

### 雪崩问题

微服务中，服务间调用关系错综复杂，一个请求，可能需要调用多个微服务接口才能实现，会形成非常复杂的调用链路：

![](https://pic.downk.cc/item/5fe4b3373ffa7d37b33f09b1.jpg)

> 如图，一次业务请求，需要调用A、P、H、I四个服务，这四个服务又可能调用其它服务。如果此时，某个服务出现异常：
>
> 微服务I  发生异常，请求阻塞，用户请求就不会得到响应，则tomcat的这个线程不会释放，于是越来越多的用户请求到来，越来越多的线程会阻塞：

![](https://pic.downk.cc/item/5fe4b3783ffa7d37b33f69e1.jpg)

服务器支持的线程和并发数有限，请求一直阻塞，会导致服务器资源耗尽，从而导致所有其它服务都不可用，形成雪崩效应。

Hystrix解决雪崩问题的手段主要是`服务降级`，包括：

* 线程隔离
* 服务熔断

#### 线程隔离

> * Hystrix为每个依赖服务调用分配一个`小的线程池`，如果线程池已满调用将被立即拒绝，默认`不采用排队`，加速失败判定时间。
> * 用户的请求将不再直接访问服务，而是通过线程池中的空闲线程来访问服务，如果`线程池已满`，或者`请求超时`，则会进行降级处理。

![](https://pic.downk.cc/item/5fe4b3e33ffa7d37b3401c39.jpg)

#### 服务降级

> 优先保证核心服务，而非核心服务不可用或弱可用。

用户的请求故障时，不会被阻塞，更不会无休止的等待或者看到系统崩溃，至少可以看到一个执行结果（例如返回友好的提示信息） 。

触发Hystrix服务降级的情况：

* 线程池已满
* 请求超时

### 实践

#### 引入依赖

在 consumer消费端系统的pom.xml文件添加如下依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
```

#### 开启熔断

`@EnableCircuitBreaker`

启动类 ConsumerApplication

```java
@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
public class ConsumerApplication {
    // ...
}
```

在微服务中，经常会引入上面的三个注解，于是Spring就提供了一个组合注
解：`@SpringCloudApplication`

#### 编写降级逻辑

`@HystrixCommand(fallbackMethod="方法名")`用来声明一个降级逻辑的方法

当目标服务的调用出现故障，我们希望快速失败，给用户一个友好提示。因此需要提前编写好失败时的降级处理逻辑，要使用`HystrixCommand`来完成。

ConsumerController.java

```java
@RestController
@RequestMapping("/consumer")
@Slf4j
public class ConsumerController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;
    @GetMapping("{id}")
    @HystrixCommand(fallbackMethod = "queryByIdFallback")
    public String queryById(@PathVariable Long id){
        String url = "http://localhost:9091/user/" + id;
            url = "http://user-service/user/" + id;
        return restTemplate.getForObject(url, String.class);
    }
    public String queryByIdFallback(Long id){
        log.error("查询用户信息失败。id：{}", id);
        return "对不起，网络太拥挤了！";
    }
}
```

要注意；因为熔断的降级逻辑方法必须跟正常逻辑方法保证：`相同的参数列表`和`返回值声明`。失败逻辑中返回User对象没有太大意义，一般会返回友好提示。所以把queryById的方法改造为`返回String`，反正也是Json数据。这样失败逻辑中返回一个错误说明，会比较方便。

#### 默认的Fallback

`@DefaultProperties(defaultFallback = "defaultFallBack")`：在类上指明`统一`的失败降级方法；该类中所有方法返回类型要与处理失败的方法的`返回类型一致`。

fallback写在了某个业务方法上，如果这样的方法很多，那岂不是要写很多。所以可以把Fallback`配置加在类`上，实现默认fallback；

```java
@RestController
@RequestMapping("/consumer")
@Slf4j
@DefaultProperties(defaultFallback = "defaultFallback")
public class ConsumerController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;
    @GetMapping("{id}")
    //@HystrixCommand(fallbackMethod = "queryByIdFallback")
    @HystrixCommand
    public String queryById(@PathVariable Long id){
        String url = "http://localhost:9091/user/" + id;
        url = "http://user-service/user/" + id;
        return restTemplate.getForObject(url, String.class);
    }
    public String queryByIdFallback(Long id){
        log.error("查询用户信息失败。id：{}", id);
        return "对不起，网络太拥挤了！";
    }
    public String defaultFallback(){
        return "默认提示：对不起，网络太拥挤了！";
    }
}
```

#### 超时设置

请求在超过1秒后都会返回错误信息，这是因为Hystrix的`默认超时时长为1`，

可以通过配置修改这个值consumer\src\main\resources\application.yml

```yaml
hystrix:
  command:
    default:
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 2000
```

这个配置会作用于`全局所有方法`。

### 服务熔断

> 在服务熔断中，使用的熔断器，也叫断路器，其英文单词为：`Circuit Breaker`熔断机制与家里使用的电路熔断原理类似；当如果电路发生短路的时候能立刻熔断电路，避免发生灾难。
>
> 在分布式系统中应用服务熔断后；服务`调用方`可以`自己进行判断`哪些服务`反应慢`或存在`大量超时`，可以针对这些服务进行`主动熔断`，防止整个系统被拖垮。
> Hystrix的服务熔断机制，可以实现`弹性容错`；当服务请求情况好转之后，可以自动重连。通过断路的方式，`将后续请求直接拒绝`，一段时间（默认5秒）之后`允许部分请求通过`，如果调用`成功则回到断路器关闭状态`，否则继续打
> 开，拒绝请求的服务。

Hystrix的熔断状态机模型：

![](https://pic.downk.cc/item/5fe4b7943ffa7d37b345c8bf.jpg)

状态机有3个状态：

* `Closed`：关闭状态（断路器关闭），所有请求都正常访问。
* `Open`：打开状态（断路器打开），`所有请求`都会被降级。Hystrix会对请求情况计数，当一定时间内`失败请求百分比`达到阈值，则触发熔断，断路器会完全打开。默认失败比例的阈值是`50%`，请求次数最少`不低于20次`。
* `Half Open`：半开状态，不是永久的，断路器打开后会进入休眠时间（默认是5S）。随后断路器会自动进入半开状态。此时会释放`部分`请求通过，若这些请求`都是健康的`，则会关闭断路器，否则继续保持打开，再次进行休眠计时

#### 实践

为了能够精确控制请求的成功或失败，在 consumer-demo 的处理器业务方法中加入一段逻辑；

```java
@GetMapping("{id}")
@HystrixCommand
public String queryById(@PathVariable("id") Long id){
    if(id == 1){
        throw new RuntimeException("太忙了");
    }
    String url = "http://user-service/user/" + id;
    String user = restTemplate.getForObject(url, String.class);
    return user;
}
```

当访问id为1的请求（超过20次）时，就会触发熔断。断路器会打开，一切请求都会被降级处理。此时你访问id为2的请求，会发现返回的也是失败，而且失败时间很短，只有20毫秒左右；因进入半开状态之后2是可以的。

默认的熔断触发要求较高，休眠时间窗较短，可以通过配置修改熔断策略：

```yaml
# 配置熔断策略：
hystrix:
  command:
    default:
      circuitBreaker:
        errorThresholdPercentage: 50 # 触发熔断错误比例阈值，默认值50%
        sleepWindowInMilliseconds: 10000 # 熔断后休眠时长，默认值5秒
        requestVolumeThreshold: 10 # 熔断触发最小请求次数，默认值是20
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 2000 # 熔断超时设置，默认为1秒
```

