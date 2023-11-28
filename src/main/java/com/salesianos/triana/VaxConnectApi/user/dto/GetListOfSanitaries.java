package com.salesianos.triana.VaxConnectApi.user.dto;

import java.time.LocalDate;

public record GetListOfSanitaries(
                                  String name,
                                  String email,
                                  LocalDate birthDate) {
}
