package io.moomin.web.servlet;

import io.moomin.domain.User;
import io.moomin.service.UserService;
import io.moomin.service.impl.UserServiceImpl;

import java.io.IOException;
import java.util.List;

@javax.servlet.annotation.WebServlet("/userListServlet")
public class UserListServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        //调用UserService完成查询
        UserService service = new UserServiceImpl();
        List<User> users = service.findAll();
        //将list存入request域
        request.setAttribute("users",users);
        //转发到list.jsp
        request.getRequestDispatcher("/list.jsp").forward(request,response);

    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        this.doPost(request, response);
    }
}
