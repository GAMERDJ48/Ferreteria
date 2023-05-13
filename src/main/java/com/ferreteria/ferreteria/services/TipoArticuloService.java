package com.ferreteria.ferreteria.services;

import com.ferreteria.ferreteria.entities.domain.Articulo;
import com.ferreteria.ferreteria.entities.domain.TipoArticulo;
import com.ferreteria.ferreteria.entities.enumerations.Moneda;
import com.ferreteria.ferreteria.repositories.BaseRepository;
import com.ferreteria.ferreteria.repositories.LowLogicRepository;
import com.ferreteria.ferreteria.repositories.TipoArticuloRepository;
import com.ferreteria.ferreteria.services.util.HelperCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.List;

@Service
public class TipoArticuloService extends LowLogicService<TipoArticulo,Long> {
    @Autowired
    TipoArticuloRepository repository;

    public static List<TipoArticulo> tipoArticulos;

    public TipoArticuloService(LowLogicRepository<TipoArticulo,Long> baseRepository){
        super(baseRepository);
    }

    public TipoArticulo checkIfExistsAndSave(String nameTipoArticulo) throws Exception {
        try {
            if(nameTipoArticulo!=null){
                if(tipoArticulos==null || tipoArticulos.isEmpty()) tipoArticulos = this.findAll();
                TipoArticulo tipoArticulo=null;
                for(TipoArticulo t: tipoArticulos){
                    if(t.getNombre().equals(nameTipoArticulo)){
                        tipoArticulo = t;
                        break;
                    }
                }
                if(tipoArticulo==null){
                    tipoArticulo= new TipoArticulo(nameTipoArticulo);
                    tipoArticulos.add( this.save(tipoArticulo));
                }
                return tipoArticulo;
            }
            return null;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    public BindingResult checkEntity(TipoArticulo tipoArticulo, BindingResult result) throws Exception{
        Hashtable<String,String> mensajes = new Hashtable<>();
        try {
            TipoArticulo otro;
            if(tipoArticulo.getNombre()!=null){
                otro = this.findByNombre(tipoArticulo.getNombre());
                if(otro!=null && otro.getId()!=tipoArticulo.getId()){
                    mensajes.put("nombre", "Ya existe un tipo de articulo con ese nombre");
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return HelperCheck.convertHashtableToBindingResult(mensajes, result);
    }

    public List<TipoArticulo> findListByNombre(String nombre){
        return repository.findListByNombre(nombre);
    }

    public TipoArticulo findByNombre(String nombre){
        return repository.findByNombre(nombre);
    }
}
