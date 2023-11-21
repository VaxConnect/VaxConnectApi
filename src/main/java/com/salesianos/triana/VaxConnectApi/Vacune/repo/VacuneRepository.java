package com.salesianos.triana.VaxConnectApi.Vacune.repo;

import com.salesianos.triana.VaxConnectApi.Vacune.modal.Vacune;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VacuneRepository extends JpaRepository<Vacune, UUID> {
}
