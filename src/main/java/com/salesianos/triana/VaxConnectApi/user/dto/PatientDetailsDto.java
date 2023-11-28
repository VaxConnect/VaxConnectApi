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
        String phoneNumber,
        String fotoUrl
) {
    public static PatientDetailsDto of (Patient p)  {

        return new PatientDetailsDto(
                p.getId(),
                p.getName(),
                p.getLastName(),
                p.getBirthDate(),
                p.getDni(),
                p.getEmail(),
                p.getPhoneNumber(),
                p.getFotoUrl()
        );
    }
}
