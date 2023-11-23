package com.salesianos.triana.VaxConnectApi.user.service;

import com.salesianos.triana.VaxConnectApi.user.dto.CreateUserRequest;
import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import com.salesianos.triana.VaxConnectApi.user.modal.UserRole;
import com.salesianos.triana.VaxConnectApi.user.repo.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public Optional<List<String>> findAllDependentsUUIDByResponsableUUID (String uuid){
        return patientRepository.findAllDependentsUUIDByResponsableUUID(uuid);
    }

    public boolean hasDependients(String uuid){

        Optional<Patient> patient = patientRepository.findById(UUID.fromString(uuid));

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

}
