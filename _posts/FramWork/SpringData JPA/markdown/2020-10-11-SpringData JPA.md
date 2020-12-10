---
layout: post
title: "SpringData JPA-03"
subtitle: "「动态查询、JPA一对多、JPA多对多、对象导航查询」"
author: "月明"
date:  2020-10-11 21:48:00
header-img: "assets/background.png"
header-mask: 0.3
tags:
  - FramWork
  - Spring Data JPA
---

# Spring Data JPA

## Specifications 动态查询

> 查询某个实体的时候，给定的条件是不固定的，这时就需要动态构建相应的查询语句，在 Spring Data JPA 中可以通过 JpaSpecificationExecutor 接口查询。相比 JPQL,其优势是类型安全,更加的面向对象。

对于 JpaSpecificationExecutor，这个接口基本是围绕着` Specification` 接口来定义的。可以简单的理解为，Specification 构造的就是`查询条件`。

Specification 接口（提供泛型：查询的对象类型）

* `root`：Root接口，代表查询的根对象，可以通过root`获取实体中的属性`
* `query`：代表一个顶层查询对象，用来自定义查询
* `cb`：用来`构建查询`，此对象里有很多条件方法

```java
//（构造查询条件）
public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb);
```

### 名称查询

`criteriaBuilder.equal(root.get("custName"), "moomin");`

```java
//匿名内部类
Specification<Customer> spec = new Specification<Customer>() {
    @Override
    public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        //获取比较的属性
        Path<Object> custName = root.get("custName");
//构造查询条件 select * from cst_customer where cust_name = 'moomin'
        Predicate moomin = 
            //进行精确匹配（比较的属性，取值）
            criteriaBuilder.equal(custName, "moomin");
        return moomin;
    }
};
Customer one = customerDao.findOne(spec);
```

### 多条件查询

`and = criteriaBuilder.and(moomin, phone);`

```java
Specification<Customer> spec = new Specification<Customer>() {
    @Override
    public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        //获取比较的属性
        Path<Object> custName = root.get("custName");
        Path<Object> custPhone = root.get("custPhone");
        //构造查询条件 select * from cst_customer where cust_name = 'moomin'
        Predicate moomin = criteriaBuilder.equal(custName, "moomin");//进行精确匹配（比较的属性，取值）
        Predicate phone = criteriaBuilder.equal(custPhone, 1715616516);
        //将多个查询条件组合在一起
        Predicate and = criteriaBuilder.and(moomin, phone);
        return and;
    }
};
Customer one = customerDao.findOne(spec);
```

### 模糊查询

`criteriaBuilder.like(custName.as(String.class), "%o%");`

```java
/**
equal：直接得到path对象（属性），然后进行比较既可
	gt，lt，ge，le，like：得到path对象，
		根据path指定比较的参数类型，再去进行比较
	指定参数类型：path.as(类型的字节码对象)
*/
Specification<Customer> spec = new Specification<Customer>() {
    @Override
    public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        //获取比较的属性
        Path<Object> custName = root.get("custName");
        //构造查询条件 select * from cst_customer where cust_name = 'moomin'
        Predicate moomin = criteriaBuilder.equal(custName, "moomin");//进行精确匹配（比较的属性，取值）
        //指定参数类型
        Predicate like = criteriaBuilder.like(custName.as(String.class), "%o%");
        return like;
    }
};
List<Customer> all = customerDao.findAll(spec);
```

### 排序查询

`Sort sort = new Sort(Sort.Direction.DESC, "custId");`

```java
//添加排序
//创建排序对象，需要调用构造方法实例化sort对象
//第一个参数，排序的顺序
//排序的属性名称
Sort sort = new Sort(Sort.Direction.DESC, "custId");
List<Customer> all = customerDao.findAll(spec, sort);
```

### 分页查询

`Pageable pageRequest = new PageRequest(0,1);`

```java
/**
* 构造分页参数
*	Pageable : 接口
*		PageRequest实现了Pageable接口，调用构造方法的形式构造
*			第一个参数：页码（从0开始）
*			第二个参数：每页查询条数
	findAll(Specification,Pageable)
*/
Specification<Customer> spec = null;
Pageable pageRequest = new PageRequest(0,1);
/**
* 分页查询，封装为Spring Data Jpa 内部的page bean
*此重载的findAll方法为分页方法需要两个参数
*第一个参数：查询条件Specification
*第二个参数：分页参数
*/
Page<Customer> all = customerDao.findAll(spec, pageRequest);
System.out.println(all.getContent());//得到数据集合列表
System.out.println(all.getTotalElements());//得到总条数
System.out.println(all.getTotalPages());//得到总页数
```

| 方法名称                    | Sql对应关系          |
| --------------------------- | -------------------- |
| equal                       | filed = value        |
| gt（greaterThan ）          | filed > value        |
| lt（lessThan ）             | filed < value        |
| ge（greaterThanOrEqualTo ） | filed >= value       |
| le（ lessThanOrEqualTo）    | filed <= value       |
| notEqule                    | filed != value       |
| like                        | filed like value     |
| notLike                     | filed not like value |

## 多表设计

> 实际开发中常用的关联关系，`一对多`和`多对多`。而一对一的情况，在实际
> 开发中几乎不用。
>
> 在实际开发中，我们数据库的表难免会有相互的关联关系，在操作表的时候就有可能会涉及到多张表的操作。而在这种实现了 ORM 思想的框架中（如 JPA），可以让我们通过`操作实体类就实现对数据库表的操作`。
>
> 掌握`配置实体之间的关联关系`。
>
> 1. 首先确定两张表之间的关系。
> 2. 在数据库中实现两张表的关系
> 3. 在实体类中描述出两个实体的关系
> 4. 配置出实体类和数据库表的`关系映射`（重点）

### 映射的注解说明

* `@OneToMany`：建立一对多的关系映射
  * targetEntityClass：指定多的多方的类的字节码
  * `mappedBy`：指定从表实体类中引用主表对象的名称。
  * `cascade`：指定要使用的级联操作
  * fetch：指定是否采用延迟加载
  * orphanRemoval：是否使用孤儿删除
* `@ManyToOne`：建立多对一的关系
  * `targetEntityClass`：指定一的一方实体类字节码
  * `cascade`：指定要使用的级联操作
  * fetch：指定是否采用延迟加载
  * optional：关联是否可选。如果设置为 false，则必须始终存在非空关系。
* `@JoinColumn`：用于定义主键字段和外键字段的对应关系。
  * `name`：指定外键字段的名称
  * `referencedColumnName`：指定引用主表的主键字段名称
  * unique：是否唯一。默认值不唯一
  * nullable：是否允许为空。默认值允许
  * insertable：是否允许插入。默认值允许
  * updatable：是否允许更新。默认值允许
  * columnDefinition：列的定义信息
* `@ManyToMany`：用于映射多对多关系
  * `cascade`：配置级联操作
  * fetch：配置是否采用延迟加载
  * `targetEntity`：配置目标的实体类。映射多对多的时候不用写
  * `mappedBy`：推荐在被动的一方放弃（放弃对中间表的维护权，解决保存中主键冲突的问题）
* `@JoinTable`：针对中间表的配置
  * `name`：配置中间表的名称
  * `joinColumns`：中间表的外键字段关联`当前实体类`所对应表的主键字段
  * `inverseJoinColumn`：中间表的外键字段`关联对方表`的主键字段
* `cascade属性`：配置级联操作
  * CascadeType.MERGE级联更新
  * CascadeType.PERSIST 级联保存
  * CascadeType.REFRESH 级联刷新
  * CascadeType.REMOVE 级联删除
  * CascadeType.ALL 包含所有
* `fetch属性`：
  * FetchType.EAGER ：立即加载
  * FetchType.LAZY：延迟加载

### JPA 中的一对多

> 把一的一方称之为主表，把多的一方称之为从表。在数据库中建立一对多的关系，需要使用数据库的`外键约束`。

```java
//配置客户和联系人之间的关系（一对多）
//@OneToMany(targetEntity = LinkMan.class)
//@JoinColumn(name = "lkm_cust_id",referencedColumnName = "cust_id")在客户实体类上（一的一方）添加了外键配置，对于客户而言，也具备了维护外键的作用
@OneToMany(mappedBy = "customer",
           cascade = CascadeType.ALL,
           fetch = FetchType.EAGER)
	//放弃外键维护权，mapperBy：对方配置关系的属性名称
    private Set<LinkMan> linkMans = new HashSet<>();
```

```java
//配置联系人到客户的多对一关系
@ManyToOne(targetEntity = Customer.class,fetch = FetchType.LAZY)
@JoinColumn(name = "lkm_cust_id",referencedColumnName = "cust_id")//配置外键的过程，配置到了多的一方，就会在多的一方维护外键
    private Customer customer;
```

#### 添加信息

```java
	@Test
    @Transactional
    @Rollback(value = false)
    public void testAdd() {
        Customer customer = new Customer();
        customer.setCustName("autumn");
        LinkMan linkMan = new LinkMan();
        linkMan.setLkmName("moomin");
        //配置了客户到联系人的关系
        //由于配置了一的一方到多的一方的关联关系（发送一条update语句，多余，放弃维护权）
        customer.getLinkMans().add(linkMan);
     //由于配置了多的一方到一的一方的关联关系（当保存的时候就已经对外键赋值）
        linkMan.setCustomer(customer);
        
        customerDao.save(customer);
        linkManDao.save(linkMan);
    }
```

#### 级联添加

```java
 	@Test
    @Transactional
    @Rollback(value = false)
    public void testCascadeAdd() {
        Customer customer = new Customer();
        customer.setCustName("autumn1");
        LinkMan linkMan = new LinkMan();
        linkMan.setLkmName("moomin1");
        //配置了客户到联系人的关系
        //由于配置了一的一方到多的一方的关联关系（发送一条update语句，多余，放弃维护权）
        customer.getLinkMans().add(linkMan);
    //由于配置了多的一方到一的一方的关联关系（当保存的时候就已经对外键赋值）
        linkMan.setCustomer(customer);

        customerDao.save(customer);
    }
```

#### 级联删除

> 在实际开发中，`级联删除请慎用！`(在一对多的情况下)

删除从表数据：可以随时任意删除。

删除主表数据：

* 有从表数据
  1. 在默认情况下，它会把外键字段置为 null，然后删除主表数据。如果在数据库的表结构上，外键字段有非空约束，默认情况就会报错了。
  2. 如果配置了放弃维护关联关系的权利，则不能删除（与外键字段是否允许为 null，没有关系）因为在删除时，它根本不会去更新从表的外键字段了。
  3. 如果还想删除，使用级联删除引用
* 没有从表数据引用：随便删

```java
	@Test
    @Transactional
    @Rollback(value = false)
    public void testCascadeRemove() {
        Customer one = customerDao.findOne(102l);
        customerDao.delete(one);
    }
```

### JPA 中的多对多

```java
/*配置多对多映射关系
     * 声明表关系的配置
     * 配置中间表（包含两个外键）
     * */
@ManyToMany(targetEntity = Role.class,cascade = CascadeType.ALL)
@JoinTable(name = "sys_user_role",
    //当前对象在中间表中的外键
	joinColumns = {@JoinColumn(
        name = "sys_user_id",referencedColumnName ="user_id")},
        //对方对象在中间表的外键
        inverseJoinColumns = {@JoinColumn(
        	name = "sys_role_id",referencedColumnName ="role_id")}
    )
    private Set<Role> roles = new HashSet<>();
```

```java
@ManyToMany(mappedBy = "roles")
/**
在多对多（保存）中，如果双向都设置关系，意味着双方都维护中间表，都会往中间表插入数据，中间表的 2 个字段又作为联合主键，所以报错，主键重复，解决保存失败的问题：只需要在任意一方放弃对中间表的维护权即可，推荐在被动的一方放弃
*/
    /*@ManyToMany(targetEntity = User.class)
    @JoinTable(name = "sys_user_role",
        joinColumns = {@JoinColumn(name = "sys_role_id",referencedColumnName = "role_id")},
            inverseJoinColumns ={@JoinColumn(name = "sys_user_id",referencedColumnName = "user_id")}
    )*/
    private Set<User> users = new HashSet<>();
```

#### 添加信息

```java
	@Test
    @Transactional
    @Rollback(value = false)
    public void testAdd() {
        User user = new User();
        user.setUserName("moomin");

        Role role = new Role();
        role.setRoleName("dsasd");

        user.getRoles().add(role);
        role.getUsers().add(user);

        userDao.save(user);
        roleDao.save(role);
    }
```

#### 级联添加

```java
	@Test
    @Transactional
    @Rollback(value = false)
    public void testCascadeAdd() {
        User user = new User();
        user.setUserName("moomin1");

        Role role = new Role();
        role.setRoleName("dsasd1");

        user.getRoles().add(role);
        role.getUsers().add(user);

        userDao.save(user);
    }
```

#### 级联删除

> 在多对多的删除时，双向级联删除根本不能配置
>
> 如果配了的话，如果数据之间有相互引用关系，可能会清空所有数据

```java
	@Test
    @Transactional
    @Rollback(value = false)
    public void testCascadeRemove() {
        User one = userDao.findOne(4l);
        userDao.delete(one);
    }
```

### 对象导航查询

> 对象图导航检索方式是根据已经加载的对象，导航到他的关联对象。它利用类与类之间的关系来检索对象。
>
> 对象导航查询的使用要求是：两个对象之间必须存在关联关系。

查询一个客户，获取该客户下的所有联系人（默认延迟加载）

```java
 	@Test
    @Transactional
    public void testQueryOneToMany() {
        //查询一个对象的时候，通过此对象查询所有的关联对象
        Customer one = customerDao.getOne(96l);
        System.out.println(one);
        //对象导航查询，此客户下的所有联系人，一到多，默认延迟加载
        Set<LinkMan> linkMans = one.getLinkMans();
        for (LinkMan linkMan : linkMans
        ) {
            System.out.println(linkMan);
        }
    }
```

查询一个联系人，获取该联系人的所有客户（默认立即加载）

```java
 	@Test
    @Transactional
    public void testQueryManyToOne() {
        LinkMan one = linkManDao.findOne(4l);
        //对象导航查询所属的客户,多到一，立即加载
        Customer customer = one.getCustomer();
        System.out.println(one);
        System.out.println(customer);
    }
```

### 使用 Specification 查询

```java
/**
* Specification的多表查询
*/
@Test
public void testFind() {
	Specification<LinkMan> spec = new Specification<LinkMan>() {
		public Predicate toPredicate(Root<LinkMan> root, 					CriteriaQuery<?> query,CriteriaBuilder cb) {
//Join代表链接查询，通过root对象获取
//创建的过程中，第一个参数为关联对象的属性名称，第二个参数为连接查询的方式（left，inner，right）
//JoinType.LEFT : 左外连接,JoinType.INNER：内连接,JoinType.RIGHT：右外连接
	Join<LinkMan, Customer> join = 												root.join("customer",JoinType.INNER);
		return cb.like(join.get("custName").
                       as(String.class),"传智播客1");
	}
};
	List<LinkMan> list = linkManDao.findAll(spec);
}
```

