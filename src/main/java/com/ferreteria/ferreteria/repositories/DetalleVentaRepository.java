package com.ferreteria.ferreteria.repositories;

import com.ferreteria.ferreteria.entities.domain.Articulo;
import com.ferreteria.ferreteria.entities.domain.DetalleVenta;
import com.ferreteria.ferreteria.entities.domain.Venta;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleVentaRepository extends BaseRepository<DetalleVenta, Long> {
    DetalleVenta findByArticuloAndVenta(Articulo articulo, Venta venta);
}
