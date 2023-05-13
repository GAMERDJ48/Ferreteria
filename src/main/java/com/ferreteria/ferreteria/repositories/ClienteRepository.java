package com.ferreteria.ferreteria.repositories;

import com.ferreteria.ferreteria.entities.domain.Cliente;
import com.ferreteria.ferreteria.entities.domain.TipoArticulo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends LowLogicRepository<Cliente,Long>{
    @Query(value = "SELECT * FROM cliente a WHERE a.nombre LIKE %:nombre%", nativeQuery = true)
    List<Cliente> findListByNombre(@Param("nombre")String nombre);

    Cliente findByNombre(String nombre);
}
