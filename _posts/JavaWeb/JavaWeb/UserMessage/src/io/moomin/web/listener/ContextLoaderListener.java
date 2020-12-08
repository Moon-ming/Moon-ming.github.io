package io.moomin.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
@WebListener
public class ContextLoaderListener implements ServletContextListener {
    /**
     * 监听ServletContext对象创建的.ServletContext对象服务器启动后自动创建
     * @param servletContextEvent
     * 在服务器启动后自动调用
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //加载资源文件
        //获取ServletContext对象
        ServletContext servletContext = servletContextEvent.getServletContext();
        //加载资源文件
        String initParameter = servletContext.getInitParameter("");
        String realPath = servletContext.getRealPath(initParameter);
        try {
            FileInputStream fileInputStream = new FileInputStream(realPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("ServletContext对象被创建了...");
    }

    /**
     * 在服务器关闭后,servletContext对象被销毁.当服务器正常关闭后该方法被调用
     * @param servletContextEvent
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("ServletContext对象被销毁了...");
    }
}
