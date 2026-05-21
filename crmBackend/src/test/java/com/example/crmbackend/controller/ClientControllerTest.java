package com.example.crmbackend.controller;

import com.example.crmbackend.dto.request.ClientRequest;
import com.example.crmbackend.dto.response.ClientResponse;
import com.example.crmbackend.service.interfaces.ClientService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clientController)
                .setCustomArgumentResolvers(new org.springframework.data.web.PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void search_ReturnsOk() throws Exception {
        ClientResponse res = new ClientResponse();
        res.setId(1L);
        Page<ClientResponse> page = new PageImpl<>(List.of(res), org.springframework.data.domain.PageRequest.of(0, 10), 1);

        when(clientService.search(any(), any(), any(), any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/clients"))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].id").value(1));
    }

    @Test
    void create_ReturnsCreated() throws Exception {
        ClientRequest req = new ClientRequest();
        req.setEmail("test@test.com");

        ClientResponse res = new ClientResponse();
        res.setId(1L);
        res.setEmail("test@test.com");

        when(clientService.create(any(ClientRequest.class))).thenReturn(res);

        mockMvc.perform(post("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.email").value("test@test.com"));
    }

    @Test
    void update_ReturnsOk() throws Exception {
        ClientRequest req = new ClientRequest();
        ClientResponse res = new ClientResponse();
        res.setId(1L);

        when(clientService.update(eq(1L), any(ClientRequest.class))).thenReturn(res);

        mockMvc.perform(put("/api/v1/clients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void delete_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/clients/1"))
                .andExpect(status().isNoContent());
    }
}
