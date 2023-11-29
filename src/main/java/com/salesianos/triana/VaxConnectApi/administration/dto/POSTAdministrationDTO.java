package com.salesianos.triana.VaxConnectApi.administration.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record POSTAdministrationDTO(

        @NotBlank(message = "email is necessary")
        String userEmail,

        @NotBlank(message = "vacune name is necessary")
        String vaccineName,

        @NotEmpty(message = "a type dosys of the vacune is necessary")
        String typeDosis,

        @NotNull
        String note

) {
}
