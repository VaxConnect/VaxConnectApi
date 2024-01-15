package com.salesianos.triana.VaxConnectApi;

import com.salesianos.triana.VaxConnectApi.user.dto.PatientBasicDataDto;
import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import com.salesianos.triana.VaxConnectApi.user.modal.UserRole;
import com.salesianos.triana.VaxConnectApi.user.repo.PatientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class PatientRespositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    PatientRepository patientRepository;

    //si hay problemas para persistir la entidad se usa merge
    //Hay dificultad a la hora de crear los datos para el testeo por lo que aprenderemos a automatizar eso
    @Test
    void findDependentsByUserId(){
        Patient patient = Patient.builder()
                .dni("123456789")
                .email("manolo@gamil.com")
                .name("manolo")
                .fotoUrl("foto.url")
                .birthDate(LocalDate.of(1990,10,12))
                .lastName("manoles")
                .phoneNumber("123456789")
                .accountNonLocked(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .roles(EnumSet.of(UserRole.PATIENT))
                .build();

        Patient patient2 = Patient.builder()
                .dni("999999999")
                .email("juan@gamil.com")
                .name("juan")
                .fotoUrl("foto.url")
                .birthDate(LocalDate.of(1990,10,12))
                .lastName("juanes")
                .phoneNumber("9999999")
                .accountNonLocked(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .roles(EnumSet.of(UserRole.PATIENT))
                .build();
        entityManager.merge(patient);
        entityManager.merge(patient2);

        Optional<List<PatientBasicDataDto>> dependientesEncontrado = patientRepository.findDependentsByUserId(patient.getId());

        Assertions.assertEquals(1, dependientesEncontrado.get().size());



    }

}
