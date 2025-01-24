package com.server.contracts;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
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
public class Company extends Contract {
    public static final String BINARY = "60806040523480156200001157600080fd5b5060405162000e6338038062000e63833981016040819052620000349162000285565b600080546001600160a01b03191633179055600162000054858262000414565b50600280546001600160a01b038086166001600160a01b031992831617909255600480549284169290911691909117905581516200009a9060039060208501906200010d565b5060048054604051635d91d18d60e11b81526001600160a01b039091169163bb23a31a91620000cf91600191309101620004e0565b600060405180830381600087803b158015620000ea57600080fd5b505af1158015620000ff573d6000803e3d6000fd5b505050505050505062000584565b82805482825590600052602060002090810192821562000165579160200282015b828111156200016557825182546001600160a01b0319166001600160a01b039091161782556020909201916001909101906200012e565b506200017392915062000177565b5090565b5b8082111562000173576000815560010162000178565b634e487b7160e01b600052604160045260246000fd5b604051601f8201601f191681016001600160401b0381118282101715620001cf57620001cf6200018e565b604052919050565b80516001600160a01b0381168114620001ef57600080fd5b919050565b600082601f8301126200020657600080fd5b815160206001600160401b038211156200022457620002246200018e565b8160051b62000235828201620001a4565b92835284810182019282810190878511156200025057600080fd5b83870192505b848310156200027a576200026a83620001d7565b8252918301919083019062000256565b979650505050505050565b600080600080608085870312156200029c57600080fd5b84516001600160401b0380821115620002b457600080fd5b818701915087601f830112620002c957600080fd5b815181811115620002de57620002de6200018e565b6020620002f4601f8301601f19168201620001a4565b8281528a828487010111156200030957600080fd5b60005b83811015620003295785810183015182820184015282016200030c565b506000818401830152975062000341898201620001d7565b9650505060408701519150808211156200035a57600080fd5b506200036987828801620001f4565b9250506200037a60608601620001d7565b905092959194509250565b600181811c908216806200039a57607f821691505b602082108103620003bb57634e487b7160e01b600052602260045260246000fd5b50919050565b601f8211156200040f57600081815260208120601f850160051c81016020861015620003ea5750805b601f850160051c820191505b818110156200040b57828155600101620003f6565b5050505b505050565b81516001600160401b038111156200043057620004306200018e565b620004488162000441845462000385565b84620003c1565b602080601f831160018114620004805760008415620004675750858301515b600019600386901b1c1916600185901b1785556200040b565b600085815260208120601f198616915b82811015620004b15788860151825594840194600190910190840162000490565b5085821015620004d05787850151600019600388901b60f8161c191681555b5050505050600190811b01905550565b604081526000808454620004f48162000385565b8060408601526060600180841660008114620005195760018114620005345762000567565b60ff1985168884015283151560051b88018301955062000567565b8960005260208060002060005b868110156200055e5781548b820187015290840190820162000541565b8a018501975050505b505050506001600160a01b03851660208501525090509392505050565b6108cf80620005946000396000f3fe608060405234801561001057600080fd5b506004361061009e5760003560e01c80638e6b8ed3116100665780638e6b8ed31461012f578063961fd0d414610144578063f2fde38b14610157578063f5ec2eed1461016a578063f851a4401461017f57600080fd5b806329878bc4146100a35780632cdad41c146100d35780637c70e791146100f65780637e4584921461010b5780638da5cb5b1461011e575b600080fd5b6004546100b6906001600160a01b031681565b6040516001600160a01b0390911681526020015b60405180910390f35b6100e66100e136600461070d565b610192565b60405190151581526020016100ca565b61010961010436600461070d565b6101a3565b005b61010961011936600461070d565b61036b565b6000546001600160a01b03166100b6565b610137610476565b6040516100ca919061073d565b6100b661015236600461078a565b6104d8565b61010961016536600461070d565b610502565b610172610616565b6040516100ca91906107a3565b6002546100b6906001600160a01b031681565b600061019d826106a4565b92915050565b336101b66000546001600160a01b031690565b6001600160a01b03161461020b5760405162461bcd60e51b815260206004820152601760248201527621b0b63632b91034b9903737ba103a34329037bbb732b960491b60448201526064015b60405180910390fd5b610214816106a4565b6102605760405162461bcd60e51b815260206004820152601860248201527f496e73706563746f7220646f6573206e6f7420657869737400000000000000006044820152606401610202565b60005b60035481101561036757816001600160a01b03166003828154811061028a5761028a6107f1565b6000918252602090912001546001600160a01b03160361035557600380546102b49060019061081d565b815481106102c4576102c46107f1565b600091825260209091200154600380546001600160a01b0390921691839081106102f0576102f06107f1565b9060005260206000200160006101000a8154816001600160a01b0302191690836001600160a01b03160217905550600380548061032f5761032f610830565b600082815260209020810160001990810180546001600160a01b03191690550190555050565b8061035f81610846565b915050610263565b5050565b3361037e6000546001600160a01b031690565b6001600160a01b0316146103ce5760405162461bcd60e51b815260206004820152601760248201527621b0b63632b91034b9903737ba103a34329037bbb732b960491b6044820152606401610202565b6103d7816106a4565b156104245760405162461bcd60e51b815260206004820152601860248201527f496e73706563746f7220616c72656164792065786973747300000000000000006044820152606401610202565b600380546001810182556000919091527fc2575a0e9e593c00f959f8c92f12db2869c3395a3b0502d05e2516446f71f85b0180546001600160a01b0319166001600160a01b0392909216919091179055565b606060038054806020026020016040519081016040528092919081815260200182805480156104ce57602002820191906000526020600020905b81546001600160a01b031681526001909101906020018083116104b0575b5050505050905090565b600381815481106104e857600080fd5b6000918252602090912001546001600160a01b0316905081565b336105156000546001600160a01b031690565b6001600160a01b0316146105655760405162461bcd60e51b815260206004820152601760248201527621b0b63632b91034b9903737ba103a34329037bbb732b960491b6044820152606401610202565b6001600160a01b0381166105bb5760405162461bcd60e51b815260206004820152601d60248201527f4e6577206f776e657220697320746865207a65726f20616464726573730000006044820152606401610202565b600080546040516001600160a01b03808516939216917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e091a3600080546001600160a01b0319166001600160a01b0392909216919091179055565b600180546106239061085f565b80601f016020809104026020016040519081016040528092919081815260200182805461064f9061085f565b801561069c5780601f106106715761010080835404028352916020019161069c565b820191906000526020600020905b81548152906001019060200180831161067f57829003601f168201915b505050505081565b6000805b60035481101561070457826001600160a01b0316600382815481106106cf576106cf6107f1565b6000918252602090912001546001600160a01b0316036106f25750600192915050565b806106fc81610846565b9150506106a8565b50600092915050565b60006020828403121561071f57600080fd5b81356001600160a01b038116811461073657600080fd5b9392505050565b6020808252825182820181905260009190848201906040850190845b8181101561077e5783516001600160a01b031683529284019291840191600101610759565b50909695505050505050565b60006020828403121561079c57600080fd5b5035919050565b600060208083528351808285015260005b818110156107d0578581018301518582016040015282016107b4565b506000604082860101526040601f19601f8301168501019250505092915050565b634e487b7160e01b600052603260045260246000fd5b634e487b7160e01b600052601160045260246000fd5b8181038181111561019d5761019d610807565b634e487b7160e01b600052603160045260246000fd5b60006001820161085857610858610807565b5060010190565b600181811c9082168061087357607f821691505b60208210810361089357634e487b7160e01b600052602260045260246000fd5b5091905056fea26469706673582212203cdc4057d90ebe57e8d67d3bb262c0cbcad044f25c4dd023c21b2aed0780045664736f6c63430008130033";

    private static String librariesLinkedBinary;

    public static final String FUNC_ADDINSPECTOR = "addInspector";

    public static final String FUNC_ADMIN = "admin";

    public static final String FUNC_CENTRALCONTRACT = "centralContract";

    public static final String FUNC_COMPANYNAME = "companyName";

    public static final String FUNC_GETINSPECTORS = "getInspectors";

    public static final String FUNC_INSPECTORSLIST = "inspectorsList";

    public static final String FUNC_ISINSPECTOR = "isInspector";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_REMOVEINSPECTOR = "removeInspector";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    @Deprecated
    protected Company(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Company(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Company(String contractAddress, Web3j web3j, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Company(String contractAddress, Web3j web3j, TransactionManager transactionManager,
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

    public RemoteFunctionCall<TransactionReceipt> addInspector(String inspector) {
        final Function function = new Function(
                FUNC_ADDINSPECTOR, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, inspector)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> admin() {
        final Function function = new Function(FUNC_ADMIN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> centralContract() {
        final Function function = new Function(FUNC_CENTRALCONTRACT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> companyName() {
        final Function function = new Function(FUNC_COMPANYNAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<List> getInspectors() {
        final Function function = new Function(FUNC_GETINSPECTORS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<String> inspectorsList(BigInteger param0) {
        final Function function = new Function(FUNC_INSPECTORSLIST, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Boolean> isInspector(String inspector) {
        final Function function = new Function(FUNC_ISINSPECTOR, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, inspector)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<String> owner() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> removeInspector(String inspector) {
        final Function function = new Function(
                FUNC_REMOVEINSPECTOR, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, inspector)), 
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
    public static Company load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new Company(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Company load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Company(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Company load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new Company(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Company load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Company(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Company> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider, String _companyName, String _admin,
            List<String> _initialInspectors, String _centralAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_companyName), 
                new org.web3j.abi.datatypes.Address(160, _admin), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(_initialInspectors, org.web3j.abi.datatypes.Address.class)), 
                new org.web3j.abi.datatypes.Address(160, _centralAddress)));
        return deployRemoteCall(Company.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), encodedConstructor);
    }

    public static RemoteCall<Company> deploy(Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider, String _companyName, String _admin,
            List<String> _initialInspectors, String _centralAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_companyName), 
                new org.web3j.abi.datatypes.Address(160, _admin), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(_initialInspectors, org.web3j.abi.datatypes.Address.class)), 
                new org.web3j.abi.datatypes.Address(160, _centralAddress)));
        return deployRemoteCall(Company.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Company> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit, String _companyName, String _admin,
            List<String> _initialInspectors, String _centralAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_companyName), 
                new org.web3j.abi.datatypes.Address(160, _admin), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(_initialInspectors, org.web3j.abi.datatypes.Address.class)), 
                new org.web3j.abi.datatypes.Address(160, _centralAddress)));
        return deployRemoteCall(Company.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Company> deploy(Web3j web3j, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit, String _companyName, String _admin,
            List<String> _initialInspectors, String _centralAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_companyName), 
                new org.web3j.abi.datatypes.Address(160, _admin), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(_initialInspectors, org.web3j.abi.datatypes.Address.class)), 
                new org.web3j.abi.datatypes.Address(160, _centralAddress)));
        return deployRemoteCall(Company.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), encodedConstructor);
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
