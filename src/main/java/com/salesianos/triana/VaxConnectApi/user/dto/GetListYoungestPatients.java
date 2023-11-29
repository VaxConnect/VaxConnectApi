package com.salesianos.triana.VaxConnectApi.user.dto;

import java.time.LocalDate;

public record GetListYoungestPatients(
        String fullName,
        LocalDate birthDate
) {

}
