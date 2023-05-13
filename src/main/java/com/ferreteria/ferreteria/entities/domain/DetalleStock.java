package com.ferreteria.ferreteria.entities.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "detallestock")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DetalleStock extends Base{
    private int cantidad;
    @OneToOne
    private Articulo articulo;
}
