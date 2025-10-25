package com.pi.pi_cloud.Security;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import org.apache.commons.codec.binary.Base32;

public class TOTPUtil {

    private static final Base32 base32 = new Base32();

    public static String generateSecret() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA1");
        keyGenerator.init(160);
        SecretKey secretKey = keyGenerator.generateKey();
        return base32.encodeToString(secretKey.getEncoded()); // 🔹 ahora en Base32
    }

    public static boolean verifyCode(String secret, String code) throws Exception {
        TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator();
        byte[] decodedKey = new Base32().decode(secret);
        Key key = new javax.crypto.spec.SecretKeySpec(decodedKey, "HmacSHA1");
        int generated = totp.generateOneTimePassword(key, Instant.now());
        return String.format("%06d", generated).equals(code);
    }
}
