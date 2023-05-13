package com.ferreteria.ferreteria.entities.filtro;

import com.ferreteria.ferreteria.entities.domain.TipoArticulo;
import com.ferreteria.ferreteria.entities.enumerations.Moneda;
import com.ferreteria.ferreteria.entities.enumerations.Proveedor;
import lombok.Getter;
import lombok.Setter;

import java.util.Hashtable;

@Setter
@Getter
public class ArticuloFiltro extends BaseFiltro{
    protected String nombre;
    protected String codigo;
    protected Moneda moneda;
    protected TipoArticulo tipoArticulo;
    protected Proveedor proveedor;

    @Override
    public String toQuerryString() {
        String cadena = super.toQuerryString();
        Hashtable<String,String> variables = new Hashtable<>();
        variables.put("nombre",convertObjectToString(this.nombre));
        variables.put("codigo",convertObjectToString(this.codigo));
        variables.put("moneda",convertObjectToString(this.moneda));
        variables.put("tipoArticulo",convertObjectToString(this.tipoArticulo));
        variables.put("proveedor",convertObjectToString(this.proveedor));
        return cadena+constructQuerry(variables);
    }
}
