package io.moomin.web.servletcontext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@WebServlet("/servletContext")
public class servletContext extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /**
         * 获取
         */
        ServletContext servletContext = request.getServletContext();
        ServletContext servletContext1 = this.getServletContext();
        System.out.println(servletContext);
        System.out.println(servletContext1);
        System.out.println(servletContext==servletContext1);//true

        /**
         * 功能
         * 获取MIME类型
         * 共享数据
         * 获取文件的真实路径
         */
        String filename = "a.jpg"; //image/jepg
        String mimeType = servletContext.getMimeType(filename);
        System.out.println(mimeType);

        servletContext1.setAttribute("msg","haha");

        String realPath = servletContext1.getRealPath("/b.txt");//web目录下资源访问
        System.out.println(realPath);
        File file = new File(realPath);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}
