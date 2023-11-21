package com.salesianos.triana.VaxConnectApi.administration.repo;

import com.salesianos.triana.VaxConnectApi.administration.model.Administration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdministrationRepository extends JpaRepository<Administration, UUID> {
}
