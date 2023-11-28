package com.salesianos.triana.VaxConnectApi.user.dto;

import java.time.LocalDate;

public record GETUserProfileDetails(
        String fullName,
        String emai,
        String dni,

        LocalDate birthDate,

        String tlfn,

        String urlFoto

) {
}
