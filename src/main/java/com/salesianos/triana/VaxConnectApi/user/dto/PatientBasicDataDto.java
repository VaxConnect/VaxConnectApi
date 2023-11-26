package com.salesianos.triana.VaxConnectApi.user.dto;

import java.time.LocalDate;
import java.util.UUID;

public record PatientBasicDataDto(
        UUID id,
        String name,
        String lastName,
        LocalDate birthDate,
        String dni,
        String email
){
}
