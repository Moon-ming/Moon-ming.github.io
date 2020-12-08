package io.moomin.web.cookie;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/CookieDemo")
public class CookieDemo extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie cookie = new Cookie("msg", "hello");
        //可以创建多个Cookie对象
        Cookie cookie1 = new Cookie("name", "zhangsan");
        cookie1.setMaxAge(30);//将cookie持久化到硬盘,30秒后自动删除cookie文件
        //cookie.setMaxAge(0);
        cookie1.setPath("/");
        response.addCookie(cookie);
        response.addCookie(cookie1);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
