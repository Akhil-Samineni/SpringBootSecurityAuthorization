package com.aws.controller;

import com.aws.login.HeaderLoginFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Collections;
import java.util.Enumeration;

@SpringBootApplication
@EnableSwagger2
@ComponentScan("com.aws")
@EnableJpaRepositories("com.aws")
@EntityScan("com.aws.model")
public class MyApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(MyApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Bean
    public Docket swaggerConfiguration() {
        return new Docket(DocumentationType.SWAGGER_2).
                select().apis(RequestHandlerSelectors.basePackage("com.aws"))
                .build().apiInfo(apiDetails());
    }

    public ApiInfo apiDetails() {
        return new ApiInfo("SpringBoot AWS APIs",
                "My API description", "1.0", "terms of service",
                new Contact("Akhil Samineni", "https://github.com/Akhil-Samineni/SpringAWSProject", "akhilsamineni47@gmail.com"),
                "Free to use", "licence url", Collections.emptyList());
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FilterRegistrationBean<HeaderLoginFilter> loggingFilter(){
        FilterRegistrationBean<HeaderLoginFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new HeaderLoginFilter());
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }
}
