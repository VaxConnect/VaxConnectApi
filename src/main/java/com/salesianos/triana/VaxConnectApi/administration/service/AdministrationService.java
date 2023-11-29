package com.salesianos.triana.VaxConnectApi.administration.service;

import com.salesianos.triana.VaxConnectApi.administration.dto.*;
import com.salesianos.triana.VaxConnectApi.administration.model.Administration;
import com.salesianos.triana.VaxConnectApi.administration.repo.AdministrationRepository;
import com.salesianos.triana.VaxConnectApi.calendarmoment.dto.GETVaccinesNotAdministratedDTO;
import com.salesianos.triana.VaxConnectApi.calendarmoment.error.CalendarMomentNotFoundException;
import com.salesianos.triana.VaxConnectApi.calendarmoment.modal.CalendarMoment;
import com.salesianos.triana.VaxConnectApi.calendarmoment.service.CalendarMomentService;
import com.salesianos.triana.VaxConnectApi.user.exception.EmailListNotFoundException;
import com.salesianos.triana.VaxConnectApi.user.exception.PatientNotFoundException;
import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import com.salesianos.triana.VaxConnectApi.user.service.PatientService;
import com.salesianos.triana.VaxConnectApi.vacune.modal.Vacune;
import com.salesianos.triana.VaxConnectApi.vacune.service.VacuneService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final VacuneService vacuneService;

    @Autowired
    public AdministrationService(@Lazy CalendarMomentService calendarMomentService, @Lazy PatientService patientService, @Lazy AdministrationRepository repo, @Lazy VacuneService vacuneService) {
        this.calendarMomentService = calendarMomentService;
        this.repo = repo;
        this.patientService = patientService;
        this.vacuneService = vacuneService;
    }

    public void createAdministration(POSTAdministrationDTO postAdministrationDTO){

        Optional<Patient> patient = patientService.findByEmail(postAdministrationDTO.userEmail());
        if(patient.isEmpty())
            throw new EntityNotFoundException();

        Optional<CalendarMoment> calendarMoment = calendarMomentService.findCalendarMomentByVaccineData(postAdministrationDTO.vaccineName(), postAdministrationDTO.typeDosis());

        if(calendarMoment.isEmpty())
            throw new CalendarMomentNotFoundException(
                    "Can`t find the calendar moment with the vaccine "
                            + postAdministrationDTO.vaccineName()+
                            " and the dosys "
                            +postAdministrationDTO.typeDosis()
            );

        Administration administration = Administration.builder()
                .patientEmail(patient.get().getEmail())
                .notes(postAdministrationDTO.note())
                .date(LocalDateTime.now())
                .calendarMoment(calendarMoment.get())
                .ageToAdministrate(
                        ((int) ChronoUnit.MONTHS.between(patient.get().getBirthDate(), LocalDate.now()))
                )
                .build();
        repo.save(administration);
    }


    public List<GETLastVaccinesAdministratedDTO> findLastVaccineImplementedByUserId (UUID userID) throws EmailListNotFoundException {

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
        }else{
            throw new PatientNotFoundException();
        }


    }

    public List<GETLastVaccinesAdministratedDTO> findAllLastVaccineImplementedByUserId(Patient patient) throws EmailListNotFoundException {

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
            throw new EmailListNotFoundException("There is an unexpected error with the patient's dependients");//make an error that tell U that there is something wrong in the code
        }
    }

    public List<UUID> getIdOfCalendarMomentNotAdministrated(String email){

        List<UUID> cmAdminIds =repo.findIdsOfCalendarsMomentsWhoAreImplementedByPatientEmail(email);
        List<UUID> allCmIds = calendarMomentService.findAllIdOfCalendarsMoments();

        allCmIds.removeAll(cmAdminIds);

        return allCmIds;

    }

    public List<GETVaccineAdministratedOnCalendar> getAllVaccinesImplementedDTO  (UUID userID){

        Optional<Patient> patient = patientService.findById(userID);
        return patient.map(value -> repo.findAllVaccinesImplementedByUserEmail(value.getEmail())).orElseThrow(PatientNotFoundException::new);
    }

    public List<GETPatientCalendarDTO> getFamilyCalendar (UUID userId){
        Optional<Patient> patient = patientService.findById(userId);
        List<GETPatientCalendarDTO> getPatientCalendarDTOS = new ArrayList<>();

        if(patient.isPresent()){

            List<GETVaccineAdministratedOnCalendar> administratedOnCalendarList = repo.findAllVaccinesImplementedByUserEmail(patient.get().getEmail());
            List<GETVaccinesNotAdministratedDTO> vaccinesNotAdministratedList = calendarMomentService.getAllVaccinesNotImplemented(patient.get().getId());
            List<Vacune> allVaccine = vacuneService.countVaccines();

            List<GETVaccineOnCalendarDTO> listVaccinesOnCalendarDTO = new ArrayList<>();


            for (int i=0; i<allVaccine.size(); i++){
               List<GetMomentToImplementVacuneDTO> getMomentToImplementVacuneDTOS = new ArrayList<>();

                int finalI = i;
                List<GETVaccineAdministratedOnCalendar> administratedFilterByVaccineName =
                        administratedOnCalendarList.stream().filter(x->x.nameVacune().equalsIgnoreCase(allVaccine.get(finalI).getName())).toList();

                List<GETVaccinesNotAdministratedDTO> vaccinesNotAdministratedFilterByName =
                        vaccinesNotAdministratedList.stream().filter(x->x.vacuneName().equalsIgnoreCase(allVaccine.get(finalI).getName())).toList();

                for (GETVaccineAdministratedOnCalendar getVaccineOnCalendarDTO : administratedFilterByVaccineName ) {
                    getMomentToImplementVacuneDTOS.add(new GetMomentToImplementVacuneDTO(
                            getVaccineOnCalendarDTO.id(),
                            getVaccineOnCalendarDTO.month(),
                            getVaccineOnCalendarDTO.typeOfDosys(),
                            true
                    ));
                }
                for (GETVaccinesNotAdministratedDTO getVaccinesNotAdministratedDTO: vaccinesNotAdministratedFilterByName) {
                    getMomentToImplementVacuneDTOS.add(new GetMomentToImplementVacuneDTO(
                            getVaccinesNotAdministratedDTO.id(),
                            getVaccinesNotAdministratedDTO.age(),
                            "",
                            false
                    ));
                }

                GETVaccineOnCalendarDTO getVaccineOnCalendarDTO = new GETVaccineOnCalendarDTO(
                    userId,
                    allVaccine.get(i).getName(),
                    getMomentToImplementVacuneDTOS
                );

                listVaccinesOnCalendarDTO.add(getVaccineOnCalendarDTO);


            }

            GETPatientCalendarDTO calendarDTO = new GETPatientCalendarDTO(
                    patient.get().getId(),
                    patient.get().getName().concat(" "+patient.get().getLastName()),
                    String.valueOf(ChronoUnit.MONTHS.between(patient.get().getBirthDate(), LocalDate.now())),
                    listVaccinesOnCalendarDTO
            );
            getPatientCalendarDTOS.add(calendarDTO);

            if(patientService.hasDependients(patient.get().getId())){
                List<String> dependientExmailList = patientService.findAllDependentsEmailByResponsableUUID(patient.get().getEmail()).get();

                for (String email : dependientExmailList) {
                    Optional<Patient> patientDependient = patientService.findByEmail(email);

                    if(patientDependient.isEmpty()){
                        throw new PatientNotFoundException();
                    }

                    List<GETVaccineAdministratedOnCalendar> administratedOnCalendarListDependient = repo.findAllVaccinesImplementedByUserEmail(patientDependient.get().getEmail());
                    List<GETVaccinesNotAdministratedDTO> vaccinesNotAdministratedListDependient = calendarMomentService.getAllVaccinesNotImplemented(patientDependient.get().getId());
                    List<Vacune> allDependientVaccine = vacuneService.countVaccines();

                    List<GETVaccineOnCalendarDTO> listVaccinesOnCalendarDependientDTO = new ArrayList<>();


                    for (int i=0; i<allDependientVaccine.size(); i++){
                        List<GetMomentToImplementVacuneDTO> getMomentToImplementVacuneDTOS = new ArrayList<>();

                        int finalI = i;
                        List<GETVaccineAdministratedOnCalendar> administratedFilterByVaccineName =
                                administratedOnCalendarListDependient.stream().filter(x->x.nameVacune().equalsIgnoreCase(allVaccine.get(finalI).getName())).toList();

                        List<GETVaccinesNotAdministratedDTO> vaccinesNotAdministratedFilterByName =
                                vaccinesNotAdministratedListDependient.stream().filter(x->x.vacuneName().equalsIgnoreCase(allVaccine.get(finalI).getName())).toList();

                        for (GETVaccineAdministratedOnCalendar getVaccineOnCalendarDTO : administratedFilterByVaccineName ) {
                            getMomentToImplementVacuneDTOS.add(new GetMomentToImplementVacuneDTO(
                                    getVaccineOnCalendarDTO.id(),
                                    getVaccineOnCalendarDTO.month(),
                                    getVaccineOnCalendarDTO.typeOfDosys(),
                                    true
                            ));
                        }
                        for (GETVaccinesNotAdministratedDTO getVaccinesNotAdministratedDTO: vaccinesNotAdministratedFilterByName) {
                            getMomentToImplementVacuneDTOS.add(new GetMomentToImplementVacuneDTO(
                                    getVaccinesNotAdministratedDTO.id(),
                                    getVaccinesNotAdministratedDTO.age(),
                                    "",
                                    false
                            ));
                        }

                        GETVaccineOnCalendarDTO getVaccineOnCalendarDTO = new GETVaccineOnCalendarDTO(
                                userId,
                                allVaccine.get(i).getName(),
                                getMomentToImplementVacuneDTOS
                        );

                        listVaccinesOnCalendarDependientDTO.add(getVaccineOnCalendarDTO);


                    }

                    GETPatientCalendarDTO calendarDependientDTO = new GETPatientCalendarDTO(
                            patientDependient.get().getId(),
                            patientDependient.get().getName().concat(" "+patientDependient.get().getLastName()),
                            String.valueOf(ChronoUnit.MONTHS.between(patientDependient.get().getBirthDate(), LocalDate.now())),
                            listVaccinesOnCalendarDependientDTO
                    );
                    getPatientCalendarDTOS.add(calendarDependientDTO);

                }
            }

            return getPatientCalendarDTOS;

        }else{
            throw new PatientNotFoundException();
        }



    }

}
