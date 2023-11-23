package com.salesianos.triana.VaxConnectApi.calendarmoment.service;

import com.salesianos.triana.VaxConnectApi.administration.service.AdministrationService;
import com.salesianos.triana.VaxConnectApi.calendarmoment.dto.GETNextVaccinesToAdministrateDTO;
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

    public List<GETNextVaccinesToAdministrateDTO> getAllNextVaccinesToAdministrateDTOS (Patient patient){


        List<GETNextVaccinesToAdministrateDTO> list = getNextVaccinesToAdministrateDTOS(patient.getEmail());
        if(!patient.getDependients().isEmpty()){
            Optional<List<String>>listDepEmails = patientService.findAllDependentsUUIDByResponsableUUID(patient.getEmail());
            for (String email:listDepEmails.get()) {
                list.addAll(getNextVaccinesToAdministrateDTOS(email));
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
            String monthToreveal = "month("+ChronoUnit.MONTHS.between(patient.get().getBirthDate(),LocalDate.now())+1 +")";
            return list.stream().filter(x-> x.vaccineType().contains(monthToreveal)).collect(Collectors.toList());

        }else{
            return null;
        }

    }

}
