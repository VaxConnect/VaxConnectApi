package com.salesianos.triana.VaxConnectApi.user.dto;

import com.salesianos.triana.VaxConnectApi.user.modal.Patient;

import java.time.LocalDate;
import java.util.UUID;

public record PatientDetailsDto(
        UUID id,
        String name,
        String lastName,
        LocalDate birthDate,
        String dni,
        String email,
        int phoneNumber,
        String fotoUrl
) {
}
