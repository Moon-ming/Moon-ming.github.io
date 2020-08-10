---
layout: post
title: "JavaScript基础"
subtitle: "「」"
author: "月明"
date:  2020-08-08 19:10:00
header-img: "assets/background6.png"
header-mask: 0.3
tags:
  - JavaWeb
  - 学习笔记
  - JavaScript
---

# JavaScript基础

> 一门客户端脚本语言，运行在客户端浏览器中的。每一个浏览器都有JavaScript的解析引擎
>
> 脚本语言：不需要编译，直接就可以被浏览器解析执行了

功能：可以来增强用户和html页面的交互过程，可以来控制html元素，让页面有一些动态的效果，增强用户的体验

## JavaScript发展史

1. 1992年，Nombase公司，开发出第一门客户端脚本语言，专门用于表单的校验。命名为：c--，后来更名为：ScriptEase。
2. 1995年，Netscape(网景)公司，开发了一门客户端脚本语言：LiveScript。后来请来SUN公司的专家，修改LiveScript，命名为JavaScript。
3. 1996年，微软抄袭JavaScript开发出JScript语言。
4. 1997年，ECMA(欧洲计算机制造商协会)，ECMAScript，就是所有客户端脚本语言的标准。

JavaScript = ECMAScript + JavaScript自己特有的东西(BOM+DOM)

## ECMAScript

> 客户端脚本语言的标准

### 基本语法

#### 与html结合方式

* 内部JS
  * 定义< script >，标签体内容就是js代码
* 外部JS
  * 定义< script >，通过src属性引入外部的js文件

> 注意: < script > 可以定义在html页面的任何地方，但是定义的位置会影响执行
>
> ​	 < script > 标签可以定义多个

#### 注释

* 单行注释：//注释内容
* 多行注释：/* 注释内容 */

#### 数据类型

* `原始`数据类型(基本数据类型)
  * `number：`数字。整数、小数、NAN(not a number 一个不是数字的数字类型)
  * `string：` 字符串。字符串"abc" "a" 'abc'
  * `boolean：`true和false
  * `null：`一个对象为空的占位符(Object 类型)
  * `undefined：`未定义。如果一个变量没有给初始化值，则会被默认赋值为undefined
* 引用数据类型：对象

#### 变量

> 一小块存储数据的内存空间
>
> Java语言是强类型语言，而JavaScript是弱类型语言
>
> * 强类型：在开辟变量存储空间时，定义了空间将来存储的数据的数据类型。只能存储固定类型的数据
> * 弱类型：在开辟变量存储空间时，不定义空间将来的存储数据类型，可以存放任意类型的数据

* 语法：
  * **`var`** `变量名 = 初始化值`

#### 运算符

* 一元运算符：只有一个运算数的运算符

  * ++，--，+(正号)
    * 在前，先自增(减)，再远算
    * 在后，先运算，再自增(减)

  > 在JS中，如果远算数不是运算符所要求的类型，那么js引擎会自动的将运算数进行类型转换
  >
  > * 其他类型转number：
  >   * string转number，按照字面值转换。如果字面值不是数字，则转为NaN(不是数字的数字)，运算后还是NaN。
  >   * boolean转number，true转为1，false转为0.

* 算数运算符

  * +，-，*，/，%

* 赋值运算符

  * =，+=，-=

* 比较运算符

  * \>， <， >=， <=， =，===(全等于)
  * 类型相同，直接比较
    * `字符串`：按照字典顺序比较，按位逐一比较，直到得出大小为止。
  * 类型不同，先进行类型转换，再比较
    * `===` 全等于。在比较之前，先判断类型，如果类型不一样，则直接返回false

* 逻辑远算符

  * &&(短路)，\||(短路)， !

    > 其他类型转boolean：
    >
    > 1. `number：`0会NaN为假，其他为真
    >
    >  	2. `string：`除了空字符串`”“`，其他都是true
    >  	3. `null&undefined：`都是false
    >  	4. `对象：`所有对象都是true

  ```javascript
  if(obj){//防止空指针异常
      alert("moomin");
  }
  ```

  

* 三元运算符

  * 表达式? 值1 : 值2

#### 流程控制语句

* `if...else...`

* `switch`

  * 在Java中，switch语句可以接收的数据类型：`byte`、`int`、`short`、`char`、`枚举(1.5)`、`String(1.7)`

    * switch(变量)：

      ​    case   值： 

  * 在JS中，switch可以接收任意的原始数据类型

* `while`

* `do...while`

* `for`

> 变量的定义使用`var`关键字，也可以不使用。
>
> * 用，定义的变量是局部变量
> * 不用，定义的变量是全局变量(不建议)

### 基本对象

* `Function：`函数(方法)对象

  1. 创建：

     * `function 方法名称(形式参数列表){`

       ​                              `方法体}`

     * `var 方法名 = function(形式参数列表){`

       ​                              `方法体}`

  2. 方法：

  3. 属性：

     * `length：`代表形参的个数

  4. 特点：

     * 方法定义时，形参类型不用写(var)
     * 方法是一个对象，如果定义名称相同的方法，会覆盖
     * 在JS中，方法的调用只与方法的名称有关，和参数列表无关
     * 在方法声明中有一个隐藏的内置对象(数组)，`arguments`，封装所有的实际参数

  5. 调用：

     * 方法名称(实际参数列表);

     

* `Array：`数组对象

  1. 创建：
     * `var arr = new Array(元素列表);`
     * `var arr = new Array(默认长度);`
     * `var arr = [元素列表];`
  2. 方法
  3. 属性
  4. 特点

* `Boolean：`

* `Date：`

* `Math：`

* `Number：`

* `String：`

* `RegExp：`

* `Global：`

## BOM

## DOM

