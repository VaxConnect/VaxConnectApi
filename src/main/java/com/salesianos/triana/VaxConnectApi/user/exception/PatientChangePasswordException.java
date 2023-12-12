package com.salesianos.triana.VaxConnectApi.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import java.net.URI;
import java.time.Instant;

public class PatientChangePasswordException extends ErrorResponseException {

    public PatientChangePasswordException() {
        super(HttpStatus.BAD_REQUEST, of("Something go wrong"), null);
    }

    public static ProblemDetail of(String message) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);
        problemDetail.setTitle("Something go wrong");
        problemDetail.setProperty("entityType", "Patient");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
