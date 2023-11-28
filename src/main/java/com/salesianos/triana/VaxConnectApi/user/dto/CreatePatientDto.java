package com.salesianos.triana.VaxConnectApi.user.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CreatePatientDto(
        String name,
        String lastName,
        LocalDate birthDate,
        String dni,
        String email,
        int phoneNumber,
        String fotoUrl
) {
}
