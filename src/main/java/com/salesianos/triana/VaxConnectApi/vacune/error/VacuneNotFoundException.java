package com.salesianos.triana.VaxConnectApi.vacune.error;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import java.net.URI;
import java.time.Instant;

public class VacuneNotFoundException extends ErrorResponseException {

    public VacuneNotFoundException() {
        super(HttpStatus.NOT_FOUND, of("Vacune not found"), null);
    }

    public static ProblemDetail of(String message) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message);
        problemDetail.setTitle("Patient cant be found");
        problemDetail.setType(URI.create("http://localhost:8080/errors/vacune-not-found"));
        problemDetail.setProperty("entityType", "Note");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
