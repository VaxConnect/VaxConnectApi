package com.salesianos.triana.VaxConnectApi.user.repo;

import com.salesianos.triana.VaxConnectApi.user.dto.GetListOfSanitaries;
import com.salesianos.triana.VaxConnectApi.user.modal.Sanitary;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.rmi.server.UID;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SanitaryRepository extends JpaRepository<Sanitary, UUID> {
    boolean existsByEmailIgnoreCase(String email);
    Optional<Sanitary> findFirstByEmail(String email);

    @Query("""
    SELECT new com.salesianos.triana.VaxConnectApi.user.dto.GetListOfSanitaries(
        s.name,
        s.email,
        s.birthDate
    )
    FROM Sanitary s
   
""")
    List<GetListOfSanitaries> getList();

    @Query("""
            SELECT new com.salesianos.triana.VaxConnectApi.user.dto.GetListOfSanitaries(
        s.name,
        s.email,
        s.birthDate
    )
    FROM Sanitary s
     WHERE s.email = ?1
            """)

    Optional<GetListOfSanitaries> getsanitaryByName(String name);




}
