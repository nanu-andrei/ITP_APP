package com.server.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalletPrivateDTO {

    private Long id;
    private String privateKey;
    private EmployeeCredentialsDTO employee;


}
