package com.ferreteria.ferreteria.entities.filtro;

import com.ferreteria.ferreteria.entities.domain.Base;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

@Getter
@Setter
public class BaseFiltro {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "{NotNull.fecha}")
    protected LocalDateTime fechaDesde;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "{NotNull.fecha}")
    protected LocalDateTime fechaHasta;

    protected Long numero;
    protected String orden;

    protected String toQuerryString(){
        Hashtable<String,String> variables = new Hashtable<>();
        variables.put("numero",convertObjectToString(this.numero));
        variables.put("fechaDesde", convertObjectToString(this.fechaDesde));
        variables.put("fechaHasta", convertObjectToString(this.fechaHasta));
        variables.put("orden", convertObjectToString(orden));
        return constructQuerry(variables);
    }

    protected String constructQuerry(Hashtable<String,String>variables){
        if(!variables.isEmpty()){
            String cadena="";
            Enumeration<String> keys = variables.keys();
            while(keys.hasMoreElements()){
                String clave = keys.nextElement();
                if(!variables.get(clave).equals("")){
                    cadena = cadena+clave+"="+variables.get(clave)+"&";
                }
            }
            return cadena;
        }
        return "";
    }

    protected String convertObjectToString(Object object){
        if(object!=null){
            if(object instanceof Number){
                return object.toString();
            }else{
                if(object instanceof Base){
                    Base base = (Base)object;
                    return String.valueOf(base.getId());
                }else{
                    if(object instanceof Date){
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        return dateFormat.format(object);
                    }else{
                        if(object instanceof String){
                            return (String)object;
                        }else{
                            if(object instanceof Boolean){
                                return object.toString();
                            }else{
                                if(object instanceof Enum){
                                    return ((Enum<?>) object).name();
                                }
                            }
                        }
                    }
                }
            }
        }
        return "";
    }
}
