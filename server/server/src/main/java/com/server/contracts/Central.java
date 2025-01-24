package com.server.contracts;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.*;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/main/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.6.0.
 */
@SuppressWarnings("rawtypes")
public class Central extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b50600080546001600160a01b0319163390811782556040519091907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e0908290a36106db8061005f6000396000f3fe608060405234801561001057600080fd5b50600436106100885760003560e01c8063b38fadc01161005b578063b38fadc014610116578063bb23a31a14610129578063bf79270c1461013c578063f2fde38b1461017057600080fd5b80630e3968d61461008d5780632b8e8131146100bc57806340922420146100f05780638da5cb5b14610105575b600080fd5b6100a061009b3660046105ad565b610183565b6040516001600160a01b03909116815260200160405180910390f35b6100a06100ca3660046105ad565b80516020818301810180516002825292820191909301209152546001600160a01b031681565b6101036100fe366004610606565b6101b4565b005b6000546001600160a01b03166100a0565b6100a06101243660046105ad565b6102e1565b610103610137366004610606565b6102f3565b6100a061014a3660046105ad565b80516020818301810180516001825292820191909301209152546001600160a01b031681565b61010361017e366004610654565b6103f6565b60006002826040516101959190610676565b908152604051908190036020019020546001600160a01b031692915050565b336101c76000546001600160a01b031690565b6001600160a01b03161461021c5760405162461bcd60e51b815260206004820152601760248201527621b0b63632b91034b9903737ba103a34329037bbb732b960491b60448201526064015b60405180910390fd5b60006001600160a01b03166001836040516102379190610676565b908152604051908190036020019020546001600160a01b03161461029d5760405162461bcd60e51b815260206004820181905260248201527f436172207769746820746869732056494e20616c7265616479206578697374736044820152606401610213565b806001836040516102ae9190610676565b90815260405190819003602001902080546001600160a01b03929092166001600160a01b03199092169190911790555050565b60006001826040516101959190610676565b336103066000546001600160a01b031690565b6001600160a01b0316146103565760405162461bcd60e51b815260206004820152601760248201527621b0b63632b91034b9903737ba103a34329037bbb732b960491b6044820152606401610213565b60006001600160a01b03166002836040516103719190610676565b908152604051908190036020019020546001600160a01b0316146103e55760405162461bcd60e51b815260206004820152602560248201527f436f6d70616e7920776974682074686973206e616d6520616c72656164792065604482015264786973747360d81b6064820152608401610213565b806002836040516102ae9190610676565b336104096000546001600160a01b031690565b6001600160a01b0316146104595760405162461bcd60e51b815260206004820152601760248201527621b0b63632b91034b9903737ba103a34329037bbb732b960491b6044820152606401610213565b6001600160a01b0381166104af5760405162461bcd60e51b815260206004820152601d60248201527f4e6577206f776e657220697320746865207a65726f20616464726573730000006044820152606401610213565b600080546040516001600160a01b03808516939216917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e091a3600080546001600160a01b0319166001600160a01b0392909216919091179055565b634e487b7160e01b600052604160045260246000fd5b600082601f83011261053157600080fd5b813567ffffffffffffffff8082111561054c5761054c61050a565b604051601f8301601f19908116603f011681019082821181831017156105745761057461050a565b8160405283815286602085880101111561058d57600080fd5b836020870160208301376000602085830101528094505050505092915050565b6000602082840312156105bf57600080fd5b813567ffffffffffffffff8111156105d657600080fd5b6105e284828501610520565b949350505050565b80356001600160a01b038116811461060157600080fd5b919050565b6000806040838503121561061957600080fd5b823567ffffffffffffffff81111561063057600080fd5b61063c85828601610520565b92505061064b602084016105ea565b90509250929050565b60006020828403121561066657600080fd5b61066f826105ea565b9392505050565b6000825160005b81811015610697576020818601810151858301520161067d565b50600092019182525091905056fea26469706673582212200f6b2a0f2e184ba950eaf1df6a60b0dc22e6ac89b5019ea1e01cb0c18194edaa64736f6c63430008130033";

    private static String librariesLinkedBinary;

    public static final String FUNC_CARCONTRACTSBYVIN = "carContractsByVin";

    public static final String FUNC_COMPANYCONTRACTS = "companyContracts";

    public static final String FUNC_GETCARCONTRACTBYVIN = "getCarContractByVin";

    public static final String FUNC_GETCOMPANYCONTRACT = "getCompanyContract";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_REGISTERCAR = "registerCar";

    public static final String FUNC_REGISTERCOMPANY = "registerCompany";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    @Deprecated
    protected Central(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Central(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Central(String contractAddress, Web3j web3j, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Central(String contractAddress, Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(
            TransactionReceipt transactionReceipt) {

        // Create a list to store event responses
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<>();

        // Extract logs from the transaction receipt
        List<Log> logs = transactionReceipt.getLogs();

        // Iterate through each log
        for (Log log : logs) {
            // Extract event values using each log
            Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log);

            // Check if eventValues is not null before proceeding
            if (eventValues != null) {
                // Create a new response object and map values
                OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
                typedResponse.log = eventValues.getLog();
                typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();

                // Add the response object to the list
                responses.add(typedResponse);
            }
        }

        // Return the list of event responses
        return responses;
    }

    public static OwnershipTransferredEventResponse getOwnershipTransferredEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log);
        OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
        typedResponse.log = log;
        typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getOwnershipTransferredEventFromLog(log));
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT));
        return ownershipTransferredEventFlowable(filter);
    }

    public RemoteFunctionCall<String> carContractsByVin(String param0) {
        final Function function = new Function(FUNC_CARCONTRACTSBYVIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> companyContracts(String param0) {
        final Function function = new Function(FUNC_COMPANYCONTRACTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> getCarContractByVin(String _vin) {
        final Function function = new Function(FUNC_GETCARCONTRACTBYVIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_vin)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> getCompanyContract(String _companyName) {
        final Function function = new Function(FUNC_GETCOMPANYCONTRACT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_companyName)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> owner() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> registerCar(String _vin, String _carContract) {
        final Function function = new Function(
                FUNC_REGISTERCAR, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_vin), 
                new org.web3j.abi.datatypes.Address(160, _carContract)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> registerCompany(String _companyName,
            String _companyContract) {
        final Function function = new Function(
                FUNC_REGISTERCOMPANY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_companyName), 
                new org.web3j.abi.datatypes.Address(160, _companyContract)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferOwnership(String newOwner) {
        final Function function = new Function(
                FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, newOwner)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static Central load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new Central(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Central load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Central(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Central load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new Central(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Central load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Central(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Central> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Central.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    public static RemoteCall<Central> deploy(Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Central.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<Central> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Central.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<Central> deploy(Web3j web3j, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Central.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static void linkLibraries(String bytecode, Map<String, String> libraryAddresses) {
        for (Map.Entry<String, String> entry : libraryAddresses.entrySet()) {
            String libraryName = entry.getKey();
            String libraryAddress = entry.getValue();
            String placeholder = "__" + libraryName + "_".repeat(38 - libraryName.length()); // Creating a placeholder pattern
            bytecode = bytecode.replace(placeholder, libraryAddress.replace("0x", ""));
        }
        librariesLinkedBinary = bytecode;
    }

    private static String getDeploymentBinary() {
        if (librariesLinkedBinary != null) {
            return librariesLinkedBinary;
        } else {
            return BINARY;
        }
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }
}
