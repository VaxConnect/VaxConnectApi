package com.salesianos.triana.VaxConnectApi.user.dto;

import com.salesianos.triana.VaxConnectApi.user.modal.Patient;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record PatientDetailsWithDependentsDto(
        UUID id,
        String name,
        String lastName,
        LocalDate birthDate,
        String dni,
        String email,
        String phoneNumber,
        String fotoUrl,
        List<PatientDetailsDto> dependents
) {
    public static PatientDetailsWithDependentsDto of (Patient p)  {

        return new PatientDetailsWithDependentsDto(
                p.getId(),
                p.getName(),
                p.getLastName(),
                p.getBirthDate(),
                p.getDni(),
                p.getEmail(),
                p.getPhoneNumber(),
                p.getFotoUrl(),
                p.getDependients()
                        .stream()
                        .map(PatientDetailsDto::of)
                        .toList()
        );
    }
}
