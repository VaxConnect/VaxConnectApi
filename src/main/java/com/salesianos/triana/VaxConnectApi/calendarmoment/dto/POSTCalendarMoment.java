package com.salesianos.triana.VaxConnectApi.calendarmoment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record POSTCalendarMoment(
        @NotBlank(message = "The calendar moment needs a vacune type.")
        String dosysType,

        int age,

        String recomendation,
        String discriminats,

        @NotBlank(message = "The calendar moment needs a vacune.")
        String vacuneName

) {
}
