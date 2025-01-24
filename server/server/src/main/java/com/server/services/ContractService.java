package com.server.services;

import com.server.Entity.Request;
import com.server.contracts.Car;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.List;

public interface ContractService {
    String deployContract(String contractType, String identifier) throws  Exception; // Can be car or company
    TransactionReceipt executeTransaction(String contractAddress, String functionName, List<Object> parameters) throws Exception;
    String getContractAddress(String contractType, String identifier) throws  Exception;

    Car.Details convertToCarContractDetails(Request.Details details);
}
