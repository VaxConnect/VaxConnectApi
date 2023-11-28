package com.salesianos.triana.VaxConnectApi.administration.dto;

import java.util.UUID;

public record GETVaccineAdministratedOnCalendar(

        UUID id,

        String nameVacune,

        String typeOfDosys,

        int month
) {
}
