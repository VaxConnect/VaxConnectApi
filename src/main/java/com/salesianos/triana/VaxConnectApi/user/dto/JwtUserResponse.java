package com.salesianos.triana.VaxConnectApi.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import com.salesianos.triana.VaxConnectApi.user.modal.Sanitary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtUserResponse extends UserResponse{

    private String token;

    public JwtUserResponse(UserResponse userResponse){

        id = userResponse.getId();
        mail = userResponse.getMail();
        fullName = userResponse.getFullName();
        avatar = userResponse.getAvatar();
        createdAt = userResponse.getCreatedAt();

    }

    public static JwtUserResponse of (Patient patient, String token){
        JwtUserResponse result= new JwtUserResponse(UserResponse.fromUser(patient));
        result.setToken(token);
        return result;

    }
    public static JwtUserResponse ofSanitary (Sanitary sanitary, String token){
        JwtUserResponse response = new JwtUserResponse(UserResponse.fromSanitary(sanitary));
        response.setToken(token);
        return response;
    }

}
