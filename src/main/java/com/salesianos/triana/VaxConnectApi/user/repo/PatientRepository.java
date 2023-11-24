package com.salesianos.triana.VaxConnectApi.user.repo;

import com.salesianos.triana.VaxConnectApi.user.dto.GetListYoungestPatients;
import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
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
    SELECT new com.salesianos.triana.VaxConnectApi.user.dto.GetListYoungestPatients(p.name, p.lastName, p.birthDate) 
    FROM Patient p 
    ORDER BY p.birthDate DESC limit 4
    """)
    List<GetListYoungestPatients> findYoungPatient();




}
