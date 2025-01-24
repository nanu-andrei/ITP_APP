package com.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.DTO.EmployeeCredentialsDTO;
import com.server.DTO.FirmDTO;
import com.server.services.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Test
    public void testLoginSuccess() throws Exception {
        EmployeeCredentialsDTO expectedDTO = new EmployeeCredentialsDTO();
        expectedDTO.setUsername("user");
        when(employeeService.login("user", "privateKey")).thenReturn(expectedDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/employees/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user\",\"privateKey\":\"privateKey\"}"))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.username").value("user"));
    }

    @Test
    public void testRegisterFirm() throws Exception {
        FirmDTO firmDTO = new FirmDTO();
        firmDTO.setFirmName("NewFirm");
        when(employeeService.registerFirm(anyString(), anyString(), anyList())).thenReturn(firmDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/employees/registerFirm?requirement=RO123456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(firmDTO)))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.firmName").value("NewFirm"));
    }
}
