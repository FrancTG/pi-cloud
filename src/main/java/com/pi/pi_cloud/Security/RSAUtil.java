package com.pi.pi_cloud.Security;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAUtil {

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }

    public static String encodeKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static PrivateKey decodePrivateKey(String encodedKey) throws Exception {
        byte[] encodedKeyBytes = Base64.getDecoder().decode(encodedKey);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedKeyBytes);

        KeyFactory keyGen = KeyFactory.getInstance("RSA");
        return keyGen.generatePrivate(keySpec);
    }

    public static PublicKey decodePublicKey(String encodedKey) throws Exception {

        byte[] encodedKeyBytes = Base64.getDecoder().decode(encodedKey);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKeyBytes);

        KeyFactory keyGen = KeyFactory.getInstance("RSA");
        return keyGen.generatePublic(keySpec);
    }
}