package com.salesianos.triana.VaxConnectApi.user.service;

import com.salesianos.triana.VaxConnectApi.user.dto.*;
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
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SanitaryService {

    private final PasswordEncoder passwordEncoder;
    private final SanitaryRepository sanitaryRepository;
    private final PatientRepository patientRepository;



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

    public Optional<Sanitary> findByEmail(String email){
        return sanitaryRepository.findFirstByEmail(email);
    }
    public List<GetListYoungestPatients> listYoungestPatients(){

        return patientRepository.findYoungPatient();

    }
    public List<Sanitary> findAll(){
        return sanitaryRepository.findAll();
    }
    public Sanitary createSanitaryWithRole(CreateUserRequest createUserRequest){
        return createSanitary(createUserRequest,EnumSet.of(UserRole.SANITARY));
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
