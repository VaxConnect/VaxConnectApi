package com.salesianos.triana.VaxConnectApi.user.repo;

import com.salesianos.triana.VaxConnectApi.user.modal.Sanitary;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.rmi.server.UID;
import java.util.Optional;
import java.util.UUID;

public interface SanitaryRepository extends JpaRepository<Sanitary, UUID> {
    boolean existsByEmailIgnoreCase(String email);
    Optional<Sanitary> findFirstByEmail(String email);
}
