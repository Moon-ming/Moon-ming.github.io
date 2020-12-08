package io.moomin.web.download;

import io.moomin.web.utils.DownLoadUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;

@WebServlet("/downLoadServlet")
public class downLoadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取请求参数,文件名称
        String filename = request.getParameter("filename");

        //2.使用字节输入流加载文件进内存

        //2.1找到文件服务器路径
        ServletContext servletContext = this.getServletContext();
        String realPath = servletContext.getRealPath("/img/" + filename);

        //2.2用字节流关联
        FileInputStream fileInputStream = new FileInputStream(realPath);

        //3设置response的响应头

        //3.1设置响应头类型 content-type
        String mimeType = servletContext.getMimeType(filename);
        response.setHeader("content-type", mimeType);

        //3.2设置响应头打开方式 content-disposition
            //解决中文文件名问题
            //获取user-agent请求头
        String header = request.getHeader("user-agent");
            //使用工具类文件编码文件名即可
        filename = DownLoadUtils.getFileName(header, filename);
        response.setHeader("content-disposition", "attachment;filename=" + filename);

        //4将输入流的数据写出到输出流中
        ServletOutputStream outputStream = response.getOutputStream();
        byte[] bytes = new byte[1024 * 8];
        int len = 0;
        while ((len = fileInputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, len);
        }
        fileInputStream.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
