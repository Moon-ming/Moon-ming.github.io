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

## 事件

> 某些组件被执行了某些操作后，触发某些代码的执行

* 如何绑定事件
  1. 直接在html标签上，指定事件的属性，属性值就是js代码
     * 事件：`onclik` 单击事件
  2. 通过js获取元素对象，指定事件属性，设置一个函数

