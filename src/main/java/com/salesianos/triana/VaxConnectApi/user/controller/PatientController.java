package com.salesianos.triana.VaxConnectApi.user.controller;

import com.salesianos.triana.VaxConnectApi.user.dto.*;
import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import com.salesianos.triana.VaxConnectApi.user.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.salesianos.triana.VaxConnectApi.security.jwt.JwtProvider;


import java.util.List;

@RestController
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @PostMapping("/auth/register")
    public ResponseEntity<UserResponse> createdPatientWithPatientRole(@RequestBody CreateUserRequest createUserRequest){
        Patient patient = patientService.createPatientWithPatientRole(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.fromUser(patient));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<JwtUserResponse> login(@RequestBody LoginRequest loginRequest) {

        // Realizamos la autenticación

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.mail(),
                                loginRequest.password()
                        )
                );

        // Una vez realizada, la guardamos en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Devolvemos una respuesta adecuada
        String token = jwtProvider.generateToken(authentication);

        Patient user = (Patient) authentication.getPrincipal();


        return ResponseEntity.status(HttpStatus.CREATED)
                .body(JwtUserResponse.of(user, token));


    }




    @GetMapping("/patient")
    public ResponseEntity<Page<GetPatientByIdDto>> getAll(@PageableDefault(page=0, size=5) Pageable pageable){
        Page<GetPatientByIdDto> pagedResult = patientService.findAllPatients(pageable);

        if (pagedResult.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(pagedResult);
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<GetPatientByIdDto> getById(@AuthenticationPrincipal Patient patient) {
        return ResponseEntity.of(patientService.findLoggedById(patient.getId()));
        //¿Esta forma de devolver response entity esta bien?
        //deberia recibir un paciente Dto
    }
}
