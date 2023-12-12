package com.salesianos.triana.VaxConnectApi.user.service;

import com.salesianos.triana.VaxConnectApi.Mail;
import com.salesianos.triana.VaxConnectApi.user.OtpUtil;
import com.salesianos.triana.VaxConnectApi.user.dto.*;
import com.salesianos.triana.VaxConnectApi.user.exception.PatientHasDependentsException;
import com.salesianos.triana.VaxConnectApi.user.exception.PatientNotFoundException;
import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import com.salesianos.triana.VaxConnectApi.user.modal.User;
import com.salesianos.triana.VaxConnectApi.user.modal.UserRole;
import com.salesianos.triana.VaxConnectApi.user.repo.PatientRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PasswordEncoder passwordEncoder;
    private final PatientRepository patientRepository;
    private final OtpUtil otpUtil;
    private final Mail mail;

    public Patient createPatient(CreateUserRequest createUserRequest, EnumSet<UserRole> roles) {
        if (patientRepository.existsByEmailIgnoreCase(createUserRequest.email()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email del usuario ya ha sido registrado");

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

    public Patient createPatientWithPatientRole(CreateUserRequest createUserRequest) {
        return createPatient(createUserRequest, EnumSet.of(UserRole.PATIENT));
    }

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    public Optional<Patient> findById(UUID id) {
        return patientRepository.findById(id);
    }

    public boolean existsById(String id) {

        if (patientRepository.existsById(UUID.fromString(id)))
            return true;

        return false;

    }

    public Optional<List<String>> findAllDependentsEmailByResponsableUUID(String email) {
        return patientRepository.findAllDependentsEmailByResponsableEmail(email);
    }

    public boolean hasDependients(UUID uuid) {

        Optional<Patient> patient = patientRepository.findById(uuid);

        return patient.filter(value -> !value.getDependients().isEmpty()).isPresent();


    }

    public GETUserProfileDetails getUserProfileDetailsDTO(UUID uuid) {

        Optional<GETUserProfileDetails> patientDTO = patientRepository.getUserProfileDetailsById(uuid);

        return patientDTO.orElse(null);

    }

    public List<GETUserProfileDetails> getFamilyOfUserDetails(UUID id) {
        Optional<List<GETUserProfileDetails>> list = patientRepository.getFamilyDetails(id);

        return list.orElse(null);

    }


    public Optional<Patient> findByEmail(String email) {
        return patientRepository.findFirstByEmail(email);
    }

    //Make the edit Patient method

    //Make the edit Patients's password method

    public void delete(Patient patient) {
        patientRepository.deleteById(patient.getId());
    }


    public void deleteById(UUID id) {
        if (patientRepository.existsById(id))
            patientRepository.deleteById(id);
    }

    public boolean passwordMatch(Patient patient, String clearPassword) {
        return passwordEncoder.matches(clearPassword, patient.getPassword());
    }

    public Optional<PatientBasicDataDto> findLoggedById(UUID id) {
        return patientRepository.findLoggedPatientById(id);
    }

    public Optional<List<PatientBasicDataDto>> findDependentsByUseId(UUID id) {
        return patientRepository.findDependentsByUserId(id);
    }

    public Patient createPatient(CreatePatientDto newPatient) {
        if (patientRepository.existsByEmailIgnoreCase(newPatient.email()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email del usuario ya ha sido registrado");

        Patient p = Patient.builder()
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

    public void deleteByPatientId(String id) {
        UUID validId = UUID.fromString(id);
        if (patientRepository.countDependentsByPatient(validId) == 0
                && patientRepository.countPatientsInChargeOf(validId) == 0)
            patientRepository.deleteById(validId);
        else
            throw new PatientHasDependentsException();
    }

    public Page<PatientDetailsDto> findPatientByName(Pageable pageable, String name) {

        return patientRepository.findPatientByName(pageable, name);

    }

    public PatientDetailsDto editPatientById(UUID id, PatientDetailsDto editedPatient) {
        Optional<Patient> findPatient = patientRepository.findById(id);
        if (findPatient.isPresent()) {
            Patient patient = findPatient.get();
            patient.setName(editedPatient.name());
            patient.setLastName(editedPatient.lastName());
            patient.setPhoneNumber(editedPatient.phoneNumber());
            patient.setFotoUrl(editedPatient.fotoUrl());
            Patient patientEdited = patientRepository.save(patient);
            return PatientDetailsDto.of(patientEdited);
        } else {
            throw new PatientNotFoundException();
        }
    }

    public String newregistrer(CreateUserRequest userResponse) {
        String otp = otpUtil.generateOpt();
        try {
            mail.sendEmail(userResponse.email(), otp);
        } catch (MessagingException e) {
            throw new RuntimeException("unable to send otp try again");
        }
        Patient patient = new Patient();
        patient.setName(userResponse.name());
        patient.setEmail(userResponse.email());
        patient.setPassword(passwordEncoder.encode(userResponse.password()));
        patient.setCreatedAt(LocalDateTime.now());
        patientRepository.save(patient);
        return "patient register";
    }

    public String verifyAccount(String email, String otp) {
        Patient patient = patientRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Patient with email " + email + "not found"));
        if (patient.getOTP().equals(otp) && Duration.between(patient.getCreatedAt(), LocalDateTime.now()).getSeconds() < (1 * 60)) {
            patient.setAccountNonExpired(true);
            patientRepository.save(patient);
            return "otp verify tou can login";

        }
        return "please regenerate otp and try again";
    }

    public String regenerateOtp(String email) {
        Patient patient = patientRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("user not found"));
        String opt = otpUtil.generateOpt();
        try {
            mail.sendEmail(email, opt);

        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp try again");
        }
        patient.setOTP(opt);
        patient.setCreatedAt(LocalDateTime.now());
        patientRepository.save(patient);
        return "email sent .. porfavor veriffye el email";

    }
public String login(LoginRequest patientBasicDataDto,CreateUserRequest createUserRequest){

        Patient patient = patientRepository.findByEmail(patientBasicDataDto.mail()).orElseThrow(()-> new RuntimeException("user not found"));
        if (!createUserRequest.password().equals(patient.getPassword())){
            return "password is incorrect";
        } else if (!patient.isAccountNonExpired()) {
            return "you account is not verify";
        }
        return "login successful";
}


}
