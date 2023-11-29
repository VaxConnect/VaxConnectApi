package com.salesianos.triana.VaxConnectApi.user.dto;

import java.time.LocalDate;
import java.util.UUID;


public record GetListOfSanitaries(UUID uuid,
                                  String img,
                                  String fullname,
                                  String email,
                                  LocalDate birthDate) {
}
