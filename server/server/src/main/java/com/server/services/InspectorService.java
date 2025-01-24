package com.server.services;

import com.server.DTO.RequestDTO;
import com.server.DTO.RequestDataDTO;
import com.server.Entity.Request;

import java.util.List;

public interface InspectorService extends EmployeeService{
    List<RequestDTO> viewAssignedContracts(String username);
    void completeContract(Request request, String cubicVolume,String pollutionCoefficient,String observations) ;

}
