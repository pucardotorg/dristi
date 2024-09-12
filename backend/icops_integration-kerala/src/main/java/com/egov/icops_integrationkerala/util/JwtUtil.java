package com.egov.icops_integrationkerala.util;

import com.egov.icops_integrationkerala.model.AuthResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {

    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    private long tokenExpirationTime = 30*60*1000L;

    private final JwtParser jwtParser;

    public JwtUtil(){
        this.jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
    }

    public AuthResponse generateToken(String serviceName) {
        Date tokenCreateTime = new Date();
        Date tokenExpiryTime = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(tokenExpirationTime));
        String accessToken =  Jwts.builder()
                .setSubject(serviceName)
                .setIssuedAt(tokenCreateTime)
                .setExpiration(tokenExpiryTime)
                .signWith(secretKey)
                .compact();
        return AuthResponse.builder()
                .accessToken(accessToken).expiresIn((int) tokenExpirationTime)
                .tokenType("Bearer").scope("").build();
    }

    public String getServiceNameFromToken(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            jwtParser.parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
