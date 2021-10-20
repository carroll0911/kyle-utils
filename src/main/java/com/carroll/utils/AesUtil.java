package com.carroll.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;

/**
 * @author carroll
 * @Date 2017-07-25 18:06
 **/
public class AesUtil {
    private static Logger logger = LoggerFactory.getLogger(AesUtil.class);

    private static final String AES = "AES";
    private static final String CIPHER_MODE = "AES/CBC/PKCS5Padding";
    private static String key;
    private static String initializationVector;
    private static final BASE64Encoder ENCODER = new BASE64Encoder();
    private static final BASE64Decoder DECODER = new BASE64Decoder();

    public static void main(String[] args) {
        String original ="hello";
        key = "aaaaaaaa";
        initializationVector = "abcdefghijklmnop";

        try {
            System.out.println("加密前："+ original);
            String encrypted = encrypt(original, key, null);
            System.out.println("加密后："+ encrypted);
            String decrypted = decrypt(encrypted, key, null);
            System.out.println("解密后："+ decrypted);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 加密
     * @param content
     * @param secretKey
     * @param vector
     * @return
     * @throws Exception
     */
    public static String encrypt(String content, String secretKey, String vector) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
        keyGenerator.init(128, new SecureRandom(secretKey.getBytes("UTF-8")));
        SecretKey key = keyGenerator.generateKey();
        Cipher cipher = Cipher.getInstance(CIPHER_MODE);
        if (vector == null) {
            cipher.init(Cipher.ENCRYPT_MODE, key);
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(vector.getBytes("UTF-8")));
        }

//        return new String(cipher.doFinal(new BASE64Decoder().decodeBuffer(content)),"UTF-8");
        return ENCODER.encode(cipher.doFinal(content.getBytes("UTF-8")));
    }

    /**
     * 解密
     * @param content
     * @param secretKey
     * @param vector
     * @return
     * @throws Exception
     */
    public static String decrypt(String content, String secretKey, String vector) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
        keyGenerator.init(128, new SecureRandom(secretKey.getBytes("UTF-8")));
        SecretKey key = keyGenerator.generateKey();
        Cipher cipher = Cipher.getInstance(CIPHER_MODE);
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(vector.getBytes("UTF-8")));
        return new String(cipher.doFinal(DECODER.decodeBuffer(content)));
    }
}
