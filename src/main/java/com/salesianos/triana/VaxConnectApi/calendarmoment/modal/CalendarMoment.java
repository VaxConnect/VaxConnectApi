package com.salesianos.triana.VaxConnectApi.calendarmoment.modal;

import com.salesianos.triana.VaxConnectApi.vacune.modal.Vacune;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacune_id")
    private Vacune vacune;

}
