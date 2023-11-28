package com.salesianos.triana.VaxConnectApi.calendarmoment.service;

import com.salesianos.triana.VaxConnectApi.administration.service.AdministrationService;
import com.salesianos.triana.VaxConnectApi.calendarmoment.dto.GETNextVaccinesToAdministrateDTO;
import com.salesianos.triana.VaxConnectApi.calendarmoment.dto.GETVaccinesNotAdministratedDTO;
import com.salesianos.triana.VaxConnectApi.calendarmoment.repo.CalendarMomentRepository;
import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import com.salesianos.triana.VaxConnectApi.user.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarMomentService {

    private final PatientService patientService;
    private final CalendarMomentRepository repository;
    private final AdministrationService administrationService;

    public List<UUID> findAllIdOfCalendarsMoments(){
        return repository.findAllIdOfCalendarMoments();
    }

    public List<GETNextVaccinesToAdministrateDTO> getAllNextVaccinesToAdministrateDTOS (String email){

        List<GETNextVaccinesToAdministrateDTO> list = getNextVaccinesToAdministrateDTOS(email);
        Optional<Patient>patient = patientService.findByEmail(email);

        if(!patient.get().getDependients().isEmpty()){
            Optional<List<String>>listDepEmails = patientService.findAllDependentsEmailByResponsableUUID(email);
            for (String depEmail:listDepEmails.get()) {
                list.addAll(getNextVaccinesToAdministrateDTOS(depEmail));
            }

        }
            return list;



    }

    public List<GETNextVaccinesToAdministrateDTO> getNextVaccinesToAdministrateDTOS(String email){
        Optional<Patient> patient = patientService.findByEmail(email);
        if(patient.isPresent()){
                List<UUID> idsFromCm = administrationService.getIdOfCalendarMomentNotAdministrated(email);
                List<GETNextVaccinesToAdministrateDTO> list = new ArrayList<>();
            for (UUID id :idsFromCm) {
                list.add(repository.getNextVaccinesToAdministrateDTOFromCmId(id,patient.get().getEmail()));
            }
            return list.stream().filter(x-> x.month()==(ChronoUnit.MONTHS.between(patient.get().getBirthDate(),LocalDate.now())+1) ).collect(Collectors.toList());
        }else{
            return null;
        }

    }

    //In the method of the top has a function similar with the bottom method, but I want to try both and compare it.

    public List<GETVaccinesNotAdministratedDTO> getAllVaccinesNotImplemented(UUID uuid){

        Optional<Patient> patient = patientService.findById(uuid);

        if(patient.isPresent()){
            List<GETVaccinesNotAdministratedDTO> list = repository
                    .getAllGetVaccinesNotAdministratedDTO(
                            administrationService.getIdOfCalendarMomentNotAdministrated(patient.get().getEmail()
                            ));
            return list;

        }else{
            return null;
        }


    }

}
