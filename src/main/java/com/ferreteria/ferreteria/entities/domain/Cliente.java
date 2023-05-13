package com.ferreteria.ferreteria.entities.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "cliente")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cliente extends LowLogic{
    @NotEmpty(message = "{NotNull.valor}")
    @Size(max=120, message = "{Size.Max.nombre}")
    private String nombre;

    @Override
    public String toString(){
        return nombre;
    }
}
