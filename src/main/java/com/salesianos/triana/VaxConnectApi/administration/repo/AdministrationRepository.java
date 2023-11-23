package com.salesianos.triana.VaxConnectApi.administration.repo;

import com.salesianos.triana.VaxConnectApi.administration.dto.GETLastVaccinesAdministratedDTO;
import com.salesianos.triana.VaxConnectApi.administration.model.Administration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface AdministrationRepository extends JpaRepository<Administration, UUID> {

    @Query("""
            select new com.salesianos.triana.VaxConnectApi.administration.dto.GETLastVaccinesAdministratedDTO(
                (select p.name || ' ' || p.lastName from Patient p where p.email = ?1),
                cm.dosisType || v.name,
                a.date
            ) from Administration a 
            left join a.calendarMoment as cm
            left join cm.vacune as v
            where a.patientEmail = ?1
            """)
    List<GETLastVaccinesAdministratedDTO> findLastVaccineImplementedByUsermail(String email);

    @Query("""
            select cm.id 
            from Administration a
            left join a.calendarMoment as cm
            where a.patientEmail = ?1
            """)
    List<UUID> findIdsOfCalendarsMomentsWhoAreImplementedByPatientEmail(String email);


    }



