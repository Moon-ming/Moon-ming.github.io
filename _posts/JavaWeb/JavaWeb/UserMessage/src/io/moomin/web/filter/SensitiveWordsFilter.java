package io.moomin.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

@WebFilter("/*")
public class SensitiveWordsFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        //创建代理对象,增强getParameter方法
        ServletRequest proxy_req = (ServletRequest) Proxy.newProxyInstance(req.getClass().getClassLoader(), req.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //增强getParameter方法
                //判断是否是getparameter方法
                if (method.getName().equals("getparameter")) {
                    //增强返回值
                    //获取返回值
                    String value =  (String) method.invoke(req,args);
                    if (value != null) {
                        for (String str : list) {
                            if (value.contains(str)) {
                                value = value.replaceAll(str, "***");
                            }
                        }
                    }
                    return value;
                }
                //判断方法名是否是getParameterMap()

                //判断方法名是否是getParameterValues()
                return method.invoke(req,args);
            }

        });
        chain.doFilter(proxy_req, resp);
    }

    private List<String> list = new ArrayList<String>();//敏感词汇集合
    public void init(FilterConfig config) throws ServletException {
        try {
            //获取文件真实路径
            ServletContext servletContext = config.getServletContext();
            String realPath = servletContext.getRealPath("/WEB-INF/classes/敏感词汇.txt");
            //读取文件
            BufferedReader bufferedReader = new BufferedReader(new FileReader(realPath));
            //将文件的每一行数据添加到list中
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                list.add(line);
                System.out.println(list);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void destroy() {
    }
}
