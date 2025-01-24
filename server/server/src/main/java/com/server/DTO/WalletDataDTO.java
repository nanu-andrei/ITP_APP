package com.server.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalletDataDTO {

    private Long id;
    private String address;
    private EmployeeCredentialsDTO employee;

}
