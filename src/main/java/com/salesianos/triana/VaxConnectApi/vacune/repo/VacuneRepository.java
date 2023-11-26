package com.salesianos.triana.VaxConnectApi.vacune.repo;

import com.salesianos.triana.VaxConnectApi.vacune.dto.GetAllVaccineDto;
import com.salesianos.triana.VaxConnectApi.vacune.modal.Vacune;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface VacuneRepository extends JpaRepository<Vacune, UUID> {
    @Query("""
            SELECT new com.salesianos.triana.VaxConnectApi.vacune.dto.GetAllVaccineDto(
                v.id,
                v.name,
                v.description
            )
            FROM Vacune v
            """)
    Page<GetAllVaccineDto> findAllVaccine(Pageable pageable);
}
