package com.ferreteria.ferreteria.repositories;

import com.ferreteria.ferreteria.entities.domain.Articulo;
import com.ferreteria.ferreteria.entities.enumerations.Proveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticuloRepository extends LowLogicRepository<Articulo,Long>{
    @Query(value = "SELECT * FROM articulo a WHERE a.codigo LIKE %:codigo%", nativeQuery = true)
    Page<Articulo> findListByCodigo(@Param("codigo")String codigo, Pageable pageable);

    Articulo findByCodigoAndProveedor(String codigo, Proveedor proveedor);

    Articulo findFirstByNombre(String nombre);

    @Query(value = "SELECT * FROM articulo a WHERE a.nombre LIKE %:nombre%", nativeQuery = true)
    Page<Articulo> findListArticuloByNombre(@Param("nombre")String nombre, Pageable pageable);


    @Query(value = "SELECT * FROM articulo a WHERE a.tipo_articulo_id=:tipoArticulo", nativeQuery = true)
    Page<Articulo> findListByTipoArticulo(@Param("tipoArticulo")Long tipoArticulo, Pageable pageable);

    @Query(value = "SELECT * FROM articulo a WHERE a.moneda=:moneda", nativeQuery = true)
    Page<Articulo> findListByMoneda(@Param("moneda")int moneda, Pageable pageable);

    List<Articulo> findByProveedor(Proveedor proveedor);
}
