package com.niit.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@Configuration
@EnableWebMvc //similar to <mvc:annotation-driven> in dispatcher-servlet.xml
@ComponentScan(basePackages="com.niit")//similar to <context:component-scan> in dispatcher-servlet.xml 
public class WebAppConfig extends WebMvcConfigurerAdapter {

	public WebAppConfig(){
		System.out.println("WebAppConfig class is loaded and instantiated");
	}
}
