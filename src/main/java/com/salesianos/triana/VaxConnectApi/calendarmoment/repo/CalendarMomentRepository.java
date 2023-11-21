package com.salesianos.triana.VaxConnectApi.calendarmoment.repo;

import com.salesianos.triana.VaxConnectApi.calendarmoment.modal.CalendarMoment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CalendarMomentRepository extends JpaRepository<CalendarMoment, UUID> {
}
