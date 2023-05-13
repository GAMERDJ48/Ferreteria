package com.ferreteria.ferreteria.services;


import com.ferreteria.ferreteria.entities.domain.Base;
import com.ferreteria.ferreteria.repositories.BaseRepository;
import com.ferreteria.ferreteria.services.util.HelperCheck;
import org.springframework.data.domain.*;
import org.springframework.validation.BindingResult;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.Serializable;
import java.util.*;


public class BaseServiceImpl<E extends Base, ID extends Serializable>{
    protected BaseRepository<E, ID> baseRepository;

    public BaseServiceImpl(BaseRepository<E, ID> baseRepository){
        this.baseRepository = baseRepository;
    }


    @Transactional
    public Page<E> findAll(Pageable pageable) throws Exception {
        try{
            return baseRepository.findAll(pageable);
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public Page<E> findAll(Pageable pageable, Sort sort) throws Exception {
        try{
            pageable.getSortOr(sort);
            return baseRepository.findAll(pageable);
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }


    @Transactional
    public List<E> findAll() throws Exception {
        try{
            return baseRepository.findAll();
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public List<E> findAll(Sort sort) throws Exception {
        try{
            return baseRepository.findAll(sort);
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public int getCount() throws Exception {
        try{
            return baseRepository.getCount();
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public E findById(ID id) throws Exception {
        try{
            Optional<E> entityOptional = baseRepository.findById(id);
            if(entityOptional.isPresent()) {return entityOptional.get();}
            else {return null;}
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public E save(E entity) throws Exception {
        try{
            return baseRepository.save(entity);
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public E update(ID id, E entity) throws Exception {
        try{
            Optional<E> entityOptional = baseRepository.findById(id);
            if(entityOptional.isPresent()){
                return baseRepository.save(entity);
            }else{
                throw new Exception("No se encontro la entidad");
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public boolean delete(ID id) throws Exception {
        try{
            if(baseRepository.existsById(id)){
                baseRepository.deleteById(id);
                return true;
            }else{
                throw new Exception();
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public E findLast() throws Exception{
        try{
            Optional<E> last = baseRepository.findLast();
            if(last.isPresent()){
                return last.get();
            }
            return null;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public BindingResult checkEntity(E articulo, BindingResult result) throws Exception{
        Hashtable<String,String> mensajes = new Hashtable<>();
        try {

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return HelperCheck.convertHashtableToBindingResult(mensajes, result);
    }

    public <F extends Base, C extends Base> Hashtable<String,String> checkEntityWithChilds(F father, List<C> childs){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<F>> violationsFather = validator.validate(father);

        Set<ConstraintViolation<C>> violationsChilds = new HashSet<>();
        for(C d : childs){
            violationsChilds.addAll(validator.validate(d));
        }

        Hashtable<String,String> mensajes = new Hashtable<>();
        for (ConstraintViolation<F> violation : violationsFather) {
            mensajes.put(violation.getMessageTemplate(), violation.getMessage());
        }
        for (ConstraintViolation<C> violation : violationsChilds) {
            mensajes.put(violation.getMessageTemplate(), violation.getMessage());
        }
        return mensajes;
    }
}

