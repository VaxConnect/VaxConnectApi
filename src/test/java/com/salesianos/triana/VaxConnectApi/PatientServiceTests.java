package com.salesianos.triana.VaxConnectApi;

import com.salesianos.triana.VaxConnectApi.user.exception.PatientHasDependentsException;
import com.salesianos.triana.VaxConnectApi.user.repo.PatientRepository;
import com.salesianos.triana.VaxConnectApi.user.service.PatientService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Autowired
    PatientService patientService;

    @Test
    void deleteByPatientIdTest (String id) {
        UUID validId = UUID.randomUUID();
        Mockito.when(patientRepository.countDependentsByPatient(validId)).thenReturn(0);
        Mockito.when(patientRepository.countPatientsInChargeOf(validId)).thenReturn(0);

        patientService.deleteByPatientId(validId.toString());

        Mockito.verify(patientRepository).deleteById(validId);
    }

}
