package com.salesianos.triana.VaxConnectApi.user.modal;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Sanitary extends User{
private String puesto;
}
