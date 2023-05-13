package com.ferreteria.ferreteria.repositories;

import com.ferreteria.ferreteria.entities.domain.Cliente;
import com.ferreteria.ferreteria.entities.domain.Venta;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepository extends BaseRepository<Venta,Long>{

    List<Venta> findByCliente(Cliente cliente);

}
