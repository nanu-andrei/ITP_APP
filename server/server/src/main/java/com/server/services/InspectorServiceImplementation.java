package com.server.services;

import com.server.DTO.RequestDTO;
import com.server.DTO.RequestDataDTO;
import com.server.DTO.RequestInfoDTO;
import com.server.Entity.EmployeeCredentials;
import com.server.Entity.Request;
import com.server.Repository.EmployeeCredentialsRepository;
import com.server.Repository.UserRequestRepository;
import com.server.contracts.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
@Service
public class InspectorServiceImplementation implements InspectorService{

    public final EmployeeCredentialsRepository employeeCredentialsRepository;
    public final UserRequestRepository requestRepository;
    public final ContractService contractService;
    @Autowired
    public InspectorServiceImplementation(EmployeeCredentialsRepository employeeCredentialsRepository, UserRequestRepository requestRepository, ContractService contractService) {
        this.employeeCredentialsRepository = employeeCredentialsRepository;
        this.requestRepository = requestRepository;
        this.contractService = contractService;
    }
    @Override
    public List<RequestDTO> viewAssignedContracts(String username) {
        List<RequestDTO> assignedContracts = new ArrayList<>();
        EmployeeCredentials inspector  = employeeCredentialsRepository.findByUsername(username);
        for(Request requests: inspector.getFirm().getRequests())
        {
            if(requests.getInspector().getUsername().equals(username))
            {
                assignedContracts.add(convertToDTO(requests));
            }
        }
        return assignedContracts;
    }


    @Override
    public void completeContract(Request request, String cubicVolume, String pollutionCoefficient, String observations) {
        try {
            String carContractAddress;
            try {
                carContractAddress = contractService.getContractAddress("car", request.getDetails().getVin());
            } catch (Exception e) {
                Car.Details carDetails = contractService.convertToCarContractDetails(request.getDetails());
                carContractAddress = contractService.deployContract("car", String.valueOf(carDetails));
            }

            Car.VerificationDetails verificationDetails = new Car.VerificationDetails(
                    contractService.convertToCarContractDetails(request.getDetails()),
                    new BigInteger(pollutionCoefficient),
                    new BigInteger(cubicVolume),
                    observations
            );

            TransactionReceipt receipt = contractService.executeTransaction(
                    carContractAddress,
                    "verifyCar",
                    List.of(verificationDetails)
            );

            if (receipt == null || !receipt.isStatusOK()) {
                throw new RuntimeException("Car verification failed on the blockchain. Transaction status: " +
                        (receipt == null ? "null receipt" : receipt.getStatus()));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while completing the contract: " + e.getMessage(), e);
        }
    }



    private RequestDTO convertToDTO(Request request) {
        RequestDTO requestDTO = new RequestDTO();
        RequestDataDTO requestDataDTO = new RequestDataDTO();
        RequestInfoDTO requestInfoDTO = new RequestInfoDTO();

        requestDataDTO.setDetails(request.getDetails());
        requestInfoDTO.setUserData(request.getUserData());
        requestInfoDTO.setInspector(request.getInspector());
        requestInfoDTO.setFirm(request.getFirm());

        requestDTO.setRequestData(requestDataDTO);
        requestDTO.setRequestInfo(requestInfoDTO);

        return requestDTO;
    }


}
