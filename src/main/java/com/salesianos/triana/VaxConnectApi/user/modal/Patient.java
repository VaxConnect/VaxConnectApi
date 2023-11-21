package com.salesianos.triana.VaxConnectApi.user.modal;

import com.salesianos.triana.VaxConnectApi.administration.model.Administration;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Patient extends User {

    @ManyToMany
    @JoinTable(name = "tbl_patients",
    joinColumns = @JoinColumn(name = "responsable"),
    inverseJoinColumns = @JoinColumn(name = "dependient"))
    private List<Patient> dependients;

    @ManyToMany
    @JoinTable(name = "tbl_patients",
            joinColumns = @JoinColumn(name = "responsable"),
            inverseJoinColumns = @JoinColumn(name = "dependient"))
    private List<Patient> inChargeOf;

    @OneToMany(mappedBy = "patient", orphanRemoval = true)
    private Set<Administration> administrations = new LinkedHashSet<>();


}
