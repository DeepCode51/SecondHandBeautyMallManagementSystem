package com.xjr.mzmall.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getSession().getAttribute("admin") == null){
            System.out.println(request.getSession().getAttribute("admin"));
            response.sendRedirect("/admin/login");
            return false;
        }
        return true;
    }
}
