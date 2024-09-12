package com.egov.icops_integrationkerala.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ServiceAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public ServiceAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String serviceName = request.getParameter("serviceName");
        String serviceKey = request.getParameter("serviceKey");

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(serviceName, serviceKey);
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write("Authentication Failed: " + failed.getMessage());
    }
}
