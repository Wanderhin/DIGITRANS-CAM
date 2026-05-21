package com.example.crmbackend.impl;

import com.example.crmbackend.dto.response.FidelisationResponse;
import com.example.crmbackend.entity.Client;
import com.example.crmbackend.entity.ProgrammeFidelite;
import com.example.crmbackend.repository.ClientRepository;
import com.example.crmbackend.repository.ProgrammeFideliteRepository;
import com.example.crmbackend.service.impl.FidelisationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FidelisationServiceImplTest {

    @Mock
    private ProgrammeFideliteRepository repo;
    @Mock
    private ClientRepository clientRepo;

    @InjectMocks
    private FidelisationServiceImpl fidelisationService;

    private Client client;
    private ProgrammeFidelite pf;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);

        pf = new ProgrammeFidelite();
        pf.setId(1L);
        pf.setClient(client);
        pf.setPoints(100);
        pf.setNiveau(ProgrammeFidelite.NiveauFidelite.BRONZE);
    }

    @Test
    void getByClient_Exists_Success() {
        when(repo.findByClientId(1L)).thenReturn(Optional.of(pf));

        FidelisationResponse response = fidelisationService.getByClient(1L);

        assertNotNull(response);
        assertEquals(100, response.getPoints());
    }

    @Test
    void getByClient_NotExists_CreatesNew() {
        when(repo.findByClientId(1L)).thenReturn(Optional.empty());
        when(clientRepo.findById(1L)).thenReturn(Optional.of(client));
        when(repo.save(any())).thenReturn(pf);

        FidelisationResponse response = fidelisationService.getByClient(1L);

        assertNotNull(response);
        verify(repo).save(any());
    }

    @Test
    void updatePoints_Existing_UpdatesLevel() {
        pf.setPoints(400);
        when(repo.findByClientId(1L)).thenReturn(Optional.of(pf));

        fidelisationService.updatePoints(1L, 200);

        assertEquals(600, pf.getPoints());
        assertEquals(ProgrammeFidelite.NiveauFidelite.ARGENT, pf.getNiveau());
        verify(repo).save(pf);
    }
    
    @Test
    void updatePoints_ToPlatine_UpdatesLevel() {
        pf.setPoints(1900);
        when(repo.findByClientId(1L)).thenReturn(Optional.of(pf));

        fidelisationService.updatePoints(1L, 200);

        assertEquals(2100, pf.getPoints());
        assertEquals(ProgrammeFidelite.NiveauFidelite.PLATINE, pf.getNiveau());
        verify(repo).save(pf);
    }
}
