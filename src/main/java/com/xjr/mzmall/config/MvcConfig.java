package com.xjr.mzmall.config;

import com.xjr.mzmall.entity.User;
import com.xjr.mzmall.interceptor.AdminInterceptor;
import com.xjr.mzmall.interceptor.SellerInterceptor;
import com.xjr.mzmall.interceptor.UserInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/staticRes/**")
                .addResourceLocations("classpath:/static/");

        //商品封面图片
        registry.addResourceHandler("/mzmall/img/cover/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") +
                        System.getProperty("file.separator") + "img" +
                        System.getProperty("file.separator") + "goodscover"+
                        System.getProperty("file.separator"));

        //商品详情图片
        registry.addResourceHandler("/mzmall/img/showimg/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") +
                        System.getProperty("file.separator") + "img" +
                        System.getProperty("file.separator") + "goodsdetailimg"+
                        System.getProperty("file.separator"));

        //用户头像
        registry.addResourceHandler("/mzmall/img/avatar/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") +
                        System.getProperty("file.separator") + "img" +
                        System.getProperty("file.separator") + "avatar"+
                        System.getProperty("file.separator"));
    }

    /**
     * 这里同一台设备登录多个账号可能会有bug 建议 演示完一个账号退出再演示另一个账号
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserInterceptor())
                .addPathPatterns("/mzmall/**")
                .excludePathPatterns("/mzmall")
                .excludePathPatterns("/mzmall/reg.html")
                .excludePathPatterns("/mzmall/login.html")
                .excludePathPatterns("/mzmall/login")
                .excludePathPatterns("/mzmall/index.html")
                .excludePathPatterns("/mzmall/index")
                .excludePathPatterns("/mzmall/user/login")
                .excludePathPatterns("/mzmall/user/reg")
                .excludePathPatterns("/mzmall/img/**")
                .excludePathPatterns("/staticRes/**");
        registry.addInterceptor(new SellerInterceptor())
                .addPathPatterns("/seller/**")
                .excludePathPatterns("/seller")
                .excludePathPatterns("/seller/reg")
                .excludePathPatterns("/seller/login")
                .excludePathPatterns("/seller/reg.html")
                .excludePathPatterns("/mzmall/img/**")
                .excludePathPatterns("/staticRes/**");
        registry.addInterceptor(new AdminInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin")
                .excludePathPatterns("/admin/login")
                .excludePathPatterns("/mzmall/img/**")
                .excludePathPatterns("/staticRes/**");
    }
}
