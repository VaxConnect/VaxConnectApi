package com.salesianos.triana.VaxConnectApi.vaccine.repo;

import com.salesianos.triana.VaxConnectApi.vaccine.modal.Vacune;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VacuneRepository extends JpaRepository<Vacune, UUID> {
}
