package io.moomin.web.request;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/RequestDemo3")
public class RequestDemo3 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String referer = request.getHeader("referer");
        System.out.println(referer);
        if (referer != null) {
            if (referer.contains("/servlet_demo")) {
                response.setContentType("text/html;charset=utf-8");
                response.getWriter().write("moomin");
            } else {
                response.setContentType("text/html;charset=utf-8");
                response.getWriter().write("autumnmoonming");
            }
        }
    }
}
