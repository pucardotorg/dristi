package com.egov.icops_integrationkerala.util;

import com.egov.icops_integrationkerala.model.AuthResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    @Test
    void testGenerateToken() {
        // Arrange
        String serviceName = "serviceName";

        // Act
        AuthResponse result = jwtUtil.generateToken(serviceName);

        // Assert
        assertNotNull(result);
    }

    @Test
    void generateToken_ValidServiceName_ReturnsAuthResponse() {
        String serviceName = "validService";
        AuthResponse result = jwtUtil.generateToken(serviceName);
        assertNotNull(result);
        assertNotNull(result.getAccessToken());
        assertEquals("Bearer", result.getTokenType());
    }

    @Test
    void getServiceNameFromToken_ValidToken_ReturnsServiceName() {
        String serviceName = "validService";
        AuthResponse authResponse = jwtUtil.generateToken(serviceName);
        String token = authResponse.getAccessToken();
        String result = jwtUtil.getServiceNameFromToken(token);
        assertEquals(serviceName, result);
    }

    @Test
    void validateToken_ValidToken_ReturnsTrue() {
        String serviceName = "validService";
        AuthResponse authResponse = jwtUtil.generateToken(serviceName);
        String token = authResponse.getAccessToken();
        boolean isValid = jwtUtil.validateToken(token);
        assertTrue(isValid);
    }

    @Test
    void validateToken_InvalidToken_ReturnsFalse() {
        String invalidToken = "invalidToken";
        boolean isValid = jwtUtil.validateToken(invalidToken);
        assertFalse(isValid);
    }
}
