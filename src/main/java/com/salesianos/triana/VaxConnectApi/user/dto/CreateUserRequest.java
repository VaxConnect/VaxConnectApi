package com.salesianos.triana.VaxConnectApi.user.dto;

public record CreateUserRequest(
        String email,

        String name,

        String lastName,
        String password,

        String verifyPassword
) {
}
