package com.server.services;


import com.server.DTO.EmployeeCredentialsDTO;
import com.server.DTO.FirmDTO;
import com.server.Entity.EmployeeCredentials;
import com.server.Entity.Firm;
import com.server.Repository.EmployeeCredentialsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    default EmployeeCredentialsDTO getEmployeeByUsername(String username)
    {
        return  new EmployeeCredentialsDTO();
    }
    default void updateEmployee(Long employeeId , EmployeeCredentialsDTO employeeCredentialsDTO)
    {}
    default FirmDTO registerFirm(String requirement,  String firmdetails , List<EmployeeCredentialsDTO> employees) throws Exception
    {
        return new FirmDTO();
    }
    @Transactional
    default EmployeeCredentialsDTO login(String username,String privateKey){ return new EmployeeCredentialsDTO(); }

    @Transactional
    default List<EmployeeCredentials> listAllCredentials() {
        return new ArrayList<>();
    }}
