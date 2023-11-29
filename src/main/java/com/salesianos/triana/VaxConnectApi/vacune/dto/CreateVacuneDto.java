package com.salesianos.triana.VaxConnectApi.vacune.dto;

import jakarta.validation.constraints.NotEmpty;

public record CreateVacuneDto(
        @NotEmpty(message = "the name must be provided")
        String name,
        String description
) {
}
