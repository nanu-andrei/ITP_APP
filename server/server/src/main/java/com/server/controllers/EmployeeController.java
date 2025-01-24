package com.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.DTO.EmployeeCredentialsDTO;
import com.server.DTO.FirmDTO;
import com.server.Entity.EmployeeCredentials;
import com.server.services.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    public EmployeeService employeeService;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    @Autowired
    public EmployeeController(@Qualifier("employeeServiceImplementation")EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public EmployeeController()
    {}
    @PostMapping("/login")
    public ResponseEntity<EmployeeCredentialsDTO> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String privateKey = credentials.get("privateKey");
        EmployeeCredentialsDTO loginData = employeeService.login(username,privateKey);
        if (loginData != null) {
            logger.info("User {} successfully logged in", username);
            return ResponseEntity.ok(loginData);
        } else {
            logger.warn("Login failed for username: {}", privateKey);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @GetMapping("/getEmployeeByUsername")
    public EmployeeCredentialsDTO getEmployeeByUsername(@RequestParam String username) {
        return employeeService.getEmployeeByUsername(username);
    }

    @PutMapping("/updateEmployee")
    public void updateEmployee(@RequestParam Long employeeId, @RequestBody EmployeeCredentialsDTO employeeCredentialsDTO) {
        employeeService.updateEmployee(employeeId, employeeCredentialsDTO);
    }

    @PostMapping("/registerFirm")
    public ResponseEntity<FirmDTO> registerFirm(@RequestParam String requirement, @RequestBody FirmDTO firm) throws JsonProcessingException {
        try {
            FirmDTO result = employeeService.registerFirm(requirement ,firm.getFirmName(),convertToDTO(firm.getEmployeeCredentialsList()));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.out.println(new ObjectMapper().writeValueAsString(firm));
            logger.error("Error registering firm: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public List<EmployeeCredentialsDTO> convertToDTO(List<EmployeeCredentials> employees)
    {
        List<EmployeeCredentialsDTO> employeesDTO= new ArrayList<>();
        for(EmployeeCredentials emp : employees)
        {
            EmployeeCredentialsDTO credentialsDTO = new EmployeeCredentialsDTO();
            credentialsDTO.setRole(emp.getRole());
            credentialsDTO.setFirm(emp.getFirm());
            credentialsDTO.setWallet(emp.getWallet());
            credentialsDTO.setUsername(emp.getUsername());
            employeesDTO.add(credentialsDTO);

        }
        return employeesDTO;
    }

    @GetMapping("/listAllCredentials")
    public List<EmployeeCredentials> listAllCredentials() {
        return employeeService.listAllCredentials();
    }


}
