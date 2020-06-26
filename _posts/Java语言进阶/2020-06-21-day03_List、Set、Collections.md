---
layout: post
title: "List、Set、Collections"
subtitle: "「数据结构、实现排序的两种方法」"
author: "月明"
date:   2020-06-21 22:00:00
header-img: "assets/background.png"
header-mask: 0.3
tags:
  - Java语言进阶
  - 学习笔记
  - 集合
  - 排序
---

# List、Set、数据结构、Collections

## 数据存储的常用结构

* 栈：stack,又称堆栈，它是运算受限的线性表
* 队列：queue,简称队，它同堆栈一样，也是一种运算受限的线性表
* 数组：Array,是有序的元素序列，数组是在内存中开辟一段连续的空间，并在此空间存放元素
* 链表：linkedlist,由一系列结点node（链表中每一个元素称为结点）组成，结点可以在运行时i动态生成
* 红黑树：一颗二叉查找树，将节点插入后，该树仍然是一颗二叉查找树。树的键值仍然是有序的。

![](/assets/image/media/a0b4fc3442f605313b5b0867ad5872f1.png)

- 约束:
    - 节点可以是红色的或者黑色的
    - 根节点是黑色的
    - 叶子节点(特指空节点)是黑色的
    - 每个红色节点的子节点都是黑色的
    - 任何一个节点到其每一个叶子节点的所有路径上黑色节点数相同

特点: 速度特别快,趋近平衡树,查找叶子元素少和多次数不多于二倍

### List的子类

`java.util.ArrayList` 集合数据存储的结构是**数组**结构。

由于**日常开发**中使用多的功能为查询数据、遍历数据，所以 `ArrayList` 是最常用的集合。

`java.util.LinkedList`
集合数据存储的结构是（双向）链表结构。方便元素添加、删除的集合。实际开发中对一个集合元素的添加与删除经常涉及到首尾操作，而LinkedList提供了大量首尾操作的方法。这些方法我们作为了解即可：

`public void addFirst(E e)` :将指定元素插入此列表的开头。

`public void addLast(E e)` :将指定元素添加到此列表的结尾。 

`public E getFirst()`:返回此列表的第一个元素。

`public E getLast()` :返回此列表的后一个元素。

`public E removeFirst()` :移除并返回此列表的第一个元素。 

`public E removeLast()`:移除并返回此列表的后一个元素。

`public E pop()` :从此列表所表示的堆栈处弹出一个元素。 

`public void push(E e)`:将元素推入此列表所表示的堆栈。 

`public boolean isEmpty()`：如果列表不包含元素，则返回true。

在开发时，LinkedList集合也可以作为堆栈，队列的结构使用

### Set的子类

`java.util.HashSet` 底层的实现其实是一个 `java.util.HashMap` 支持。

`HashSet`是根据对象的哈希值来确定元素在集合中的存储位置，因此具有良好的存取和查找性能。**保证元素唯一性的方式依赖于：hashCode 与 equals 方法。**

#### 哈希表

在JDK1.8之前，哈希表底层采用`数组`+`链表`实现，即使用链表处理冲突，同一hash值的链表都存储在一个链表里。但是当位于一个桶中的元素较多，即hash值相等的元素较多时，通过key值依次查找的效率较低。

而JDK1.8中，哈希表存储采用数组+链表+红黑树实现，当链表长度超过阈值（8）时，将链表转换为红黑树，这样大大减少了查找时间。

![](/assets/image/media/9fbc7f4f749bc1d1b627081753cdeee4.png)

JDK1.8引入红黑树大程度优化了HashMap的性能

#### HashSet的子类

`java.util.LinkedHashSet`，它是链表和哈希表组合的一个数据存储结构。**唯一**，保证**有序**。

##### 可变参数

```java
修饰符 返回值类型 方法名(参数类型... 形参名){  }
```

可以直接传递数据即可。...
用在参数上，称之为可变参数。同样是代表数组，但是在调用这个带有可变参数的方法时，不用创建数组(这就是简单之处)，直接将数组中的元素作为实际参数进行传递，其实编译成的class文件，将这些元素先封装到一个数组中，在进行传递。这些动作都在编译.class文件时，自动完成了。

### Collections

`java.utils.Collections`是集合工具类，用来对集合进行操作。

`public static <T> boolean addAll(Collection<T> c, T... elements)`:往集合中添加一些元素。

`public static void shuffle(List<?> list)` 打乱顺序 :打乱集合顺序。

`public static <T> void sort(List<T> list)`:将集合中元素按照默认规则排序。

实际上要求了被排序的类型需要实现Comparable接口完成比较的功能，在String类型上实现，并完成了比较规则的定义。

`public static <T> void sort(List<T> list，Comparator<? super T> )` :将集合中元素按照指定规则排序。

### 排序

在JAVA中提供了两种比较实现的方式：
比较死板的采用 `java.lang.Comparable` 接口去实现
灵活的当我需要做排序的时候在去选择的 `java.util.Comparator` 接口完成。

排序是comparator能实现的功能之一,该接口代表一个比较器，比较器具有可比性！顾名思义就是做排序的，通俗地讲需要比较两个对象谁排在前谁排在后，那么比较的方法就是：

`public int compare(String o1, String o2)` ：比较其两个参数的顺序。

* 两个对象比较的结果有三种：大于，等于，小于。 

  * 如果要按照升序（o1-o2）排序，则o1 小于o2，返回（负数），

  * 相等返回0，

  * 01大于02返回（正数） 
* 如果要按照降序（o2-o1）排序 则o1小于o2，返回（正数），相等返回0，01大于02返回（负数）

#### 简述Comparable和Comparator两个接口的区别

`Comparable`：强行对实现它的每个类的对象进行整体排序。这种排序被称为类的自然排序，类的compareTo方法被称为它的自然比较方法。只能在类中实现compareTo()一次，不能经常修改类的代码实现自己想要的排序。实现此接口的对象列表（和数组）可以通过Collections.sort（和Arrays.sort）进行自动排序，对象可以用作有序映射中的键或有序集合中的元素，无需指定比较器。`Comparator`强行对某个对象进行整体排序。可以将Comparator传递给sort方法（如Collections.sort或Arrays.sort），从而允许在排序顺序上实现精确控制。还可以使用Comparator来控制某些数据结构（如有序set或有序映射）的顺序，或者为那些没有自然顺序的对象collection提供排序。