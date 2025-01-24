package com.server;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.server.DTO.SensitiveDataDTO;
import com.server.DTO.UserSignupDTO;
import com.server.Entity.UserCredentials;
import com.server.Entity.UserData;
import com.server.Repository.UserCredentialsRepository;
import com.server.Repository.UserDataRepository;
import com.server.services.UserServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplementationTest {

    @Mock
    private UserDataRepository userDataRepository;
    @Mock
    private UserCredentialsRepository userCredentialsRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImplementation userService;

    @Test
    public void testSignup() {
        // Setup
        UserSignupDTO signupDTO = new UserSignupDTO();
        SensitiveDataDTO sensitiveDataDTO = new SensitiveDataDTO();
        sensitiveDataDTO.setUsername("user");
        sensitiveDataDTO.setPassword("password");
        signupDTO.setSensitiveDataDTO(sensitiveDataDTO);

        UserCredentials credentials = new UserCredentials();
        credentials.setUsername("user");
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userCredentialsRepository.save(any(UserCredentials.class))).thenReturn(credentials);

        UserData userData = new UserData();
        when(userDataRepository.save(any(UserData.class))).thenReturn(userData);

        // Execute
        UserSignupDTO result = userService.signup(signupDTO);

        // Verify
        assertNotNull(result);
        assertEquals("user", result.getSensitiveDataDTO().getUsername());
        verify(userCredentialsRepository).save(any(UserCredentials.class));
        verify(userDataRepository).save(any(UserData.class));
    }
}
