package com.salesianos.triana.VaxConnectApi.calendarmoment.dto;

import java.util.UUID;

public record GETVaccinesNotAdministratedDTO(
        UUID id,
        String vacuneName,

        int age
) {
}
