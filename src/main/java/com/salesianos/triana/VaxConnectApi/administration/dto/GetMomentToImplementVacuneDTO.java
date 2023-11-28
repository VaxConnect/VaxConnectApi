package com.salesianos.triana.VaxConnectApi.administration.dto;

import java.time.LocalDate;
import java.util.UUID;

public record GetMomentToImplementVacuneDTO(
        UUID id,

        int monthToImplement,

        String typeDosis,

        boolean isImplemented

) {
}
