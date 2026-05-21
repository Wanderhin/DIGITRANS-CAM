package com.example.crmbackend.impl;

import com.example.crmbackend.dto.request.RestaurantRequest;
import com.example.crmbackend.dto.response.RestaurantResponse;
import com.example.crmbackend.entity.Restaurant;
import com.example.crmbackend.exception.ResourceNotFoundException;
import com.example.crmbackend.mapper.RestaurantMapper;
import com.example.crmbackend.repository.RestaurantRepository;
import com.example.crmbackend.service.impl.RestaurantServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceImplTest {

    @Mock
    private RestaurantRepository repo;
    @Mock
    private RestaurantMapper mapper;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    private Restaurant restaurant;
    private RestaurantRequest req;
    private RestaurantResponse res;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setActif(true);

        req = new RestaurantRequest();
        req.setNom("Resto 1");

        res = new RestaurantResponse();
        res.setId(1L);
    }

    @Test
    void list_Success() {
        when(repo.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(restaurant)));
        when(mapper.toResponse(any())).thenReturn(res);

        Page<RestaurantResponse> result = restaurantService.list(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getById_Success() {
        when(repo.findById(1L)).thenReturn(Optional.of(restaurant));
        when(mapper.toResponse(restaurant)).thenReturn(res);

        RestaurantResponse result = restaurantService.getById(1L);

        assertNotNull(result);
    }

    @Test
    void getById_NotFound_ThrowsException() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> restaurantService.getById(1L));
    }

    @Test
    void create_Success() {
        when(mapper.toEntity(req)).thenReturn(restaurant);
        when(repo.save(any())).thenReturn(restaurant);
        when(mapper.toResponse(restaurant)).thenReturn(res);

        RestaurantResponse result = restaurantService.create(req);

        assertNotNull(result);
    }

    @Test
    void update_Success() {
        when(repo.findById(1L)).thenReturn(Optional.of(restaurant));
        when(repo.save(restaurant)).thenReturn(restaurant);
        when(mapper.toResponse(restaurant)).thenReturn(res);

        RestaurantResponse result = restaurantService.update(1L, req);

        assertNotNull(result);
        verify(mapper).updateEntity(req, restaurant);
    }

    @Test
    void delete_Success() {
        when(repo.findById(1L)).thenReturn(Optional.of(restaurant));

        restaurantService.delete(1L);

        assertFalse(restaurant.getActif());
        verify(repo).save(restaurant);
    }
}
