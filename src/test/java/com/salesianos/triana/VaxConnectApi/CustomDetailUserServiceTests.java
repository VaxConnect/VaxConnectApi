package com.salesianos.triana.VaxConnectApi;

import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import com.salesianos.triana.VaxConnectApi.user.modal.Sanitary;
import com.salesianos.triana.VaxConnectApi.user.service.PatientService;
import com.salesianos.triana.VaxConnectApi.user.service.SanitaryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.stream.Stream;

public class CustomDetailUserServiceTests {

    @InjectMocks
    PatientService patientService;

    @InjectMocks
    SanitaryService sanitaryService;

    @ParameterizedTest
    @MethodSource("generateArray")
    void loadUserByUsername(String email){
        Optional<Patient> patient = patientService.findByEmail(email);
        Optional<Sanitary> sanitary = sanitaryService.findByEmail(email);

        if (patient.isPresent()){
             patient.get();
        }else{
            if(sanitary.isPresent()){
                 sanitary.get();
            }else{
                throw( new UsernameNotFoundException("No user with email: " +  email));
                
            }
        }
        //Mockito.when(repositorio.findAll()).thenReturn(data);

    }

    static Stream<Arguments> generateArray(){
        return Stream.of(
                Arguments.arguments("aaa"),
                Arguments.arguments("bbbb@aa"),
                Arguments.arguments("cccc")
        );
    }



}
