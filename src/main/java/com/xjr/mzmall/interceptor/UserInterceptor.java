package com.xjr.mzmall.interceptor;

import com.xjr.mzmall.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getSession().getAttribute("user") == null){
            System.out.println(request.getSession().getAttribute("user"));
            response.sendRedirect("/mzmall/login");
            return false;
        }
        return true;
    }
}
