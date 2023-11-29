package com.salesianos.triana.VaxConnectApi.vacune.error;

import jakarta.persistence.EntityNotFoundException;

public class VacuneNotFoundException extends EntityNotFoundException {


    public VacuneNotFoundException(String string) {
        super(string);
    }
}
