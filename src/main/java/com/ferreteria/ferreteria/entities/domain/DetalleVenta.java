package com.ferreteria.ferreteria.entities.domain;

import com.ferreteria.ferreteria.services.util.ConversionUtil;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detalleventa")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DetalleVenta extends Base{
    private int cantidad;
    private BigDecimal precioUnitario;
    @ManyToOne
    private Articulo articulo;

    private BigDecimal subTotal;

    @ManyToOne(optional = false, cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_venta")
    private Venta venta;

    public void setSubTotal(){
        this.subTotal = this.precioUnitario.multiply(new BigDecimal(this.cantidad));
        ConversionUtil.setScaleBigDecimal(this.subTotal);
        ConversionUtil.setScaleBigDecimal(this.precioUnitario);
    }
}
