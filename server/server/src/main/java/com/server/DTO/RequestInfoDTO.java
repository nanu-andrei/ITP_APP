package com.server.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.Entity.EmployeeCredentials;
import com.server.Entity.Firm;
import com.server.Entity.Request;
import com.server.Entity.UserData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestInfoDTO {
        private UserData userData; // The user making the request
        private Firm firm; // The firm the request is made to
        private EmployeeCredentials inspector; // The inspector assigned to handle the request

    @JsonCreator
    public RequestInfoDTO(@JsonProperty("userData") UserData userData ,@JsonProperty("firm") Firm firm) {
        this.userData = userData;
        this.firm = firm;
    }
}


