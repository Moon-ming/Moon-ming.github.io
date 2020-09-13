---
layout: post
title: "Cookie&Session"
subtitle: "「」"
author: "月明"
date:  2020-09-05 16:58:00
header-img: "assets/background4.png"
header-mask: 0.3
tags:
  - JavaWeb
  - 学习笔记
  - Cookie
  - Session
---

# 会话技术

> 一次会话中包含多次请求和响应
>
> 一次会话：浏览器第一次给服务器资源发送请求，会话建立，直到有一方断开为止

功能：在一次会话的范围内的多次请求间，共享数据

方式

* 客户端会话技术：Cookie
* 服务器端会话技术：Session

# Cookie

> 客户端会话技术，将数据保存到客户端

快速入门

* 使用步骤

  1. 创建Cookie对象，绑定数据

     `new Cookie(String name,String value)`

  2. 发送Cookie对象

     `response.addCookie(Cookie cookie)`

  3. 获取Cookie，拿到数据

     `Cookie[] request.getCookies()`

实现原理：基于响应头set-cookie和请求头cookie实现

cookie的细节

1. 一次可不可以发送多个cookie？

   * 可以
   * 可以创建多个Cookie对象，使用response调用多次addCookie方法发送cookie即可

2. cookie在浏览器中保存多场时间？

   1. 默认情况下，当浏览器关闭后，Cookie数据被销毁

   2. 持久化存储

      `setMaxAge(int seconds)`

      * 正数：将Cookie数据写到硬盘的文件中持久化存储。并指定cookie存活时间，时间到后，cookie文件自动失效
      * 负数：默认值
      * 零：删除cookie信息

   3. cookie能不能存中文？

      * 在tomcat 8 之前cookie中不能直接存储中文数据
      * 需要将中文数据转码——一般采用URL编码
      * 在tomcat 8 之后，cookie支持中文数据。特色字符还是不支持，建议使用URL编码存储，URL解码解析

   4. cookie共享问题？

      1. 假设在一个tomcat服务器中，部署了多个web项目，那么在这些web项目中cookie能不能共享？
         * 默认情况下cookie不能共享
         * `setPath(String path)：`设置cookie的获取范围。默认情况下，设置当前的虚拟目录
           * 如果要共享，则可以将path设置为“/”
      2. 不同的tomcat服务器键cookie共享问题？
         * `setDomain(String path)：`如果设置一级域名相同，那么多个服务器之间cookie可以共享
         * `setDomain(".baidu.com")`，那么tieba.baidu.com和news.baidu.com中cookie可以共享

   5. Cookie的特点和作用

      1. cookie存储数据在客户端浏览器
      2. 浏览器对于单个cookie的大小有限制（4kb）以及对同一个域名下的总cookie数量也有限制（20个）
      * 作用
        * cookie一般用于存储少量的不太敏感的数据
        * 在不登录的情况下，完成服务器对客户端的身份识别
      
   6. 案例：记住上一次访问时间

   > 1. 访问⼀个Servlet，如果是第⼀次访问，则提
   > ⽰：您好，欢迎您⾸次访问。
   > 2. 如果不是第⼀次访问，则提⽰：欢迎回来，您上
   > 次访问时间为:显⽰时间字符串

分析：
1. 可以采⽤Cookie来完成
2. 在服务器中的Servlet判断是否有⼀个名为
  lastTime的cookie
  1. 有：不是第⼀次访问
     1. 响应数据：欢迎回来，您上次访问时
       间为:2018年6⽉10⽇11:50:20
     2. 写回Cookie：lastTime=2018年6
       ⽉10⽇11:50:01
  2. 没有：是第⼀次访问
     1. 响应数据：您好，欢迎您⾸次访问
     2. 写回Cookie：lastTime=2018年6
       ⽉10⽇11:50:01

```java
package cn.itcast.cookie;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/cookieTest")
public class CookieTest extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置响应的消息体的数据格式以及编码
        response.setContentType("text/html;charset=utf-8");
        //1.获取所有Cookie
        Cookie[] cookies = request.getCookies();
        boolean flag = false;//没有cookie为lastTime
        //2.遍历cookie数组
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                //3.获取cookie的名称
                String name = cookie.getName();
                //4.判断名称是否是：lastTime
                if ("lastTime".equals(name)) {
                    //有该Cookie，不是第⼀次访问
                    flag = true;//有lastTime的cookie
                    //设置Cookie的value
                    //获取当前时间的字符串，重新设置Cookie的值，重新发送cookie
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM⽉dd⽇ HH:mm: ss ");
                    String str_date = sdf.format(date);
                    System.out.println("编码前：" + str_date);
                    //URL编码
                    str_date = URLEncoder.encode(str_date, "utf-8");
                    System.out.println("编码后：" + str_date);
                    cookie.setValue(str_date);
                    //设置cookie的存活时间
                    cookie.setMaxAge(60 * 60 * 24 * 30);//⼀个⽉
                    response.addCookie(cookie);
                    //响应数据
                    //获取Cookie的value，时间
                    String value = cookie.getValue();
                    System.out.println("解码前：" + value);
                    //URL解码：
                    value = URLDecoder.decode(value, "utf-8");
                    System.out.println("解码后：" + value);
                    response.getWriter().write("<h1>欢迎回来，您上次访问时间为:" + value + " </h1 > ");
                    break;
                }
            }
        }
        if (cookies == null || cookies.length == 0 || flag == false) {
            //没有，第⼀次访问
            //设置Cookie的value
            //获取当前时间的字符串，重新设置Cookie的值，重新发送cookie
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM⽉dd⽇ HH:mm:ss");
            String str_date = sdf.format(date);
            System.out.println("编码前：" + str_date);
            //URL编码
            str_date = URLEncoder.encode(str_date, "utf-8");
            System.out.println("编码后：" + str_date);
            Cookie cookie = new Cookie("lastTime", str_date);
            //设置cookie的存活时间
            cookie.setMaxAge(60 * 60 * 24 * 30);//⼀个⽉
            response.addCookie(cookie);
            response.getWriter().write(" < h1 > 您好，欢迎您⾸次访问 < / h1 > ");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
```

# JSP

>  Java Server Pages：java服务端页面
>
> 可以理解为：一个特色的页面，期中即可以指定定义html标签，又可以定义java代码
>
> 用于简化书写！！！

JSP本质上就是一个Servlet

JSP的脚本：JSP定义Java代码的方式

1. `<%代码%>：`定义的java代码，在service方法中。service方法中可以定义什么，该脚本中就可以定义什么。
2. `<%!代码 %>`

