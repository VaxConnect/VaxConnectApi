package com.salesianos.triana.VaxConnectApi.vacune.dto;

import java.util.UUID;

public record GetAllVaccineDto(
        UUID id,
        String name,
        String description
) {
}
