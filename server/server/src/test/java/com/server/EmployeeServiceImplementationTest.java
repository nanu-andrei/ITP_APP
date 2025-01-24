package com.server;

import com.server.DTO.EmployeeCredentialsDTO;
import com.server.DTO.FirmDTO;
import com.server.Entity.EmployeeCredentials;
import com.server.Entity.Firm;
import com.server.Repository.EmployeeCredentialsRepository;
import com.server.Repository.FirmRepository;
import com.server.Repository.WalletRepository;
import com.server.services.AdminService;
import com.server.services.EmployeeServiceImplementation;
import com.server.services.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplementationTest {

    @Mock
    private EmployeeCredentialsRepository credentialsRepository;
    @Mock
    private FirmRepository firmRepository;
    @Mock
    private WalletRepository walletRepository;
    @Mock
    private WalletService walletService;
    @Mock
    private AdminService adminService;

    @InjectMocks
    private EmployeeServiceImplementation employeeService;

    // Example unit test for the method `registerFirm`
    @Test
    void testRegisterFirmValidCIF() throws Exception {
        when(firmRepository.findByFirmName("FirmName")).thenReturn(null);
        when(firmRepository.save(any(Firm.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<EmployeeCredentialsDTO> employees = new ArrayList<>();
        EmployeeCredentialsDTO adminDTO = new EmployeeCredentialsDTO();
        adminDTO.setUsername("admin");
        adminDTO.setRole(EmployeeCredentials.Role.ADMIN);
        employees.add(adminDTO);

        FirmDTO result = employeeService.registerFirm("RO1234567890", "FirmName", employees);

        assertNotNull(result);
        assertEquals("FirmName", result.getFirmName());
        assertNotNull(result.getPrimaryAdminId());
    }
}
