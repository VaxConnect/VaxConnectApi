package com.salesianos.triana.VaxConnectApi.administration.controller;

import com.salesianos.triana.VaxConnectApi.administration.dto.GETLastVaccinesAdministratedDTO;
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
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patient/administration")
public class AdministrationController {

    private final AdministrationService service;


    @GetMapping("/findLastVaccineImplementedByUserId/")
    public ResponseEntity<List<GETLastVaccinesAdministratedDTO>> findLastVaccineImplementedByUserId (@AuthenticationPrincipal Patient patient){
            return ResponseEntity.status(200).body(service.findLastVaccineImplementedByUserId(patient.getId()));
    }

}
