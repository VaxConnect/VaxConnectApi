package com.salesianos.triana.VaxConnectApi.user.dto;

import java.util.UUID;

public record GetPatientByIdDto(
        UUID id,
        String name,
        String lastName
){
}
