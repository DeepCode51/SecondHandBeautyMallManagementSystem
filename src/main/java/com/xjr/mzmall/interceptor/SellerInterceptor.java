package com.xjr.mzmall.interceptor;

import com.xjr.mzmall.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SellerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || user.getRole() == 0){
            System.out.println(request.getSession().getAttribute("user"));
            response.sendRedirect("/seller/login");
            return false;
        }
        return true;
    }
}
