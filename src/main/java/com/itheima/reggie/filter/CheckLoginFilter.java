package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(value = "/*")
public class CheckLoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("-------登陆过滤器开启过滤--------");
    }

    //确定需要放行的路径
    String[] urls = {
            "/backend/**",
            "/front/**",
            "/employee/login",
            "/common/**",
            "/user/sendMsg",
            "/user/login",
            "/doc.html",
            "/webjars/**",
            "/swagger-resources",
            "/v2/api-docs"
    };

    //路径匹配类
    AntPathMatcher antPathMatcher = new AntPathMatcher();


    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        //0.获取request中的方法,将req和resp进行强转
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        //1.获取用户访问的路径
        String requestURI = request.getRequestURI();
        //System.out.println(requestURI);
        //例如:  /backend/index.html    /employee/page

        //2.判断用户访问的路径是否在放行的路径中,若是直接放行
        boolean flag = checkURI(requestURI);
        if (flag) {
            chain.doFilter(request,response);
            return;
        }

        //3.若是需要拦截的路径.再获取session中登陆对象
        Object employeeId = request.getSession().getAttribute("employee");
        Object userId = request.getSession().getAttribute("user");

        //4.若有对象,直接放行
        if (userId != null || employeeId!=null) {
            chain.doFilter(request,response);
            return;
        }

        //5.若没有对象,返回R.error("NOTLOGIN")
        R<Object> r = R.error("NOTLOGIN");

        //将r转成json数据写回浏览器
        response.setContentType("application/json;charset=utf-8");//可以省略
        response.getWriter().print(JSON.toJSONString(r));
    }

    private boolean checkURI(String requestURI) {
        for (String url : urls) {
            //若当前请求的路径和url能够匹配上就放行
            if (antPathMatcher.match(url, requestURI)) {
                return true;
            }
        }
        return false;
    }
}
