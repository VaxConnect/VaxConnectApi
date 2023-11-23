package com.salesianos.triana.VaxConnectApi.user.modal;

import com.salesianos.triana.VaxConnectApi.administration.model.Administration;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Patient extends User {

    @ManyToMany
    @JoinTable(name = "tbl_patient_dependients",
            joinColumns = @JoinColumn(name = "responsable_id"),
            inverseJoinColumns = @JoinColumn(name = "dependient_id"))
    private List<Patient> dependients = new ArrayList<>();

    @ManyToMany(mappedBy = "dependients")
    private List<Patient> inChargeOf;

}
