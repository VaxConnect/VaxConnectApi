package com.salesianos.triana.VaxConnectApi;

import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import com.salesianos.triana.VaxConnectApi.user.repo.PatientRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class InitData {

    private final PatientRepository patientRepository;


    @PostConstruct
    public  void initData(){

        Patient patient = Patient.builder()
                .dni("123456789")
                .email("manolo@gamil.com")
                .name("manolo")
                .fotoUrl("foto.url")
                .birthDate(LocalDate.of(2004,10,12))
                .lastName("manoles")
                .password("12345678")
                .phoneNumber(123456789)
                .build();

        patientRepository.save(patient);

    }

}
