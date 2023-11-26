package com.salesianos.triana.VaxConnectApi.vacune.controller;

import com.salesianos.triana.VaxConnectApi.vacune.dto.GetAllVaccineDto;
import com.salesianos.triana.VaxConnectApi.vacune.repo.VacuneRepository;
import com.salesianos.triana.VaxConnectApi.vacune.service.VacuneService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/vacune")
public class VacuneController {

    private final VacuneService vacuneService;

    @GetMapping("/all")
    private ResponseEntity<Page<GetAllVaccineDto>> getAllVaccine(@PageableDefault(page=0, size=10)Pageable pageable) {
        Page<GetAllVaccineDto> pagedResult = vacuneService.findAllVaccine(pageable);

        if(pagedResult.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(pagedResult);
    }
}
