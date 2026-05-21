package com.example.crmbackend.controller;

import com.example.crmbackend.dto.request.RestaurantRequest;
import com.example.crmbackend.dto.response.RestaurantResponse;
import com.example.crmbackend.service.interfaces.RestaurantService;
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
class RestaurantControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private RestaurantController restaurantController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(restaurantController)
                .setCustomArgumentResolvers(new org.springframework.data.web.PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void list_ReturnsOk() throws Exception {
        RestaurantResponse res = new RestaurantResponse();
        res.setId(1L);
        Page<RestaurantResponse> page = new PageImpl<>(List.of(res), org.springframework.data.domain.PageRequest.of(0, 10), 1);

        when(restaurantService.list(any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].id").value(1));
    }

    @Test
    void create_ReturnsCreated() throws Exception {
        RestaurantRequest req = new RestaurantRequest();
        RestaurantResponse res = new RestaurantResponse();
        res.setId(1L);

        when(restaurantService.create(any(RestaurantRequest.class))).thenReturn(res);

        mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void update_ReturnsOk() throws Exception {
        RestaurantRequest req = new RestaurantRequest();
        RestaurantResponse res = new RestaurantResponse();
        res.setId(1L);

        when(restaurantService.update(eq(1L), any(RestaurantRequest.class))).thenReturn(res);

        mockMvc.perform(put("/api/v1/restaurants/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void delete_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/restaurants/1"))
                .andExpect(status().isNoContent());
    }
}
