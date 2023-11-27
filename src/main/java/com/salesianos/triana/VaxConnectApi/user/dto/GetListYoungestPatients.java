package com.salesianos.triana.VaxConnectApi.user.dto;

import java.time.LocalDate;

public record GetListYoungestPatients(
        String name,
        String lastName,
        LocalDate birthDate
) {

}
