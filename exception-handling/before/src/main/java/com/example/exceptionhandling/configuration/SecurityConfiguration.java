package com.example.exceptionhandling.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// NOTE : example 13 - Handle access denied to resources exceptions
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/person", "/api/person/*", "/api/person**").permitAll()
                .antMatchers("/api/admin","/api/admin/demo").hasAnyRole("ROLE_ADMIN")
            .and().cors().and().csrf().disable();
    }
}
