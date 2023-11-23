package com.salesianos.triana.VaxConnectApi.calendarmoment.repo;

import com.salesianos.triana.VaxConnectApi.calendarmoment.dto.GETNextVaccinesToAdministrateDTO;
import com.salesianos.triana.VaxConnectApi.calendarmoment.modal.CalendarMoment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CalendarMomentRepository extends JpaRepository<CalendarMoment, UUID> {

    @Query("""
            SELECT a.id FROM CalendarMoment a
            """)
    List<UUID> findAllIdOfCalendarMoments();

    @Query("""
            select new com.salesianos.triana.VaxConnectApi.calendarmoment.dto.GETNextVaccinesToAdministrateDTO(
            (select p.name || ' ' || p.lastName from Patient p where p.email = ?2),
                cm.dosisType || v.name || 'month('|| cm.age || ')'
            ) from CalendarMoment cm 
            left join cm.vacune as v
            where cm.id = ?1
            """)
    GETNextVaccinesToAdministrateDTO getNextVaccinesToAdministrateDTOFromCmId(UUID uuid,String email);


}
