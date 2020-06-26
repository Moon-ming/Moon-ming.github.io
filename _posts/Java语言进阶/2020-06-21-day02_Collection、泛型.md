---
layout: post
title: "Collection、泛型"
subtitle: "「常用功能、常用方法」"
author: "月明"
date:   2020-06-21 20:00:00
header-img: "assets/background.png"
header-mask: 0.3
tags:
  - Java语言进阶
  - 学习笔记
  - 集合
---

# Collection、泛型

在开发中一般当对象多的时候，使用集合进行存储。
集合按照其存储结构可以分为两大类，分别是单列集合 `java.util.Collection`和双列集合`java.util.Map`

## Collection：

**java.util.List**（有序，可重复）：java.util.ArrayList 和 java.util.LinkedList

**java.util.Set**（无序，不可重复）：java.util.HashSet 和 java.util.TreeSet

![](/assets/image/media/5cf8edb44f0ba4d0901ed47ba52eb7f4.png)

### Collection 常用功能：

```java
public boolean add(E e) 把给定的对象添加到当前集合中。

public void clear() 清空集合中所有的元素

public boolean remove(E e)  把给定的对象在当前集合中删除

public boolean contains(E e)  判断当前集合中是否包含给定的对象。

public boolean isEmpty()  判断当前集合是否为空

public int size()  返回集合中元素的个数

public Object[] toArray()  把集合中的元素，存储到数组中
```


#### List接口中常用方法: (跟索引相关)

```java
public void add(int index, E element) 将指定的元素，添加到该集合中的指定位置上

public E get(int index)  返回集合中指定位置的元素

public E remove(int index)
移除列表中指定位置的元素,返回的是被移除的元素

public E set(int index, E element) 用指定元素替换集合中指定位置的元素,返回值的更新前的元素
```

 Set接口与 Collection 接口中的方法基本一致，并没有对 Collection接口进行功能上的扩充，只是比 Collection 接口更加严格了。

**Iterator迭代器**

    在程序开发中，经常需要遍历集合中的所有元素。Iterator 主要用于迭代访问（即遍历）Collection 中的元 素，因此 Iterator 对象也被称为迭代器。
```java
public boolean hasNext() :如果仍有元素可以迭代，则返回 true。

public E next() :返回迭代的下一个元素。
```

    注意：在进行集合元素取出时，如果集合中已经没有元素了，还继续使用迭代器的next方法，将会发生java.util.NoSuchElementException没有集合元素的错误。

增强for循环(也称for each循环)，专门用来遍历数组和集合的。它的内部原
理其实是个Iterator迭代器，所以在遍历的过程中，不能对集合中的元素进行增删操作。

泛型：可以在类或方法中预支地使用未知的类型。

集合中是可以存放任意对象的，只要把对象存储集合后，那么这时他们都会被提升成Object类型。当我们在取出每一个对象，并且进行相应的操作，这时必须采用类型转换。

    注意：java.lang.ClassCastException。类型转换异常

好处：将运行时期的ClassCastException，转移到了编译时期变成了编译失败。
避免了类型强转的麻烦。

当使用泛型类或者接口时，传递的数据中，泛型类型不确定，可以通过通配符\<?\>表示。但是一旦使用泛型的通配
符后，只能使用Object类中的共性方法，集合中元素自身方法无法使用。此时只能接受数据,不能往该集合中存储数据

在JAVA的泛型中可以指定一个泛型的上限（只能接收该类型及其子类）和下限（只能接收该类型及其父类型）。