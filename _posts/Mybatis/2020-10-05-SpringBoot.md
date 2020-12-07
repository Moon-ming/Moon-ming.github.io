---
layout: post
title: "SpringBoot"
subtitle: "「核心功能、原理分析、配置文件、技术整合」"
author: "月明"
date:  2020-10-05 16:53:00
header-img: "assets/background11.png"
header-mask: 0.3
tags:
  - FramWork
  - SpringBoot
---

# SpringBoot

> Spring是Java企业版（Java Enterprise Edition，JEE，也称J2EE）的轻量级代替品。无需开发重量级的EnterpriseJavaBean（EJB），Spring为企业级Java开发提供了一种相对简单的方法，通过依赖注入和面向切面编程，用简单的Java对象（Plain Old Java Object，POJO）实现了EJB的功能。
>
> 虽然Spring的组件代码是轻量级的，但它的配置却是重量级的。一开始，Spring用XML配置，而且是很多XML配置。Spring 2.5引入了基于注解的组件扫描，这消除了大量针对应用程序自身组件的显式XML配置。Spring 3.0引入了基于Java的配置，这是一种类型安全的可重构配置方式，可以代替XML。
>
> 所有这些配置都代表了开发时的损耗。因为在思考Spring特性配置和解决业务问题之间需要进行思维切换，所以编写配置挤占了编写应用程序逻辑的时间。和所有框架一样，Spring实用，但与此同时它要求的回报也不少。
>
> 除此之外，项目的依赖管理也是一件耗时耗力的事情。在环境搭建时，需要分析要导入哪些库的坐标，而且还需要分析导入与之有依赖关系的其他库的坐标，一旦选错了依赖的版本，随之而来的不兼容问题就会严重阻碍项目的开发进度。

SpringBoot基于约定优于配置的思想，可以让开发人员不必在配置与逻辑
业务之间进行思维的切换，全身心的投入到逻辑业务的代码编写中，从而大大提高了开发的效率，一定程度上缩短了项目周期。

## 核心功能

* 起步依赖

起步依赖本质上是一个Maven项目对象模型（Project Object Model，POM），定义了对其他库的传递依赖，这些东西加在一起即支持某项功能。
简单的说，起步依赖就是将`具备某种功能的坐标打包到一起`，并提供一些默认的功能。

* 自动配置

Spring Boot的自动配置是一个运行时（更准确地说，是应用程序启动时）的过程，考虑了众多因素，才决定Spring配置应该用哪个，不该用哪个。该过程是Spring自动完成的。

## 原理分析

### spring-boot-starter-parent

```xml
<parent>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-dependencies</artifactId>
  <version>2.0.1.RELEASE</version>
  <relativePath>../../spring-boot-dependencies</relativePath>
</parent>
```

从spring-boot-starter-dependencies的pom.xml中我们可以发现，一部分坐标的版本、依赖管理、插件管理已经定义好，所以SpringBoot工程继承spring-boot-starter-parent后已经具备版本锁定等配置了。起步依赖的作用就是进行依赖的传递。

### spring-boot-starter-web

从spring-boot-starter-web的pom.xml中我们可以发现，就是将web开发要使用的
spring-web、spring-webmvc等坐标进行了“打包”，这样工程只要引入spring-boot-starter-web起步依赖的坐标就可以进行web开发了，同样体现了依赖传递的作用。

### 自动配置原理解析

`@SpringBootApplication`

* @SpringBootConfiguration：等同与@Configuration，既标注该类是Spring的一个配置类
* @EnableAutoConfiguration：SpringBoot自动配置功能开启
  * @Import(AutoConfigurationImportSelector.class) 导入了AutoConfigurationImportSelector类
    * SpringFactoriesLoader.loadFactoryNames 方法的作用就是从META-INF/spring.factories文件中读取指定类对应的类名称列表
      * spring.factories 文件中有关自动配置的配置信息,存在大量的以Configuration为结尾的类名称，这些类就是存有自动配置信息的类，而SpringApplication在获取这些类名后再加载
        * @EnableConfigurationProperties(ServerProperties.class) 代表加载ServerProperties服务器配置属性类
          * ServerProperties.class：prefix = "server" 表示SpringBoot配置文件中的前缀，SpringBoot会将`配置文件中以server开始`的属性映射到该类的字段中。

## 配置文件

>  SpringBoot是基于约定的，所以很多配置都有默认值，但如果想使用自己的配置替换默认配置的话，就可以使用`application.properties`或者`application.yml`（application.yaml）进行配置。
>
> SpringBoot默认会从Resources目录下加载配置文件，其中application.properties文件是键值对类型的文件。

### application.yml配置文件

> YML文件格式是`YAML (YAML Aint Markup Language)`编写的文件格式，YAML是一种直观的能够被电脑识别的的数据数据序列化格式，并且容易被人类阅读，容易和脚本语言交互的，可以被支持YAML库的不同的编程语言程序导
> 入，比如： C/C++, Ruby, Python, Java, Perl, C#, PHP等。YML文件是以数据为核心的，比传统的xml方式更加简洁。

YML文件的扩展名可以使用`.yml`或者`.yaml`。

普通数据

```yaml
  name: haohao
```
对象数据/Map数据

```yaml
  person:
    name: haohao
    age: 31
    addr: beijing
    # person: {name: haohao,age: 31,addr: beijing}
```

数组（List、Set）数据

```yaml
city:
  - beijing
  - tianjin
  - shanghai
  - chongqing
  # city: [beijing,tianjin,shanghai,chongqing]

#集合中的元素是对象形式
student:
  - name: zhangsan
    age: 18
    score: 100
  - name: lisi
    age: 28
    score: 88
  - name: wangwu
    age: 38
    score: 90
```

### 配置文件与配置类的属性映射方式

#### 使用注解@Value映射

可以通过@Value注解将配置文件中的值映射到一个Spring管理的Bean的字段上

```java
@Value("${person.name}")
private String name;
```

#### 使用注解@ConfigurationProperties映射

> 使用@ConfigurationProperties方式可以进行配置文件与实体字段的自动映射，但需要字段`必须提供set方法才可以`，而使用@Value注解修饰的字段不需要提供set方法

通过注解`@ConfigurationProperties(prefix="配置文件中的key的前缀")`可以将配置文件中的配置自动与实体进行映射

```java
@ConfigurationProperties(prefix = "person")
public class QuickStartController {
    private String name;
    
    public void setName(String name) {
        this.name = name;
    }
}
```

## SpringBoot整合Mybatis

添加Mybatis的起步依赖

```xml
<!--mybatis起步依赖-->
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>1.1.1</version>
</dependency>
```

添加数据库驱动坐标

```xml
<!-- MySQL连接驱动 -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
```

添加数据库连接信息

```properties
#DB Configuration:
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql:///springboot?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root

#spring集成Mybatis环境
#pojo别名扫描包
mybatis.type-aliases-package=io.moomin.domain
#加载Mybatis映射文件
mybatis.mapper-locations=classpath:mapper/*Mapper.xml
```

 编写Mapper

> @Mapper标记该类是一个mybatis的mapper接口，可以被spring boot自动扫描到spring上下文中

```java
@Mapper
public interface UserMapper {
    public List<User> queryUserList();
}
```

配置Mapper映射文件

> 在src\main\resources\mapper路径下加入UserMapper.xml配置文件"

```xml
<mapper namespace="io.moomin.mapper.UserMapper">
    <select id="queryUserList" resultType="user">
        select * from user
    </select>
</mapper>
```

## SpringBoot整合Junit

添加Junit的起步依赖

```xml
<!--测试的起步依赖-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

编写测试类

> SpringRunner继承自SpringJUnit4ClassRunner，使用哪一个Spring提供的测试测试引擎都可以
>
> @SpringBootTest的属性指定的是引导类的字节码对象

```java
import org.springframework.test.context.junit4.SpringRunner;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MySpringBootApplication.class)
public class MapperTest {
    @Autowired
    private UserMapper userMapper;
    @Test
    public void test() {
        List<User> users = userMapper.queryUserList();
        System.out.println(users);
    } 
}
```

## SpringBoot整合Spring Data JPA

添加Spring Data JPA的起步依赖

```xml
<!-- springBoot JPA的起步依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

```properties
#JPA Configuration:
spring.jpa.database=MySQL
spring.jpa.show-sql=true
spring.jpa.generate-ddl=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.hibernate.naming_strategy=org.hibernate.cfg.ImprovedNamingStrategy
```

配置实体

```java
@Entity
public class User {
    // 主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 用户名
    private String username;
    // 密码
    private String password;
    // 姓名
    private String name;
 
    //此处省略setter和getter方法... ...
}
```

编写UserRepository

```java
public interface UserRepository extends JpaRepository<User,Long>{
    public List<User> findAll();
}
```

编写测试类

```java
@RunWith(SpringRunner.class)
@SpringBootTest(classes=MySpringBootApplication.class)
public class JpaTest {
 
    @Autowired
    private UserRepository userRepository;
 
    @Test
    public void test(){
        List<User> users = userRepository.findAll();
        System.out.println(users);
    }
}
```

## SpringBoot整合Redis

添加redis的起步依赖

```xml
<!-- 配置使用redis启动器 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

配置redis的连接信息

```properties
#Redis
spring.redis.host=127.0.0.1
spring.redis.port=6379
```

 注入RedisTemplate测试redis操作

```java
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootJpaApplication.class)
public class RedisTest {
 
    @Autowired
    private UserRepository userRepository;
 
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
 
    @Test
    public void test() throws JsonProcessingException {
        //从redis缓存中获得指定的数据
        String userListData = redisTemplate.boundValueOps("user.findAll").get();
        //如果redis中没有数据的话
        if(null==userListData){
            //查询数据库获得数据
            List<User> all = userRepository.findAll();
            //转换成json格式字符串
            ObjectMapper om = new ObjectMapper();
            userListData = om.writeValueAsString(all);
//将数据存储到redis中，下次在查询直接从redis中获得数据，不用在查询数据库
  redisTemplate.boundValueOps("user.findAll").set(userListData);
            System.out.println("===从数据库获得数据===");
        }else{
            System.out.println("===从redis缓存中获得数据===");
        }
        System.out.println(userListData);
    }
}
```

