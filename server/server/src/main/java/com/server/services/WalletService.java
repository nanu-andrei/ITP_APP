package com.server.services;

import com.server.Entity.EmployeeCredentials;


public interface WalletService {
    public void createWallet(String password,EmployeeCredentials employeeCredentials) throws Exception;
    String encryptPrivateKey(String privateKey, String password) throws Exception;

    String decryptPrivateKey(String privateKey, String encryptionPassword) throws  Exception;

}
