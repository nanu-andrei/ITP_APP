package com.server.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
public class RequestDTO {
    public Long id;
    public RequestDataDTO requestData;
    public RequestInfoDTO requestInfo;
    @JsonCreator
    public RequestDTO(@JsonProperty("requestInfo") RequestInfoDTO requestInfo,
                      @JsonProperty("requestData") RequestDataDTO requestData) {
        this.requestInfo = requestInfo;
        this.requestData = requestData;
    }
}
