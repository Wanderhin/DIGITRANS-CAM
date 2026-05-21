package com.example.crmbackend.controller;

import com.example.crmbackend.dto.request.InteractionRequest;
import com.example.crmbackend.dto.response.InteractionResponse;
import com.example.crmbackend.service.interfaces.InteractionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class InteractionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private InteractionService interactionService;

    @InjectMocks
    private InteractionController interactionController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(interactionController)
                .setCustomArgumentResolvers(new org.springframework.data.web.PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void listByClient_ReturnsOk() throws Exception {
        InteractionResponse res = new InteractionResponse();
        res.setId(1L);
        Page<InteractionResponse> page = new PageImpl<>(List.of(res), org.springframework.data.domain.PageRequest.of(0, 10), 1);

        when(interactionService.listByClient(any(), any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/interactions/client/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].id").value(1));
    }

    @Test
    void create_ReturnsCreated() throws Exception {
        InteractionRequest req = new InteractionRequest();
        InteractionResponse res = new InteractionResponse();
        res.setId(1L);

        when(interactionService.create(any(), any())).thenReturn(res);

        mockMvc.perform(post("/api/v1/interactions")
                .principal(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken("user", "pass"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1));
    }
}
