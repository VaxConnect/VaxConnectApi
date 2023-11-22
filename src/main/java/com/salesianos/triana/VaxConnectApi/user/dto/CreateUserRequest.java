package com.salesianos.triana.VaxConnectApi.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record CreateUserRequest(
        String email,

        String name,

        String lastName,
        String password,

        String verifyPassword,

        String dni,

        int tlfn,

        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate birthDate
) {
}
