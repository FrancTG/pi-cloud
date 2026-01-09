package com.pi.pi_cloud.lib;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.KeySpec;
import java.util.Arrays;

public class Cifrado {

    public static SecretKey generarClaveAes() {
        try {
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(256);
            return kg.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static AesResult cifrarArchivoConAes(byte[] plain, SecretKey key) throws GeneralSecurityException {
        byte[] iv = new byte[12];
        SecureRandom rnd = SecureRandom.getInstanceStrong();
        rnd.nextBytes(iv);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        byte[] ciphertext = cipher.doFinal(plain);
        return new AesResult(iv, ciphertext);
    }

    public static byte[] descifrarArchivoConAes(byte[] blob, SecretKey key) throws GeneralSecurityException {
        byte[] iv = Arrays.copyOfRange(blob, 0, 12);
        byte[] ciphertext = Arrays.copyOfRange(blob, 12, blob.length);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        return cipher.doFinal(ciphertext);
    }

    public static byte[] cifrarClaveConRSA(byte[] clave, Key key) throws GeneralSecurityException{
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(clave);
    }

    public static byte[] descifrarClaveConRSA(byte[] clave, Key key) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(clave);
    }

    public static SecretKey derivarClave(char[] password, byte[] salt) throws GeneralSecurityException{
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        KeySpec spec = new PBEKeySpec(password, salt, 100_000, 256);
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();

        return new SecretKeySpec(keyBytes, "AES");
    }
}
