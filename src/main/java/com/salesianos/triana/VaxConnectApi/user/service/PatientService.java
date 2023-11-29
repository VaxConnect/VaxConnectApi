package com.salesianos.triana.VaxConnectApi.user.service;

import com.salesianos.triana.VaxConnectApi.user.dto.CreatePatientDto;
import com.salesianos.triana.VaxConnectApi.user.dto.CreateUserRequest;
import com.salesianos.triana.VaxConnectApi.user.dto.GETUserProfileDetails;
import com.salesianos.triana.VaxConnectApi.user.dto.PatientBasicDataDto;
import com.salesianos.triana.VaxConnectApi.user.dto.PatientDetailsDto;
import com.salesianos.triana.VaxConnectApi.user.exception.PatientHasDependentsException;
import com.salesianos.triana.VaxConnectApi.user.exception.PatientNotFoundException;
import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import com.salesianos.triana.VaxConnectApi.user.modal.UserRole;
import com.salesianos.triana.VaxConnectApi.user.repo.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PasswordEncoder passwordEncoder;
    private final PatientRepository patientRepository;

    public Patient createPatient(CreateUserRequest createUserRequest, EnumSet<UserRole>roles){
        if (patientRepository.existsByEmailIgnoreCase(createUserRequest.email()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"El email del usuario ya ha sido registrado");

        Patient patient = Patient.builder()
                .email(createUserRequest.email())
                .name(createUserRequest.name())
                .lastName(createUserRequest.lastName())
                .createdAt(LocalDateTime.now())
                .birthDate(createUserRequest.birthDate())
                .password(passwordEncoder.encode(createUserRequest.password()))
                .roles(roles)
                .build();
        return patientRepository.save(patient);

    }

    public Patient createPatientWithPatientRole(CreateUserRequest createUserRequest){
        return createPatient(createUserRequest,EnumSet.of(UserRole.PATIENT));
    }

    public List<Patient> findAll(){
        return patientRepository.findAll();
    }
    public Optional<Patient>findById(UUID id){
        return patientRepository.findById(id);
    }
    public boolean existsById (String id){

        if(patientRepository.existsById(UUID.fromString(id)))
            return true;

        return false;

    }

    public Optional<List<String>> findAllDependentsEmailByResponsableUUID (String email){
        return patientRepository.findAllDependentsEmailByResponsableEmail(email);
    }

    public boolean hasDependients(UUID uuid){

        Optional<Patient> patient = patientRepository.findById(uuid);

        return patient.filter(value -> !value.getDependients().isEmpty()).isPresent();


    }

    public GETUserProfileDetails getUserProfileDetailsDTO (UUID uuid ){

        Optional<GETUserProfileDetails> patientDTO = patientRepository.getUserProfileDetailsById(uuid);

        return patientDTO.orElse(null);

    }
    public List<GETUserProfileDetails> getFamilyOfUserDetails(UUID id){
        Optional<List<GETUserProfileDetails>> list = patientRepository.getFamilyDetails(id);

        return list.orElse(null);

    }



   public Optional<Patient>findByEmail(String email){
        return patientRepository.findFirstByEmail(email);
    }

    //Make the edit Patient method

    //Make the edit Patients's password method

    public void delete(Patient patient){
        patientRepository.deleteById(patient.getId());
    }


    public void deleteById(UUID id){
        if (patientRepository.existsById(id))
            patientRepository.deleteById(id);
    }

    public boolean passwordMatch(Patient patient, String clearPassword) {
        return passwordEncoder.matches(clearPassword, patient.getPassword());
    }

    public Optional<PatientBasicDataDto>findLoggedById(UUID id){
        return patientRepository.findLoggedPatientById(id);
    }
    public Optional<List<PatientBasicDataDto>>findDependentsByUseId(UUID id){
        return patientRepository.findDependentsByUserId(id);
    }

    /*
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
     */

    public Patient createPatient(CreatePatientDto newPatient){
        if (patientRepository.existsByEmailIgnoreCase(newPatient.email()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"El email del usuario ya ha sido registrado");

        Patient p =  Patient.builder()
                .email(newPatient.email())
                .name(newPatient.name())
                .lastName(newPatient.lastName())
                .birthDate(newPatient.birthDate())
                .dni(newPatient.dni())
                .phoneNumber(newPatient.phoneNumber())
                .fotoUrl(newPatient.fotoUrl())
                .password(passwordEncoder.encode(newPatient.password()))
                .build();

        List<Patient> dependents = newPatient.dependents()
                .stream()
                .map(id -> patientRepository.getReferenceById(UUID.fromString(id))) // Vlad Mihalcea
                .toList();

        p.setDependients(dependents);

        return patientRepository.save(p);
    }

    public void deleteByPatientId (String id){
        UUID validId = UUID.fromString(id);
        if (patientRepository.countDependentsByPatient(validId) == 0)
            patientRepository.deleteById(validId);
         else
            throw new PatientHasDependentsException();
    }

    public Page<PatientDetailsDto> findPatientByName(Pageable pageable, String name){

        return patientRepository.findPatientByName(pageable, name);

    }

    public PatientDetailsDto editPatientById(UUID id, PatientDetailsDto editedPatient){
        Optional<Patient> findPatient = patientRepository.findById(id);
        if (findPatient.isPresent()){
            Patient patient = findPatient.get();
            patient.setName(editedPatient.name());
            patient.setLastName(editedPatient.lastName());
            patient.setPhoneNumber(editedPatient.phoneNumber());
            patient.setFotoUrl(editedPatient.fotoUrl());
            Patient patientEdited = patientRepository.save(patient);
            return PatientDetailsDto.of(patientEdited);
        }else {
            throw new PatientNotFoundException();
        }
    }

}
