package com.aws.login;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class HeaderLoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("In init method of HeaderLoginFilter");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("In doFilter method of HeaderLoginFilter");
        MutableHttpServletRequest mutableHttpServletRequest=new MutableHttpServletRequest((HttpServletRequest) servletRequest);
        mutableHttpServletRequest.putHeader("SM_USER","kim");
        filterChain.doFilter(mutableHttpServletRequest,servletResponse);
    }

    @Override
    public void destroy() {
        System.out.println("In destroy method of HeaderLoginFilter");

    }
}
