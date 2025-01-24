package com.server.services;

import com.server.Entity.EmployeeCredentials;
import com.server.Entity.Firm;
import com.server.Entity.Request;
import com.server.Entity.Wallet;
import com.server.Repository.FirmRepository;
import com.server.Repository.UserRequestRepository;
import com.server.Repository.WalletRepository;
import com.server.contracts.Car;
import com.server.contracts.Central;
import com.server.contracts.Company;
import com.server.utils.CentralContractManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContractServiceImplementation implements ContractService {

    private final Web3j web3j;
    private final CentralContractManager centralContractManager;
    private final ContractGasProvider gasProvider = new DefaultGasProvider();
    private final Credentials adminInspectorCredentials;
    private final Central centralContract;
    private final FirmRepository firmRepository;
    private final WalletRepository walletRepository;
    private final UserRequestRepository requestRepository;

    @Autowired
    public ContractServiceImplementation(Web3j web3j, CentralContractManager centralContractManager, Credentials adminInspectorCredentials, FirmRepository firmRepository, WalletRepository walletRepository, UserRequestRepository requestRepository) {
        this.web3j = web3j;
        this.centralContractManager = centralContractManager;
        this.adminInspectorCredentials = adminInspectorCredentials;
        this.centralContract = centralContractManager.getCentralContract();
        this.firmRepository = firmRepository;
        this.walletRepository = walletRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public String deployContract(String contractType, String identifier) throws Exception {
        switch (contractType.toLowerCase()) {
            case "car":
                Request request = requestRepository.findByDetailsVin(identifier);
                Car.Details carDetails = convertToCarContractDetails(request.getDetails());
                return deployCarContract(request.getUserData().getFirstName() + "" + request.getUserData().getLastName(), carDetails);

            case "company":
                Firm firm = firmRepository.findByFirmName(identifier);
                StringBuilder temp = new StringBuilder();
                List<String> initialInspectors = new ArrayList<>();
                for (EmployeeCredentials emp : firm.getEmployeeCredentials()) {
                    if (emp.getRole() == EmployeeCredentials.Role.ADMIN) {
                        temp.append(emp.getWallet().getAddress());
                    } else {
                        initialInspectors.add(emp.getWallet().getAddress());
                    }
                }
                String admin = temp.toString();
                Wallet wallet = walletRepository.findByAddress(admin);
                Credentials adminCredentials = Credentials.create(walletRepository.findByAddress(admin).getPrivateKey());
                return deployCompanyContract(identifier, admin, initialInspectors, adminCredentials);

            default:
                throw new IllegalArgumentException("Unknown contract type: " + contractType);
        }
    }

    @Override
    public TransactionReceipt executeTransaction(String contractAddress, String functionName, List<Object> parameters) throws Exception {
        switch (functionName) {
            case "verifyCar": {
                Car.VerificationDetails verificationDetails = (Car.VerificationDetails) parameters.get(0);
                return verifyCar(contractAddress, verificationDetails);
            }
            case "updateCarDetails": {
                Car.Details newDetails = (Car.Details) parameters.get(0);
                return updateCarDetails(contractAddress, newDetails);
            }
            case "addInspector": {
                String inspectorAddress = (String) parameters.get(0);
                return addInspector(contractAddress, inspectorAddress);
            }
            case "removeInspector": {
                String inspectorAddress = (String) parameters.get(0);
                return removeInspector(contractAddress, inspectorAddress);
            }
            default:
                throw new IllegalArgumentException("Unknown function: " + functionName);
        }
    }

    @Override
    public String getContractAddress(String contractType, String identifier) throws Exception {
        if ("car".equalsIgnoreCase(contractType)) {
            return centralContract.getCarContractByVin(identifier).send();
        } else if ("company".equalsIgnoreCase(contractType)) {
            return centralContract.getCompanyContract(identifier).send();
        } else {
            throw new IllegalArgumentException("Unknown contract type: " + contractType);
        }
    }

    private String deployCarContract(String carOwnerName, Car.Details carDetails) throws Exception {
        Credentials centralContractCredentials = centralContractManager.getCentralContractCredentials();
        Car.CarInfo carInfo = new Car.CarInfo(carOwnerName, carDetails);

        Car carContract = Car.deploy(
                web3j, centralContractCredentials, gasProvider, carInfo, centralContract.getContractAddress()
        ).send();

        TransactionReceipt receipt = carContract.getTransactionReceipt().orElseThrow(() ->
                new RuntimeException("Failed to retrieve deployment transaction receipt."));

        System.out.println("Car contract deployed. Transaction hash: " + receipt.getTransactionHash());

        carContract.transferOwnership(centralContract.getContractAddress()).send();

        return carContract.getContractAddress();
    }

    private String deployCompanyContract(String companyName, String adminAddress, List<String> initialInspectors, Credentials adminCredentials) throws Exception {
        Central centralContract = centralContractManager.getCentralContract();

        Company companyContract = Company.deploy(
                web3j, adminCredentials, gasProvider, companyName, adminAddress, initialInspectors, centralContract.getContractAddress()
        ).send();

        TransactionReceipt receipt = companyContract.getTransactionReceipt().orElseThrow(() ->
                new RuntimeException("Failed to retrieve deployment transaction receipt."));

        System.out.println("Company contract deployed. Transaction hash: " + receipt.getTransactionHash());

        companyContract.transferOwnership(centralContract.getContractAddress()).send();

        return companyContract.getContractAddress();
    }

    private TransactionReceipt verifyCar(String carContractAddress, Car.VerificationDetails verificationDetails) throws Exception {
        Car carContract = Car.load(carContractAddress, web3j, centralContractManager.getCentralContractCredentials(), gasProvider);
        return carContract.verifyCar(verificationDetails).send();
    }

    private TransactionReceipt updateCarDetails(String carContractAddress, Car.Details newDetails) throws Exception {
        Car carContract = Car.load(carContractAddress, web3j, centralContractManager.getCentralContractCredentials(), gasProvider);
        return carContract.updateDetails(newDetails).send();
    }

    private TransactionReceipt addInspector(String companyContractAddress, String inspectorAddress) throws Exception {
        Company companyContract = Company.load(companyContractAddress, web3j, centralContractManager.getCentralContractCredentials(), gasProvider);
        return companyContract.addInspector(inspectorAddress).send();
    }

    private TransactionReceipt removeInspector(String companyContractAddress, String inspectorAddress) throws Exception {
        Company companyContract = Company.load(companyContractAddress, web3j, centralContractManager.getCentralContractCredentials(), gasProvider);
        return companyContract.removeInspector(inspectorAddress).send();
    }

    public Car.Details convertToCarContractDetails(Request.Details requestDetails) {
        return new Car.Details(
                requestDetails.getVin(),
                requestDetails.getManufacturer(),
                requestDetails.getModel(),
                requestDetails.getProductionYear(),
                requestDetails.getFuelType(),
                requestDetails.getPlateNumber(),
                requestDetails.getColor()
        );
    }
}
