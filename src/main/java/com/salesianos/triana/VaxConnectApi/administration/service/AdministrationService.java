package com.salesianos.triana.VaxConnectApi.administration.service;

import com.salesianos.triana.VaxConnectApi.administration.dto.GETLastVaccinesAdministratedDTO;
import com.salesianos.triana.VaxConnectApi.administration.repo.AdministrationRepository;
import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import com.salesianos.triana.VaxConnectApi.user.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdministrationService {

    private final AdministrationRepository repo;
    private final PatientService patientService;


    public List<GETLastVaccinesAdministratedDTO> findLastVaccineImplementedByUserId (UUID userID){

        Optional<Patient> patient = patientService.findById(userID);
        if(patient.isPresent()){
            if(!patient.get().getDependients().isEmpty()){
                return findAllLastVaccineImplementedByUserId(patient.get());
            }else{
                return repo.findLastVaccineImplementedByUserId(
                        patient.get().getEmail())
                        .stream()
                        .sorted(
                                (x,y)->y.timeOfImplementation().compareTo(x.timeOfImplementation())
                        )
                        .collect(Collectors.toList())
                        .subList(0,4);
            }
        }
        return null;

    }

    public List<GETLastVaccinesAdministratedDTO> findAllLastVaccineImplementedByUserId(Patient patient) {

        Optional<List<String>> listaUuid = patientService.findAllDependentsUUIDByResponsableUUID(patient.getEmail());
        List<GETLastVaccinesAdministratedDTO> list = repo.findLastVaccineImplementedByUserId(patient.getEmail());

        if(listaUuid.isPresent()){
            for (String dependientUuid:listaUuid.get()) {
                list.addAll(repo.findLastVaccineImplementedByUserId(dependientUuid));
            }
            return list.stream()
                    .sorted(
                            (x,y)->y.timeOfImplementation().compareTo(x.timeOfImplementation())
                    ).collect(Collectors.toList())
                    .subList(0,4);
        }else{
            //make an error that tell U that there is something wrong in the code
            return null;
        }
    }


}
