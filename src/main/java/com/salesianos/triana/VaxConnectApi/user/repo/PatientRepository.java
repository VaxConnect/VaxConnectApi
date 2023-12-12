package com.salesianos.triana.VaxConnectApi.user.repo;

import com.salesianos.triana.VaxConnectApi.user.dto.GETUserProfileDetails;
import com.salesianos.triana.VaxConnectApi.user.dto.PatientBasicDataDto;
import com.salesianos.triana.VaxConnectApi.user.dto.GetListYoungestPatients;
import com.salesianos.triana.VaxConnectApi.user.dto.PatientDetailsDto;
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
    Optional<List<String>> findAllDependentsEmailByResponsableEmail(String email);

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
        SELECT new com.salesianos.triana.VaxConnectApi.user.dto.GetListYoungestPatients(p.name || ' ' || p.lastName, p.birthDate) 
        FROM Patient p 
        ORDER BY p.birthDate DESC limit 4
    """)
    List<GetListYoungestPatients> findYoungPatient();

    @Query("""
        SELECT new com.salesianos.triana.VaxConnectApi.user.dto.GetListYoungestPatients(p.name || ' ' || p.lastName, p.birthDate) 
        FROM Patient p 
        ORDER BY p.createdAt DESC limit 4
    """)
    List<GetListYoungestPatients> findLastPatientAded();

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

    @Query("""
            SELECT new com.salesianos.triana.VaxConnectApi.user.dto.GETUserProfileDetails(
                p.name || p.lastName,
                p.email,
                p.dni,
                p.birthDate,
                p.phoneNumber,
                p.fotoUrl
            )
            FROM Patient p2
            LEFT JOIN p2.dependients p
            WHERE p2.id = ?1
            """)
    Optional<List<GETUserProfileDetails>> getFamilyDetails(UUID id);

    @Query("""
            SELECT new com.salesianos.triana.VaxConnectApi.user.dto.PatientDetailsDto(
                    p.id,
                    p.name,
                    p.lastName,
                    p.birthDate,
                    p.dni,
                    p.email,
                    p.phoneNumber,
                    p.fotoUrl
                )
            FROM Patient p
           """)
    Page<PatientDetailsDto> findAllPatients(Pageable pageable);
    @Query("""
                SELECT new com.salesianos.triana.VaxConnectApi.user.dto.PatientDetailsDto(
                    p.id,
                    p.name,
                    p.lastName,
                    p.birthDate,
                    p.dni,
                    p.email,
                    p.phoneNumber,
                    p.fotoUrl
                )
                FROM Patient p
                WHERE p.id = ?1
            """)
    Optional<PatientDetailsDto> findByPatientId(UUID id);

    @Query("""
            SELECT COUNT(d)
            FROM Patient p JOIN p.dependients d
            WHERE p.id = ?1
            """)
    int countDependentsByPatient(UUID id);

    @Query("""
                SELECT COUNT(ic)
                    FROM Patient p JOIN p.inChargeOf ic
                    WHERE p.id = ?1
            """)
    int countPatientsInChargeOf(UUID id);

    @Query("""
             SELECT new com.salesianos.triana.VaxConnectApi.user.dto.PatientDetailsDto(
                    p.id,
                    p.name,
                    p.lastName,
                    p.birthDate,
                    p.dni,
                    p.email,
                    p.phoneNumber,
                    p.fotoUrl
                )
                FROM Patient p
                WHERE p.name ILIKE %:name%
            """)
    Page<PatientDetailsDto> findPatientByName(Pageable pageable, @Param("name") String name);

    Optional<Patient> findByEmail(String email);

}
