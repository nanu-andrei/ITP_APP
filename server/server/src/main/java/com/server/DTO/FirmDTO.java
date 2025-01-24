package com.server.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.Entity.EmployeeCredentials;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class FirmDTO {
    private Long id;
    private String firmName;
    private List<EmployeeCredentials> employeeCredentialsList;

    private Long primaryAdminId;
    public FirmDTO() {
    }
    @JsonCreator
    public FirmDTO(@JsonProperty("id") Long id, @JsonProperty("name") String name, @JsonProperty("employeeCredentialsList") List<EmployeeCredentials> employeeCredentialsList, Long primaryAdminId) {
        this.id = id;
        this.firmName = name;
        this.employeeCredentialsList = employeeCredentialsList;
        this.primaryAdminId = primaryAdminId;
    }

}

