package com.salesianos.triana.VaxConnectApi.user.repo;

import com.salesianos.triana.VaxConnectApi.user.dto.GETUserProfileDetails;
import com.salesianos.triana.VaxConnectApi.user.dto.PatientBasicDataDto;
import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import com.sun.security.auth.UnixNumericUserPrincipal;
import io.swagger.v3.oas.models.media.UUIDSchema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID> {

    boolean existsByEmailIgnoreCase(String email);


    @Query("""
        SELECT b.email FROM Patient a JOIN a.dependients b WHERE a.email = ?1
    """)
    Optional<List<String>> findAllDependentsUUIDByResponsableEmail(String email);



    Optional<Patient> findFirstByEmail(String email);

    @Query("""
                SELECT new com.salesianos.triana.VaxConnectApi.user.dto.PatientBasicDataDto(
                    p.id,
                    p.name,
                    p.lastName,
                    p.birthDate,
                    p.dni,
                    p.email
                )
                FROM Patient p
                WHERE p.id = ?1
            """)
    Optional<PatientBasicDataDto> findLoggedPatientById(UUID id);



    @Query("""
            SELECT new com.salesianos.triana.VaxConnectApi.user.dto.PatientBasicDataDto(
                    p.id,
                    p.name,
                    p.lastName,
                    p.birthDate,
                    p.dni,
                    p.email
                )
            FROM Patient p
           """)
    Page<PatientBasicDataDto> findAllPatients(Pageable pageable);

    @Query("""
                SELECT new com.salesianos.triana.VaxConnectApi.user.dto.PatientBasicDataDto(
                    d.id,
                    d.name,
                    d.lastName,
                    d.birthDate,
                    d.dni,
                    d.email
                )
                FROM Patient p
                LEFT JOIN p.dependients d
                WHERE p.id = ?1
            """)
    Optional<List<PatientBasicDataDto>> findDependentsByUserId(UUID id);


    @Query("""
            SELECT new com.salesianos.triana.VaxConnectApi.user.dto.GETUserProfileDetails(
                p.name || p.lastName,
                p.email,
                p.dni,
                p.birthDate,
                p.phoneNumber,
                p.fotoUrl
            )
            FROM Patient p
            WHERE p.id = ?1
            """)
    Optional<GETUserProfileDetails> getUserProfileDetailsById(UUID id);

}
