package com.server.DTO;

import com.fasterxml.jackson.annotation.*;
import com.server.Entity.EmployeeCredentials;
import com.server.Entity.Firm;
import com.server.Entity.Wallet;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class EmployeeCredentialsDTO {

    @JsonProperty("id")
    public Long id;
    @JsonProperty("username")
    private String username;
    @JsonProperty("role")
    private EmployeeCredentials.Role role;
    @JsonProperty("firm")
    private Firm firm;
    @JsonProperty("wallet")
    private Wallet wallet;
    public EmployeeCredentialsDTO() {
    }
    @JsonCreator
    public EmployeeCredentialsDTO(Long id,String username, EmployeeCredentials.Role role , Firm firm) {
        this.username = username;
        this.role=role;
        this.firm = firm;
    }



}

