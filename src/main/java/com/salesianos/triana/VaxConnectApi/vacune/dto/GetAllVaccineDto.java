package com.salesianos.triana.VaxConnectApi.vacune.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.UUID;

public record GetAllVaccineDto(
        @NotBlank(message = "The id must be granted")
        UUID id,
        @NotEmpty(message = "the name must be provided")
        String name,
        String description
) {
}
