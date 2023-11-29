package com.salesianos.triana.VaxConnectApi.administration.controller;

import com.salesianos.triana.VaxConnectApi.administration.dto.GETVaccineAdministratedOnCalendar;
import com.salesianos.triana.VaxConnectApi.administration.dto.GETLastVaccinesAdministratedDTO;
import com.salesianos.triana.VaxConnectApi.administration.dto.GETPatientCalendarDTO;
import com.salesianos.triana.VaxConnectApi.administration.dto.POSTAdministrationDTO;
import com.salesianos.triana.VaxConnectApi.administration.service.AdministrationService;
import com.salesianos.triana.VaxConnectApi.user.exception.EmailListNotFoundException;
import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patient/administration")
public class AdministrationController {

    private final AdministrationService service;


    @Operation(summary = "Get last vaccines implemented")
    @ApiResponses(value = {
             @ApiResponse(responseCode = "200",
             description = "Last Vaccines implemented was found",
             content = {
                     @Content(mediaType = "application/json",
                     array = @ArraySchema(schema = @Schema(implementation = GETLastVaccinesAdministratedDTO.class)),
                             examples = {@ExampleObject
                                     (value = """
                                             {
                                             [
                                                 {
                                                     "completeName": "Maria Rodriguez",
                                                     "vaccineType": "DTPA7Difteria, Tétanos, Tosferina (DTP) Vaccine",
                                                     "timeOfImplementation": "2037-01-25T14:15:00"
                                                 },
                                                 {
                                                     "completeName": "Maria Rodriguez",
                                                     "vaccineType": "VPHVirus del Papiloma Humano (HPV) Vaccine",
                                                     "timeOfImplementation": "2036-10-15T13:30:00"
                                                 },
                                                 {
                                                     "completeName": "Maria Rodriguez",
                                                     "vaccineType": "DTPA5Difteria, Tétanos, Tosferina (DTP) Vaccine",
                                                     "timeOfImplementation": "2029-01-20T10:30:00"
                                                 },
                                                 {
                                                     "completeName": "Maria Rodriguez",
                                                     "vaccineType": "MenB MenC MenACWYEnfermedad Meningocócica Vaccine",
                                                     "timeOfImplementation": "2029-01-15T14:30:00"
                                                 }
                                             ]
                                             }
                                             """
                                     )}
                     )})
    })
    @GetMapping("/findLastVaccineImplementedByUserId/")
    public ResponseEntity<List<GETLastVaccinesAdministratedDTO>> findLastVaccineImplementedByUserId (@AuthenticationPrincipal Patient patient) throws EmailListNotFoundException {
            return ResponseEntity.status(200).body(service.findLastVaccineImplementedByUserId(patient.getId()));
    }


    @Operation(summary = "Get all vaccines implemented")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "All Vaccines implemented was found",
                    content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = GETVaccineAdministratedOnCalendar.class)),
                                    examples = {@ExampleObject
                                            (value = """
                                                    {
                                                           [
                                                               {
                                                                   "id": "d3568c4e-db6d-4422-9e14-e48e4e061b25",
                                                                   "nameVacune": "Gripe (Influenza) Vaccine",
                                                                   "typeOfDosys": "Flu",
                                                                   "month": 0
                                                               },
                                                               {
                                                                   "id": "56511882-41a2-4f3a-a11c-9ba9e1cab6ce",
                                                                   "nameVacune": "Gripe (Influenza) Vaccine",
                                                                   "typeOfDosys": "Annual Flu",
                                                                   "month": 6
                                                               }
                                                           ]
                                                    }
                                                    """
                                            )}
                            )})
    })
    @GetMapping("/findAllImplementedVaccines/")
    public ResponseEntity<List<GETVaccineAdministratedOnCalendar>> findAllAdministration (@AuthenticationPrincipal Patient patient){
        return ResponseEntity.status(200).body(service.getAllVaccinesImplementedDTO(patient.getId()));
    }


    @Operation(summary = "Get calendar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Get calendar",
                    content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = GETPatientCalendarDTO.class)),
                                    examples = {@ExampleObject
                                            (value = """
                                                    {
                                                           [
                                                               {
                                                                   "id": "4fb54b4e-4522-4f79-9617-70849ac7ef95",
                                                                   "fullname": "Maria Rodriguez",
                                                                   "age": "459",
                                                                   "allVaccine": [
                                                                       {
                                                                           "id": "4fb54b4e-4522-4f79-9617-70849ac7ef95",
                                                                           "nameVacune": "Poliomielitis Vaccine",
                                                                           "getMomentToImplementVacuneDTOS": [
                                                                               {
                                                                                   "id": "f0ab4d64-4f43-4111-93c2-0ef46eec5cfb",
                                                                                   "monthToImplement": 2,
                                                                                   "typeDosis": "VP1",
                                                                                   "isImplemented": true
                                                                               }
                                                                           ]
                                                                       }
                                                                   ]
                                                               }
                                                           ]
                                                    }
                                                    """
                                            )}
                            )})
    })
    @GetMapping("/getCalendar/")
    public ResponseEntity<List<GETPatientCalendarDTO>> getCalendar(@AuthenticationPrincipal Patient patient){
        return ResponseEntity.status(200).body(service.getFamilyCalendar(patient.getId()));
    }

}
