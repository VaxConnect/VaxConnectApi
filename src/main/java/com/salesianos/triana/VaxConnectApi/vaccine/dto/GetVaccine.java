package com.salesianos.triana.VaxConnectApi.vaccine.dto;

import com.salesianos.triana.VaxConnectApi.vaccine.modal.Vacune;

public record GetVaccine(
        String name,
        String description
) {
    public static GetVaccine of (Vacune vacune) {
        return new GetVaccine(
                vacune.getName(),
                vacune.getDescription()
        );
    }
}
