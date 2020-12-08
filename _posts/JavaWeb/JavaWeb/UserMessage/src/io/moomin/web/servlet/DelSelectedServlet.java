package io.moomin.web.servlet;

import io.moomin.domain.PageBean;
import io.moomin.service.UserService;
import io.moomin.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/delSelectedServlet")
public class DelSelectedServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] uids = request.getParameterValues("uid");
        UserService userService = new UserServiceImpl();
        userService.delSelectedUser(uids);
        HttpSession session = request.getSession();
        PageBean userPageBean = (PageBean) session.getAttribute("userPageBean");
        response.sendRedirect(request.getContextPath()+"/findUserByPageServlet?currentPage="+userPageBean.getCurrentPage()+"&rows=5");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
