---
layout: post
title: "Stream流、方法引用"
subtitle: "「Supplier、Consumer、Predicate、Function」"
author: "月明"
date:  2020-06-26 22:00:00
header-img: "assets/background3.png"
header-mask: 0.3
tags:
  - Java语言进阶
  - 学习笔记
  - Stream
---

# Stream流

> 在Java8中，得益于Lambda所带来的函数式编程，引入了一个全新的Stream概念，**用于解决已有集合类库既有的弊端**

## 1.1 引言

几乎所有的集合(`Collection`接口或`Map`接口等)都支持直接或间接的遍历操作。而当我们需要对集合中的元素操作的时候，除了必需的添加、删除、获取外，最典型的就是集合遍历。

Java 8的Lambda让我们可以更加专注于**做什么**(what)，而不是**怎么做**(how)。

for循环的语法是怎么做，循环体才是做什么。

遍历是指每一个元素逐一进行处理，**而并不是从第一个到最后一个顺次处理的循环**，前者是目的，后者是方式。

Stream的更优写法

```java
list.stream()
    .filter(s -> s.startsWith("张"))
    .filter(s -> s.length() == 3)
    .forEach(System.out::println);
```

**获取流、过滤姓张、过滤长度为3、逐一打印**。代码中并没有体现使用线性循环或是其他任何算法进行遍历，我们真正要做的事情内容被更好地体现在代码中。

## 1.2 流式思想概述

流式思想类似于工厂车间的**生产流水线**。

当需要对多个元素进行操作(特别是多步操作)的时候，考虑到性能及便利性，我们应该首先拼好一个”模型“步骤方案，然后再按照方案去执行它。

> Stream流 其实是一个集合元素的函数模型，它并不是集合，也不是数据结构，其本身并不存储任何元素(或其地址值)。

Stream(流)是一个来自数据源的元素队列

* 元素是特定类型的对象，形成一个队列。Java中的Stream并不会存储元素，而是按需计算。
* **数据源** 流的来源。可以是集合，数组等。

Stream操作还有两个基础的特征：

* **Pipelining**：中间操作都会返回流对象本身。这样多个操作可以串联成一个管道，如同流式风格(fluent style)。这样做可以对操作进行优化，比如延迟执行(laziness)和短路(short-circuiting)。
* **内部迭代**：以前对集合遍历都是通过Iterator或者增强for的方式，显式的在集合外部进行迭代，这叫外部迭代。Stream提供内部迭代的方式，流可以直接调用遍历方法。

当使用一个流的时候，通常包括三个基本步骤：获取一个数据源(source)->数据转换->执行操作获取想要的结果，每次转换原有Stream对象不改变，返回一个新的Stream对象(可以有多次转换)，这就允许对其操作可以像链条一样排列，变成一个管道。

## 1.3 获取流

`java.util.stream.Stream<T>`是Java 8新加入的最常用的流接口。(这并不是一个函数式接口)

获取流常用方式：

* 所有的`Collection`集合都可以通过`Stream`默认方法获取流；
* `Stream`接口的静态方法`of`可以获取数组对应的流

### 根据Collection获取流

`java.util.Collection`接口中加入了default方法`stream`用来获取流

```java
import java.util.*;
import java.util.stream.Stream;
public class DemoStream{
    public static void main(String[] args){
        List<String> list = new ArrayList<>();
        Stream<String> stream1 = list.stream();
        Set<String> set = new HashSet<>();
        Stream<String> stream2 = set.stream();
        Vector<String> vector = new Vector<>();
        Stream<String> stream3 = vector.stream();
    }
}
```

### 根据Map获取流

`java.util.Map`接口不是`Collection`的子接口，且其K-V数据结构不符合流元素的单一特征，所以获取对应的流需要分key、value或entry等情况：

```java
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
public class DemoGetStream{
    public static void main(String[] args){
        Map<String,String> map = new HashMap<>();
        Stream<String> keyStream = map.keySet().stream();
        Stream<String> valueStream = map.values().stream();
        Stream<Map.Entry<String,String>> entryStream = map.entreySet().stream();
    }
}
```

### 根据数组获取流

如果使用的不是集合或映射而是数组，由于数组对象不可能添加默认方法，所有`Strea`接口中提供了静态方法`of`

```java
import java.util.stream.Stream;
public class DemoGetStream{
    public static void main(String[] args){
        String[] array = {"张无忌","张三丰","张一山"}
        Stream<String> stream = Stream.of(array);
    }
}
```

> of方法的参数其实是一个可变参数，所以支持数组

## 1.4 常用方法

流模型的操作常用API，分为：

* 延迟方法：返回值类型仍然是`Stream`接口自身类型的方法，因此支持链式调用。(除了终结方法外，其余方法都是延迟方法)
* 终结方法：返回值类型不再是`Stream`接口自身类型的方法，因此不再支持类似`StringBuilder`那样的链式调用。包括`count`和`forEach`方法。

### 逐一处理：forEach

```java
void forEach(Consumer<? super T> action);
```

该方法接受一个`Consumer抽象方法void accepte(T t)`接口函数，会将每一个流元素交给该函数进行处理。

### 过滤：filter

可以通过`filter`方法将一个流转换成另一个子集流。

```java
Stream<T> filter(Predicate<? super T> predicate);
```

该接口接受一个`Predicate(唯一抽象方法boolean test(T t))`函数式接口参数(可以是一个Lambda或方法引用)作为筛选条件。

### 映射：map

将流中的元素映射到另一个流中

```java
<R> Stream<R> map(Function<? super T,? extends R> mapper);
```

该接口需要一个`Function(唯一抽象方法R apply(T t))`函数式接口参数，可以将当前流中的T类型数据转换为另一种R类型的流。这种转换的动作，就成为”映射“。

### 统计个数：count

```java
long count()；
```

该方法返回一个long值代表元素个数

### 取用前几个：limit

`limit`方法可以对流进行截取，只取用前n个。

```java
Stream<T> limit(long maxSize);
```

参数是一个long型，如果集合当前长度大于参数则进行截取；否则不进行操作。

### 跳过前几个：skip

跳过前几个元素，可以使用`skip`方法获取一个截取之后的新流：

```java
Stream<T> skip(long n);
```

如果流的当前长度大于n，则跳过前n个；否则将会得到一个长度为0的空流。

### 组合：concat

两个流，希望合并成为一个流，那么可以使用`Stream`接口的静态方法`concat`：

```java
static <T> Stream<T> concat(Stream<? extends T> a,Stream<? extends T> b)
```

> 这是一个静态方法，与`java.lang.String`当中的`concat`方法是不同的。

# 方法引用

在使用Lambda表达式的时候，实际上传递进去的代码就是一种解决方案：拿什么参数做什么操作。