package org.drishti.esign.cipher;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;

import static org.drishti.esign.config.ServiceConstants.*;

@Component
@Slf4j
public class Decryption {

    private final ResourceLoader resourceLoader;

    @Autowired
    public Decryption(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * New function to get public key from .cer file
     *
     * @param filename
     * @return
     * @throws IOException
     */
    private PublicKey getPublicKeyNew(String filename) throws IOException {
        InputStream inStream = null;
        inStream = new FileInputStream(filename);
        try {
            CertificateFactory cf = CertificateFactory.getInstance(X_509);
            X509Certificate cert = (X509Certificate) cf.generateCertificate(inStream);
            return cert.getPublicKey();
        } catch (Exception e) {

            return null;
        } finally {
            inStream.close();
        }
    }

    private String getKey(String filename) throws IOException {
        // Read key from file
        StringBuilder strKeyPEM = new StringBuilder();
        Resource resource = resourceLoader.getResource("classpath:"+filename);
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                strKeyPEM.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return strKeyPEM.toString();
    }

    /**
     * Constructs a private key (RSA) from the given file
     *
     * @param filename PEM Private Key
     * @return RSA Private Key
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public PrivateKey getPrivateKey(String filename) throws IOException, GeneralSecurityException {
        String privateKeyPEM = getKey(filename);
        return getPrivateKeyFromString(privateKeyPEM);
    }

    /**
     * Constructs a private key (RSA) from the given string
     *
     * @param key PEM Private Key
     * @return RSA Private Key
     * @throws GeneralSecurityException
     */
    public PrivateKey getPrivateKeyFromString(String key) throws GeneralSecurityException {
        String privateKeyPEM = key;

        privateKeyPEM = privateKeyPEM.substring(31, privateKeyPEM.indexOf("\n-----END RSA PRIVATE KEY-----"));

        privateKeyPEM = privateKeyPEM.trim();
        java.security.Security.addProvider(
                new org.bouncycastle.jce.provider.BouncyCastleProvider()
        );
        // Base64 decode data
        byte[] encoded = Base64.decodeBase64(privateKeyPEM);
        PKCS8EncodedKeySpec spec =
                new PKCS8EncodedKeySpec(encoded);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        return kf.generatePrivate(spec);
    }

    /**
     * Constructs a public key (RSA) from the given file
     *
     * @param filename PEM Public Key
     * @return RSA Public Key
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public PublicKey getPublicKey(String filename) throws IOException, GeneralSecurityException {
        String publicKeyPEM = getKey(filename);
        return getPublicKeyFromString(publicKeyPEM);
    }

    /**
     * Constructs a public key (RSA) from the given string
     *
     * @param key PEM Public Key
     * @return RSA Public Key
     * @throws GeneralSecurityException
     */

    public PublicKey getPublicKeyFromString(String key) throws GeneralSecurityException {
        key = key.replace("-----BEGIN CERTIFICATE-----", "")
                .replace("-----END CERTIFICATE-----", "")
                .replaceAll("\\s", "");

        byte[] certificateData = Base64.decodeBase64(key);

        CertificateFactory certificateFactory = CertificateFactory.getInstance(X_509);
        X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(
                new ByteArrayInputStream(certificateData)
        );
        return certificate.getPublicKey();
    }
    String charSetName = UTF_8;
    /**
     * @param privateKey
     * @param message
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     * @throws UnsupportedEncodingException
     */
    public String sign(PrivateKey privateKey, String message) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
        Signature sign = Signature.getInstance(SHA1_WITH_RSA);
        sign.initSign(privateKey);
        sign.update(message.getBytes(charSetName));
        return new String(Base64.encodeBase64(sign.sign()), charSetName);
    }

    /**
     * @param publicKey
     * @param message
     * @param signature
     * @return
     * @throws SignatureException
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     */
    public boolean verify(PublicKey publicKey, String message, String signature) throws SignatureException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        Signature sign = Signature.getInstance(SHA1_WITH_RSA);
        sign.initVerify(publicKey);
        sign.update(message.getBytes(charSetName));
        return sign.verify(Base64.decodeBase64(signature.getBytes(charSetName)));
    }

    /**
     * Encrypts the text with the public key (RSA)
     *
     * @param rawText   Text to be encrypted
     * @param publicKey
     * @return
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public String encrypt(String rawText, PrivateKey privateKey) throws IOException, GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return Base64.encodeBase64String(cipher.doFinal(rawText.getBytes(charSetName)));
    }

    /**
     * Decrypts the text with the private key (RSA)
     *
     * @param cipherText Text to be decrypted
     * @param privateKey
     * @return Decrypted text (Base64 encoded)
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public String decrypt(String cipherText, PublicKey publicKey) throws IOException, GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return new String(cipher.doFinal(Base64.decodeBase64(cipherText)), charSetName);
    }
}

