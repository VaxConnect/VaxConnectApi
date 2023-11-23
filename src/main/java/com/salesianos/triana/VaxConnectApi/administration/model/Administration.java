package com.salesianos.triana.VaxConnectApi.administration.model;

import com.salesianos.triana.VaxConnectApi.calendarmoment.modal.CalendarMoment;
import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Administration {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", type = org.hibernate.id.UUIDGenerator.class)
    private UUID id;


    private LocalDateTime date;

    @Column(name="age_to_administrate")
    //months
    private int ageToAdministrate;

    private String notes;

    @Column(name = "patient_email")
    private String patientEmail;

    @ManyToOne(optional = false)
    @JoinColumn(name = "calendar_moment_id", nullable = false)
    private CalendarMoment calendarMoment;

}
