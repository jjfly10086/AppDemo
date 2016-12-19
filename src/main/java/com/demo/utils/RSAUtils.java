package com.demo.utils;

/**
 * Created by jwh on 2016/12/17.
 * RSA�ǶԳƼ��ܹ�����
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
     * ������Կ��
     */
    public static Map<String,String> generateKeyPair() {
        //������Կ���ַ���map
        Map<String, String> keyMap = new HashMap<>();
        try {
            //1.��ʼ����Կ
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            //��Կ����
            keyPairGenerator.initialize(512);
            //��ʼ����Կ��
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            //��Կ
            PublicKey publicKey = keyPair.getPublic();
            //˽Կ
            PrivateKey privateKey = keyPair.getPrivate();
            //��Կ�ַ���
            String publicKeyStr = new String(Base64.encodeBase64(publicKey.getEncoded()));
            //˽Կ�ַ���
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
     * ��publicKeyStr�ַ���ת��ΪPublicKey����
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
     * ��privateKeyStr�ַ���ת��ΪPrivateKey����
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
     * �ù�Կ�����ļ���
     * @param publicKey ��Կ
     * @param plainText ����
     * @return ���ؼ��ܺ���ַ���
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
     * ��˽Կ�����Ľ���
     * @param privateKey ˽Կ
     * @param enStr ����
     * @return ���ؽ��ܺ���ַ���
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
       System.out.println("�����ַ�����"+enStr);
       String deStr = decrypt(getPrivateKey(map.get("privateKey")),enStr);
       System.out.println("�����ַ�����"+deStr);
    }
}
