package com.salesianos.triana.VaxConnectApi.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CreatePatientDto(


        String name,
        String lastName,
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate birthDate,
        String dni,
        String email,
        String phoneNumber,
        String fotoUrl,
        String password,
        List<String> dependents
) {

}
