package com.salesianos.triana.VaxConnectApi.vacune.service;

import com.salesianos.triana.VaxConnectApi.vacune.dto.GetAllVaccineDto;
import com.salesianos.triana.VaxConnectApi.vacune.repo.VacuneRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class VacuneService {

    private final VacuneRepository vacuneRepository;

    public Page<GetAllVaccineDto> findAllVaccine(Pageable pageable) {
        return vacuneRepository.findAllVaccine(pageable);
    }
}
