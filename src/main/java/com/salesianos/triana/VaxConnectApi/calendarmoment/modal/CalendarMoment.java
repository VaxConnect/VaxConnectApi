package com.salesianos.triana.VaxConnectApi.calendarmoment.modal;

import com.salesianos.triana.VaxConnectApi.Vacune.modal.Vacune;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CalendarMoment {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",type = org.hibernate.id.UUIDGenerator.class)
    private UUID id;

    //month
    private int age;

    @Column(name = "dosis_type")
    private String dosisType;

    private String recomendations;

    private String discriminants;

    @OneToMany
    @JoinColumn(name = "calendar_moment_id", nullable = false)
    private Set<Vacune> vacunes = new LinkedHashSet<>();

}
