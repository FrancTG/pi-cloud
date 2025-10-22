package com.pi.pi_cloud.lib;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class Cifrado {

    // La llave se debe guardar en un servidor de claves
    public static final SecretKey CLAVE_SERVIDOR = generarClaveAes();

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

    public static byte[] decryptAesGcm(byte[] blob, SecretKey key) throws GeneralSecurityException {
        byte[] iv = Arrays.copyOfRange(blob, 0, 12);
        byte[] ciphertext = Arrays.copyOfRange(blob, 12, blob.length);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        return cipher.doFinal(ciphertext);
    }
}
