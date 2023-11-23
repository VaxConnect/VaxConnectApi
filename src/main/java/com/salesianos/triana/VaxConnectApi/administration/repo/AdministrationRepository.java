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
            (select p.name || ' ' || p.lastName from Patient p where p.id = ?1),
            a.calendarMoment.dosisType || a.calendarMoment.vacune.name,
            a.calendarMoment.date.toString()
            )from Administration a where a.patientUUID = ?1
            """)
    List<GETLastVaccinesAdministratedDTO> findLastVaccineImplementedByUserId(String userId);

    }
