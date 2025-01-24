package com.server.services;

import com.server.DTO.EmployeeCredentialsDTO;
import com.server.DTO.FirmDTO;
import com.server.Entity.EmployeeCredentials;
import com.server.DTO.RequestDTO;

import java.util.List;

public interface AdminService extends EmployeeService{
    void addInspectorToFirm(String firmName, List<EmployeeCredentialsDTO> inspectors) throws Exception;
    void removeInspectorFromFirm(List<EmployeeCredentialsDTO> inspectors);
    void assignContract(String inspectorName , String adminName , Long requestId);
    List<RequestDTO> viewRequests(String firmName);


}
