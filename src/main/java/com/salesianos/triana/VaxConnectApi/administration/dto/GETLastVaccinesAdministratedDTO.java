package com.salesianos.triana.VaxConnectApi.administration.dto;

import java.time.LocalDateTime;

public record GETLastVaccinesAdministratedDTO(

        String completeName,

        String vaccineType,

        String timeOfImplementation

) {
}
