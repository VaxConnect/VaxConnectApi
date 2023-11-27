package com.salesianos.triana.VaxConnectApi.administration.dto;

import com.salesianos.triana.VaxConnectApi.calendarmoment.dto.GETVaccinesNotAdministratedDTO;

import java.util.List;
import java.util.UUID;

public record GETPatientCalendarDTO(
        UUID id,
        String fullname,

        String age,

        List<GETAllVaccinesImplementedDTO> allVaccinesImplemented,

        List<GETVaccinesNotAdministratedDTO> vaccinesNotAdministrated

) {
}
