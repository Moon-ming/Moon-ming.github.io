package io.moomin.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
//浏览器直接请求资源时,该过滤器会被执行
//转发访问,该过滤器会执行
@WebFilter(value = "/*",dispatcherTypes = {DispatcherType.REQUEST,DispatcherType.FORWARD})
public class FilterDemo1 implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("filterdemo1 is runnig");
        chain.doFilter(req, resp);
        System.out.println("filterdemo1 is back");
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
