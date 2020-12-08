package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    //声明UserService业务对象
    private UserService userService = new UserServiceImpl();
    /**
     * 注册用户
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //验证校验
        String check = request.getParameter("check");
        //从session中获取验证码
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        //为了保证验证码只能使用一次
        session.removeAttribute("CHECKCODE_SERVER");
        //比较
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)) {
            //验证码错误
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("验证码失败!");
            /*ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(resultInfo);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);*/
            writeValue(resultInfo,response);
            return;
        }

        //1.获取数据
        Map<String, String[]> map = request.getParameterMap();
        //2.封装对象
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //3.调用service完成注册
        boolean flag = userService.regist(user);
        ResultInfo resultInfo = new ResultInfo();
        //4.响应结果
        if (flag) {
            //注册成功
            resultInfo.setFlag(true);
        } else {
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("注册失败!");
        }

        /*//将resultInfo对象序列化为Json
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(resultInfo);
        //将json数据写回客户端
        //设置content-type
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);*/
        writeValue(resultInfo,response);
    }

    /**
     * 登录用户
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //验证校验
        String check = request.getParameter("check");
        //从session中获取验证码
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        //为了保证验证码只能使用一次
        session.removeAttribute("CHECKCODE_SERVER");
        //比较
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)) {
            //验证码错误
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("验证码失败!");
            /*ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(resultInfo);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);*/
            writeValue(resultInfo,response);
            return;
        }

        //1.获取用户名和密码数据
        Map<String, String[]> map = request.getParameterMap();
        //2.封装User对象
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //3.调用Service查询
        User loginUser = userService.login(user);
        ResultInfo resultInfo = new ResultInfo();
        //4.判断用户对象是否为null
        if (loginUser == null) {
            //用户名密码错误
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("用户名密码错误");
        }
        //5.判断用户是否激活
        if (loginUser != null && !"Y".equals(loginUser.getStatus())) {
            //用户尚未激活
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("用户尚未激活");
        }
        //6.判断登录成功
        if (loginUser != null && "Y".equals(loginUser.getStatus())) {
            //登录成功
            resultInfo.setFlag(true);
            request.getSession().setAttribute("user", loginUser);
        }
        //7.响应数据
        /*ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(resultInfo);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);*/
        writeValue(resultInfo,response);
    }

    /**
     * 查询单个用户
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //从session中获取登录用户
        Object user = req.getSession().getAttribute("user");
        //将user写回客户端
        /*ObjectMapper objectMapper = new ObjectMapper();
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(objectMapper.writeValueAsString(user));*/
        writeValue(user,resp);
    }

    /**
     * 退出
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //销毁seesion
        req.getSession().invalidate();
        //跳转页面
        resp.sendRedirect(req.getContextPath() + "/login.html");
    }

    /**
     * 激活
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void active(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.获取激活码
        String code = req.getParameter("code");
        if (code != null) {
            //2.调用service完成激活
            boolean flag = userService.active(code);
            //3.判断标记
            String msg = null;
            if (flag) {
                msg = "激活成功,请<a href='"+req.getContextPath()+"/login.html'>登录</a>";
            } else {
                msg = "激活失败,请联系管理员";
            }
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().write(msg);
        }
    }
}
