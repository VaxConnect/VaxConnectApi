package com.salesianos.triana.VaxConnectApi.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CreatePatientDto(

        @NotBlank(message = "Name is necessary")
        String name,
        @NotEmpty(message = "Last name cant be empty")
        String lastName,
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate birthDate,
        @NotBlank(message = "Dni is necessary")
        String dni,
        @Email(message = "Se debe introducir un email")
        @NotBlank(message = "Email is necessary")
        String email,
        String phoneNumber,
        String fotoUrl,
        String password,
        List<String> dependents
) {

}
