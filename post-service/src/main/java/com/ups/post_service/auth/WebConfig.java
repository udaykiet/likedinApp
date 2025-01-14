package com.ups.post_service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Autowired
    private UserInterceptor userInterceptor;

   public void addInterceptors(InterceptorRegistry registry){
       registry.addInterceptor(userInterceptor);
   }
}
