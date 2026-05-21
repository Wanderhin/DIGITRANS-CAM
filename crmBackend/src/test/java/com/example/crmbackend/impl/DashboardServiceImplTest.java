package com.example.crmbackend.impl;

import com.example.crmbackend.dto.response.DashboardResponse;
import com.example.crmbackend.mapper.ClientMapper;
import com.example.crmbackend.repository.ClientRepository;
import com.example.crmbackend.repository.CommandeRepository;
import com.example.crmbackend.service.impl.DashboardServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceImplTest {

    @Mock
    private CommandeRepository commandeRepo;
    @Mock
    private ClientRepository clientRepo;
    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    @Test
    void getStats_Success() {
        when(commandeRepo.count()).thenReturn(100L);
        when(clientRepo.count()).thenReturn(50L);

        DashboardResponse response = dashboardService.getStats();

        assertNotNull(response);
        assertEquals(100L, response.getNbCommandes());
        assertEquals(50L, response.getNouveauxClients());
        assertEquals(BigDecimal.valueOf(1500000), response.getCaMois());
    }
}
