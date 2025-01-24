// SPDX-License-Identifier: MIT
pragma solidity ^0.8.19;

import "./Central.sol";

contract Car {
    // Static car details that do not change often
    struct Details {
        string vin;
        string manufacturer;
        string model;
        string productionYear;
        string fuelType;
        string plateNumber;
        string color;
    }

    // Car information struct including owner name and static details
    struct CarInfo {  
        string carOwnerName;  // Store owner's name instead of address
        Details details;  // Static details of the car including VIN
    }

    // Verification details to check against static details and dynamic verification info
    struct VerificationDetails {
        Details details;  // Static details that need to match the saved car details
        uint256 pollutionCoefficient;  // Example dynamic detail
        uint256 cubicVolume;  // Another dynamic detail
        string observations;  // Observations or notes by the inspector
    }

    CarInfo public carDetails;  // Static car information
    VerificationDetails[] public verifications;  // Array to store all verifications
    uint256 private _tokenId;  // Token ID for the minted NFT
    address private _owner;
    Central public centralContract;  // Reference to the Central contract
    event OwnershipTransferred(address indexed previousOwner, address indexed newOwner);


    constructor(
        CarInfo memory _carDetails,  // Static car details provided during deployment
        address _centralAddress  // Address of the Central contract
    )  {
        _owner= msg.sender;
        centralContract = Central(_centralAddress);  // Initialize Central contract address
        carDetails = _carDetails;  // Set the static car details
        centralContract.registerCar(_carDetails.details.vin, address(this));
    }

    // Function to explicitly update static car details, only callable by the Central contract
    function updateDetails(Details memory newDetails) public onlyOwner {
        carDetails.details = newDetails;
    }

    // Verification function that checks provided verification details against saved car details
    function verifyCar(VerificationDetails memory verificationDetails) public onlyOwner returns (bool) {
        // Verify static details against the saved car details
        if (
            keccak256(abi.encodePacked(verificationDetails.details.vin)) != keccak256(abi.encodePacked(carDetails.details.vin)) ||
            keccak256(abi.encodePacked(verificationDetails.details.manufacturer)) != keccak256(abi.encodePacked(carDetails.details.manufacturer)) ||
            keccak256(abi.encodePacked(verificationDetails.details.model)) != keccak256(abi.encodePacked(carDetails.details.model)) ||
            keccak256(abi.encodePacked(verificationDetails.details.productionYear)) != keccak256(abi.encodePacked(carDetails.details.productionYear)) ||
            keccak256(abi.encodePacked(verificationDetails.details.fuelType)) != keccak256(abi.encodePacked(carDetails.details.fuelType)) ||
            keccak256(abi.encodePacked(verificationDetails.details.plateNumber)) != keccak256(abi.encodePacked(carDetails.details.plateNumber)) ||
            keccak256(abi.encodePacked(verificationDetails.details.color)) != keccak256(abi.encodePacked(carDetails.details.color))
        ) {
            return false;  // Fail if any of the static details do not match
        }

        // Example dynamic checks
        if (verificationDetails.pollutionCoefficient < 0 || verificationDetails.pollutionCoefficient > 100) {
            return false;  // Fail if pollution coefficient is out of range
        }

        if (verificationDetails.cubicVolume <= 0 || verificationDetails.cubicVolume >=7) {
            return false;  // Fail if cubic volume is not valid
        }

        // Check for specific problematic phrases in observations using substring search
        if (containsSubstring(verificationDetails.observations, "Does not function") || containsSubstring(verificationDetails.observations, "Does not work")) {
            return false;  // Fail if problematic phrases are found anywhere in the observations
        }

        // If all checks pass, store successful verification details
        verifications.push(verificationDetails);
        return true;  // Pass if all checks are satisfied
    }

    // Utility function to check if a substring exists within a string
    function containsSubstring(string memory _str, string memory _substr) internal pure returns (bool) {
        bytes memory strBytes = bytes(_str);
        bytes memory substrBytes = bytes(_substr);

        if (substrBytes.length > strBytes.length) {
            return false;
        }

        for (uint i = 0; i <= strBytes.length - substrBytes.length; i++) {
            bool matchFound = true;
            for (uint j = 0; j < substrBytes.length; j++) {
                if (strBytes[i + j] != substrBytes[j]) {
                    matchFound = false;
                    break;
                }
            }
            if (matchFound) {
                return true;
            }
        }
        return false;
    }

    // Retrieve all verifications for auditing or review purposes
    function getVerificationHistory() public view returns (VerificationDetails[] memory) {
        return verifications;
    }

    // Retrieve the static car details
    function getCarDetails() public view returns (CarInfo memory) {
        return carDetails;
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
