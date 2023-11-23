package com.salesianos.triana.VaxConnectApi.administration.service;

import com.salesianos.triana.VaxConnectApi.administration.dto.GETLastVaccinesAdministratedDTO;
import com.salesianos.triana.VaxConnectApi.administration.repo.AdministrationRepository;
import com.salesianos.triana.VaxConnectApi.user.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdministrationService {

    private final AdministrationRepository repo;
    private final PatientService patientService;


    public List<GETLastVaccinesAdministratedDTO> findLastVaccineImplementedByUserId (String userID){

        if(patientService.existsById(userID))
            if(patientService.hasDependients(userID)){
                return findAllLastVaccineImplementedByUserId(userID);
            }else{
                return repo.findLastVaccineImplementedByUserId(userID);
            }

        //Administrate error
        return null;


    }

    public List<GETLastVaccinesAdministratedDTO> findAllLastVaccineImplementedByUserId(String userId){

        Optional<List<String>> listaUuid = patientService.findAllDependentsUUIDByResponsableUUID(userId);
        List<GETLastVaccinesAdministratedDTO> lastVaccinesAdministratedDTOS =  repo.findLastVaccineImplementedByUserId(userId);

        if(listaUuid.isPresent()){
            for (String dependientUuid:listaUuid.get()) {
                lastVaccinesAdministratedDTOS.addAll(repo.findLastVaccineImplementedByUserId(dependientUuid));
            }
            return lastVaccinesAdministratedDTOS;
        }else{
            //make an error that tell U that there is something wrong in the code
            return null;
        }



    }



}
