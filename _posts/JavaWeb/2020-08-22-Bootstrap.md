---
layout: post
title: "Bootstrap"
subtitle: "「」"
author: "月明"
date:  2020-08-22 22:24:00
header-img: "assets/background9.png"
header-mask: 0.3
tags:
  - JavaWeb
  - 学习笔记
  - 开发框架
---

# Bootstrap

> Bootstrap是美国[Twitter](https://baike.baidu.com/item/Twitter/2443267)公司的设计师Mark Otto和Jacob Thornton合作基于HTML、CSS、[JavaScript](https://baike.baidu.com/item/JavaScript/321142) 开发的简洁、直观、强悍的[前端](https://baike.baidu.com/item/前端/5956545)开发框架，使得 Web 开发更加快捷。
>
> * 框架：一个半成品软件，开发人员可以在框架基础上，再进行开发，简化编码。
>
> * 好处：
>   1. 定义了很多的css样式和js插件。我们开发人员可以直接使用这些样式和插件得到丰富的页面效果。
>   2. 响应式布局：同一套页面可以兼容不同分辨率的设备

## 快速入门

1. 下载Bootstrap
2. 在项目中将这三个文件夹复制
3. 创建html页面，引入必要的资源文件

```html
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>Bootstrap HelloWorld</title>

    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="js/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.min.js"></script>
</head>
<body>
<h1>你好，世界！</h1>

</body>
</html>
```

## 响应式布局

实现：

依赖于栅格系统：将一行平均分成12个格子，可以指定元素占几个格子。

步骤：

* 定义容器。相当于之前的table。
  * `container：`两边留白
  * `container-fluid：`每一种设备都是100%宽度
* 定义行。相当于之前的tr。样式：`row`
* 定义元素。指定该元素在不同的设备上，所占的格子数目。样式：`col-设备代号-格子数目`
  * `.col-xs-：`**超小屏幕** 手机 (<768px)
  * `.col-sm-：`**小屏幕** 平板 (≥768px)
  * `.col-md-：`**中等屏幕** 桌面显示器 (≥992px)
  * `.col-lg-：` 大屏幕 大桌面显示器 (≥1200px)

注意：

> 1. 一行中如果格子数目超过12，则超出部分自动换行。
> 2. 栅格类属性可以向上兼容。栅格类适用于屏幕宽度大于或等于分界点大小的设备。
> 3. 如果真实设备宽度小于了设置栅格类属性的设备代码的最小值，会一个元素占满一整行。

## CSS样式和JS插件

全局CSS样式：

* 按钮：`class="btn btn-default"`

```html
<!-- Standard button -->
<button type="button" class="btn btn-default">（默认样式）Default</button>

<!-- Provides extra visual weight and identifies the primary action in a set of buttons -->
<button type="button" class="btn btn-primary">（首选项）Primary</button>

<!-- Indicates a successful or positive action -->
<button type="button" class="btn btn-success">（成功）Success</button>

<!-- Contextual button for informational alert messages -->
<button type="button" class="btn btn-info">（一般信息）Info</button>

<!-- Indicates caution should be taken with this action -->
<button type="button" class="btn btn-warning">（警告）Warning</button>

<!-- Indicates a dangerous or potentially negative action -->
<button type="button" class="btn btn-danger">（危险）Danger</button>

<!-- Deemphasize a button by making it look like a link while maintaining button behavior -->
<button type="button" class="btn btn-link">（链接）Link</button>
```

* 图片

```html
<img src="..." class="img-responsive" alt="Responsive image">
//图片在任意尺寸都占100%
```

  * 图片形状：

```html
<img src="..." alt="..." class="img-rounded">//方形
<img src="..." alt="..." class="img-circle">//圆形
<img src="..." alt="..." class="img-thumbnail">//相框
```

* 表格

  * `table`
  * `table-bordered`
  * `table-hover`

* 表单：给表单项添加`class="form-control"`

```html
<form class="form-horizontal">
  <div class="form-group">
    <label for="inputEmail3" class="col-sm-2 control-label">Email</label>
    <div class="col-sm-10">
      <input type="email" class="form-control" id="inputEmail3" placeholder="Email">
    </div>
  </div>
  <div class="form-group">
    <label for="inputPassword3" class="col-sm-2 control-label">Password</label>
    <div class="col-sm-10">
      <input type="password" class="form-control" id="inputPassword3" placeholder="Password">
    </div>
  </div>
  <div class="form-group">
    <div class="col-sm-offset-2 col-sm-10">
      <div class="checkbox">
        <label>
          <input type="checkbox"> Remember me
        </label>
      </div>
    </div>
  </div>
  <div class="form-group">
    <div class="col-sm-offset-2 col-sm-10">
      <button type="submit" class="btn btn-default">Sign in</button>
    </div>
  </div>
</form>
```

组件：

* 导航条
* 分页条

插件：

* 轮播图