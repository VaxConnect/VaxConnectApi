package com.salesianos.triana.VaxConnectApi.user.repo;

import com.salesianos.triana.VaxConnectApi.user.dto.GetVaccinesMoreAdministrated;
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
    @Query("""
SELECT
new com.salesianos.triana.VaxConnectApi.user.dto.GetVaccinesMoreAdministrated(
count(v.name),
a.date
)
FROM Administration a
left join a.calendarMoment as cm
left join cm.vacune as v
group by v.name,a.date

""")
    List<GetVaccinesMoreAdministrated> getVaccinesMoreAdministrated();

    Optional<Sanitary> findFirstByEmail(String email);





}
