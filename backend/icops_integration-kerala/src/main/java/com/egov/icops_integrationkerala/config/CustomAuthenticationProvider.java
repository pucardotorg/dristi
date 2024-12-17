package com.egov.icops_integrationkerala.config;

import com.egov.icops_integrationkerala.model.ServiceCredential;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${service.name}")
    private String serviceName;

    @Value("${service.ky}")
    private String serviceKey;

    @Value("${auth.type}")
    private String authType;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Object serviceCredentials = authentication.getCredentials();
        String authType = authentication.getName();
        if(!(serviceCredentials instanceof ServiceCredential credentials)) {
            throw new AuthenticationException("Invalid Authentication Credentials") {};
        }
        String serviceName = credentials.getServiceName();
        String serviceKey = credentials.getServiceKey();
        if(isValidCredentials(serviceName, serviceKey, authType)) {
            UserDetails userDetails = new User(serviceName, serviceKey, new ArrayList<>());
            return new UsernamePasswordAuthenticationToken(userDetails, serviceKey, userDetails.getAuthorities());
        } else {
            throw new AuthenticationException("Authentication Failed") {};
        }
    }

    private boolean isValidCredentials(String serviceName, String serviceKey, String authType) {
        return this.serviceName.equalsIgnoreCase(serviceName) && this.serviceKey.equalsIgnoreCase(serviceKey) && this.authType.equalsIgnoreCase(authType);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
