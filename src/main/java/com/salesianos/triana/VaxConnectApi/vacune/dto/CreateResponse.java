package com.salesianos.triana.VaxConnectApi.vacune.dto;

import com.salesianos.triana.VaxConnectApi.user.dto.UserResponse;
import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import com.salesianos.triana.VaxConnectApi.vacune.modal.Vacune;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CreateResponse {

    protected String id;
    protected String name, description;

    public static CreateResponse fromVacune(Vacune vacune){
        return CreateResponse.builder()
                .id(vacune.getId().toString())
                .name(vacune.getName())
                .description(vacune.getDescription())
                .build();


    }
}
