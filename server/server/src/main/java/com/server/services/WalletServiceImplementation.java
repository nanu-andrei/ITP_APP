package com.server.services;

import com.server.Entity.EmployeeCredentials;
import com.server.Entity.Wallet;
import com.server.Repository.EmployeeCredentialsRepository;
import com.server.Repository.WalletRepository;
import com.server.utils.CentralContractManager;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.protocol.Web3j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.spec.KeySpec;
import java.util.Base64;

@Service
public class WalletServiceImplementation implements WalletService {

    private static final String ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256;
    private static final String SALT = "IStartToHateThis";
    private final Web3j web3j;
    public final WalletRepository walletRepository;
    public final EmployeeCredentialsRepository employeeCredentialsRepository;
    private final CentralContractManager contractManager;

    private EntityManager entityManager;



    @Autowired
    public WalletServiceImplementation(WalletRepository walletRepository, EmployeeCredentialsRepository employeeCredentialsRepository, EntityManager entityManager, CentralContractManager contractManager, Web3j web3j) {
        this.walletRepository = walletRepository;
        this.employeeCredentialsRepository = employeeCredentialsRepository;
        this.entityManager =entityManager;
        this.web3j = web3j;
        this.contractManager = contractManager;
    }

    @Override
    public void createWallet( String password, EmployeeCredentials credentials) throws Exception {

        if (credentials.getId() == null || !entityManager.contains(credentials)) {
            credentials = entityManager.merge(credentials);
        }
        Wallet wallet = new Wallet();
        wallet.setEmployeeCredentials(credentials);
        credentials.setWallet(wallet);

        setupWallet(wallet, password);
        walletRepository.save(wallet);
        entityManager.refresh(credentials);

    }
    private void setupWallet(Wallet wallet, String password) throws Exception {
        ECKeyPair keyPair = Keys.createEcKeyPair();
        String publicKey = keyPair.getPublicKey().toString(16);
        String address = Keys.getAddress(publicKey);

        contractManager.fundWalletWithGanache(address);
        String privateKey = keyPair.getPrivateKey().toString(16);
        String encryptedPrivateKey = encryptPrivateKey(privateKey, password);

        wallet.setEncryptedPrivateKey(encryptedPrivateKey);
        wallet.setPrivateKey(privateKey);
        wallet.setAddress(address);
    }

    public String encryptPrivateKey(String privateKey, String password) throws Exception {
        byte[] saltBytes = SALT.getBytes();
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, ITERATION_COUNT, KEY_LENGTH);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] iv = cipher.getIV();
        byte[] encryptedBytes = cipher.doFinal(privateKey.getBytes());

        byte[] combined = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);

        return Base64.getEncoder().encodeToString(combined);
    }
    public String decryptPrivateKey(String encryptedPrivateKey, String password) throws Exception {
        byte[] saltBytes = SALT.getBytes();
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, ITERATION_COUNT, KEY_LENGTH);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        byte[] combined = Base64.getDecoder().decode(encryptedPrivateKey);
        byte[] iv = new byte[16];
        byte[] encryptedBytes = new byte[combined.length - 16];

        System.arraycopy(combined, 0, iv, 0, iv.length);
        System.arraycopy(combined, iv.length, encryptedBytes, 0, encryptedBytes.length);

        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes);
    }




}