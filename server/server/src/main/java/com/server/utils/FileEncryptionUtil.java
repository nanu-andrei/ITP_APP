package com.server.utils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.spec.KeySpec;
import java.util.Base64;

public class FileEncryptionUtil {

    private static final String SALT = "salt";
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256;

    public static void encryptFile(String password, String inputFilePath, String outputFilePath) throws Exception {
        byte[] saltBytes = SALT.getBytes(StandardCharsets.UTF_8);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, ITERATION_COUNT, KEY_LENGTH);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] iv = cipher.getIV();

        try (FileInputStream fileInputStream = new FileInputStream(new File(inputFilePath));
             FileOutputStream fileOutputStream = new FileOutputStream(new File(outputFilePath))) {

            fileOutputStream.write(iv);

            byte[] buffer = new byte[64];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                byte[] encryptedBytes = cipher.update(buffer, 0, bytesRead);
                if (encryptedBytes != null) {
                    fileOutputStream.write(encryptedBytes);
                }
            }

            byte[] encryptedBytes = cipher.doFinal();
            if (encryptedBytes != null) {
                fileOutputStream.write(encryptedBytes);
            }
        }
    }

    public static void decryptFile(String password, String inputFilePath, String outputFilePath) throws Exception {
        byte[] saltBytes = SALT.getBytes(StandardCharsets.UTF_8);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, ITERATION_COUNT, KEY_LENGTH);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

        try (FileInputStream fileInputStream = new FileInputStream(new File(inputFilePath));
             FileOutputStream fileOutputStream = new FileOutputStream(new File(outputFilePath))) {

            byte[] iv = new byte[16];
            fileInputStream.read(iv);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

            byte[] buffer = new byte[64];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                byte[] decryptedBytes = cipher.update(buffer, 0, bytesRead);
                if (decryptedBytes != null) {
                    fileOutputStream.write(decryptedBytes);
                }
            }

            byte[] decryptedBytes = cipher.doFinal();
            if (decryptedBytes != null) {
                fileOutputStream.write(decryptedBytes);
            }
        }
    }
}
