package com.server.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.contracts.Central;
import com.server.contracts.Car;
import com.server.contracts.Company;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class CentralContractManager {

    @Value("${wallet.file}")
    private String walletFileRelative;

    @Value("${encrypted.wallet.file}")
    private String encryptedWalletFileRelative;

    @Value("${encryption.key}")
    private String encryptionKey;

    @Value("${wallet.directory}")
    private String walletDirectoryRelative;

    @Value("${encrypted.contract.file}")
    private String encryptedContractFileRelative;

    @Value("${wallet.password}")
    private String walletPassword;

    @Value("${ganache.sender.private.key}")
    private String ganacheSenderPrivateKey;

    private final Web3j web3j;
    private final ContractGasProvider gasProvider;
    private Credentials centralContractCredentials;
    private Central centralContract;

    private Path walletFile;
    private Path encryptedWalletFile;
    private Path walletDirectory;
    private Path encryptedContractFile;

    public CentralContractManager(Web3j web3j) {
        this.web3j = web3j;
        BigInteger gasPrice = Convert.toWei("100", Convert.Unit.GWEI).toBigInteger();
        BigInteger gasLimit = BigInteger.valueOf(10000000);  // Adjust the gas limit as needed
        this.gasProvider = new StaticGasProvider(gasPrice, gasLimit);
    }

    @PostConstruct
    public void initializeCentralContractManager() {
        resolvePaths();
        initializeCentralContractWallet();
        initializeCentralContract();
    }

    private void resolvePaths() {
        String baseDir = System.getProperty("user.dir");
        this.walletFile = Paths.get(baseDir, walletFileRelative).toAbsolutePath();
        this.encryptedWalletFile = Paths.get(baseDir, encryptedWalletFileRelative).toAbsolutePath();
        this.walletDirectory = Paths.get(baseDir, walletDirectoryRelative).toAbsolutePath();
        this.encryptedContractFile = Paths.get(baseDir, encryptedContractFileRelative).toAbsolutePath();
    }

    private void initializeCentralContractWallet() {
        try {
            if (encryptedWalletExists()) {
                this.centralContractCredentials = loadEncryptedCentralContractCredentials();
            } else {
                this.centralContractCredentials = createCentralContractWallet(walletPassword);
                encryptAndStoreCentralContractWalletDetails(centralContractCredentials);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error initializing Central Contract wallet", e);
        }
    }

    private Credentials createCentralContractWallet(String password) {
        try {
            Files.createDirectories(walletDirectory);
            String walletFileName = WalletUtils.generateFullNewWalletFile(password, new File(walletDirectory.toString()));
            File walletFile = new File(walletDirectory.toString(), walletFileName);
            Credentials credentials = WalletUtils.loadCredentials(password, walletFile);
            String walletAddress = credentials.getAddress();
            fundWalletWithGanache(walletAddress);
            return credentials;
        } catch (Exception e) {
            throw new RuntimeException("Error creating Central Contract wallet", e);
        }
    }

    private void encryptAndStoreCentralContractWalletDetails(Credentials credentials) throws Exception {
        String privateKey = credentials.getEcKeyPair().getPrivateKey().toString(16);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> keyMap = new HashMap<>();
        keyMap.put("privateKey", privateKey);

        String tempWalletJsonFile = "tempWallet.json";
        objectMapper.writeValue(new File(tempWalletJsonFile), keyMap);

        FileEncryptionUtil.encryptFile(walletPassword, tempWalletJsonFile, encryptedWalletFile.toString());

        Files.deleteIfExists(Paths.get(tempWalletJsonFile));
    }

    private boolean encryptedWalletExists() {
        return Files.exists(encryptedWalletFile);
    }

    private Credentials loadEncryptedCentralContractCredentials() throws Exception {
        String tempDecryptedWalletFile = "tempDecryptedWallet.json";

        FileEncryptionUtil.decryptFile(walletPassword, encryptedWalletFile.toString(), tempDecryptedWalletFile);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> keyMap = objectMapper.readValue(new File(tempDecryptedWalletFile), Map.class);
        String privateKey = keyMap.get("privateKey");

        Credentials credentials = Credentials.create(privateKey);

        Files.deleteIfExists(Paths.get(tempDecryptedWalletFile));

        return credentials;
    }

    private void initializeCentralContract() {
        try {
            String storedAddress = retrieveEncryptedCentralContractAddress();
            if (storedAddress != null && !storedAddress.isEmpty()) {
                this.centralContract = Central.load(storedAddress, web3j, centralContractCredentials, gasProvider);
            } else {
                deployCentralContract();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error initializing Central Contract", e);
        }
    }

    private void deployCentralContract() {
        try {
            Central centralContract = Central.deploy(web3j, centralContractCredentials, gasProvider).send();
            String contractAddress = centralContract.getContractAddress();
            this.centralContract = centralContract;
            encryptAndStoreCentralContractAddress(contractAddress);
        } catch (Exception e) {
            throw new RuntimeException("Error deploying Central Contract", e);
        }
    }

    public void fundWalletWithGanache(String recipientAddress) throws Exception {
        Credentials senderCredentials = Credentials.create(ganacheSenderPrivateKey);
        TransactionReceipt receipt = Transfer.sendFunds(
                web3j, senderCredentials, recipientAddress,
                Convert.toWei("100000000000000", Convert.Unit.ETHER), Convert.Unit.WEI).send();
        TimeUnit.SECONDS.sleep(10);
    }

    private void encryptAndStoreCentralContractAddress(String address) throws Exception {
        String tempAddressFile = "tempDecryptedAddress.txt";
        Files.write(Paths.get(tempAddressFile), address.getBytes());
        FileEncryptionUtil.encryptFile("central_contract", tempAddressFile, encryptedContractFile.toString());
        Files.deleteIfExists(Paths.get(tempAddressFile));
    }

    private String retrieveEncryptedCentralContractAddress() throws Exception {
        if (Files.exists(encryptedContractFile)) {
            String tempDecryptedAddressFile = "tempDecryptedAddress.txt";
            FileEncryptionUtil.decryptFile("central_contract", encryptedContractFile.toString(), tempDecryptedAddressFile);
            String address = new String(Files.readAllBytes(Paths.get(tempDecryptedAddressFile)));
            Files.deleteIfExists(Paths.get(tempDecryptedAddressFile));
            return address;
        } else {
            return null;
        }
    }

    public String getOrDeployCentralContract() {
        if (centralContract != null) {
            return centralContract.getContractAddress();
        } else {
            initializeCentralContract();
            return centralContract.getContractAddress();
        }
    }

    @Bean
    public Credentials getCentralContractCredentials() {
        return this.centralContractCredentials;
    }

    public Central getCentralContract() {
        return this.centralContract;
    }
}
