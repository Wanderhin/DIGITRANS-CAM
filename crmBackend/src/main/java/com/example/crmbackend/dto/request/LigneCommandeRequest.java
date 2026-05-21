package com.example.crmbackend.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LigneCommandeRequest {
    @NotBlank private String libelleProduit;
    @NotNull @Min(1) private Integer quantite;
    @NotNull @DecimalMin("0.0") private BigDecimal prixUnitaire;
}
