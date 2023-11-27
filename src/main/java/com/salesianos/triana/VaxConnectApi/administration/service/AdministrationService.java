package com.salesianos.triana.VaxConnectApi.administration.service;

import com.salesianos.triana.VaxConnectApi.administration.dto.GETAllVaccinesImplementedDTO;
import com.salesianos.triana.VaxConnectApi.administration.dto.GETLastVaccinesAdministratedDTO;
import com.salesianos.triana.VaxConnectApi.administration.dto.GETPatientCalendarDTO;
import com.salesianos.triana.VaxConnectApi.administration.repo.AdministrationRepository;
import com.salesianos.triana.VaxConnectApi.calendarmoment.service.CalendarMomentService;
import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import com.salesianos.triana.VaxConnectApi.user.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdministrationService {

    private final AdministrationRepository repo;
    private final PatientService patientService;
    private final CalendarMomentService calendarMomentService;

    @Autowired
    public AdministrationService(@Lazy CalendarMomentService calendarMomentService, @Lazy PatientService patientService, @Lazy AdministrationRepository repo) {
        this.calendarMomentService = calendarMomentService;
        this.repo = repo;
        this.patientService = patientService;
    }



    public List<GETLastVaccinesAdministratedDTO> findLastVaccineImplementedByUserId (UUID userID){

        Optional<Patient> patient = patientService.findById(userID);
        if(patient.isPresent()){
            if(!patient.get().getDependients().isEmpty()){
                return findAllLastVaccineImplementedByUserId(patient.get());
            }else{
                List<GETLastVaccinesAdministratedDTO> list =repo.findLastVaccineImplementedByUsermail(
                        patient.get().getEmail())
                        .stream()
                        .sorted(
                                (x,y)->y.timeOfImplementation().compareTo(x.timeOfImplementation())
                        )
                        .toList()
                        .subList(0,4);

                    return list;


            }
        }
        return null;

    }

    public List<GETLastVaccinesAdministratedDTO> findAllLastVaccineImplementedByUserId(Patient patient) {

        Optional<List<String>> listaEmail = patientService.findAllDependentsEmailByResponsableUUID(patient.getEmail());
        List<GETLastVaccinesAdministratedDTO> list = repo.findLastVaccineImplementedByUsermail(patient.getEmail());

        if(listaEmail.isPresent()){
            for (String dependientUuid:listaEmail.get()) {
                list.addAll(repo.findLastVaccineImplementedByUsermail(dependientUuid));
            }
            return list.stream()
                    .sorted(
                            (x,y)->y.timeOfImplementation().compareTo(x.timeOfImplementation())
                    ).collect(Collectors.toList())
                    .subList(0,4);



        }else{
            //make an error that tell U that there is something wrong in the code
            return null;
        }
    }

    public List<UUID> getIdOfCalendarMomentNotAdministrated(String email){

        List<UUID> cmAdminIds =repo.findIdsOfCalendarsMomentsWhoAreImplementedByPatientEmail(email);
        List<UUID> allCmIds = calendarMomentService.findAllIdOfCalendarsMoments();

        allCmIds.removeAll(cmAdminIds);

        return allCmIds;

    }

    public List<GETAllVaccinesImplementedDTO> getAllVaccinesImplementedDTO  (UUID userID){

        Optional<Patient> patient = patientService.findById(userID);
        return patient.map(value -> repo.findAllVaccinesImplementedByUserEmail(value.getEmail())).orElse(null);
    }

    public List<GETPatientCalendarDTO> getFamilyCalendar (UUID userId){
        Optional<Patient> patient = patientService.findById(userId);

        if(patient.isPresent()){
            GETPatientCalendarDTO ownCalendar = new GETPatientCalendarDTO(
                    patient.get().getId(),
                    patient.get().getName().concat(" "+ patient.get().getLastName()),
                    String.valueOf(ChronoUnit.MONTHS.between(patient.get().getBirthDate(), LocalDate.now())),
                    getAllVaccinesImplementedDTO(patient.get().getId()),
                    calendarMomentService.getAllVaccinesNotImplemented(patient.get().getId())
            );

            List<GETPatientCalendarDTO> calendar = new ArrayList<>();
            calendar.add(ownCalendar);
            if(patientService.hasDependients(userId)){

                Optional<List<String>> listaEmail = patientService.findAllDependentsEmailByResponsableUUID(patient.get().getEmail());

                for (String email: listaEmail.get()){

                    Optional<Patient> patientFor = patientService.findByEmail(email);

                    calendar.add(new GETPatientCalendarDTO(
                            patientFor.get().getId(),
                            patientFor.get().getName().concat(" "+ patientFor.get().getLastName()),
                            String.valueOf(ChronoUnit.MONTHS.between(patientFor.get().getBirthDate(), LocalDate.now())),
                            getAllVaccinesImplementedDTO(patientFor.get().getId()),
                            calendarMomentService.getAllVaccinesNotImplemented(patientFor.get().getId())
                    ));

                }

            }

            return calendar;

        }else{
            return null;
        }



    }

}
