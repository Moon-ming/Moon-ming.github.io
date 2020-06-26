---
layout: post
title: "Object类、常用API"
subtitle: "「toString、equals、Objects、Date、StringBuilder」"
author: "月明"
date:   2020-06-20 20:00:00
header-img: "assets/background2.png"
header-mask: 0.3
tags:
  - Java语言进阶
  - 学习笔记
  - 常用API
---

# Object类、常用API

**java.lang.Object** 类是Java语言中的根类，即所有类的父类。

**toString方法**
``` java
public String toString() 返回该对象的字符串表示。
该字符串内容就是对象的类型+\@+内存地址值。在开发中，经常需要按照对象的属性得到相应的字符串表现形式，因此也需要重写它
```

    在IntelliJ IDEA中，可以点击 Code 菜单中的 Generate... ，也可以使用快捷键alt+insert ，点击 toString() 选项。选择需要包含的成员变量并确定。

**equals方法**
``` java
public boolean equals(Object obj) 指示其他某个对象是否与此对象“相等”。判断这两个对象是否是相同的。这里的“相同”有默认和自定义两种方式
```

- 默认进行 == 运算符的对象地址比较，只要不是同一个对象，结果 必然为false。如果希望进行对象的内容比较，即所有或指定的部分成员变量相同就判定两个对象相同，则可以覆盖重写equals方法。
    - 如果对象地址一样，则认为相同
    - 如果参数为空，或者类型信息不一样，则认为不同
    - 转换为当前类型
    - a.util.Objects类的equals静态方法取用结果

在IntelliJ IDEA中，可以使用 Code 菜单中的 Generate… 选项，也可以使用快捷键alt+insert ，并选 择 equals() and hashCode() 进行自动代码生成。

**Objects类**

在JDK7添加了一个Objects工具类，它提供了一些方法来操作对象，它由一些静态的实用方法组成，这些方法是
null-save（空指针安全的）或null-tolerant（容忍空指针的），用于计算对象的hashcode、返回对象的字符串表示形式、比较两个对象。

在比较两个对象的时候，Object的equals方法容易抛出空指针异常，而Objects类中的equals方法就优化了这个问题。方法如下：
``` java
public static boolean equals(Object a, Object b) 判断两个对象是否相等
```


**日期时间类**

    java.util.Date类 表示特定的瞬间，精确到毫秒。
``` java
public Date() 分配Date对象并初始化此对象，以表示分配它的时间（精确到毫秒）

public Date(long date) 分配Date对象并初始化此对象，以表示自从标准基准时间（称为“历元（epoch）”，即1970年1月1日00:00:00 GMT）以来的指定毫秒数

public long getTime() 把日期对象转换成对应的时间毫秒值。
```
简单来说：使用无参构造，可以自动设置当前系统时间的毫秒时刻；指定long类型的构造参数，可以自定义毫秒时刻。

**java.text.DateFormat**
是日期/时间格式化子类的抽象类，我们通过这个类可以帮我们完成日期和文本之间的转换,也就是可以在Date对象与String对象之间进行来回转换。

**DataFormat类**

java.text.DateFormat是日期/时间格式化子类的抽象类，我们通过这个类可以帮我们完成日期和文本之间的转换,也就是可以在Date对象与String对象之间进行来回转换。

需要常用的子类 java.text.SimpleDateFormat 。这个类需要一个
模式（格式）来指定格式化(从Date对象转换为String对象)或解析(从String对象转换为Date对象)的标准。

![](/assets/image/media/609c2317c6c6c016c79971ae633e33e3.png)

                                                  格式规则

``` java
public SimpleDateFormat(String pattern) 用给定的模式和默认语言环境的日期格式符号构造 SimpleDateFormat。

public String format(Date date) 将Date对象格式化为字符串。

public Date parse(String source) 将字符串解析为Date对象。
```

**java.util.Calendar**
是(抽象类)日历类，在Date后出现，替换掉了许多Date的方法。该类将所有可能用到的时间信息封装为静态成员变量，方便获取。日历类就是方便获取各个时间属性的。

Calendar类在创建对象时并非直接创建，而是通过静态方法创建，返回子类对象
``` java
public static Calendar getInstance() 使用默认时区和语言环境获得一个日历

public int get(int field)  返回给定日历字段的值

public void set(int field, int value) 将给定的日历字段设置为给定值

public abstract void add(int field, int amount) 根据日历的规则，为给定的日历字段添加或减去指 定的时间量

public Date getTime() 返回一个表示此Calendar时间值（从历元到现在的毫秒偏移量）的Date对象给定的日历字段
```

![](/assets/image/media/9b194ca638a6e3bacb0b9f5cd19b4049.png)

                                                 System类

``` java
public static long currentTimeMillis() 返回以毫秒为单位的当前时间
public static void arraycopy(Object src, int srcPos, Object dest, int destPos, int length) 将数组中指定的数据拷贝到另一个数组中
```

    数组的拷贝动作是系统级的，性能很高

![](/assets/image/media/17bf5f5c72fc517709a9c9e68f0bcce0.png)

                                                参数含义

**StringBuilder类**

==字符串拼接问题==，String类的对象内容不可改。，每次拼接，都会构建一个新的String对象，既耗时，又浪费空间。

**java.lang.StringBuilder**为可变字符序列，字符串缓冲区，即它是一个容器，容器中可以装很多字符串。并且能够对其中的字符串进行各种操作。

内部拥有一个数组，自动维护数组的扩容。(默认16字符空间，超过自动扩充)

![](/assets/image/media/53d2c8080f05ec18010d2933933b9476.png)

 **构造方法**
``` java
 public StringBuilder() 构造一个空的StringBuilder容器。
 public StringBuilder(String str) 构造一个StringBuilder容器，并将字符串添加进去
```

 **常用方法**
``` java
public StringBuilder append(...) 添加**任意类型**数据的字符串形式，并返回当前对象自身

public String toString() 将当前StringBuilder对象转换为不可变String对象
```

    在我们开发中，会遇到调用一个方法后，返回一个对象的情况。然后使用返回的对象继续调用方法。这种时候，我们就可以把代码写在一起，如append方法一样，代码如下:
        链式编程
        builder.append("hello").append("world").append(true).append(100);

**包装类**

    装箱：从基本类型转换为对应的包装类对象。
    拆箱：从包装类对象转换为对应的基本类型（看懂代码即可）

从Java 5（JDK 1.5）开始，基本类型与包装类的装箱、拆箱动作可以自动完成
基本类型转换为String：基本类型直接与””相连接即可String转换成对应的基本类型

 除了Character类之外，其他所有包装类都具有parseXxx静态方法可以将字符串参数转换为对应的基本类型：

``` java
public static byte parseByte(String s) 将字符串参数转换为对应的byte基本类型

public static short parseShort(String s) 将字符串参数转换为对应的short基本类型

public static int parseInt(String s)将字符串参数转换为对应的int基本类型

public static long parseLong(String s) 将字符串参数转换为对应的long基本类型

public static float parseFloat(String s) 将字符串参数转换为对应的ﬂoat基本类型

 public static double parseDouble(String s) 将字符串参数转换为对应的double基本类型。

public static boolean parseBoolean(String s) 将字符串参数转换为对应的boolean基本类型
```

    注意:如果字符串参数的内容无法正确转换为对应的基本类型，则会抛出java.lang.NumberFormatException 异常