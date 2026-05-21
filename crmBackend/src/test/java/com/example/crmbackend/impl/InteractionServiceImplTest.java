package com.example.crmbackend.impl;

import com.example.crmbackend.dto.request.InteractionRequest;
import com.example.crmbackend.dto.response.InteractionResponse;
import com.example.crmbackend.entity.Client;
import com.example.crmbackend.entity.Interaction;
import com.example.crmbackend.entity.User;
import com.example.crmbackend.exception.ResourceNotFoundException;
import com.example.crmbackend.mapper.InteractionMapper;
import com.example.crmbackend.repository.ClientRepository;
import com.example.crmbackend.repository.InteractionRepository;
import com.example.crmbackend.repository.UserRepository;
import com.example.crmbackend.service.impl.InteractionServiceImpl;
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
class InteractionServiceImplTest {

    @Mock
    private InteractionRepository repo;
    @Mock
    private ClientRepository clientRepo;
    @Mock
    private UserRepository userRepo;
    @Mock
    private InteractionMapper mapper;

    @InjectMocks
    private InteractionServiceImpl interactionService;

    private Client client;
    private User user;
    private Interaction interaction;
    private InteractionRequest req;
    private InteractionResponse res;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);

        user = new User();
        user.setEmail("admin@test.com");

        interaction = new Interaction();
        interaction.setId(1L);

        req = new InteractionRequest();
        req.setClientId(1L);
        req.setType(Interaction.TypeInteraction.APPEL);
        req.setContenu("Test content");

        res = new InteractionResponse();
        res.setId(1L);
    }

    @Test
    void listByClient_Success() {
        when(repo.findByClientId(any(), any())).thenReturn(new PageImpl<>(List.of(interaction)));
        when(mapper.toResponse(any())).thenReturn(res);

        Page<InteractionResponse> result = interactionService.listByClient(1L, PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void create_Success() {
        when(clientRepo.findById(1L)).thenReturn(Optional.of(client));
        when(userRepo.findByEmail("admin@test.com")).thenReturn(Optional.of(user));
        when(repo.save(any(Interaction.class))).thenReturn(interaction);
        when(mapper.toResponse(any())).thenReturn(res);

        InteractionResponse result = interactionService.create(req, "admin@test.com");

        assertNotNull(result);
        verify(repo).save(any(Interaction.class));
    }

    @Test
    void create_ClientNotFound_ThrowsException() {
        when(clientRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> interactionService.create(req, "admin@test.com"));
    }
}
