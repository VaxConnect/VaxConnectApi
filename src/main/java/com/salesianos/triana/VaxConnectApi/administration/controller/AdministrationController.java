package com.salesianos.triana.VaxConnectApi.administration.controller;

import com.salesianos.triana.VaxConnectApi.administration.dto.GETVaccineAdministratedOnCalendar;
import com.salesianos.triana.VaxConnectApi.administration.dto.GETLastVaccinesAdministratedDTO;
import com.salesianos.triana.VaxConnectApi.administration.dto.GETPatientCalendarDTO;
import com.salesianos.triana.VaxConnectApi.administration.dto.POSTAdministrationDTO;
import com.salesianos.triana.VaxConnectApi.administration.service.AdministrationService;
import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/findAllImplementedVaccines/")
    public ResponseEntity<List<GETVaccineAdministratedOnCalendar>> findAllAdministration (@AuthenticationPrincipal Patient patient){
        return ResponseEntity.status(200).body(service.getAllVaccinesImplementedDTO(patient.getId()));
    }

    @GetMapping("/getCalendar/")
    public ResponseEntity<List<GETPatientCalendarDTO>> getCalendar(@AuthenticationPrincipal Patient patient){
        return ResponseEntity.status(200).body(service.getFamilyCalendar(patient.getId()));
    }

}
