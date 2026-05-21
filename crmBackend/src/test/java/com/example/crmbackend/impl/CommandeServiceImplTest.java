package com.example.crmbackend.impl;

import com.example.crmbackend.dto.request.CommandeRequest;
import com.example.crmbackend.dto.request.LigneCommandeRequest;
import com.example.crmbackend.dto.response.CommandeResponse;
import com.example.crmbackend.entity.Client;
import com.example.crmbackend.entity.Commande;
import com.example.crmbackend.entity.LigneCommande;
import com.example.crmbackend.entity.Restaurant;
import com.example.crmbackend.exception.ResourceNotFoundException;
import com.example.crmbackend.mapper.CommandeMapper;
import com.example.crmbackend.repository.ClientRepository;
import com.example.crmbackend.repository.CommandeRepository;
import com.example.crmbackend.repository.RestaurantRepository;
import com.example.crmbackend.service.impl.CommandeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommandeServiceImplTest {

    @Mock
    private CommandeRepository repo;
    @Mock
    private ClientRepository clientRepo;
    @Mock
    private RestaurantRepository restaurantRepo;
    @Mock
    private CommandeMapper mapper;

    @InjectMocks
    private CommandeServiceImpl commandeService;

    private Commande commande;
    private CommandeRequest req;
    private CommandeResponse res;
    private Client client;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);

        restaurant = new Restaurant();
        restaurant.setId(1L);

        commande = new Commande();
        commande.setId(1L);
        commande.setClient(client);

        LigneCommandeRequest ligneReq = new LigneCommandeRequest();
        ligneReq.setQuantite(2);

        req = new CommandeRequest();
        req.setClientId(1L);
        req.setRestaurantId(1L);
        req.setLignes(List.of(ligneReq));

        res = new CommandeResponse();
        res.setId(1L);
    }

    @Test
    void list_Success() {
        when(repo.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(commande)));
        when(mapper.toResponse(any(Commande.class))).thenReturn(res);

        Page<CommandeResponse> result = commandeService.list(null, PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void listByStatut_Success() {
        when(repo.findByStatut(any(), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(commande)));
        when(mapper.toResponse(any(Commande.class))).thenReturn(res);

        Page<CommandeResponse> result = commandeService.list(Commande.StatutCommande.EN_COURS, PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getById_Success() {
        when(repo.findById(1L)).thenReturn(Optional.of(commande));
        when(mapper.toResponse(commande)).thenReturn(res);

        CommandeResponse result = commandeService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getById_NotFound_ThrowsException() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> commandeService.getById(1L));
    }

    @Test
    void create_Success() {
        when(clientRepo.findById(1L)).thenReturn(Optional.of(client));
        when(restaurantRepo.findById(1L)).thenReturn(Optional.of(restaurant));
        
        LigneCommande ligne = new LigneCommande();
        ligne.setPrixUnitaire(BigDecimal.valueOf(10));
        ligne.setQuantite(2);
        when(mapper.toEntity(any(LigneCommandeRequest.class))).thenReturn(ligne);
        
        when(repo.save(any())).thenReturn(commande);
        when(mapper.toResponse(any(Commande.class))).thenReturn(res);

        CommandeResponse result = commandeService.create(req);

        assertNotNull(result);
        verify(repo).save(any(Commande.class));
    }

    @Test
    void create_ClientNotFound_ThrowsException() {
        when(clientRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> commandeService.create(req));
    }
}
