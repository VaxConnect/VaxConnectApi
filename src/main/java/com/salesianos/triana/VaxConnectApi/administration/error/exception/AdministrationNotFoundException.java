package com.salesianos.triana.VaxConnectApi.administration.error.exception;

import jakarta.persistence.EntityNotFoundException;

public class AdministrationNotFoundException extends EntityNotFoundException {

    public AdministrationNotFoundException(){
        super("The Administration was not found");
    }

    public AdministrationNotFoundException(Long id){
        super(String.format("The Administration with id %d could not be found",id));
    }

}
