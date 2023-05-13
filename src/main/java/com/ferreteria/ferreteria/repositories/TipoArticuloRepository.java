package com.ferreteria.ferreteria.repositories;

import com.ferreteria.ferreteria.entities.domain.Articulo;
import com.ferreteria.ferreteria.entities.domain.TipoArticulo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoArticuloRepository extends LowLogicRepository<TipoArticulo,Long> {
    TipoArticulo findByNombre(String nombre);

    @Query(value = "SELECT * FROM tipoarticulo a WHERE a.nombre LIKE %:nombre%", nativeQuery = true)
    List<TipoArticulo> findListByNombre(@Param("nombre")String nombre);
}
