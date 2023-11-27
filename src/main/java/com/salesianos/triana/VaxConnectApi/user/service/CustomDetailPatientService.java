package com.salesianos.triana.VaxConnectApi.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Primary
@Service("patientDetailsService")
@RequiredArgsConstructor
public class CustomDetailPatientService implements UserDetailsService {

    private final PatientService patientService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return  patientService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user with email: " +  email));
    }




}
