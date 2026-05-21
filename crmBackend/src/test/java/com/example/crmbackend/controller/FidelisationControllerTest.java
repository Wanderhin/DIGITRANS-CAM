package com.example.crmbackend.controller;

import com.example.crmbackend.dto.response.FidelisationResponse;
import com.example.crmbackend.service.interfaces.FidelisationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class FidelisationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FidelisationService fidelisationService;

    @InjectMocks
    private FidelisationController fidelisationController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(fidelisationController).build();
    }

    @Test
    void getByClient_ReturnsOk() throws Exception {
        FidelisationResponse res = FidelisationResponse.builder().points(100).build();

        when(fidelisationService.getByClient(1L)).thenReturn(res);

        mockMvc.perform(get("/api/v1/fidelisation/client/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.points").value(100));
    }
}
