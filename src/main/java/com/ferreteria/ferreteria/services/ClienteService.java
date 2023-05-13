package com.ferreteria.ferreteria.services;

import com.ferreteria.ferreteria.entities.domain.Cliente;
import com.ferreteria.ferreteria.entities.domain.TipoArticulo;
import com.ferreteria.ferreteria.repositories.ClienteRepository;
import com.ferreteria.ferreteria.repositories.LowLogicRepository;
import com.ferreteria.ferreteria.services.util.HelperCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Hashtable;
import java.util.List;

@Service
public class ClienteService extends LowLogicService<Cliente,Long> {
    @Autowired
    ClienteRepository repository;

    public ClienteService(LowLogicRepository<Cliente,Long> lowLogicRepository){
        super(lowLogicRepository);
    }

    public List<Cliente> findListByNombre(String nombre){
        return repository.findListByNombre(nombre);
    }

    public Cliente findByNombre(String nombre){
        return repository.findByNombre(nombre);
    }

    public BindingResult checkEntity(Cliente cliente, BindingResult result) throws Exception{
        Hashtable<String,String> mensajes = new Hashtable<>();
        try {
            Cliente otro;
            if(cliente.getNombre()!=null){
                otro = this.findByNombre(cliente.getNombre());
                if(otro!=null && otro.getId()!=cliente.getId()){
                    mensajes.put("nombre", "Ya existe un cliente con ese nombre");
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return HelperCheck.convertHashtableToBindingResult(mensajes, result);
    }
}
