package com.server.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.DTO.*;
import com.server.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserSignupDTO> signup(@RequestBody UserSignupDTO signupDTO) {
        SensitiveDataDTO sensitiveDataDTO = signupDTO.getSensitiveDataDTO();
        if (sensitiveDataDTO == null)
        {
            System.out.println("Error, the dto is null");
        }
        else {
        logger.info("Received signup request for username: {}", sensitiveDataDTO.getUsername());}

        try {
            UserSignupDTO createdUser = userService.signup(signupDTO);
            logger.info("User {} successfully signed up", signupDTO.getSensitiveDataDTO().getUsername());
            System.out.println(new ObjectMapper().writeValueAsString(signupDTO));

            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            logger.error("Signup failed for username: {}", signupDTO.getSensitiveDataDTO().getUsername(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<DataDTO> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("name");
        String password = credentials.get("password");
        logger.info("Received login request for username: {}", username);
        DataDTO loginData = userService.login(username, password);
        if (loginData != null) {
            logger.info("User {} successfully logged in", username);
            return ResponseEntity.ok(loginData);
        } else {
            logger.warn("Login failed for username: {}", username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @PostMapping("/requests")
    public ResponseEntity<RequestDTO> createRequest(@RequestParam String firm, @RequestBody RequestDTO requestDTO) throws JsonProcessingException {
        try {
            RequestDTO responseDTO = userService.request(firm, requestDTO);
            if (responseDTO == null) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            System.out.println(new ObjectMapper().writeValueAsString(responseDTO));
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            System.out.println(new ObjectMapper().writeValueAsString(requestDTO));
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/inbox")
    public ResponseEntity<List<RequestDTO>> getUserRequests(@RequestParam Long userId) {
        try {
            List<RequestDTO> requests = userService.getAllRequestsForUser(userId);
            return new ResponseEntity<>(requests, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
