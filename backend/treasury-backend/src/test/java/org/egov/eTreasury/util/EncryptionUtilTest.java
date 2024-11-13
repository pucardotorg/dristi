package org.egov.eTreasury.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionUtilTest {

    @InjectMocks
    private EncryptionUtil encryptionUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetClientSecretAndAppKey() throws Exception {
        String clientSecret = "testClientSecret";
        String publicKeyString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAu1SU1LfVLPHCozMxH2Mo4lgOEePzNm0tRgeLezV6ffAt0gunVTLw7onLRnrq0/IzW7yWR7QkrmBL7jTKEn5u+qKhbwKfBstIs+bMY2Zkp18gnTxKLxoS2tFczGkPLPgizskuemMghRniWaoLcyehkd3qqGElvW/VDL5AaWTg0nLVkjRo9z+40RQzuVaE8AkAFmxZzow3x+VJYKdjykkJ0iT9wCS0DRTXu269V264Vf/3jvredZiKRkgwlL9xNAwxXFg0x/XFw005UWVRIkdgcKWTjpBP2dPwVZ4WWC+9aGVd+Gyn1o0CLelf4rEjGoXbAAEgAqeGUxrcIlbjXfbcmwIDAQAB";

        Map<String, String> result = encryptionUtil.getClientSecretAndAppKey(clientSecret, publicKeyString);

        assertNotNull(result);
        assertTrue(result.containsKey("appKey"));
        assertTrue(result.containsKey("encryptedClientSecret"));
        assertTrue(result.containsKey("encodedAppKey"));

        assertNotEquals(clientSecret, result.get("encryptedClientSecret"));
        assertNotEquals(result.get("appKey"), result.get("encodedAppKey"));
    }

    @Test
    void testGetPublicKeyFromString() throws Exception {
        String publicKeyString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAu1SU1LfVLPHCozMxH2Mo4lgOEePzNm0tRgeLezV6ffAt0gunVTLw7onLRnrq0/IzW7yWR7QkrmBL7jTKEn5u+qKhbwKfBstIs+bMY2Zkp18gnTxKLxoS2tFczGkPLPgizskuemMghRniWaoLcyehkd3qqGElvW/VDL5AaWTg0nLVkjRo9z+40RQzuVaE8AkAFmxZzow3x+VJYKdjykkJ0iT9wCS0DRTXu269V264Vf/3jvredZiKRkgwlL9xNAwxXFg0x/XFw005UWVRIkdgcKWTjpBP2dPwVZ4WWC+9aGVd+Gyn1o0CLelf4rEjGoXbAAEgAqeGUxrcIlbjXfbcmwIDAQAB";

        Method method = EncryptionUtil.class.getDeclaredMethod("getPublicKeyFromString", String.class);
        method.setAccessible(true);

        assertDoesNotThrow(() -> method.invoke(encryptionUtil, publicKeyString));
    }

    @Test
    void testGenerateHMAC() throws Exception {
        String data = "Test data for HMAC";
        String key = "testKey";

        String hmac = encryptionUtil.generateHMAC(data, key);

        assertNotNull(hmac);
        assertNotEquals(data, hmac);
    }

    @Test
    void testDecryptResponse() throws Exception {
        String originalData = "Test data for encryption";
        String key = "1234567890123456"; // 16 bytes for AES-128

        // Encrypt the data first
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        String encryptedData = Base64.getEncoder().encodeToString(cipher.doFinal(originalData.getBytes()));

        // Now decrypt
        String decryptedData = encryptionUtil.decryptResponse(encryptedData, key);

        assertEquals(originalData, decryptedData);
    }

    @Test
    void testDecryptAES() throws Exception {
        String originalData = "Test data for encryption";
        String key = Base64.getEncoder().encodeToString("1234567890123456".getBytes()); // 16 bytes for AES-128

        // Encrypt the data first
        SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        String encryptedData = Base64.getEncoder().encodeToString(cipher.doFinal(originalData.getBytes()));

        // Now decrypt
        String decryptedData = encryptionUtil.decryptAES(encryptedData, key);

        assertEquals(originalData, decryptedData);
    }
}