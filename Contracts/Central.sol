// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract Central {
    address private _owner;

    event OwnershipTransferred(address indexed previousOwner, address indexed newOwner);

    mapping(string => address) public carContractsByVin;  // Maps VIN to car contract address
    mapping(string => address) public companyContracts;  // Maps company name to company contract address

   
    constructor() {
        _owner = msg.sender;
        emit OwnershipTransferred(address(0), _owner);
    }

   
    function owner() public view returns (address) {
        return _owner;
    }

   
    modifier onlyOwner() {
        require(owner() == msg.sender, "Caller is not the owner");
        _;
    }

    
    function transferOwnership(address newOwner) public onlyOwner {
        require(newOwner != address(0), "New owner is the zero address");
        emit OwnershipTransferred(_owner, newOwner);
        _owner = newOwner;
    }

    // Function to register a Car contract after deployment
    function registerCar(string memory _vin, address _carContract) external onlyOwner {
        require(carContractsByVin[_vin] == address(0), "Car with this VIN already exists");  // Ensure no duplicate VIN
        carContractsByVin[_vin] = _carContract;  // Register the car contract address
    }

    // Function to register a Company contract after deployment
    function registerCompany(string memory _companyName, address _companyContract) external onlyOwner {
        require(companyContracts[_companyName] == address(0), "Company with this name already exists");  // Ensure no duplicate company name
        companyContracts[_companyName] = _companyContract;  // Register the company contract address
    }

    // Retrieve the Car contract address by VIN
    function getCarContractByVin(string memory _vin) public view returns (address) {
        return carContractsByVin[_vin];
    }

    // Retrieve the Company contract address by company name
    function getCompanyContract(string memory _companyName) public view returns (address) {
        return companyContracts[_companyName];
    }
}
