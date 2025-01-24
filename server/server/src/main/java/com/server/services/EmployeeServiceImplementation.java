package com.server.services;

import com.server.DTO.EmployeeCredentialsDTO;
import com.server.DTO.FirmDTO;


import com.server.Entity.EmployeeCredentials;
import com.server.Entity.Firm;
import com.server.Repository.EmployeeCredentialsRepository;
import com.server.Repository.FirmRepository;
import com.server.Repository.WalletRepository;
import com.server.utils.LoggedEmployee;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import org.web3j.protocol.Web3j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Service
@Primary
public class EmployeeServiceImplementation implements EmployeeService{

    public final FirmRepository firmRepository;
    public final WalletRepository walletRepository;
    public WalletService walletService;
    public AdminService adminService;
    public Web3j web3j;
    public EntityManager entityManager;
    public final EmployeeCredentialsRepository credentialsRepository;
    public final ContractServiceImplementation contractService;

    @Autowired
    public EmployeeServiceImplementation(EmployeeCredentialsRepository credentialsRepository, FirmRepository firmRepository, WalletRepository walletRepository, WalletService walletService, AdminService adminService, EntityManager entityManager, Web3j web3j, ContractServiceImplementation contractService)
    {
        this.credentialsRepository = credentialsRepository;
        this.firmRepository = firmRepository;
        this.walletRepository = walletRepository;
        this.walletService = walletService;
        this.adminService = adminService;
        this.entityManager = entityManager;
        this.web3j=web3j;
        this.contractService = contractService;
    }

    @Override
    public EmployeeCredentialsDTO getEmployeeByUsername(String username)
    {
        EmployeeCredentials credentials = credentialsRepository.findByUsername(username);
        return convertToDTO(credentials);
    }
    @Override
    public void updateEmployee(Long employeeId, EmployeeCredentialsDTO employeeCredentialsDTO)
    {

    }
    @Transactional
    @Override
    public FirmDTO registerFirm(String requirement,  String firmName, List<EmployeeCredentialsDTO> employees) throws Exception {
        if (!isValidCIF(requirement)) {
            throw new IllegalArgumentException("Invalid CIF provided: " + requirement);
        }

        Firm firm = new Firm();

        firm.setFirmName(firmName);
        firm.setEmployeeCredentials(new ArrayList<>());
        firmRepository.save(firm);

        EmployeeCredentials admin = null;
        List<EmployeeCredentialsDTO> employeeData = new ArrayList<>();

        for (Iterator<EmployeeCredentialsDTO> iterator = employees.iterator(); iterator.hasNext();) {
            EmployeeCredentialsDTO emp = iterator.next();
            if (emp.getRole() == EmployeeCredentials.Role.ADMIN && admin == null) {
                emp.setFirm(firm);
                admin = convertToEntity(emp);
                admin= credentialsRepository.save(admin);
                walletService.createWallet(emp.getUsername() + "WP", admin);
                firm.setAdmin(admin);

            } else {
                employeeData.add(emp);
            }
        }

        if (admin == null) {
            throw new IllegalArgumentException("No admin found in provided employee list");
        }

        if (!employeeData.isEmpty()) {
            adminService.addInspectorToFirm(firmName, employeeData);
        }

        firm =firmRepository.saveAndFlush(firm);
        entityManager.refresh(firm);
        contractService.deployContract("company",firmName);

        return convertToDTO(firm);
    }
    @Transactional
    public List<EmployeeCredentials> listAllCredentials() {
        return credentialsRepository.findAll();
    }

    public FirmDTO convertToDTO(Firm firm)
    {
        FirmDTO firmDTO = new FirmDTO();
        firmDTO.setId(firm.getId());
        firmDTO.setFirmName(firmDTO.getFirmName());
        firmDTO.setPrimaryAdminId(firm.getAdmin().getId());
        firmDTO.setEmployeeCredentialsList(firm.getEmployeeCredentials());
        entityManager.refresh(firm);
        return firmDTO;
    }
    public EmployeeCredentialsDTO convertToDTO(EmployeeCredentials credentials)
    {
        EmployeeCredentialsDTO credentialsDTO = new EmployeeCredentialsDTO();
        credentialsDTO.setRole(credentials.getRole());
        credentialsDTO.setFirm(credentials.getFirm());
        credentialsDTO.setWallet(credentials.getWallet());
        credentialsDTO.setUsername(credentials.getUsername());
        entityManager.refresh(credentials);
        return credentialsDTO;
    }
    public EmployeeCredentials convertToEntity(EmployeeCredentialsDTO credentialsDTO)
    {
        EmployeeCredentials credentials = new EmployeeCredentials();
        credentials.setRole(credentialsDTO.getRole());
        credentials.setFirm(credentialsDTO.getFirm());
        credentials.setUsername(credentialsDTO.getUsername());
        credentials.setWallet(credentialsDTO.getWallet());
        credentials.setId(credentials.getId());
        return credentials;
    }

    @Override
    @Transactional
    public EmployeeCredentialsDTO login(String username, String privateKey) {
        try {
            EmployeeCredentials emp = credentialsRepository.findByUsername(username);

            if (emp == null) {
                throw new IllegalArgumentException("Invalid Login Credentials: User not found");
            }
            String encryptionPassword = username + "WP";
            LoggedEmployee.getInstance().setEmployeeId(emp.getId());

            String decryptPrivateKey = walletService.decryptPrivateKey(emp.getWallet().getEncryptedPrivateKey(), encryptionPassword);

            System.out.println("Stored encrypted private key: " + emp.getWallet().getEncryptedPrivateKey());
            System.out.println("Computed encrypted private key: " + decryptPrivateKey);
            System.out.println("Pk :"+ privateKey);
            if (privateKey.equals(decryptPrivateKey)) {
                return convertToDTO(emp);
            } else {
                throw new IllegalArgumentException("Invalid Login Credentials: User mismatch");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Login Credentials", e);
        }

    }

    public static boolean isValidCIF(String cif) {

        if (!cif.startsWith("RO") || cif.length() < 4 || cif.length() > 12) {
            return false;
        }

        String cifNr = cif.substring(2);

        for (int i = 0; i < cifNr.length(); i++) {
            if (!Character.isDigit(cifNr.charAt(i))) {
                return false;
            }
        }

        String key = "753217532";
        String reversedCIF = new StringBuilder(cifNr).reverse().toString();
        int controlDigit = Character.getNumericValue(reversedCIF.charAt(0));
        String digits = reversedCIF.substring(1);
        String reversedKey = new StringBuilder(key).reverse().toString();

        int sum = 0;
        for (int i = 0; i < digits.length(); i++) {
            int digit = Character.getNumericValue(digits.charAt(i));
            int keyDigit = Character.getNumericValue(reversedKey.charAt(i));
            sum += digit * keyDigit;
        }

        int verificationDigit = (sum * 10) % 11;
        if (verificationDigit == 10) {
            verificationDigit = 0;
        }

        return controlDigit == verificationDigit;
    }

}
