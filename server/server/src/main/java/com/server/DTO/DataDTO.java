package com.server.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
public class DataDTO {
    public Long id;
    public String firstName;
    public String lastName;
    public String dateOfBirth;
    public String cnp;
    public String gender;

}
