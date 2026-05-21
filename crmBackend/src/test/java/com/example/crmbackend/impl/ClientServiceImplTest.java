package com.example.crmbackend.impl;

import com.example.crmbackend.dto.request.ClientRequest;
import com.example.crmbackend.dto.response.ClientResponse;
import com.example.crmbackend.entity.Client;
import com.example.crmbackend.exception.BusinessException;
import com.example.crmbackend.exception.ResourceNotFoundException;
import com.example.crmbackend.mapper.ClientMapper;
import com.example.crmbackend.repository.ClientRepository;
import com.example.crmbackend.service.impl.ClientServiceImpl;
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
class ClientServiceImplTest {

    @Mock
    private ClientRepository repo;
    @Mock
    private ClientMapper mapper;

    @InjectMocks
    private ClientServiceImpl clientService;

    private Client client;
    private ClientRequest req;
    private ClientResponse res;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);
        client.setEmail("client@test.com");
        client.setActif(true);

        req = new ClientRequest();
        req.setEmail("client@test.com");

        res = new ClientResponse();
        res.setId(1L);
        res.setEmail("client@test.com");
    }

    @Test
    void search_Success() {
        Page<Client> page = new PageImpl<>(List.of(client));
        when(repo.search(any(), any(), any(), any())).thenReturn(page);
        when(mapper.toResponse(client)).thenReturn(res);

        Page<ClientResponse> result = clientService.search(null, null, null, PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getById_Success() {
        when(repo.findById(1L)).thenReturn(Optional.of(client));
        when(mapper.toResponse(client)).thenReturn(res);

        ClientResponse result = clientService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getById_NotFound_ThrowsException() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientService.getById(1L));
    }

    @Test
    void create_Success() {
        when(repo.existsByEmail(req.getEmail())).thenReturn(false);
        when(mapper.toEntity(req)).thenReturn(client);
        when(repo.save(any())).thenReturn(client);
        when(mapper.toResponse(client)).thenReturn(res);

        ClientResponse result = clientService.create(req);

        assertNotNull(result);
        assertEquals("client@test.com", result.getEmail());
    }

    @Test
    void create_EmailExists_ThrowsException() {
        when(repo.existsByEmail(req.getEmail())).thenReturn(true);

        assertThrows(BusinessException.class, () -> clientService.create(req));
    }

    @Test
    void update_Success() {
        when(repo.findById(1L)).thenReturn(Optional.of(client));
        when(repo.save(client)).thenReturn(client);
        when(mapper.toResponse(client)).thenReturn(res);

        ClientResponse result = clientService.update(1L, req);

        assertNotNull(result);
        verify(mapper).updateEntity(req, client);
    }

    @Test
    void delete_Success() {
        when(repo.findById(1L)).thenReturn(Optional.of(client));

        clientService.delete(1L);

        assertFalse(client.getActif());
        verify(repo).save(client);
    }
}
