package com.example.crmbackend.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CommandeRequest {
    @NotNull private Long clientId;
    @NotNull private Long restaurantId;
    @NotEmpty private List<LigneCommandeRequest> lignes;
}
