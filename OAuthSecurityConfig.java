//package com.aws.login;
//
//import com.auth0.spring.security.api.JwtWebSecurityConfigurer;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
//@EnableWebSecurity
//@Configuration
//@EnableGlobalMethodSecurity
//@Order(1)
//public class OAuthSecurityConfig extends WebSecurityConfigurerAdapter {
//    @Value(value = "${auth0.apiAudience}")
//    private String apiAudience;
//    @Value(value = "${auth0.issuer}")
//    private String issuer;
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.cors().disable();
//        http.csrf().disable();
//
//        JwtWebSecurityConfigurer.forRS256(apiAudience,issuer)
//                .configure(http.antMatcher("/oauth/**")).authorizeRequests()
//                .anyRequest().authenticated();
//
//    }
//}
