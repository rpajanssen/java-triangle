package com.example.exceptionhandling.configuration;

import com.example.exceptionhandling.domain.api.ErrorResponse;
import com.example.exceptionhandling.exceptions.ErrorCodes;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/person", "/api/person/*", "/api/person**").permitAll()
                .antMatchers("/api/admin","/api/admin/demo").hasAnyRole("ROLE_ADMIN")
                .and().cors().and().csrf().disable();

        http
                .exceptionHandling()
                .authenticationEntryPoint((request, response, e) ->
                {
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(ErrorCodes.ACCESS_DENIED.getHttpStatus().value());
                    objectMapper.writeValue(response.getWriter(), new ErrorResponse<String>(ErrorCodes.ACCESS_DENIED.getCode()));
                });
    }
}
