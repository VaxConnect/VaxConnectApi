package com.salesianos.triana.VaxConnectApi.user.repo;

import com.salesianos.triana.VaxConnectApi.user.dto.GetPatientByIdDto;
import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID> {

    boolean existsByEmailIgnoreCase(String email);


    @Query("""
    SELECT b.email FROM Patient a JOIN a.dependients b WHERE a.email = ?1
""")
    Optional<List<String>> findAllDependentsUUIDByResponsableUUID(String email);




    Optional<Patient> findFirstByEmail(String email);

    @Query("""
                SELECT new com.salesianos.triana.VaxConnectApi.user.dto.GetPatientByIdDto(
                    p.id,
                    p.name,
                    p.lastName
                )
                FROM Patient p
                WHERE p.id = ?1
            """)
    Optional<GetPatientByIdDto> findLoggedPatientById(UUID id);



    @Query("""
            SELECT new com.salesianos.triana.VaxConnectApi.user.dto.GetPatientByIdDto(
                    p.id,
                    p.name,
                    p.lastName
                )
            FROM Patient p
           """)
    Page<GetPatientByIdDto> findAllPatients(Pageable pageable);


}
