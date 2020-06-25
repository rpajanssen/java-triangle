package com.example.exceptionhandling.configuration;

import org.json.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

// NOTE : example 13 - Handle access denied exceptions
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/admin/*").hasAnyRole("ROLE_ADMIN");

        http
                .exceptionHandling()
                .authenticationEntryPoint((request, response, e) ->
                {
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write(new JSONObject()
                            .put("timestamp", LocalDateTime.now())
                            .put("message", "Access denied")
                            .toString());
                });
    }
}
