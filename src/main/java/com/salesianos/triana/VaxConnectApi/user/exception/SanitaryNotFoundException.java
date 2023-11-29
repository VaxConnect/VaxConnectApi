package com.salesianos.triana.VaxConnectApi.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import java.net.URI;
import java.time.Instant;

public class SanitaryNotFoundException extends ErrorResponseException {
    public SanitaryNotFoundException () {
        super(HttpStatus.NOT_FOUND, of("Sanitary hasn't been found"),null);
    }

    public static ProblemDetail of (String message){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,message);
        problemDetail.setTitle("Sanitary cant be found");
        problemDetail.setType(URI.create("https://api.midominio.com/errors/patient-not-found"));
        problemDetail.setProperty("entityType", "Note");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
