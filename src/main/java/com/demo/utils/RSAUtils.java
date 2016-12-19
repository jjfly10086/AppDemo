package com.demo.utils;

/**
 * Created by jwh on 2016/12/17.
 * RSA非对称加密工具类
 */



import java.math.BigInteger;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;

public class RSAUtils {

    private static Cipher cipher;

    static{
        try {
            cipher = Cipher.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }
    /**
     * 生成密钥对
     */
    public static Map<String,String> generateKeyPair() {
        //返回密钥对字符串map
        Map<String, String> keyMap = new HashMap<>();
        try {
            //1.初始化秘钥
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            //秘钥长度
            keyPairGenerator.initialize(512);
            //初始化秘钥对
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            //公钥
            PublicKey publicKey = keyPair.getPublic();
            //私钥
            PrivateKey privateKey = keyPair.getPrivate();
            //公钥字符串
            String publicKeyStr = new String(Base64.encodeBase64(publicKey.getEncoded()));
            //私钥字符串
            String privateKeyStr = new String(Base64.encodeBase64(privateKey.getEncoded()));
            keyMap.put("publicKey", publicKeyStr);
            keyMap.put("privateKey", privateKeyStr);
            return keyMap;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 由publicKeyStr字符串转换为PublicKey对象
     * @param key
     * @return
     */
    public static PublicKey getPublicKey(String key) throws Exception{
        byte[] keyBytes = Base64.decodeBase64(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 由privateKeyStr字符串转换为PrivateKey对象
     * @param key
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * 用公钥对明文加密
     * @param publicKey 公钥
     * @param plainText 明文
     * @return 返回加密后的字符串
     */
    public static String encrypt(PublicKey publicKey,String plainText){

        try {
            cipher.init(Cipher.ENCRYPT_MODE,publicKey);
            byte[] enBytes = cipher.doFinal(plainText.getBytes());
            return new String(Base64.encodeBase64(enBytes));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return  null;
    }

    /**
     * 用私钥对密文解密
     * @param privateKey 私钥
     * @param enStr 密文
     * @return 返回解密后的字符串
     */
    public static String decrypt(PrivateKey privateKey,String enStr){
        try {
            cipher.init(Cipher.DECRYPT_MODE,privateKey);
            byte[] deBytes = cipher.doFinal(Base64.decodeBase64(enStr));
            return new String(deBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args) throws Exception{
       Map<String,String> map = generateKeyPair();
       String enStr = encrypt(getPublicKey(map.get("publicKey")),"1234444");
       System.out.println("加密字符串："+enStr);
       String deStr = decrypt(getPrivateKey(map.get("privateKey")),enStr);
       System.out.println("解密字符串："+deStr);
    }
}
