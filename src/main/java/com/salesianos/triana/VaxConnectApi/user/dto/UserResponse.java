package com.salesianos.triana.VaxConnectApi.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import com.salesianos.triana.VaxConnectApi.user.modal.Sanitary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserResponse {

    protected String id;
    protected String mail, avatar, fullName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    protected LocalDateTime createdAt;

    public static UserResponse fromUser(Patient patient){
        return UserResponse.builder()
                .id(patient.getId().toString())
                .mail(patient.getEmail())
                .avatar(patient.getFotoUrl())
                .fullName(patient.getName().concat(" "+patient.getLastName()))
                .createdAt(patient.getCreatedAt())
                .build();


    }
    public static UserResponse fromSanitary(Sanitary sanitary){
        return UserResponse.builder()
                .id(sanitary.getId().toString())
                .mail(sanitary.getEmail())
                .avatar(sanitary.getFotoUrl())
                .fullName(sanitary.getName().concat(" "+sanitary.getLastName()))
                .createdAt(sanitary.getCreatedAt())
                .build();
    }

}
