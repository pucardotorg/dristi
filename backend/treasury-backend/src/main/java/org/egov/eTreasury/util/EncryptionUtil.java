package org.egov.eTreasury.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.egov.eTreasury.config.ServiceConstants.TRANSFORMATION;

@Component
@Slf4j
public class EncryptionUtil {

    public Map<String, String> getClientSecretAndAppKey(String clientSecret, String publicKeyString) throws NoSuchAlgorithmException, NoSuchPaddingException,InvalidKeySpecException,InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        Map<String, String> secretMap = new HashMap<>();

        // Generate a random AES key (256 bits)
        byte[] appKey = new byte[32];
        SecureRandom random = new SecureRandom();
        random.nextBytes(appKey);
        String appKeyBase64 = java.util.Base64.getEncoder().encodeToString(appKey);
        secretMap.put("appKey",appKeyBase64);

        // Convert the ClientSecret string to bytes (replace "yourClientSecret" with the actual secret)
        byte[] clientSecretBytes = clientSecret.getBytes();

        // Encrypt the ClientSecret using AES
        SecretKeySpec aesKey = new SecretKeySpec(appKey, "AES");
        Cipher aesCipher = Cipher.getInstance(TRANSFORMATION);
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encryptedClientSecretBytes = aesCipher.doFinal(clientSecretBytes);

        // Encrypt the AppKey using RSA with the public key provided by the Treasury
        PublicKey publicKey = getPublicKeyFromString(publicKeyString); // Implement this method to get the public key
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
        rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedAppKeyBytes = rsaCipher.doFinal(appKey);

        // Convert the encrypted bytes to Base64 strings for transmission or storage
        String encryptedClientSecretBase64 = Base64.getEncoder().encodeToString(encryptedClientSecretBytes);
        String encryptedAppKeyBase64 = Base64.getEncoder().encodeToString(encryptedAppKeyBytes);

        secretMap.put("encryptedClientSecret", encryptedClientSecretBase64);
        secretMap.put("encodedAppKey", encryptedAppKeyBase64);
        return secretMap;
    }

    private PublicKey getPublicKeyFromString(String publicKeyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    public String decryptAES(String encryptedData, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public String generateHMAC(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256Hmac.init(secretKey);
        byte[] hmacBytes = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return bytesToEncodedString(hmacBytes);
    }

    private String bytesToEncodedString(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return Base64.getEncoder().encodeToString(result.toString().getBytes());
    }

    public String decryptResponse(String encryptedData, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}

