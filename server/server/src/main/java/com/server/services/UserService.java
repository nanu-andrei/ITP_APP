package com.server.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.server.DTO.DataDTO;
import com.server.DTO.RequestDTO;
import com.server.DTO.RequestDataDTO;
import com.server.DTO.UserSignupDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
    public UserSignupDTO signup(UserSignupDTO signupDTO);
    public DataDTO login(String username , String password);
    @Transactional
    public RequestDTO request(String firm,RequestDTO requestDTO) throws JsonProcessingException;

    List<RequestDTO> getAllRequestsForUser(Long userId);
}

