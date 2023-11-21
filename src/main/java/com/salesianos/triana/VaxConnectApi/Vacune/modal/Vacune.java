package com.salesianos.triana.VaxConnectApi.Vacune.modal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Entity
@Data
@Getter
@Service
@AllArgsConstructor
@NoArgsConstructor
public class Vacune {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",type = org.hibernate.id.UUIDGenerator.class)
    private UUID id;

    private String name;

    private String description;

}
