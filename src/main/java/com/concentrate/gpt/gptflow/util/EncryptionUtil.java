package com.concentrate.gpt.gptflow.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.concentrate.gpt.gptflow.logger.DebugAbleLogger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncryptionUtil {

    private static Logger logger = new DebugAbleLogger(LoggerFactory.getLogger(EncryptionUtil.class));

    public static final String SECRET_KEY = "concentrate"; // 密钥，需要与前端保持一致

    public static String encrypt(String plainText) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            return Base64.encodeBase64String(encryptedBytes);
        } catch (Exception e) {
            logger.error(plainText,"加密失败！",e);
        }
        return null;
    }

    public static String decrypt(String encryptedText) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.decodeBase64(encryptedText));
            return new String(decryptedBytes);
        } catch (Exception e) {
            logger.error(encryptedText,"解密失败！",e);
        }
        return null;
    }

}