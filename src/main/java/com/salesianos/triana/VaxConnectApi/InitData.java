package com.salesianos.triana.VaxConnectApi;

import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import com.salesianos.triana.VaxConnectApi.user.modal.UserRole;
import com.salesianos.triana.VaxConnectApi.user.repo.PatientRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class InitData {

    private final PatientRepository patientRepository;

    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public  void initData(){



        Patient patient = Patient.builder()
                .dni("123456789")
                .email("manolo@gamil.com")
                .name("manolo")
                .fotoUrl("foto.url")
                .birthDate(LocalDate.of(2004,10,12))
                .lastName("manoles")
                .password(passwordEncoder.encode("12345678"))
                .phoneNumber(123456789)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .roles(EnumSet.of(UserRole.PATIENT))
                .build();

        patientRepository.save(patient);

    }

}
