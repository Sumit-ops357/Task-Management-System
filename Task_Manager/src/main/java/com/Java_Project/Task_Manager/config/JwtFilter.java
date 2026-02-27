package com.Java_Project.Task_Manager.config;

import com.Java_Project.Task_Manager.util.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends GenericFilter{

    private final JwtUtil jwtUtil;
    
    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    //Overriding means Child class is inheriting the properties from the Parent class

    //Child class can also inherit the methods of parent class like here ....GenericFilter(Parent class) has
    //doFilter method so child class can use this method

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authHeader = httpRequest.getHeader("Authorization");

        if(authHeader != null && authHeader.startsWith("Bearer"))
        {
            String token = authHeader.substring(7);

            if(jwtUtil.validateToken(token))
            {
                String username = jwtUtil.extractUsername(token);

                //Creating authentication object
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                          username,
                          null,
                          Collections.emptyList()
                        );

                //This tells Spring Security that the user is authenticated
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        //Very Important Step:-
        //It passes the request to the next filter in the filter chain irrespective of whether it is authenticated or not
        chain.doFilter(request, response);
    }
}
