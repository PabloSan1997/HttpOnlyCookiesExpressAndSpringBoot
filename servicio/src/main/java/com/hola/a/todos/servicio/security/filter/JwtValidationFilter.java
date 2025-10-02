package com.hola.a.todos.servicio.security.filter;


import com.hola.a.todos.servicio.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Arrays;

public class JwtValidationFilter extends BasicAuthenticationFilter {

    private final JwtService jwtService;

    public JwtValidationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String token = generateToken(request);
        if(token == null){
            chain.doFilter(request, response);
            return;
        }

        try{
            var res = jwtService.validate(token);
            var auth = new UsernamePasswordAuthenticationToken(
                    res.getUsername(), null, res.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
        }catch (Exception ignore){}

        chain.doFilter(request, response);
    }

    private String generateToken(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")){
            return header.replace("Bearer ", "");
        }
        Cookie[] cookie = request.getCookies();
        if(request.getCookies()!=null){
            for(Cookie cook:cookie){
                if("mitoken".equals(cook.getName()))
                    return cook.getValue();
            }
        }
        return null;
    }
}
