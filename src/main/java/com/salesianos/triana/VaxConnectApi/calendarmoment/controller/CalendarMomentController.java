package com.salesianos.triana.VaxConnectApi.calendarmoment.controller;

import com.salesianos.triana.VaxConnectApi.calendarmoment.dto.GETNextVaccinesToAdministrateDTO;
import com.salesianos.triana.VaxConnectApi.calendarmoment.dto.GETVaccinesNotAdministratedDTO;
import com.salesianos.triana.VaxConnectApi.calendarmoment.dto.POSTCalendarMoment;
import com.salesianos.triana.VaxConnectApi.calendarmoment.service.CalendarMomentService;
import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patient/calendarmoment")
public class CalendarMomentController {

    private final CalendarMomentService calendarMomentService;



    @GetMapping("/findNextVaccines/")
    public ResponseEntity<List<GETNextVaccinesToAdministrateDTO>> findNextVaccines (@AuthenticationPrincipal Patient patient){
        return ResponseEntity.status(200).body(calendarMomentService.getAllNextVaccinesToAdministrateDTOS(patient.getEmail()));

    }
    @GetMapping("/findVaccinesNotAdministrated/")
    public ResponseEntity<List<GETVaccinesNotAdministratedDTO>> findVaccinesNotAdministrated(@AuthenticationPrincipal Patient patient){
        return ResponseEntity.status(200).body(calendarMomentService.getAllVaccinesNotImplemented(patient.getId()));
    }


}
