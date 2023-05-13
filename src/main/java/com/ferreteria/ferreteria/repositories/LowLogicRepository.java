package com.ferreteria.ferreteria.repositories;

import com.ferreteria.ferreteria.entities.domain.Base;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface LowLogicRepository <E extends Base, ID extends Serializable> extends BaseRepository<E, ID> {
    @Query(value = "SELECT * FROM #{#entityName} e WHERE e.inactive IS FALSE", nativeQuery = true)
    Page<E> findActivas(Pageable pageable);

    @Query(value = "SELECT * FROM #{#entityName} e WHERE e.inactive IS FALSE", nativeQuery = true)
    List<E> findActivas();
}
