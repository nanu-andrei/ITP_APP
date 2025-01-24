package com.server.services;

import com.server.Entity.EmployeeCredentials;
import com.server.Entity.Wallet;
import com.server.Repository.WalletRepository;
import com.server.utils.LoggedEmployee;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;

@Service
public class AuthenticationService {

    private final WalletService walletService;
    private final WalletRepository walletRepository;

    public AuthenticationService(WalletService walletService, WalletRepository walletRepository) {
        this.walletService = walletService;
        this.walletRepository = walletRepository;
    }

    public Credentials getCredentialsForEmployee(Long employeeId) {
        try {
            Wallet wallet = walletRepository.findByEmployeeCredentialsId(employeeId);
            String privateKey = walletService.decryptPrivateKey(wallet.getEncryptedPrivateKey(), wallet.getEmployeeCredentials().getUsername() + "WP");
            return Credentials.create(privateKey);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load credentials for employee: " + employeeId, e);
        }
    }

    public Credentials getCredentialsForLoggedInEmployee() {
        Long employeeId = LoggedEmployee.getInstance().getEmployeeId();
        return getCredentialsForEmployee(employeeId);
    }
}
