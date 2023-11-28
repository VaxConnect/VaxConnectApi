package com.salesianos.triana.VaxConnectApi.user.controller;

import com.salesianos.triana.VaxConnectApi.security.jwt.JwtProvider;
import com.salesianos.triana.VaxConnectApi.user.dto.*;
import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import com.salesianos.triana.VaxConnectApi.user.modal.Sanitary;
import com.salesianos.triana.VaxConnectApi.user.service.SanitaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.context.annotation.Primary;
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
import org.springframework.web.bind.annotation.*;

import java.net.CacheRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor

public class SanitaryController {


    private final SanitaryService sanitaryService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;


    @GetMapping("/sanitary/patients/young")
    public ResponseEntity<List<GetListYoungestPatients>> listYoungestPatients(@AuthenticationPrincipal Sanitary sanitary){
    List<GetListYoungestPatients> youngest = sanitaryService.listYoungestPatients();
    return ResponseEntity.ok(youngest);
    }
    @GetMapping("/sanitary/grafic")
    public ResponseEntity<List<GetVaccinesMoreAdministrated>> VaccinesMoreAdministrated(@AuthenticationPrincipal Sanitary sanitary){
        List<GetVaccinesMoreAdministrated> getVaccinesMoreAdministrateds = sanitaryService.VaccinesMoreAdministrated();
        return ResponseEntity.ok(getVaccinesMoreAdministrateds);
    }
    @PostMapping("/auth/register/sanitary")
    public ResponseEntity<UserResponse> createSanitary(@RequestBody CreateUserRequest createUserRequest){
        Sanitary sanitary = sanitaryService.createSanitaryWithRole(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.fromSanitary(sanitary));
    }

    @PostMapping("/auth/login/sanitary")
    public ResponseEntity<JwtUserResponse> loginSanitary(@RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.mail(),
                        loginRequest.password()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        Sanitary sanitary1 = (Sanitary) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(JwtUserResponse.ofSanitary(sanitary1,token));
    }



    @Operation(summary = "Get all patients")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="200",
                    description = "Patients have been found",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PatientBasicDataDto.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "content": [
                                                    {
                                                        "id": "9fd2ebc1-6b09-44a3-9d3f-034c3a87e9cb",
                                                        "name": "Clara",
                                                        "lastName": "Gomez",
                                                        "birthDate": "2000-06-25",
                                                        "dni": "999888777",
                                                        "email": "clara@gmail.com"
                                                    },
                                                    {
                                                        "id": "7ae1b662-8f6c-40ad-979e-388ea9b1a1d0",
                                                        "name": "Javier",
                                                        "lastName": "Diaz",
                                                        "birthDate": "1975-09-30",
                                                        "dni": "111222333",
                                                        "email": "javier@gmail.com"
                                                    }
                                                ],
                                                "pageable": {
                                                    "pageNumber": 1,
                                                    "pageSize": 4,
                                                    "sort": {
                                                        "empty": true,
                                                        "sorted": false,
                                                        "unsorted": true
                                                    },
                                                    "offset": 4,
                                                    "paged": true,
                                                    "unpaged": false
                                                },
                                                "last": true,
                                                "totalElements": 8,
                                                "totalPages": 2,
                                                "size": 4,
                                                "number": 1,
                                                "sort": {
                                                    "empty": true,
                                                    "sorted": false,
                                                    "unsorted": true
                                                },
                                                "first": false,
                                                "numberOfElements": 4,
                                                "empty": false
                                            }
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "no patient has been found",
                    content = @Content),
    })
    @GetMapping("/sanitary/patient")
    public ResponseEntity<Page<PatientDetailsDto>> getAllPatients(@PageableDefault(page=0, size=5) Pageable pageable){
        Page<PatientDetailsDto> pagedResult = sanitaryService.findAllPatients(pageable);

        if (pagedResult.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(pagedResult);
    }

    @GetMapping("/sanitary/patient/{id}")
    public ResponseEntity<PatientDetailsDto> findPatientById(@PathVariable String id) {
        UUID StringToUUID = UUID.fromString(id);
        return sanitaryService.findPatientById(StringToUUID);
    }
    @GetMapping("/sanitary/patient/dependents/{id}")
    public ResponseEntity <List<PatientBasicDataDto>> findDependentsByPatientId(@PathVariable String id) {
        UUID StringToUUID = UUID.fromString(id);
        return ResponseEntity.of(sanitaryService.findDependentsByPatientId(StringToUUID));
    }

}
