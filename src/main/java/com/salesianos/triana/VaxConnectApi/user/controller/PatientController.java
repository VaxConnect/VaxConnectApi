package com.salesianos.triana.VaxConnectApi.user.controller;

import com.salesianos.triana.VaxConnectApi.user.dto.*;
import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import com.salesianos.triana.VaxConnectApi.user.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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



    @Operation(summary = "Get logged patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="200",
                    description = "Patient have been found",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PatientBasicDataDto.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "id": "744ae11c-3332-42ec-bb45-f2839eb06e21",
                                                "name": "Maria",
                                                "lastName": "Rodriguez",
                                                "birthDate": "1985-08-22",
                                                "dni": "987654321",
                                                "email": "maria@gmail.com"
                                            }
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "the patient has not been found",
                    content = @Content),
    })
    @GetMapping("/patient/logged")
    public ResponseEntity<PatientBasicDataDto> getLoggedPatient(@AuthenticationPrincipal Patient patient) {
        return ResponseEntity.of(patientService.findLoggedById(patient.getId()));
        //¿Esta forma de devolver response entity esta bien?
        //deberia recibir un paciente Dto?
    }

    @Operation(summary = "Get dependents of a patient by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="200",
                    description = "Dependents have been found",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PatientBasicDataDto.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {
                                                    "id": "e8885f71-6cf0-40de-af10-930d3d9a7f6c",
                                                    "name": "Laura",
                                                    "lastName": "Martinez Rodriguez",
                                                    "birthDate": "2023-09-10",
                                                    "dni": "111223344",
                                                    "email": "laura@gmail.com"
                                                },
                                                {
                                                    "id": "4e9f7244-1069-435b-bf74-dd3aff6e6932",
                                                    "name": "Juan",
                                                    "lastName": "Martinez Rodriguez",
                                                    "birthDate": "2023-09-07",
                                                    "dni": "555667788",
                                                    "email": "juan@gmail.com"
                                                }
                                            ]
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "The patient does not have dependents",
                    content = @Content),
    })
    @GetMapping("/patient/dependents")
    public ResponseEntity<List<PatientBasicDataDto>> findDependentsByUserId(@AuthenticationPrincipal Patient patient) {
        return ResponseEntity.of(patientService.findDependentsByUseId(patient.getId()));
    }
    
    @GetMapping("/patient/myprofile/")
    public ResponseEntity<GETUserProfileDetails> viewMyProfile(@AuthenticationPrincipal Patient patient){
        return ResponseEntity.status(200).body(patientService.getUserProfileDetailsDTO(patient.getId()));
    }

}
