package com.salesianos.triana.VaxConnectApi.administration.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record GETAllVaccinesImplementedDTO(

        UUID id,

        String nameVacune,

        int month
) {
}
