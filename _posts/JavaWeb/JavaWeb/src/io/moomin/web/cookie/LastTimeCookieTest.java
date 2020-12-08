package io.moomin.web.cookie;

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

/**
 * 在服务器中的Servlet判断是否有⼀个名为 lastTime的cookie
 * 有：不是第⼀次访问
 * 响应数据：欢迎回来，您上次访问时 间为:xxxxx
 * 写回Cookie：lastTime=xxxx
 * 没有：是第⼀次访问
 * 响应数据：您好，欢迎您⾸次访问
 * 写回Cookie：lastTime=xxxxx
 */
@WebServlet("/CookieTest")
public class LastTimeCookieTest extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       response.setContentType("text/html;charset=utf-8");
        //1.获取所有Cookie
        Cookie[] cookies = request.getCookies();
        boolean flag = false;
        //2.遍历cookie数组
        if (cookies != null && cookies.length > 0) {
            for (Cookie c :
                    cookies) {
                //3.获取Cookie的名称
                String name = c.getName();
                //判断名称是否是:lastTime
                if ("lastTime".equals(name)) {
                    //有该Cookie,不是第一次访问
                    flag = true;
                    //设置Cookie的value
                    //获取当前时间的字符串,重新设置Cookie的值,重新发送cookie
                    Date date = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                    String format = simpleDateFormat.format(date);
                    System.out.println("编码前" + format);
                    //URL编码
                    format = URLEncoder.encode(format, "utf-8");
                    System.out.println("编码后" + format);
                    c.setValue(format);
                    //设置cookie的存活时间
                    c.setMaxAge(60 * 60 * 24 * 30);
                    response.addCookie(c);
                    //响应数据
                    //获取Cookie的value:时间
                    String value = c.getValue();
                    System.out.println("解码前:" + value);
                    //URL解码
                    value = URLDecoder.decode(value, "utf-8");
                    System.out.println("解码后:"+value);
                    response.getWriter().write("<h1>欢迎回来,您上次访问的时间为:" + value + "</h1>");
                    break;
                }


            }
        }
        if (cookies == null || cookies.length == 0 || flag == false) {
            //没有,第一次访问

            //获取当前时间的字符串,重新设置Cookie的值,重新发送cookie
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            String format = simpleDateFormat.format(date);
            System.out.println("编码前" + format);
            //URL编码
            format = URLEncoder.encode(format, "utf-8");
            System.out.println("编码后" + format);
            Cookie lastTime = new Cookie("lastTime", format);
            //设置cookie的存活时间
            lastTime.setMaxAge(60 * 60 * 24 * 30);
            response.addCookie(lastTime);
            response.getWriter().write("<h1>您好,欢迎首次访问</h1>");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
