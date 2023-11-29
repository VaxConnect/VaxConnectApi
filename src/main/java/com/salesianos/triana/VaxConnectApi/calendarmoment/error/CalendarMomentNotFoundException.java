package com.salesianos.triana.VaxConnectApi.calendarmoment.error;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.time.Instant;


public class CalendarMomentNotFoundException extends EntityNotFoundException {

    public CalendarMomentNotFoundException(String message) {
        super(message);
    }



}

