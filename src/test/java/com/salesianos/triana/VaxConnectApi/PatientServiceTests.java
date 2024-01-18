package com.salesianos.triana.VaxConnectApi;

import com.salesianos.triana.VaxConnectApi.user.exception.PatientHasDependentsException;
import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import com.salesianos.triana.VaxConnectApi.user.repo.PatientRepository;
import com.salesianos.triana.VaxConnectApi.user.service.PatientService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

public class PatientServiceTests {
    /*
    public void deleteByPatientId (String id){
        UUID validId = UUID.fromString(id);
        if (patientRepository.countDependentsByPatient(validId) == 0
                && patientRepository.countPatientsInChargeOf(validId) == 0)
            patientRepository.deleteById(validId);
         else
            throw new PatientHasDependentsException();
    }
     */

    @Autowired
    PatientRepository patientRepository;


    @Test
    void deleteByPatientIdTest () throws PatientHasDependentsException {
        Optional<Patient> patient = patientRepository.findFirstByEmail("manolo@gamil.com");

        Assertions.assertTrue(patient.isPresent());

        Assertions.assertTrue(patientRepository.countDependentsByPatient(patient.get().getId())>= 0);
        Assertions.assertTrue(patientRepository.countPatientsInChargeOf(patient.get().getId())>= 0);
    }

}
