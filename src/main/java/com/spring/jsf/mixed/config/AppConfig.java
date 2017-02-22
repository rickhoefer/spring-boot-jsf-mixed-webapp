package com.spring.jsf.mixed.config;

import com.spring.jsf.mixed.security.LocalPermissionEvaluator;
import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.annotation.Resource;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.spring.jsf.mixed", "com.spring.jsf.mixed.config",//
        "com.spring.jsf.mixed.model", "com.spring.jsf.mixed.repository", "com.spring.jsf.mixed.service",//
        "com.spring.jsf.mixed.security", "com.spring.jsf.mixed.ui.spring"})

@PropertySource({"classpath:config/application.properties", "classpath:config/datasource/${spring.profiles.active}-datasource.properties"})

@EnableAutoConfiguration(exclude = JpaRepositoriesAutoConfiguration.class)
public class AppConfig {
    static Logger logger = Logger.getLogger(AppConfig.class);
    @Resource
    private Environment env;

    @Bean
    @Order(value = 1)
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    @ExceptionHandler
    public DefaultMethodSecurityExpressionHandler expressionHandlerBean(LocalPermissionEvaluator localPermissionEvaluator) throws Exception {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(localPermissionEvaluator);
        return expressionHandler;
    }


}
