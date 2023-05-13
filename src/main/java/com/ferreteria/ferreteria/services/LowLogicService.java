package com.ferreteria.ferreteria.services;

import com.ferreteria.ferreteria.entities.domain.LowLogic;
import com.ferreteria.ferreteria.repositories.LowLogicRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class LowLogicService<E extends LowLogic, ID extends Serializable> extends BaseServiceImpl<E,ID>{
    protected LowLogicRepository<E, ID> repository;

    public LowLogicService(LowLogicRepository<E,ID> repositoryBase){
        super(repositoryBase);
        repository = repositoryBase;
    }

    @Transactional
    public boolean recover(ID id) throws Exception {
        try{
            Optional<E> e = baseRepository.findById(id);
            if(e.isPresent() && e.get().isInactive()){
                e.get().setInactive(false);
                super.update(id, e.get());
                return true;
            }else{
                return false;
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean delete(ID id) throws Exception {
        Optional<E> e;
        try{
            e = baseRepository.findById(id);
            if(e.isPresent()){
                E entity = e.get();
                entity.setInactive(true);
                repository.save(entity);
                return true;
            }else{
                return false;
            }
        }catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    @Transactional
    public Page<E> findActivas(Pageable pageable) throws Exception{
        try{
            return repository.findActivas(pageable);
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public List<E> findActivas() throws Exception{
        try{
            return repository.findActivas();
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }
}

