package com.salesianos.triana.VaxConnectApi.user.service;

import com.salesianos.triana.VaxConnectApi.user.dto.CreatePatientDto;
import com.salesianos.triana.VaxConnectApi.user.dto.CreateUserRequest;
import com.salesianos.triana.VaxConnectApi.user.dto.GETUserProfileDetails;
import com.salesianos.triana.VaxConnectApi.user.dto.PatientBasicDataDto;
import com.salesianos.triana.VaxConnectApi.user.dto.PatientDetailsDto;
import com.salesianos.triana.VaxConnectApi.user.exception.PatientHasDependentsException;
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

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PasswordEncoder passwordEncoder;
    private final PatientRepository patientRepository;

    public Patient createPatient(CreateUserRequest createUserRequest, EnumSet<UserRole>roles){
        if (patientRepository.existsByEmailIgnoreCase(createUserRequest.email()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Elemail del usuario ya ha sido registrado");

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

    public Patient save(CreatePatientDto newPatient){
        Patient p = new Patient();
        p.setName(newPatient.name());
        p.setLastName(newPatient.lastName());
        p.setBirthDate(newPatient.birthDate());
        p.setDni(newPatient.dni());
        p.setEmail(newPatient.email());
        p.setPhoneNumber(newPatient.phoneNumber());
        p.setFotoUrl(newPatient.fotoUrl());

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



}
