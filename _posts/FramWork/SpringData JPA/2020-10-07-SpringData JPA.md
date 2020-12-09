---
layout: post
title: "SpringData JPA-01"
subtitle: "「ORM、JPA、JPQL」"
author: "月明"
date:  2020-10-07 3:19:00
header-img: "assets/background12.png"
header-mask: 0.3
tags:
  - FramWork
  - Spring Data JPA
---

# SpringData JPA

## ORM

> ORM（Object-Relational Mapping） 表示对象关系映射。在面向对象的软件开发中，通过 ORM，就可以把对象映射到关系型数据库中。只要有一套程序能够做到建立对象与数据库的关联，操作对象就可以直接操作数据库数据，就可以说这套程序实现了 ORM 对象关系映射。
>
> 简单的说：ORM 就是建立实体类和数据库表之间的关系，从而达到操作实体类就相当于操作数据库表的目的。

常见的 orm 框架：Mybatis（ibatis）、Hibernate、Jpa

### Hibernate

Hibernate 是一个开放源代码的对象关系映射框架，它对 JDBC 进行了非常轻量级的对象封装，它将 POJO 与数据库表建立映射关系，是一个全自动的 orm 框架，hibernate 可以自动生成 SQL 语句，自动执行，使得 Java 程序员可以随心所欲的使用对象编程思维来操纵数据库。

### JPA

> JPA 的全称是 Java Persistence API， 即 Java 持久化 API，是 SUN 公司推出的一套基于 ORM的规范，内部是由一系列的接口和抽象类构成。
>
> JPA 通过 JDK 5.0 注解描述对象－关系表的映射关系，并将运行期的实体对象持久化到数据库中。

## JPA 与 hibernate 的关系

![](https://pic.downk.cc/item/5fcfa7e93ffa7d37b32fdadd.jpg)

JPA `规范`本质上就是一种 ORM 规范，注意不是 ORM 框架——因为 JPA 并未提供 ORM 实现，它只是制订了一些规范，提供了一些编程的 API 接口，但具体实现则由服务厂商来提供实现。

JPA 和 Hibernate 的关系就像 JDBC 和 JDBC 驱动的关系，JPA 是规范，Hibernate 除了作为ORM 框架之外，它也是一种 JPA 实现。JPA 怎么取代 Hibernate 呢？JDBC 规范可以驱动底层数据库吗？答案是否定的，也就是说，如果使用 JPA 规范进行数据库操作，底层`需要 hibernate 作为其实现类`完成数据持久化工作。

## 快速入门

```xml
<properties>
<project.build.sourceEncoding>UTF8</project.build.sourceEncoding>
<project.hibernate.version>5.0.7.Final</project.hibernate.version>
</properties>
<!-- hibernate对jpa的支持包 -->
<dependency>
<groupId>org.hibernate</groupId>
<artifactId>hibernate-entitymanager</artifactId>
<version>${project.hibernate.version}</version>
</dependency>
<!-- c3p0 -->
<dependency>
<groupId>org.hibernate</groupId>
<artifactId>hibernate-c3p0</artifactId>
<version>${project.hibernate.version}</version>
</dependency>
```

### 编写实体类和数据库表的映射配置[重点]

在实体类上使用 JPA 注解的形式配置映射关系

```java
@Entity //声明实体类
@Table(name="cst_customer") //建立实体类和表的映射关系
public class Customer {
	@Id//声明当前私有属性为主键
	@GeneratedValue(strategy=GenerationType.IDENTITY) //配置主键的生成策略
	@Column(name="cust_id") //指定和表中 cust_id 字段的映射关系
private Long custId;
    @Column(name="cust_name") //指定和表中 cust_name 字段的映射关系
private String custName;
}
```

#### 常用注解

* `@Entity`：指定当前类是实体类。

* `@Table`：指定实体类和表之间的对应关系。

  * name：指定数据库表的名称

* `@Id`：作用：指定当前字段是主键。

* `@GeneratedValue`：指定主键的生成方式。

  * strategy ：指定主键生成策略

    * GenerationType.`IDENTITY`：主键由数据库自动生成（主要是自动增长型）
    * GenerationType.`SEQUENCE`：根据底层数据库的序列来生成主键，条件是数据库支持序列。

    ```java
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="payablemoney_seq")
    @SequenceGenerator(name="payablemoney_seq", sequenceName="seq_payment")
    private Long custId;
    ```

    * GenerationType.`AUTO`：主键由程序控制

    * GenerationType.`TABLE`：使用一个特定的数据库表格来保存主键

    ```java
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator="payablemoney_gen")
    @TableGenerator(name = "pk_gen",
    table="tb_generator",
    pkColumnName="gen_name",
    valueColumnName="gen_value",
    pkColumnValue="PAYABLEMOENY_PK",
    allocationSize=1
    )
    private Long custId;
    ```

* `@Column`：指定实体类属性和数据库表之间的对应关系

  * name：指定数据库表的列名称。
  * unique：是否唯一
  * nullable：是否可以为空
  * inserttable：是否可以插入
  * updateable：是否可以更新
  * columnDefinition: 定义建表时创建此列的 DDL
  * secondaryTable: 从表名。如果此列不建在主表上（默认建在主表），该属性定义该列所在从表的名字搭建开发环境[重点]

### 配置 JPA 的核心配置文件

在 java 工程的resource路径下创建一个名为` META-INF` 的文件夹，在此文件夹下创建一个名为`persistence.xml `的配置文件

```xml
<!--配置持久化单元
name：持久化单元名称
transaction-type：事务类型
	RESOURCE_LOCAL：本地事务管理
	JTA：分布式事务管理 -->
<persistence-unit name="myJpa" transaction-type="RESOURCE_LOCAL">
<!--配置 JPA 规范的服务提供商 -->
<provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
<properties>
<!-- 数据库驱动 -->
<propertyname="javax.persistence.jdbc.driver"
value="com.mysql.jdbc.Driver" />
<!-- 数据库地址 -->
<propertyname="javax.persistence.jdbc.url"
value="jdbc:mysql:///jpa" />
<!-- 数据库用户名 -->
<property name="javax.persistence.jdbc.user" value="root" />
<!-- 数据库密码 -->
<property name="javax.persistence.jdbc.password" value="root" />

    <!--jpa 提供者的可选配置：我们的 JPA 规范的提供者为 hibernate，所以 jpa 的核心配置中兼容 hibernate 的配 -->
<property name="hibernate.show_sql" value="true" />
<property name="hibernate.format_sql" value="true" />
<property name="hibernate.hbm2ddl.auto" value="update" />
</properties>
</persistence-unit>
```

### JPA实现操作

#### Persistence 对象

> Persistence 对象主要作用是用于获取 EntityManager`Factory` 对象的 。通过调用该类的createEntityManagerFactory 静 态 方 法 ， 根 据 配 置 文 件 中` 持 久 化 单 元 名 称` 创 建EntityManagerFactory。

```java
//加载配置文件创建工厂（实体管理器工厂）对象
EntityManagerFactory factory= Persistence.createEntityManagerFactory("myJpa");
```

#### EntityManagerFactory

> EntityManagerFactory是 一 个 `线 程 安 全` 的 对 象 （ 即 多 个 线 程 访 问 同 一 个EntityManagerFactory 对象不会有线程安全问题），并且 EntityManagerFactory 的`创建极其浪费资源`，所以在使用 JPA 编程时，我们可以对 EntityManagerFactory 的创建进行优化，只需要做到一个工程只存在一个 EntityManagerFactory 即可

```java
//创建实体管理类
EntityManager entityManager = factory.createEntityManager();
```

#### EntityManager

> 在 JPA 规范中,` EntityManager `是完成持久化操作的`核心对象`。实体类作为普通 java 对象，只有在调用 EntityManager 将其持久化后才会变成持久化对象。EntityManager 对象在一组实体类与底层数据源之间进行 O/R 映射的管理。它可以用来管理和更新 Entity Bean, 根椐主键查找 Entity Bean, 还可以通过 `JPQL `语句查询实体。

可以通过调用 EntityManager 的方法完成获取事务，以及持久化数据库的操作

* getTransaction : 获取事务对象

```java
EntityTransaction transaction = entityManager.getTransaction();
```

* persist ： 保存操作

```java
entityManager.persist(customer);
```

* merge ： 更新操作

```java
entityManager.merge(customer);
```

* remove ： 删除操作

```java
entityManager.remove(customer);
```

* find/getReference ： 根据 id 查询

```java
//立即加载 
Customer customer = entityManager.find(Customer.class, 2l);
```

```java
//延迟加载
Customer customer = entityManager.getReference(Customer.class, 2l);
```

#### EntityTransaction

> 在 JPA 规范中, EntityTransaction 是完成事务操作的核心对象

* begin：开启事务
* commit：提交事务
* rollback：回滚事务

### JPQL实现操作

> JPQL 全称 Java Persistence Query Language
>
> 基于首次在 EJB2.0 中引入的 EJB 查询语言(EJB QL),Java 持久化查询语言(JPQL)是一种可移植的查询语言，旨在以面向对象表达式语言的表达式，将 SQL 语法和简单查询语义绑定在一起·使用这种语言编写的查询是可移植的，可以被编译成所有主流数据库服务器上的 SQL。
>
> 其特征与原生 SQL 语句类似，并且完全面向对象，通过类名和属性访问，而不是表名和表的属性

#### 查询全部

```java
		String jpql = "from io.moomin.domain.Customer";
        Query query = entityManager.createQuery(jpql);
        List resultList = query.getResultList();
```

#### 分页查询

```java
 		String jpql = "from Customer";
        Query query = entityManager.createQuery(jpql);
        //设置参数
		query.setFirstResult(1);
        query.setMaxResults(2);
        List resultList = query.getResultList();
```

#### 条件查询

```java
		String jpql = "from Customer where custName like ?";
        Query query = entityManager.createQuery(jpql);
		//设置参数
        query.setParameter(1, "%u%");
        List resultList = query.getResultList();
```

#### 排序查询

```java
		String jpql = "from Customer order by custId desc";
        Query query = entityManager.createQuery(jpql);
        List resultList = query.getResultList();
```

#### 统计查询

```java
		String jpql = "select count(custId) from Customer";
        Query query = entityManager.createQuery(jpql);
        Object singleResult = query.getSingleResult();
```

