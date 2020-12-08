---
layout: post
title: "JavaScript高级"
subtitle: "「BOM、DOM、事件」"
author: "月明"
date:  2020-08-15 18:10:00
header-img: "assets/background7.png"
header-mask: 0.3
tags:
  - JavaWeb
  - 学习笔记
  - JavaScript
---

# BOM

> Browser Object Model 浏览器对象模型，将浏览器的各个组成部分封装成对象。

## Navigator：浏览器对象

## `window`：窗口对象

* 创建

* 方法

  * **与弹出框有关的方法**
    1. `alert()：`显示带有一段消息和一个确认按钮的警告框
    2. **`confirm()：`**显示带有一段消息以及确认按钮和取消按钮的对话框
       * 如果用户点击确定按钮，则方法返回true
       * 如果用户点击取消按钮，则方法返回false
    3. `prompt()：`显示可提示用户输入的对话框
       * 返回值：获取用户输入的值
  * **与打开关闭有关的方法**
    1. `close()：`关闭浏览器窗口
       * 谁调我，我关谁
    2. `open()：`打开一个新的浏览器窗口
       * 返回新的window对象
  * **与定时器有关的方法**
    1. `setTimeout()：`在指定的毫秒数后调用函数或计算表达式
       * 参数
         1. js代码或者方法对象
         2. 毫秒值
       * 返回值：唯一标识，用于取消定时器
    2. `clearTimeout()：`取消由setTimeout()方法设置的timeout
    3. `setInterval()：`按照指定的周期(以毫秒计)来调用函数或计算表达式
    4. `clearInterval()：`取消由setInterval()设置的timeout

* 属性

  1. 获取其他BOM对象：

     `history`

     `location`

     `Navigator`

     `Screen`

  2. 获取DOM对象

     `document`

* 特点

  * window对象不需要创建可以直接使用window使用。`window.方法名()`
  * window引用可以省略。`方法名()`

## `location`：地址栏对象

* 创建(获取)

  1. `window.location`
  2. `location`

* 方法

  `reload()：`重新加载当前文档。刷新

* 属性

  `href：`设置或返回完整的URL

## `History`：历史记录对象

* 创建

  1. `window.history`
  2. `history`

* 方法

  `back()：`加载history列表中的前一个URL

  `forward()：`加载history列表中的下一个URL

  `go()：`加载history列表中的某个具体页面

  > 正数：前进几个历史记录
  >
  > 负数：后退几个历史记录

* 属性

  `length：`返回当前窗口历史列表中的URL数量

## Screen：显示器屏幕对象

## DOM对象 Document对象

# DOM

> Document Object Model 文档对象模型，将标记语言文档的各个组成部分，封装成对象。可以使用这些对象，对标记语言文档进行CRUD的动态操作。
>
> 控制html文档的内容

* `document.getElementById(id值)`:通过元素的id获取页面标签(元素)对象

* 操作Element对象

  * 修改属性值

    1. 明确获取的对象是哪一个
    2. 查看API文档，找其中有哪些属性可以设置

  * 修改标签体内容 

    * 属性：`innerHTML`

    1. 获取元素对象
    2. 使用innerHTML属性修改标签体内容

## W3C DOM标准被分为3个不同的部分：

### 核心DOM：针对任何结构化文档的标准模型

#### `Document：`文档对象

* 创建(获取)：在html dom模型中可以使用window对象来获取
  * `window.document`
  * `document`
* 方法
  * 获取Element对象
    1. `getElementById()：`根据id属性值获取元素对象。id属性值一般唯一
    2. `getElementsByTagName()：`根据元素名称获取元素对象们。返回值是一个数组
    3. `getElementsByClassName()：`根据Class属性值获取元素对象们。返回值是一个数组
    4. `getElementsByName()：`根据name属性值获取元素对象们。返回值是一个数组
  * 创建其他DOM对象
    1. `createAttribute(name)：`
    2. `createComment()：`
    3. `createElement()：`
    4. `createTextNode()：`
* 属性

#### `Element：`元素对象

* 创建(获取)：通过document来获取和创建

* 方法：

  `removeAttribute()：`删除属性

  `setAttribute()：`设置属性

#### `Attribute：`属性对象

#### `Text：`文本对象

#### `Comment:`注释对象

#### `Node:`节点对象，其他5个的父对象

* 特点：所有dom对象都可以被认为是一个节点

* 方法

  * CRUD dom树

    `appendChild()：`向节点的子节点列表的结尾添加新的子节点

    `removeChild()：`删除(并返回)当前节点的指定子节点

    `replaceChild()：`用新节点替换一个子节点

* 属性

  `parentNode`返回节点的父节点

### XML DOM：针对XML文档的标准模型

### HTML DOM：针对HTML文档的标准模型

* 标签体的设置和获取：`innerHTML`

* 使用html元素对象的属性

* 控制样式

  * 使用元素的`style`属性来设置

  ```html
  div1.style.border = "1px solid red";
  div1.style.width = "200px";
  //font -size ---> fontsize
  div1.style.fontsize = "20px";
  ```

  * 提前定义好类选择器的样式，通过元素的`className`属性来设置其class属性

## 事件

> 某些组件被执行了某些操作后，触发某些代码的执行
>
> 事件：某些操作。如：单击，双击，键盘按下，鼠标移动了
>
> 事件源：组建。如按钮，文本输入框
>
> 监听器：代码
>
> 注册监听：将事件，事件源，监听器结合在一起。当事件源上发生来某个事件，则触发执行某个监听器代码

* 如何绑定事件
  1. 直接在html标签上，指定事件的属性，属性值就是js代码
     * 事件：`onclik` 单击事件
  2. 通过js获取元素对象，指定事件属性，设置一个函数

### 常见的事件

* 点击事件
  
  * `onclick：`单击事件
  * `ondblclick：`双击事件
* 焦点事件

  * **`onblur：`** 失去焦点
    * 一般用于表单校验
  * `onfocus：`元素获得焦点
* 加载事件

  * **`onload：`** 一个页面或一副图像完成加载
    * 一般给body、window
* 鼠标事件

  * `onmousedown：`鼠标按钮被按下
  * 定义方法时，定义一个形参，接受`event`对象。
    * `event`对象的`button`属性可以获取鼠标哪个按钮键被点击了
* `onmouseup：`鼠标按钮被松开
  * `onmousemove：`鼠标被移动
* `onmouseout：`鼠标从某元素移开
  * **`onmouseover：`** 鼠标移到某元素之上
* 键盘事件
  * **`onkeydown：`** 某个键盘按钮被按下
  * `onkeyup：`某个键盘按钮被松开
  * `onkeypress：`某个键盘按钮被按下并松开
* 选中和改变
  * **`onchange：`** 域的内容被改变
  * `onselect：`  文本被选中
* 表单事件
  * `onsubmit：`确认按钮被点击
    * 可以阻止表单的提交
    * 方法返回false则表单被阻止提交
  * `onreset：`重置按钮被点击

