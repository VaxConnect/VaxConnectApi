package com.salesianos.triana.VaxConnectApi.user.dto;

import java.time.LocalDate;

public record GetSanitaryByEmail(String img,
                                 String fullname,
                                 String email,
                                 String phoneNumber,
                                 String dni,
                                 LocalDate birthDate) {
}
