package com.salesianos.triana.VaxConnectApi.user.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record GetVaccinesMoreAdministrated(Long name,
                                           LocalDateTime date) {
}
