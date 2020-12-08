package io.moomin.controller;

import io.moomin.domain.Account;
import io.moomin.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/param")
public class ParamController {
    @RequestMapping("testParam")
    public String testParam(String username,String password) {
        System.out.println("running.."+ username + ","+ password);
        return "success";
    }

    @RequestMapping("saveAccount")
    public String testParam(Account account) {
        System.out.println(account);
        return "success";
    }

    //自定义类型转换器
    @RequestMapping("saveUser")
    public String testParam(User user)  {
        System.out.println(user);
        return "success";
    }


    @RequestMapping("testServlet")
    public String testServlet(HttpServletRequest request, HttpServletResponse response) {
        System.out.println(request);
        System.out.println(response);
        HttpSession session = request.getSession();
        System.out.println(session);
        ServletContext servletContext = session.getServletContext();
        System.out.println(servletContext);
        return "success";
    }
}
