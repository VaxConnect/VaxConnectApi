package com.salesianos.triana.VaxConnectApi.administration.controller;

import com.salesianos.triana.VaxConnectApi.administration.service.AdministrationService;
import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patient/administration")
public class AdministrationController {

    private final AdministrationService service;

    /*
    * I wll continue asking for the permision error that trows this -> {
    "status": "UNAUTHORIZED",
    "message": "Full authentication is required to access this resource",
    "path": "/patient/administration/findLastVaccineImplementedByUserId",
    "dateTime": "23/11/2023 10:32:29"
    } now I'm focus on error and validation implementation.
    * */
    @GetMapping("/findLastVaccineImplementedByUserId/")
    public ResponseEntity<?> findLastVaccineImplementedByUserId (@AuthenticationPrincipal Patient patient){
            return ResponseEntity.status(200).body(service.findLastVaccineImplementedByUserId(patient.getId().toString()));
    }

}
