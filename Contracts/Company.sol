// SPDX-License-Identifier: MIT
pragma solidity ^0.8.19;

import "./Central.sol";

contract Company  {
    address private _owner;
    string public companyName;  // Company name
    address public admin;  // Admin address
    address[] public inspectorsList;  // Array to store inspector addresses
    Central public centralContract;  // Reference to the Central contract
    event OwnershipTransferred(address indexed previousOwner, address indexed newOwner);

    constructor(
        string memory _companyName, 
        address _admin, 
        address[] memory _initialInspectors,
        address _centralAddress  // Address of the Central contract
    ) {
        _owner=msg.sender;
        companyName = _companyName;  // Initialize company name
        admin = _admin;  // Set the admin address
        centralContract = Central(_centralAddress);  

        // Directly assign the initial inspectors list
        inspectorsList = _initialInspectors;

        // Register the company with the Central contract
        centralContract.registerCompany(companyName, address(this));
    }

    // Add a new inspector
    function addInspector(address inspector) public onlyOwner {
        require(!_isInspectorInList(inspector), "Inspector already exists");
        inspectorsList.push(inspector);  // Add to list
    }

    // Remove an inspector by address
    function removeInspector(address inspector) public onlyOwner {
        require(_isInspectorInList(inspector), "Inspector does not exist");

        // Remove from list (maintain order)
        for (uint i = 0; i < inspectorsList.length; i++) {
            if (inspectorsList[i] == inspector) {
                inspectorsList[i] = inspectorsList[inspectorsList.length - 1];
                inspectorsList.pop();
                break;
            }
        }
    }

    // Check if an address is an inspector
    function isInspector(address inspector) public view returns (bool) {
        return _isInspectorInList(inspector);
    }

    // Get the list of inspectors
    function getInspectors() public view returns (address[] memory) {
        return inspectorsList;
    }

    // Private helper function to check if an inspector is in the list
    function _isInspectorInList(address inspector) internal view returns (bool) {
        for (uint i = 0; i < inspectorsList.length; i++) {
            if (inspectorsList[i] == inspector) {
                return true;
            }
        }
        return false;
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
}