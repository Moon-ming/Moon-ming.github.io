---
layout: post
title: "SpringData JPA-02"
subtitle: "「环境、整合、基本查询、JPQL查询、SQL查询、方法命名规则查询」"
author: "月明"
date:  2020-10-09 0:51:00
header-img: "assets/background13.png"
header-mask: 0.3
tags:
  - FramWork
  - Spring Data JPA
---

# SpringData JPA

> Spring Data JPA 是 Spring 基于 ORM 框架、JPA 规范的基础上封装的一套 JPA 应用框架，可使开发者用极简的代码即可实现对数据库的访问和操作。它提供了包括增删改查等在内的常用功能，且易于扩展，学习并使用 Spring Data JPA 可以极大提高开发效率。
>
> Spring Data JPA 让我们解脱了 DAO 层的操作，基本上所有 CRUD 都可以依赖于它来实现,使用了 SpringDataJpa，dao 层中只需要写接口，就自动具有了增删改查、分页查询等方法。
>
> 在实际的工作工程中，推荐使用 `Spring Data JPA + ORM`（如：hibernate）完成操作，这样在切换不同的 ORM框架时提供了极大的方便，同时也使数据库层操作更加简单，方便解耦。
>
> Spring Data JPA 是 Spring 提供的一套对 JPA 操作更加高级的封装，是在 JPA 规范下的专门用来进行数据持久化的解决方案。

## 开发环境

> 需要整合 Spring 与 Spring Data JPA，并且需要提供 JPA 的服务提供者 hibernate，所以需要导入 spring 相关坐标，hibernate 坐标，数据库驱动坐标等

```xml
<properties>
<spring.version>4.2.4.RELEASE</spring.version>
<hibernate.version>5.0.7.Final</hibernate.version>
<slf4j.version>1.6.6</slf4j.version>
<log4j.version>1.2.12</log4j.version>
<c3p0.version>0.9.1.2</c3p0.version>
<mysql.version>5.1.6</mysql.version>
</properties>
<dependencies>
<!-- junit单元测试 -->
<dependency>
<groupId>junit</groupId>
<artifactId>junit</artifactId>
<version>4.9</version>
<scope>test</scope>
</dependency>
<!-- spring beg -->
<dependency>
<groupId>org.aspectj</groupId>
<artifactId>aspectjweaver</artifactId>
<version>1.6.8</version>
</dependency>
<dependency>
<groupId>org.springframework</groupId>
<artifactId>spring-aop</artifactId>
<version>${spring.version}</version>
</dependency>
<dependency>
<groupId>org.springframework</groupId>
<artifactId>spring-context</artifactId>
<version>${spring.version}</version>
</dependency>
<dependency>
<groupId>org.springframework</groupId>
<artifactId>spring-context-support</artifactId>
<version>${spring.version}</version>
</dependency>
<dependency>
<groupId>org.springframework</groupId>
<artifactId>spring-orm</artifactId>
<version>${spring.version}</version>
</dependency>
<dependency>
<groupId>org.springframework</groupId>
<artifactId>spring-beans</artifactId>
<version>${spring.version}</version>
</dependency>
<dependency>
<groupId>org.springframework</groupId>
<artifactId>spring-core</artifactId>
<version>${spring.version}</version>
</dependency>
  <!-- spring end -->
<!-- hibernate beg -->
<dependency>
<groupId>org.hibernate</groupId>
<artifactId>hibernate-core</artifactId>
<version>${hibernate.version}</version>
</dependency>
<dependency>
<groupId>org.hibernate</groupId>
<artifactId>hibernate-entitymanager</artifactId>
<version>${hibernate.version}</version>
</dependency>
<dependency>
<groupId>org.hibernate</groupId>
<artifactId>hibernate-validator</artifactId>
<version>5.2.1.Final</version>
</dependency>
<!-- hibernate end -->
<!-- c3p0 beg -->
<dependency>
<groupId>c3p0</groupId>
<artifactId>c3p0</artifactId>
<version>${c3p0.version}</version>
</dependency>
<!-- c3p0 end -->
<!-- log end -->
<dependency>
<groupId>log4j</groupId>
<artifactId>log4j</artifactId>
<version>${log4j.version}</version>
</dependency>
<dependency>
<groupId>org.slf4j</groupId>
<artifactId>slf4j-api</artifactId>
<version>${slf4j.version}</version>
</dependency>
<dependency>
<groupId>org.slf4j</groupId>
<artifactId>slf4j-log4j12</artifactId>
<version>${slf4j.version}</version>
</dependency>
<!-- log end -->
<dependency>
<groupId>mysql</groupId>
<artifactId>mysql-connector-java</artifactId>
<version>${mysql.version}</version>
</dependency>
<dependency>
<groupId>org.springframework.data</groupId>
<artifactId>spring-data-jpa</artifactId>
<version>1.9.0.RELEASE</version>
</dependency>
<dependency>
<groupId>org.springframework</groupId>
<artifactId>spring-test</artifactId>
<version>4.2.4.RELEASE</version>
</dependency>
<!-- el beg 使用spring data jpa 必须引入 -->
<dependency>
<groupId>javax.el</groupId>
<artifactId>javax.el-api</artifactId>
<version>2.2.4</version>
</dependency>
<dependency>
<groupId>org.glassfish.web</groupId>
<artifactId>javax.el</artifactId>
<version>2.2.4</version>
</dependency>
<!-- el end -->
</dependencies> 
```

## 整合 Spring Data JPA 与 Spring

```xml
<!--spring和spring data jpa配置-->
    <!--创建EntityManagerFactory对象交给Spring容器管理-->
    <bean id="entityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <!--配置的扫描的包（实体类所在的包）-->
        <property name="packagesToScan" value="io.moomin.domain"/>
        <!--jpa的实现厂家-->
        <property name="persistenceProvider">
            <bean class="org.hibernate.jpa.HibernatePersistenceProvider"/>
        </property>
        
         <!--jpa的供应商适配器-->
        <property name="jpaVendorAdapter">
            <bean class="org.springframework.orm.jpa.
                         vendor.HibernateJpaVendorAdapter">
                <!--配置是否自动创建数据库表-->
                <property name="generateDdl" value="false"/>
                <!--指定数据库类型，全部大写-->
                <property name="database" value="MYSQL"/>
                <!--数据库方言，支持的特有语法-->
                <property name="databasePlatform"
                      value="org.hibernate.dialect.MySQLDialect"/>
                <!--是否显示sql-->
                <property name="showSql" value="true"/>
            </bean>
        </property>
        
        <!--jpa的方言：高级的特性-->
        <property name="jpaDialect">
            <bean class="org.springframework.orm.jpa
                         .vendor.HibernateJpaDialect" />
        </property>
    </bean>

		<!-- 整合spring data jpa-->
    <jpa:repositories base-package="io.moomin.dao"
                      transaction-manager-ref="transactionManager"
                      entity-manager-factory-										  ref="entityManagerFactory">
    </jpa:repositories>

		<!--配置数据库连接池-->
    <bean id="dataSource" class="com.mchange.v2.c3p0
                                 .ComboPooledDataSource">
        <property name="driverClass" 
                  value="com.mysql.jdbc.Driver" />
        <property name="jdbcUrl" 												  value="jdbc:mysql://localhost:3306/jpa" />
        <property name="user" value="root" />
        <property name="password" value="root" />
    </bean>

		<!-- JPA事务管理器-->
    <bean id="transactionManager"
        class="org.springframework.orm.jpa.JpaTransactionManager">
        <property name="entityManagerFactory" 									  ref="entityManagerFactory" />
    </bean>
		
		<!-- txAdvice-->
    <tx:advice id="txAdvice" 
               transaction-manager="transactionManager">
        <tx:attributes>
            <tx:method name="save*" propagation="REQUIRED"/>
            <tx:method name="insert*" propagation="REQUIRED"/>
            <tx:method name="update*" propagation="REQUIRED"/>
            <tx:method name="delete*" propagation="REQUIRED"/>
            <tx:method name="get*" read-only="true"/>
            <tx:method name="find*" read-only="true"/>
            <tx:method name="*" propagation="REQUIRED"/>
        </tx:attributes>
    </tx:advice>
		
		<!-- aop-->
    <aop:config>
        <aop:pointcut id="pointcut"
           expression="execution(* io.moomin.service.*.*(..))" />
        <aop:advisor advice-ref="txAdvice" 
                     pointcut-ref="pointcut" />
    </aop:config>
    <context:component-scan base-package="io.moomin"></context:component-scan>
```

## 编写符合 Spring Data JPA 规范的 Dao 层接口

> Spring Data JPA 是 spring 提供的一款对于数据访问层（Dao 层）的框架，使用 Spring Data JPA，只需要按照框架的规范提供 dao 接口，不需要实现类就可以完成数据库的增删改查、分页查询等方法的定义，极大的简化了我们的开发过程。

1. 创建一个 Dao 层接口，并实现 `JpaRepository` 和 `JpaSpecificationExecutor`
2. 提供相应的泛型

```java
/**
* JpaRepository<实体类类型，主键类型>：用来完成基本CRUD操作
* JpaSpecificationExecutor<实体类类型>：用于复杂查询（分页等查询操作）
*/
public interface CustomerDao extends JpaRepository<Customer,Long>,
JpaSpecificationExecutor<Customer> {
}
```

![](https://pic.downk.cc/item/5fd071d23ffa7d37b3dde663.png)

当程序执行的时候，会通过 JdkDynamicAopProxy 的 `invoke `方法，对 customerDao 对象生成`动态代理对象`。要想进行 findOne 查询方法，会出现 JPA 规范的 API 完成操作，那么这些底层代码存在于通过`JdkDynamicAopProxy` 生 成 的 `SimpleJpaRepository`动 态 代 理 对 象 当 中 

通过SimpleJpaRepository的源码分析，定位到了findOne方法，在此方法中，返回em.find()的返回结果

em 就是 `EntityManager `对象，而他是 JPA 原生的实现方式，所以我们得到结论 Spring Data JPA 只是对标准 JPA 操作进行了进一步封装，简化了 Dao层代码的开发

## CRUD 操作

### 基本操作

 保存客户：调用save(obj)方法

修改客户：调用save(obj)方法，如果执行此方法是对象中存在id属性，即为更新操作会先根据id查询，再更新。

```java
customerDao.save(customer);
```

根据id删除：调用delete(id)方法

```java
customerDao.delete(1l);
```

根据id查询：调用findOne(id)方法，立即加载

```java
Customer customer = customerDao.findOne(2l);
```

根据id查询：调用getOne(id)方法，延迟加载

```java
Customer one = customerDao.getOne(8l);
```

根据id查询是否存在：调用exists(id)方法，返回布尔值

```java
boolean exists = customerDao.exists(8l);
```

查询所有：调用findAll()方法，返回集合

```java
List<Customer> list = customerDao.findAll();
```

统计个数：调用count()方法，返回个数

```java
long count = customerDao.count();
```

### 使用 JPQL 的方式查询

> 还需要灵活的构造查询条件，这时就可以使用`@Query `注解，结合 JPQL 的语句方式完成查询。
>
> 只需在方法上面标注该注解，同时提供一个 JPQL 查询语句即可。

根据name查询

```java
@Query(value = "from Customer where custName = ?")
public Customer findJpql(String custName);
```

根据name和id查询

```java
@Query(value = "from Customer  where custName = ? and custId = ?")
public Customer findCustNameAndId(String name, Long id);
```

更新操作（?1代表参数的占位符，其中1对应方法中的参数索引）

`@Modifying`将该操作标识为修改查询

```java
@Query(value = "update Customer set custName = ?2 where custId = ?1")
@Modifying
public void UpdateId(Long id,String name);

/*
    * springdataJpa使用jpql完成更新/删除操作
    * 需要手动添加事务支持
    * 默认执行结束后回滚事务
    * */
    @Test
    @Transactional
    @Rollback(value = false)
    public void testJpqlUpdateNameAndId() {
        customerDao.UpdateId(8l,"jsaodia");
    }
```

### 使用 SQL 语句查询

查询操作

`nativeQuery` : 使用本地sql的方式查询

```java
@Query(value = "select * from cst_customer" ,nativeQuery = true)
public List<Object[]> findSql();
```

模糊查询

```java
@Query(value = "select * from cst_customer where cust_name like ?" ,nativeQuery = true)
public List<Object[]> findSqlName(String name);
```

### 方法命名规则查询

> 方法命名规则查询就是`根据方法的名字`，就能创建查询。只需要按照 Spring Data JPA提供的方法`命名规则`定义方法的名称，就可以完成查询工作。Spring Data JPA 在程序执行的时候会根据方法名称进行解析，并自动生成查询语句进行查询
>
> 按照 Spring Data JPA 定义的规则，查询方法以` findBy` 开头，涉及条件查询时，条件的属性用条件关键字连接，要注意的是：`条件属性首字母需大写`。框架在进行方法名解析时，会先把方法名多余的前缀截取掉，然后对剩下部分进行解析

根据name查询

```java
public Customer findByCustName(String custName);
```

模糊查询

```java
public List<Customer> findByCustNameLike(String custName);
```

根据name，id查询

```java
public Customer findByCustNameAndCustId(String custName, Long id)
```

| Keyword           | Sample                                       | JPQL                                                         |
| ----------------- | -------------------------------------------- | ------------------------------------------------------------ |
| And               | findByLastnameAndFirstname                   | … where x.lastname = ?1 and x.firstname = ?2                 |
| Or                | findByLastnameOrFirstname                    | … where x.lastname = ?1 or x.firstname = ?2                  |
| Is,Equals         | findByFirstnameIs,<br/>findByFirstnameEquals | … where x.firstname = ?1                                     |
| Between           | findByStartDateBetween                       | … where x.startDate between ?1 and ?2                        |
| LessThan          | findByAgeLessThan                            | … where x.age < ?1                                           |
| LessThanEqual     | findByAgeLessThanEqual                       | … where x.age <= ?1                                          |
| GreaterThan       | findByAgeGreaterThan                         | … where x.age > ?1                                           |
| GreaterThanEqual  | findByAgeGreaterThanEqual                    | … where x.age >= ?1                                          |
| After             | findByStartDateAfter                         | … where x.startDate > ?1                                     |
| Before            | findByStartDateBefore                        | … where x.startDate < ?1                                     |
| IsNull            | findByAgeIsNull                              | … where x.age is null                                        |
| IsNotNull,NotNull | findByAge(Is)NotNull                         | … where x.age not null                                       |
| Like              | findByFirstnameLike                          | … where x.firstname like ?1                                  |
| NotLike           | findByFirstnameNotLike                       | … where x.firstname not like ?1                              |
| StartingWith      | findByFirstnameStartingWith                  | … where x.firstname like ?1 (parameter bound with appended %) |
| EndingWith        | findByFirstnameEndingWith                    | … where x.firstname like ?1 (parameter bound with prepended %) |
| Containing        | findByFirstnameContaining                    | … where x.firstnamelike ?1 (parameter boundwrapped in %)     |
| OrderBy           | findByAgeOrderByLastnameDesc                 | … where x.age = ?1 order by x.lastname desc                  |
| Not               | findByLastnameNot                            | … where x.lastname <> ?1                                     |
| In                | findByAgeIn(Collection ages)                 | … where x.age in ?1                                          |
| NotIn             | findByAgeNotIn(Collection age)               | … where x.age not in ?1                                      |
| TRUE              | findByActiveTrue()                           | … where x.active = true                                      |
| FALSE             | findByActiveFalse()                          | … where x.active = false                                     |
| IgnoreCase        | findByFirstnameIgnoreCase                    | … where UPPER(x.firstame) = UPPER(?1)                        |









