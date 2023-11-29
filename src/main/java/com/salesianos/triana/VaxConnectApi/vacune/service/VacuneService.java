package com.salesianos.triana.VaxConnectApi.vacune.service;

import com.salesianos.triana.VaxConnectApi.vacune.dto.CreateVacuneDto;
import com.salesianos.triana.VaxConnectApi.vacune.dto.GetAllVaccineDto;
import com.salesianos.triana.VaxConnectApi.vacune.modal.Vacune;
import com.salesianos.triana.VaxConnectApi.vacune.repo.VacuneRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class VacuneService {

    private final VacuneRepository vacuneRepository;

    public Optional<Vacune> findByName(String name){
        return vacuneRepository.findByName(name);
    }

    public Page<GetAllVaccineDto> findAllVaccine(Pageable pageable) {
        return vacuneRepository.findAllVaccine(pageable);
    }

    public List<Vacune> countVaccines(){
        return vacuneRepository.findAll();
    }

    public Page<GetAllVaccineDto> findVaccineBySearchParameter(Pageable pageable, String name) {
        return vacuneRepository.findVaccineBySearchParameter(pageable, name);
    }

    public ResponseEntity<GetAllVaccineDto> findVacuneById(UUID id) {
        if(vacuneRepository.existsById(id))
            return ResponseEntity.of(vacuneRepository.findVacuneById(id));
        else
            return ResponseEntity.notFound().build();
    }

    public ResponseEntity<GetAllVaccineDto> createVacune(CreateVacuneDto create) {
        Vacune vacune = Vacune.builder()
                .name(create.name())
                .description(create.description())
                .build();
        vacuneRepository.save(vacune);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Optional<GetAllVaccineDto>> editVacune(UUID id, CreateVacuneDto edit) {
        if(vacuneRepository.existsById(id)) {
            Optional<Vacune> result = vacuneRepository.findById(id);

            result.get().setName(edit.name());
            result.get().setDescription(edit.description());

            vacuneRepository.save(result.get());


            return ResponseEntity.ok(vacuneRepository.findVacuneById(id));
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    public ResponseEntity<GetAllVaccineDto> deleteVacune(UUID id) {
        if(vacuneRepository.existsById(id)) {
            Optional<Vacune> result = vacuneRepository.findById(id);
            vacuneRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
