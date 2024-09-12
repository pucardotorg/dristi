package com.egov.icops_integrationkerala.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String serviceName = authentication.getName();
        String serviceKey = authentication.getCredentials().toString();
        if (isValidCredentials(serviceName, serviceKey)) {
            UserDetails userDetails = new User(serviceName, serviceKey, new ArrayList<>());
            return new UsernamePasswordAuthenticationToken(userDetails, serviceKey, userDetails.getAuthorities());
        } else {
            throw new AuthenticationException("Authentication Failed") {};
        }
    }

    private boolean isValidCredentials(String serviceName, String serviceKey) {
        return "serviceName".equalsIgnoreCase(serviceName) && "serviceKey".equalsIgnoreCase(serviceKey);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
