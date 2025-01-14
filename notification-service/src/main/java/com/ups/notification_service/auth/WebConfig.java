package com.ups.notification_service.auth;

import com.netflix.discovery.converters.Auto;
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
