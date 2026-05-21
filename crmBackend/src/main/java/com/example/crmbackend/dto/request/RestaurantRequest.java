package com.example.crmbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RestaurantRequest {
    @NotBlank @Size(max = 150) private String nom;
    @NotBlank @Size(max = 80) private String ville;
    private String adresse;
    private String telephone;
    @NotNull private Boolean actif;
}
