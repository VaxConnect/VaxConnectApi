package com.salesianos.triana.VaxConnectApi.user.repo;

import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID> {

    boolean existsByEmailIgnoreCase(String email);


    @Query("""
            select p.dependients.id.toString() from Patient p
            """)
    Optional<List<String>> findAllDependentsUUIDByResponsableUUID(String uuid);

    Optional<Patient> findFirstByEmail(String email);


}
