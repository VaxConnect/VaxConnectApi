package com.salesianos.triana.VaxConnectApi;

import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import com.salesianos.triana.VaxConnectApi.user.modal.UserRole;
import com.salesianos.triana.VaxConnectApi.user.repo.PatientRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
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
                .roles(EnumSet.of(UserRole.PATIENT))
                .build();

        patientRepository.save(patient);



        Patient patient3 = Patient.builder()
                .dni("111223344")
                .email("laura@gmail.com")
                .name("Laura")
                .fotoUrl("https://example.com/laura.jpg")
                .birthDate(LocalDate.of(2023, 9, 10))
                .lastName("Martinez Rodriguez")
                .password(passwordEncoder.encode("laura123"))
                .phoneNumber(555111222)
                .roles(EnumSet.of(UserRole.PATIENT))
                .dependients(new ArrayList<>()) // Initialize the dependients list
                .build();
        patientRepository.save(patient3);

        // Example 4
        Patient patient4 = Patient.builder()
                .dni("555667788")
                .email("juan@gmail.com")
                .name("Juan")
                .fotoUrl("https://example.com/juan.jpg")
                .birthDate(LocalDate.of(2023, 9, 7))
                .lastName("Martinez Rodriguez")
                .password(passwordEncoder.encode("juanpass"))
                .phoneNumber(987654321)
                .roles(EnumSet.of(UserRole.PATIENT))
                .build();
        patientRepository.save(patient4);


        Patient patient2 = Patient.builder()
                .dni("987654321")
                .email("maria@gmail.com")
                .name("Maria")
                .fotoUrl("https://example.com/maria.jpg")
                .birthDate(LocalDate.of(1985, 8, 22))
                .lastName("Rodriguez")
                .password(passwordEncoder.encode("securepassword"))
                .phoneNumber(987654321)
                .roles(EnumSet.of(UserRole.PATIENT))
                .dependients(List.of(patient3, patient4))
                .build();
        patientRepository.save(patient2);


        // Example 5
        Patient patient5 = Patient.builder()
                .dni("999888777")
                .email("clara@gmail.com")
                .name("Clara")
                .fotoUrl("https://example.com/clara.jpg")
                .birthDate(LocalDate.of(2000, 6, 25))
                .lastName("Gomez")
                .password(passwordEncoder.encode("clara456"))
                .phoneNumber(123456789)
                .roles(EnumSet.of(UserRole.PATIENT))
                .build();
        patientRepository.save(patient5);

        Patient patient6 = Patient.builder()
                .dni("777888999")
                .email("carlos@gmail.com")
                .name("Carlos")
                .fotoUrl("https://example.com/carlos.jpg")
                .birthDate(LocalDate.of(1988, 7, 18))
                .lastName("Hernandez")
                .password(passwordEncoder.encode("carlos789"))
                .phoneNumber(555444333)
                .roles(EnumSet.of(UserRole.PATIENT))
                .build();
        patientRepository.save(patient6);

        // Example 7
        Patient patient7 = Patient.builder()
                .dni("444555666")
                .email("ana@gmail.com")
                .name("Ana")
                .fotoUrl("https://example.com/ana.jpg")
                .birthDate(LocalDate.of(1992, 2, 14))
                .lastName("Lopez")
                .password(passwordEncoder.encode("ana567"))
                .phoneNumber(987654321)
                .roles(EnumSet.of(UserRole.PATIENT))
                .build();
        patientRepository.save(patient7);

        // Example 8
        Patient patient8 = Patient.builder()
                .dni("111222333")
                .email("javier@gmail.com")
                .name("Javier")
                .fotoUrl("https://example.com/javier.jpg")
                .birthDate(LocalDate.of(1975, 9, 30))
                .lastName("Diaz")
                .password(passwordEncoder.encode("javier321"))
                .phoneNumber(123456789)
                .roles(EnumSet.of(UserRole.PATIENT))
                .build();
        patientRepository.save(patient8);



    }

}
