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
            select b.id.toString() from Patient a join fetch a.dependients as b where a.id.toString() = ?1
            """)
    Optional<List<String>> findAllDependentsUUIDByResponsableUUID(String uuid);

    Optional<Patient> findFirstByEmail(String email);


}
