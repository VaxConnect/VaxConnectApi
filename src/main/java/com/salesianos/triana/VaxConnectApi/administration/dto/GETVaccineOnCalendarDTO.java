package com.salesianos.triana.VaxConnectApi.administration.dto;

import java.util.List;
import java.util.UUID;

public record GETVaccineOnCalendarDTO(

        UUID id,

        String nameVacune,

        List<GetMomentToImplementVacuneDTO> getMomentToImplementVacuneDTOS
) {
}
