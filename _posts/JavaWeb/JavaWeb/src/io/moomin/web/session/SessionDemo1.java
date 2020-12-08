package io.moomin.web.session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/SessionDemo1")
public class SessionDemo1 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object msg = session.getAttribute("msg");
        System.out.println(msg);


        //期望客户端关闭后,session也能相同
        Cookie cookie = new Cookie("JSESSIONID", session.getId());
        cookie.setMaxAge(60*60);
        response.addCookie(cookie);
        System.out.println(session);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
