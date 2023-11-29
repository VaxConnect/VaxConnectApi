package com.salesianos.triana.VaxConnectApi.vacune.controller;

import com.salesianos.triana.VaxConnectApi.user.dto.PatientBasicDataDto;
import com.salesianos.triana.VaxConnectApi.vacune.dto.CreateVacuneDto;
import com.salesianos.triana.VaxConnectApi.vacune.dto.GetAllVaccineDto;
import com.salesianos.triana.VaxConnectApi.vacune.repo.VacuneRepository;
import com.salesianos.triana.VaxConnectApi.vacune.service.VacuneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/vacune")
public class VacuneController {

    private final VacuneRepository vacuneRepository;
    private final VacuneService vacuneService;

    @Operation(summary = "Get all vaccine")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="200",
                    description = "Vaccine found",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetAllVaccineDto.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            "content": [
                                                    {
                                                        "id": "a35e71c5-1c5d-4d0b-bb6f-4e4451e4693e",
                                                        "name": "Gripe (Influenza) Vaccine",
                                                        "description": "The influenza vaccine, commonly known as the flu shot, provides protection against the influenza viruses that circulate each flu season. It is recommended for everyone, especially those at higher risk of complications."
                                                    }
                                                ],
                                                "pageable": {
                                                    "pageNumber": 1,
                                                    "pageSize": 10,
                                                    "sort": {
                                                        "sorted": false,
                                                        "empty": true,
                                                        "unsorted": true
                                                    },
                                                    "offset": 10,
                                                    "paged": true,
                                                    "unpaged": false
                                                },
                                                "totalPages": 2,
                                                "totalElements": 11,
                                                "last": true,
                                                "number": 1,
                                                "size": 10,
                                                "numberOfElements": 1,
                                                "sort": {
                                                    "sorted": false,
                                                    "empty": true,
                                                    "unsorted": true
                                                },
                                                "first": false,
                                                "empty": false
                                            }
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No vaccine found",
                    content = @Content),
    })
    @GetMapping("/all")
    private ResponseEntity<Page<GetAllVaccineDto>> getAllVaccine(@PageableDefault(page=0, size=10)Pageable pageable) {
        Page<GetAllVaccineDto> pagedResult = vacuneService.findAllVaccine(pageable);

        if(pagedResult.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(pagedResult);
    }

    @Operation(summary = "Get a vacune by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="200",
                    description = "Vacune found",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetAllVaccineDto.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            "content": 
                                                    {
                                                        "id": "8b341c8c-f420-4e86-b9f8-c5e69664a955",
                                                        "name": "vacuna de la polio",
                                                        "description": "muy buena description"
                                                    }
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "Not found",
                    content = @Content),
    })
    @GetMapping("/info/{id}")
    private ResponseEntity<GetAllVaccineDto> getVacuneById(@PathVariable String id) {
        UUID idValido = UUID.fromString(id);
        return vacuneService.findVacuneById(idValido);
    }

    @Operation(summary = "Search Vaccine by its name patern")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="200",
                    description = "Vaccine found",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetAllVaccineDto.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            "content": [
                                                    {
                                                        "id": "8f7673f1-a763-479f-844c-16a2a87faed7",
                                                        "name": "Poliomielitis Vaccine",
                                                        "description": "The polio vaccine is designed to protect against poliomyelitis, a highly contagious viral infection that can lead to paralysis. It has played a crucial role in the global effort to eradicate polio."
                                                    },
                                                    {
                                                        "id": "8b341c8c-f420-4e86-b9f8-c5e69664a955",
                                                        "name": "vacuna de la polio",
                                                        "description": "muy buena description"
                                                    }
                                                ],
                                                "pageable": {
                                                    "pageNumber": 0,
                                                    "pageSize": 10,
                                                    "sort": {
                                                        "sorted": false,
                                                        "empty": true,
                                                        "unsorted": true
                                                    },
                                                    "offset": 0,
                                                    "paged": true,
                                                    "unpaged": false
                                                },
                                                "totalPages": 1,
                                                "totalElements": 2,
                                                "last": true,
                                                "number": 0,
                                                "size": 10,
                                                "numberOfElements": 2,
                                                "sort": {
                                                    "sorted": false,
                                                    "empty": true,
                                                    "unsorted": true
                                                },
                                                "first": true,
                                                "empty": false
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "Not found",
                    content = @Content),
    })
    @GetMapping("/search/{name}")
    private ResponseEntity<Page<GetAllVaccineDto>> getVaccineBySearchParameter(@PageableDefault(page=0, size=10)Pageable pageable,
                                                                               @PathVariable String name) {
        String fullString = UriUtils.decode(name, "UTF-8");
        fullString = fullString.replace("%20", " ");
        return ResponseEntity.ok(vacuneService.findVaccineBySearchParameter(pageable, fullString));
    }
    @Operation(summary = "Create a new Vacune")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="200",
                    description = "Vaccine created",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetAllVaccineDto.class))
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Bad request",
                    content = @Content),
    })
    @PostMapping("/new")
    private ResponseEntity<GetAllVaccineDto> createVacune(@RequestBody CreateVacuneDto create) {
        return vacuneService.createVacune(create);
    }

    @Operation(summary = "Edit an existin vacune by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="200",
                    description = "Vaccine found",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetAllVaccineDto.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                 "id": "8b341c8c-f420-4e86-b9f8-c5e69664a955",
                                                 "name": "polio",
                                                 "description": "changed description"
                                            }
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Bad Request",
                    content = @Content),
    })
    @PutMapping("/edit/{id}")
    private ResponseEntity<Optional<GetAllVaccineDto>> EditVacune(@PathVariable String id, @RequestBody CreateVacuneDto edit) {
        return vacuneService.editVacune(UUID.fromString(id), edit);
    }

    @Operation(summary = "Delete an existin vacune by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="200",
                    description = "Vaccine found",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetAllVaccineDto.class))
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "Not found",
                    content = @Content),
    })
    @DeleteMapping("/delete/{id}")
    private ResponseEntity<GetAllVaccineDto> DeleteVacune(@PathVariable String id) {
        return vacuneService.deleteVacune(UUID.fromString(id));
    }
}