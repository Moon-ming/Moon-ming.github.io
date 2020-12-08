package io.moomin.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@WebServlet("/findUserServlet")
public class FindUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        String username = request.getParameter("username");
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        if ("tom".equals(username)) {
            stringObjectHashMap.put("userExsit",true);
            stringObjectHashMap.put("msg","换一个");
        } else {
            stringObjectHashMap.put("userExsit",false);
            stringObjectHashMap.put("msg","ok");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(),stringObjectHashMap);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
