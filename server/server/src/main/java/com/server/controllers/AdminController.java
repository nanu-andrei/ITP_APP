package com.server.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.server.DTO.EmployeeCredentialsDTO;
import com.server.DTO.RequestDTO;
import com.server.Entity.EmployeeCredentials;
import com.server.Repository.EmployeeCredentialsRepository;
import com.server.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.core.Response;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Long.parseLong;

@RestController
@RequestMapping("/employees/admin")
public class AdminController extends EmployeeController{
    public final AdminService adminService;
    public final EmployeeCredentialsRepository credentialsRepository;
    @Autowired
    public AdminController(AdminService adminService, EmployeeCredentialsRepository credentialsRepository)
    {

        this.adminService=adminService;
        this.credentialsRepository = credentialsRepository;
    }
    @PostMapping("/addInspector")
    public void addInspectorToFirm(@RequestParam("firmName") String name, @RequestBody List<EmployeeCredentialsDTO> inspectors) {
        try {
            adminService.addInspectorToFirm(name,inspectors);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @DeleteMapping("/removeInspector")
    public void removeInspectorFromFirm(@RequestParam String username ) {
        List<EmployeeCredentials> employees = new ArrayList<>();
        EmployeeCredentials employee = credentialsRepository.findByUsername(username);
        adminService.removeInspectorFromFirm(convertToDTO(employees));
    }

    @PostMapping("/assignContract")
    public void assignContract(@RequestParam String requestId , @RequestBody ObjectNode employees) {
        String inspectorName = employees.get("inspector").asText();
        String adminName= employees.get("admin").asText();
        Long requestId_l = parseLong(requestId);
        adminService.assignContract(inspectorName, adminName, requestId_l);
    }

    @GetMapping("/viewRequests")
    public ResponseEntity<List<RequestDTO>> viewRequests(@RequestParam String firmName)
    {
        try{
            List<RequestDTO> requestDTOS = adminService.viewRequests(firmName);
            System.out.println("Requests are:"+ requestDTOS);
            return new ResponseEntity<>(requestDTOS, HttpStatus.OK);
        }catch (Exception e){return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);}
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





}
