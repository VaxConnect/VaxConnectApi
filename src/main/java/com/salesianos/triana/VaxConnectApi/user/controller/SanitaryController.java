package com.salesianos.triana.VaxConnectApi.user.controller;

import com.salesianos.triana.VaxConnectApi.administration.dto.POSTAdministrationDTO;
import com.salesianos.triana.VaxConnectApi.calendarmoment.dto.POSTCalendarMoment;
import com.salesianos.triana.VaxConnectApi.security.jwt.JwtProvider;
import com.salesianos.triana.VaxConnectApi.user.dto.*;
import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import com.salesianos.triana.VaxConnectApi.user.modal.Sanitary;
import com.salesianos.triana.VaxConnectApi.user.service.PatientService;
import com.salesianos.triana.VaxConnectApi.user.service.SanitaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

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
    private final PatientService patientService;




    @GetMapping("/sanitary/patients/young")
    public ResponseEntity<List<GetListYoungestPatients>> listYoungestPatients(@AuthenticationPrincipal Sanitary sanitary){
    List<GetListYoungestPatients> youngest = sanitaryService.listYoungestPatients();
    return ResponseEntity.ok(youngest);
    }
    @GetMapping("/sanitary/list")
    public ResponseEntity<List<GetListOfSanitaries>> getList(@AuthenticationPrincipal Sanitary sanitary){
        List<GetListOfSanitaries> getListOfSanitaries = sanitaryService.listOfSanitaries();
        return ResponseEntity.ok(getListOfSanitaries);
    }


    @PostMapping("/sanitary/calendarMoment/create/")
    public ResponseEntity<?> createCalendarMoment(@Valid @RequestBody POSTCalendarMoment postCalendarMoment){
        sanitaryService.createCalendarMoment(postCalendarMoment);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/sanitary/administration/create/")
    public ResponseEntity<?> createAdministration(@Valid @RequestBody POSTAdministrationDTO postAdministrationDTO){
        sanitaryService.createAdministration(postAdministrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/auth/register/sanitary")
    public ResponseEntity<UserResponse> createSanitary(@RequestBody CreateUserRequest createUserRequest) {
        Sanitary sanitary = sanitaryService.createSanitaryWithRole(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.fromSanitary(sanitary));
    }
    @PostMapping("/auth/login/sanitary")
    public ResponseEntity<JwtUserResponse> loginSanitary(@RequestBody LoginRequest loginRequest) {
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
                .body(JwtUserResponse.ofSanitary(sanitary1, token));
    }

    @GetMapping("/sanitary/{email}")
    public ResponseEntity<Optional<GetSanitaryByEmail>> findByEmail(@PathVariable String email){
        Optional<GetSanitaryByEmail> getListOfSanitaries = sanitaryService.findByEmailDto(email);
        return ResponseEntity.ok(getListOfSanitaries);
    }




    @GetMapping("/sanitary/patients/last/")
    public ResponseEntity<List<GetListYoungestPatients>> listLastPatients(@AuthenticationPrincipal Sanitary sanitary) {
        List<GetListYoungestPatients> youngest = sanitaryService.findLastAddedPatient();
        return ResponseEntity.ok(youngest);
    }



    @Operation(summary = "Get all patients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Patients have been found",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PatientDetailsDto.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "content": [
                                                    {
                                                                 "id": "6d31c447-aab3-4bcb-883c-7ee0ecea9151",
                                                                 "name": "manolo",
                                                                 "lastName": "manoles",
                                                                 "birthDate": "1990-10-12",
                                                                 "dni": "123456789",
                                                                 "email": "manolo@gamil.com",
                                                                 "phoneNumber": 123456789,
                                                                 "fotoUrl": "foto.url"
                                                             },
                                                             {
                                                                 "id": "66690825-6145-470c-b5a8-18bf386f1ceb",
                                                                 "name": "a",
                                                                 "lastName": "manoles",
                                                                 "birthDate": "2004-10-12",
                                                                 "dni": "123456789",
                                                                 "email": "a@gamil.com",
                                                                 "phoneNumber": 123456789,
                                                                 "fotoUrl": "foto.url"
                                                             },
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
    public ResponseEntity<Page<PatientDetailsDto>> getAllPatients(@PageableDefault(page = 0, size = 8) Pageable pageable) {
        Page<PatientDetailsDto> pagedResult = sanitaryService.findAllPatients(pageable);

        if (pagedResult.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(pagedResult);
    }


    @Operation(summary = "Get patient by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Patient has been found",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PatientDetailsDto.class)),
                            examples = {@ExampleObject(
                                    value = """
                                                {
                                                        "id": "66690825-6145-470c-b5a8-18bf386f1ceb",
                                                        "name": "a",
                                                        "lastName": "manoles",
                                                        "birthDate": "2004-10-12",
                                                        "dni": "123456789",
                                                        "email": "a@gamil.com",
                                                        "phoneNumber": 123456789,
                                                        "fotoUrl": "foto.url"
                                                    }
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "Patient hasnt been found",
                    content = @Content)
    })
    @GetMapping("/sanitary/patient/{id}")
    public ResponseEntity<PatientDetailsDto> findPatientById(@PathVariable String id) {
        UUID StringToUUID = UUID.fromString(id);
        return sanitaryService.findPatientById(StringToUUID);
    }

    @Operation(summary = "Get dependents by patient id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Dependents has been found",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PatientBasicDataDto.class)),
                            examples = {@ExampleObject(
                                    value = """
                                               {
                                                             "id": "ca5c4309-ca83-44a8-a377-ecdf92bd4370",
                                                                 "name": "m",
                                                                 "lastName": "manoles",
                                                                 "birthDate": "2011-10-12",
                                                                 "dni": "123456789",
                                                                 "email": "m@gamil.com"
                                                         },
                                                         {
                                                             "id": "fd58c6ed-cd01-4811-ba30-9c284bf4dc3b",
                                                                 "name": "Juan",
                                                                 "lastName": "Martinez Rodriguez",
                                                                 "birthDate": "2023-09-07",
                                                                 "dni": "555667788",
                                                                 "email": "juan@gmail.com"
                                                         }
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "Dependents dont found",
                    content = @Content)
    })
    @GetMapping("/sanitary/patient/dependents/{id}")
    public ResponseEntity<List<PatientBasicDataDto>> findDependentsByPatientId(@PathVariable String id) {
        UUID StringToUUID = UUID.fromString(id);
        return ResponseEntity.of(sanitaryService.findDependentsByPatientId(StringToUUID));
    }

    @Operation(summary = "Create new patient with dependents associated")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="201",
                    description = "Patient has been created",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PatientDetailsWithDependentsDto.class)),
                            examples = {@ExampleObject(
                                    value = """
                                                {
                                                    "id": "6ebe29c7-aeb7-44d8-ba6c-23fd68d6666e",
                                                    "name": "bb",
                                                    "lastName": "aaa",
                                                    "birthDate": "2023-09-30",
                                                    "dni": "asaaaaa",
                                                    "email": "vvv@gmail.com",
                                                    "phoneNumber": 332,
                                                    "fotoUrl": "https://example.com/aa.jpg",
                                                    "dependents": [
                                                        {
                                                            "id": "b70b274d-ad73-41b2-bd8a-c0ba1dd83983",
                                                            "name": "manolo",
                                                            "lastName": "manoles",
                                                            "birthDate": "1990-10-12",
                                                            "dni": "123456789",
                                                            "email": "manolo@gamil.com",
                                                            "phoneNumber": 123456789,
                                                            "fotoUrl": "foto.url"
                                                        },
                                                        {
                                                            "id": "03fb0513-684e-4711-9906-23fbc4ff341e",
                                                            "name": "a",
                                                            "lastName": "manoles",
                                                            "birthDate": "2004-10-12",
                                                            "dni": "123456789",
                                                            "email": "a@gamil.com",
                                                            "phoneNumber": 123456789,
                                                            "fotoUrl": "foto.url"
                                                        }
                                                    ]
                                                }
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Cant create the patient",
                    content = @Content),
    })
    @PostMapping("/sanitary/patient")
    public ResponseEntity<PatientDetailsWithDependentsDto> createPatient(@RequestBody CreatePatientDto newPatient) {
        Patient patient = patientService.createPatient(newPatient);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PatientDetailsWithDependentsDto.of(patient));
        //el email debe ser unico crear una excepcion para eso
    }

    @Operation(summary = "Delete patient by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Patient delete successfully",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Cant delete patients with dependents",
                    content = @Content
            )
    })
    @DeleteMapping("/sanitary/patient/{id}")
    public ResponseEntity<?> deleteByPatientId(@PathVariable String id){
        patientService.deleteByPatientId(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get patient by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Patient has been found",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PatientDetailsDto.class)),
                            examples = {@ExampleObject(
                                    value = """
                                               {
                                                             "id": "ca5c4309-ca83-44a8-a377-ecdf92bd4370",
                                                                 "name": "j",
                                                                 "lastName": "manoles",
                                                                 "birthDate": "2011-10-12",
                                                                 "dni": "123456789",
                                                                 "email": "m@gamil.com"
                                                         },
                                                         {
                                                             "id": "fd58c6ed-cd01-4811-ba30-9c284bf4dc3b",
                                                                 "name": "Juan",
                                                                 "lastName": "Martinez Rodriguez",
                                                                 "birthDate": "2023-09-07",
                                                                 "dni": "555667788",
                                                                 "email": "juan@gmail.com"
                                                         }
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "Patient hasnt been found",
                    content = @Content)
    })
    @GetMapping("/sanitary/search/{name}")
    private ResponseEntity<Page<PatientDetailsDto>> findPatientByName(@PageableDefault(page=0, size=10)Pageable pageable,
                                                                      @PathVariable String name) {
        String validString = UriUtils.decode(name, "UTF-8");
        validString = validString.replace("%20", " ");
        Page<PatientDetailsDto> findPatients = patientService.findPatientByName(pageable, validString);

        if (findPatients.isEmpty())
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(findPatients);
    }

    @Operation(summary = "Edit patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="201",
                    description = "Patient has been edited",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PatientDetailsWithDependentsDto.class)),
                            examples = {@ExampleObject(
                                    value = """
                                                {
                                                        "id": "66690825-6145-470c-b5a8-18bf386f1ceb",
                                                        "name": "a",
                                                        "lastName": "manoles",
                                                        "birthDate": "2004-10-12",
                                                        "dni": "123456789",
                                                        "email": "a@gamil.com",
                                                        "phoneNumber": 123456789,
                                                        "fotoUrl": "foto.url"
                                                    }
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "Patient not found",
                    content = @Content),
    })
    @PutMapping("/sanitary/patient/{id}")
    public ResponseEntity<PatientDetailsDto> editPatientById(@PathVariable String id, @RequestBody PatientDetailsDto newPatient) {
        UUID StringToUUID = UUID.fromString(id);
        PatientDetailsDto patient = patientService.editPatientById(StringToUUID, newPatient);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(patient);
    }


}
