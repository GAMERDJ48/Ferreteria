package com.ferreteria.ferreteria.entities.filtro;

import com.ferreteria.ferreteria.entities.domain.Cliente;
import com.ferreteria.ferreteria.entities.enumerations.EstadoVenta;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Hashtable;

@Getter
@Setter
public class VentaFiltro extends ArticuloFiltro{
    private boolean modoVenta;
    private EstadoVenta estadoVenta;
    private Cliente cliente;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime fechaDesde;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime fechaHasta;

    @Override
    public String toQuerryString() {
        String cadena = super.toQuerryString();
        Hashtable<String,String> variables = new Hashtable<>();
        variables.put("modoVenta",convertObjectToString(this.modoVenta));
        variables.put("estadoVenta",convertObjectToString(this.estadoVenta));
        variables.put("cliente",convertObjectToString(this.cliente));
        variables.put("fechaDesde",convertObjectToString(this.fechaDesde));
        variables.put("fechaHasta",convertObjectToString(this.fechaHasta));
        return cadena+constructQuerry(variables);
    }
}
