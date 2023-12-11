package com.salesianos.triana.VaxConnectApi.persistence.dao;

import com.salesianos.triana.VaxConnectApi.persistence.model.DeviceMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DeviceMetadataRepository extends JpaRepository<DeviceMetadata, UUID> {

    List<DeviceMetadata> findByUserId(UUID userId);
}
