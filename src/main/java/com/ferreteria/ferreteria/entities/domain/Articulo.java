package com.ferreteria.ferreteria.entities.domain;

import com.ferreteria.ferreteria.entities.enumerations.Moneda;
import com.ferreteria.ferreteria.entities.enumerations.Proveedor;
import com.ferreteria.ferreteria.services.DolarService;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "articulo")
@Getter
@Setter
@AllArgsConstructor
@ToString
public class Articulo extends LowLogic{
    @NotEmpty(message = "{NotNull.valor}")
    private String codigo;
    @NotEmpty(message = "{NotNull.valor}")
    @Size(max=120, message = "{Size.Max.nombre}")
    private String nombre;

    @PositiveOrZero(message = "{PositiveOrZero}")
    private BigDecimal precio;

    private Moneda moneda;

    private Proveedor proveedor;

    @ManyToOne
    private TipoArticulo tipoArticulo;

    public Articulo(){
        precio=new BigDecimal(0);
    }

}
