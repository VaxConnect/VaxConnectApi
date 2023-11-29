package com.salesianos.triana.VaxConnectApi.user.dto;

import java.time.LocalDate;


public record GetListOfSanitaries(String img,
                                  String fullname,
                                  String email,
                                  LocalDate birthDate) {
}
