package com.salesianos.triana.VaxConnectApi.calendarmoment.controller;

import com.salesianos.triana.VaxConnectApi.calendarmoment.dto.GETNextVaccinesToAdministrateDTO;
import com.salesianos.triana.VaxConnectApi.calendarmoment.service.CalendarMomentService;
import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patient/calendarmoment")
public class CalendarMomentController {

    private final CalendarMomentService calendarMomentService;

    @GetMapping("/findNextVaccines/")
    public ResponseEntity<List<GETNextVaccinesToAdministrateDTO>> findNextVaccines (@AuthenticationPrincipal Patient patient){
        return ResponseEntity.status(200).body(calendarMomentService.getAllNextVaccinesToAdministrateDTOS(patient));

    }


}
