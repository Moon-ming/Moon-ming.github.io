package io.moomin.web.servlet;

import io.moomin.domain.User;
import io.moomin.service.UserService;
import io.moomin.service.impl.UserServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置编码
        request.setCharacterEncoding("utf-8");
        //获取数据
        String verifycode = request.getParameter("verifycode");
        //验证码校验
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");
        if (!checkcode_server.equalsIgnoreCase(verifycode)) {
            //验证码不正确
            //提示信息
            request.setAttribute("login_msg","验证码错误");
            //跳转登录页面
            request.getRequestDispatcher("login.jsp").forward(request,response);
            return;
        }

        Map<String, String[]> parameterMap = request.getParameterMap();
        //封装User对象
        User user = new User();
        try {
            BeanUtils.populate(user,parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //调用Service查询
        UserService userService = new UserServiceImpl();
        User login = userService.login(user);
        //判断是否登录成功
        if (login != null) {
            //将用户存入session
            session.setAttribute("user", login);
            //跳转页面
            response.sendRedirect(request.getContextPath()+"index.jsp");
        } else {
            //提示信息
            request.setAttribute("login_msg","用户名或密码错误");
            //跳转登录页面
            request.getRequestDispatcher("login.jsp").forward(request,response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
