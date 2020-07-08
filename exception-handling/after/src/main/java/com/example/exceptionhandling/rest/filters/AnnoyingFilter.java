package com.example.exceptionhandling.rest.filters;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A demo filter that will result in exceptions if we pass a the header "DumbHeader" when we call our REST api.
 * Of course only for demo purposes :)
 */
@Component
@Order(100)
public class AnnoyingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if(request.getHeader("DumbHeader") != null) {
            throw new ServletException("something hit the fan");
        }

        super.doFilter(request, response, filterChain);
    }
}
