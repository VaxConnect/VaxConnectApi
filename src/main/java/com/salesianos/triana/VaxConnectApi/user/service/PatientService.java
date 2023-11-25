package com.salesianos.triana.VaxConnectApi.user.service;

import com.salesianos.triana.VaxConnectApi.user.dto.CreateUserRequest;
import com.salesianos.triana.VaxConnectApi.user.dto.GetPatientByIdDto;
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
import java.time.LocalDate;
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

    public Optional<List<String>> findAllDependentsUUIDByResponsableUUID (String email){
        return patientRepository.findAllDependentsUUIDByResponsableEmail(email);
    }

    public boolean hasDependients(UUID uuid){

        Optional<Patient> patient = patientRepository.findById(uuid);

        return patient.filter(value -> !value.getDependients().isEmpty()).isPresent();


    }

    public Optional<Patient>findByEmail(String email){
        return patientRepository.findFirstByEmail(email);
    }

    //Make the edit Patient method

    //Make the edit Patients's password method

    public void delete(Patient patient){
        deleteById(patient.getId());
    }

    public void deleteById(UUID id){
        if (patientRepository.existsById(id))
            patientRepository.deleteById(id);
    }

    public boolean passwordMatch(Patient patient, String clearPassword) {
        return passwordEncoder.matches(clearPassword, patient.getPassword());
    }



    public Page<GetPatientByIdDto>findAllPatients(Pageable p){
        return patientRepository.findAllPatients(p);
    }

    public Optional<GetPatientByIdDto>findLoggedById(UUID id){
        return patientRepository.findLoggedPatientById(id);
    }
    public Optional<List<GetPatientByIdDto>>findDependentsByUseId(UUID id){
        return patientRepository.findDependentsByUserId(id);
    }


}
