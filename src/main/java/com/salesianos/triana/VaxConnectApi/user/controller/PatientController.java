package com.salesianos.triana.VaxConnectApi.user.controller;

import com.salesianos.triana.VaxConnectApi.user.dto.CreateUserRequest;
import com.salesianos.triana.VaxConnectApi.user.dto.JwtUserResponse;
import com.salesianos.triana.VaxConnectApi.user.dto.LoginRequest;
import com.salesianos.triana.VaxConnectApi.user.dto.UserResponse;
import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import com.salesianos.triana.VaxConnectApi.user.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.salesianos.triana.VaxConnectApi.security.jwt.JwtProvider;

@CrossOrigin( origins = "http://localhost:4200")
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
}
