package com.example.crmbackend.mapper;

import com.example.crmbackend.dto.response.InteractionResponse;
import com.example.crmbackend.entity.Interaction;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface InteractionMapper {
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "user.id", target = "userId")
    InteractionResponse toResponse(Interaction i);

    List<InteractionResponse> toResponseList(List<Interaction> list);
}
