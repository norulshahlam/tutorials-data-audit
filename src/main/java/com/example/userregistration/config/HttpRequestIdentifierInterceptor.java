package com.example.userregistration.config;

import lombok.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Configuration
public class HttpRequestIdentifierInterceptor implements HandlerInterceptor, WebMvcConfigurer {
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler) throws Exception {
        request.setAttribute("requestId", UUID.randomUUID().toString());
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HttpRequestIdentifierInterceptor());
    }

}
