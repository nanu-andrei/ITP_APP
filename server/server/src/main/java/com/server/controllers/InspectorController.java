package com.server.controllers;

import com.server.DTO.RequestDTO;
import com.server.DTO.RequestDataDTO;
import com.server.Entity.Request;
import com.server.services.InspectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees/inspector")
public class InspectorController extends EmployeeController{

    public final InspectorService inspectorService;
    @Autowired
    public InspectorController(InspectorService inspectorService)
    {
        this.inspectorService=inspectorService;
    }

    @GetMapping("/viewAssignedContracts")
    public List<RequestDTO> viewAssignedContracts(@RequestParam String username) {
        return inspectorService.viewAssignedContracts(username);
    }

    @PostMapping("/completeContract")
    public void completeContract( @RequestBody Request requests ,@RequestParam String cubicVolume,@RequestParam String pollutionCoefficient,@RequestParam String observations ) {
        inspectorService.completeContract(requests , cubicVolume,pollutionCoefficient,observations);
    }
}

