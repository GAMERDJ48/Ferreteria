package com.ferreteria.ferreteria.entities.wrappers;

import com.ferreteria.ferreteria.entities.domain.DetalleVenta;
import com.ferreteria.ferreteria.entities.domain.Venta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class VentaWrapper {
    private Venta venta;
    private List<DetalleVenta> detalles;

    public VentaWrapper(){
        this.venta = new Venta();
        this.detalles = new ArrayList<>();
    }


}
