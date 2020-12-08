package io.moomin.web.servlet;

import io.moomin.domain.PageBean;
import io.moomin.domain.User;
import io.moomin.service.UserService;
import io.moomin.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@WebServlet("/findUserByPageServlet")
public class FindUserByPageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        //获取参数
        String currentPage = request.getParameter("currentPage");//当前页码
        String rows = request.getParameter("rows");//每页显示条数

        if (currentPage == null || "".equals(currentPage)) {
            currentPage = "1";
        }
        if (rows == null || "".equals(rows)) {
            rows = "5";
        }
        //获取条件查询参数
        Map<String, String[]> parameterMap = request.getParameterMap();

        //调用service查询
        UserService userService = new UserServiceImpl();
        PageBean<User> userPageBean = userService.findUserByPage(currentPage, rows,parameterMap);
        //将pageBean存入request
        request.setAttribute("userPageBean", userPageBean);
        request.setAttribute("condition",parameterMap);
        HttpSession session = request.getSession();
        session.setAttribute("userPageBean", userPageBean);
        request.getRequestDispatcher("/list.jsp").forward(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
