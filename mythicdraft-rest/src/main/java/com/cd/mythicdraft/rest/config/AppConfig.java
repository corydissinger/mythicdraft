package com.cd.mythicdraft.rest.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
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

	@Autowired
	private Environment environment;	
	
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
    	
    	cacheMappings.setProperty("/draft/*/deck/**", "31536000");
    	cacheMappings.setProperty("/draft/*/pack/*/pick/**", "31536000");
    	cacheMappings.setProperty("/draft/recent", "1");
    	cacheMappings.setProperty("/draft/player/**", "15");
    	
    	cacheMappings.setProperty("/formats/**", "1");
    	cacheMappings.setProperty("/format/**", "1");    	
    	
    	interceptor.setCacheMappings(cacheMappings);
		return interceptor;
	}    
    
    @Bean
    public ApplicationProperties appProperties() {
    	ApplicationProperties appProperties = new ApplicationProperties();
    	
    	appProperties.setProjectVersion(environment.getRequiredProperty("project.version"));
    	
    	return appProperties;
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
        registry.addResourceHandler("/robots.txt").addResourceLocations("/resources/robots.txt");
    }    
    	
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	registry.addInterceptor(webContentInterceptor());
    }

}
