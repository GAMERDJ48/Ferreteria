package com.ferreteria.ferreteria.entities.domain;

import com.ferreteria.ferreteria.entities.enumerations.EstadoVenta;
import com.ferreteria.ferreteria.services.util.FechaUtil;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "venta")
@Getter
@Setter
@AllArgsConstructor
public class Venta extends Base{
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "{NotNull.fecha}")
    private LocalDateTime fecha;

    private BigDecimal importe;
    private BigDecimal descuento;

    private EstadoVenta estadoVenta;

    @PositiveOrZero
    private BigDecimal pagado;

    @ManyToOne
    Cliente cliente;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval=true, fetch = FetchType.EAGER)
    private List<DetalleVenta> detalles;

    public String getFechaString(){
        return FechaUtil.formatearLocalDateTime(this.fecha);
    }

    public void verificarEstadoVenta(){
        this.estadoVenta = (this.pagado.compareTo(this.importe)>=0) ? EstadoVenta.PAGADO : EstadoVenta.SIN_PAGAR;
    }

    public BigDecimal pagar(@NotNull  BigDecimal monto){
        BigDecimal vuelto=new BigDecimal(0);
        this.pagado = this.pagado.add(monto);
        if(this.pagado.compareTo(this.importe)>=0) {
            vuelto=this.pagado.subtract(this.importe);
            this.pagado = this.importe;
            this.estadoVenta=EstadoVenta.PAGADO;
        }
        return vuelto;
    }

    public Venta() {
        importe = new BigDecimal(0);
        descuento = new BigDecimal(0);
        pagado = new BigDecimal(0);
        fecha = LocalDateTime.now();
        estadoVenta = EstadoVenta.SIN_PAGAR;
    }

    public String toString(){
        return "Venta";
    }
}
