package com.salesianos.triana.VaxConnectApi.administration.dto;

import java.util.List;
import java.util.UUID;

public record GETPatientCalendarDTO(
        UUID id,
        String fullname,

        String age,

        List<GETVaccineOnCalendarDTO> allVaccine


) {
}
