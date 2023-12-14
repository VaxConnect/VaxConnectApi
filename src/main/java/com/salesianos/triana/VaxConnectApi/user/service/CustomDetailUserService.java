package com.salesianos.triana.VaxConnectApi.user.service;

import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import com.salesianos.triana.VaxConnectApi.user.modal.Sanitary;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Primary
@Service("patientDetailsService")
@RequiredArgsConstructor
public class CustomDetailUserService implements UserDetailsService {

    private final PatientService patientService;
    private final SanitaryService sanitaryService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Patient> patient = patientService.findByEmail(email);
        Optional<Sanitary> sanitary = sanitaryService.findByEmail(email);

        if (patient.isPresent()){
            return patient.get();
        }else{
            if(sanitary.isPresent()){
                return sanitary.get();
            }else{
                throw( new UsernameNotFoundException("No user with email: " +  email));
            }
        }
                /*
                .orElseThrow(() -> new UsernameNotFoundException("No user with email: " +  email));
                */
    }
}
