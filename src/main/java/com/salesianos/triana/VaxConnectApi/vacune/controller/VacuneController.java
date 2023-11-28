package com.salesianos.triana.VaxConnectApi.vacune.controller;

import com.salesianos.triana.VaxConnectApi.vacune.dto.CreateVacuneDto;
import com.salesianos.triana.VaxConnectApi.vacune.dto.GetAllVaccineDto;
import com.salesianos.triana.VaxConnectApi.vacune.repo.VacuneRepository;
import com.salesianos.triana.VaxConnectApi.vacune.service.VacuneService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/vacune")
public class VacuneController {

    private final VacuneRepository vacuneRepository;
    private final VacuneService vacuneService;

    @GetMapping("/all")
    private ResponseEntity<Page<GetAllVaccineDto>> getAllVaccine(@PageableDefault(page=0, size=10)Pageable pageable) {
        Page<GetAllVaccineDto> pagedResult = vacuneService.findAllVaccine(pageable);

        if(pagedResult.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(pagedResult);
    }

    @GetMapping("/info/{id}")
    private ResponseEntity<GetAllVaccineDto> getVacuneById(@PathVariable String id) {
        UUID idValido = UUID.fromString(id);
        return vacuneService.findVacuneById(idValido);
    }

    @GetMapping("/search/{name}")
    private ResponseEntity<Page<GetAllVaccineDto>> getVaccineBySearchParameter(@PageableDefault(page=0, size=10)Pageable pageable,
                                                                               @PathVariable String name) {
        String fullString = UriUtils.decode(name, "UTF-8");
        fullString = fullString.replace("%20", " ");
        return ResponseEntity.ok(vacuneService.findVaccineBySearchParameter(pageable, fullString));
    }

    @PostMapping("/new")
    private ResponseEntity<GetAllVaccineDto> createVacune(@RequestBody CreateVacuneDto create) {
        return vacuneService.createVacune(create);
    }

    @PutMapping("/edit/{id}")
    private ResponseEntity<Optional<GetAllVaccineDto>> EditVacune(@PathVariable String id, @RequestBody CreateVacuneDto edit) {
    return vacuneService.editVacune(UUID.fromString(id), edit);
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<GetAllVaccineDto> DeleteVacune(@PathVariable String id) {
        return vacuneService.deleteVacune(UUID.fromString(id));
    }
}
