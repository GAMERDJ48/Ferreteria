package com.ferreteria.ferreteria.entities.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tipoarticulo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TipoArticulo extends LowLogic{
    private String nombre;

    public String toString(){
        return nombre;
    }

}
