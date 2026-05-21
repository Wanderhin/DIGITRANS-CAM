package com.example.crmbackend.dto.request;

import com.example.crmbackend.entity.Interaction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InteractionRequest {
    @NotNull private Long clientId;
    @NotNull private Interaction.TypeInteraction type;
    @NotBlank private String contenu;
}
