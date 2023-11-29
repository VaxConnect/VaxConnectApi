package com.salesianos.triana.VaxConnectApi.user.service;


import com.salesianos.triana.VaxConnectApi.administration.dto.POSTAdministrationDTO;
import com.salesianos.triana.VaxConnectApi.administration.service.AdministrationService;
import com.salesianos.triana.VaxConnectApi.calendarmoment.dto.POSTCalendarMoment;
import com.salesianos.triana.VaxConnectApi.calendarmoment.modal.CalendarMoment;
import com.salesianos.triana.VaxConnectApi.calendarmoment.service.CalendarMomentService;

import com.salesianos.triana.VaxConnectApi.user.dto.*;
import com.salesianos.triana.VaxConnectApi.user.exception.SanitaryNotFoundException;
import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import com.salesianos.triana.VaxConnectApi.user.modal.Sanitary;
import com.salesianos.triana.VaxConnectApi.user.modal.UserRole;
import com.salesianos.triana.VaxConnectApi.user.repo.PatientRepository;
import com.salesianos.triana.VaxConnectApi.user.repo.SanitaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SanitaryService {

    private final PasswordEncoder passwordEncoder;
    private final SanitaryRepository sanitaryRepository;
    private final PatientRepository patientRepository;
    private final AdministrationService administrationService;
    private final CalendarMomentService calendarMomentService;

    public Sanitary createSanitary (CreateUserRequest createUserRequest, EnumSet<UserRole>roles){
        if (sanitaryRepository.existsByEmailIgnoreCase(createUserRequest.email())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"The email has benn registred");
        }
        Sanitary sanitary = Sanitary.builder()
                .email(createUserRequest.email())
                .name(createUserRequest.name())
                .lastName(createUserRequest.lastName())
                .createdAt(LocalDateTime.now())
                .birthDate(createUserRequest.birthDate())
                .password(passwordEncoder.encode(createUserRequest.password()))
                .roles(roles)
                .build();
        return sanitaryRepository.save(sanitary);
    }

    public void createCalendarMoment(POSTCalendarMoment postCalendarMoment){
        calendarMomentService.createCalendarMoment(postCalendarMoment);
    }
    public void createAdministration(POSTAdministrationDTO postAdministrationDTO){
        administrationService.createAdministration(postAdministrationDTO);
    }


 public Optional<Sanitary> findByEmail(String nombre){
        Optional<Sanitary> sanitary;
        sanitary=sanitaryRepository.findFirstByEmail(nombre);
        if (sanitary.isPresent()){
            return sanitaryRepository.findFirstByEmail(nombre);
        }else {
            throw new SanitaryNotFoundException();
        }
    }
    public List<GetListYoungestPatients> listYoungestPatients(){

        return patientRepository.findYoungPatient();

    }

    public List<GetListYoungestPatients> findLastAddedPatient(){

        return patientRepository.findLastPatientAded();

    }

    public List<Sanitary> findAll(){
        return sanitaryRepository.findAll();
    }

    public List<GetListOfSanitaries> listOfSanitaries (){
        return sanitaryRepository.getList();
    }
    public Sanitary createSanitaryWithRole(CreateUserRequest createUserRequest){
        return createSanitary(createUserRequest,EnumSet.of(UserRole.SANITARY));
    }
    public Optional<GetSanitaryByEmail> findByEmailDto (String email){
        Optional<GetSanitaryByEmail> getSanitaryByEmail =sanitaryRepository.getsanitaryByName(email);
        if (getSanitaryByEmail.isPresent()){
            return sanitaryRepository.getsanitaryByName(email);
        }else {
            throw new SanitaryNotFoundException();
        }
    }

    public Page<PatientDetailsDto> findAllPatients(Pageable p){
        return patientRepository.findAllPatients(p);
    }
    public Optional<Sanitary> findById(UUID id){return sanitaryRepository.findById(id);}

    public Optional<PatientDetailsDto> findByPatientId(UUID id){return patientRepository.findByPatientId(id);}

    public ResponseEntity<PatientDetailsDto> findPatientById(UUID id){
        if(patientRepository.existsById(id))
            return ResponseEntity.of(patientRepository.findByPatientId(id));
        else
            return ResponseEntity.notFound().build();
    }
    public Optional<List<PatientBasicDataDto>>findDependentsByPatientId(UUID id){
        return patientRepository.findDependentsByUserId(id);
    }

}
