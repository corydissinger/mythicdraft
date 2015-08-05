package com.cd.mythicdraft.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.WebContentInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.cd.mythicdraft")
public class AppConfig extends WebMvcConfigurerAdapter {

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }
    
    @Bean
    public CommonsMultipartResolver multipartResolver() {
    	return new CommonsMultipartResolver();
    }

    @Bean
	public WebContentInterceptor webContentInterceptor() {
    	WebContentInterceptor interceptor = new WebContentInterceptor();
    	
    	Properties cacheMappings = new Properties();
    	
    	cacheMappings.setProperty("/draft/*/pack/*/pick/**", "31536000");
    	cacheMappings.setProperty("/draft/recent", "1");
    	cacheMappings.setProperty("/draft/player/**", "15");    	
    	
    	interceptor.setCacheMappings(cacheMappings);
		return interceptor;
	}    
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }    
    	
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	registry.addInterceptor(webContentInterceptor());
    }

}
