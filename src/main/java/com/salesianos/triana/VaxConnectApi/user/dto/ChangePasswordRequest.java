package com.salesianos.triana.VaxConnectApi.user.dto;

public record ChangePasswordRequest(
        //EXTRA
        String oldPassword,
        String newPassword,
        String verifyNewPassword

) {
}
