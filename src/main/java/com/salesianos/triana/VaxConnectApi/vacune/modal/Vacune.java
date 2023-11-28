package com.salesianos.triana.VaxConnectApi.vacune.modal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Entity
@Getter
@Setter
@Service
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Vacune {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",type = org.hibernate.id.UUIDGenerator.class)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

}
