package com.ferreteria.ferreteria.services;

import com.ferreteria.ferreteria.entities.domain.Articulo;
import com.ferreteria.ferreteria.entities.domain.TipoArticulo;
import com.ferreteria.ferreteria.entities.enumerations.Moneda;
import com.ferreteria.ferreteria.entities.enumerations.Proveedor;
import com.ferreteria.ferreteria.entities.filtro.ArticuloFiltro;
import com.ferreteria.ferreteria.repositories.ArticuloRepository;
import com.ferreteria.ferreteria.repositories.LowLogicRepository;
import com.ferreteria.ferreteria.services.util.HelperCheck;
import com.ferreteria.ferreteria.services.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.*;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class ArticuloService extends LowLogicService<Articulo,Long>{
    @Autowired
    ArticuloRepository repository;
    @Autowired
    TipoArticuloService tipoArticuloService;

    public static List<Articulo> articulos;

    public ArticuloService(LowLogicRepository<Articulo,Long> baseRepository){
        super(baseRepository);
    }

    public void checkIfExistsAndSave(String codigo, String nombre, BigDecimal precio, Moneda moneda, String nameTipoArticulo, Proveedor proveedor) throws Exception {
        try {
            if(articulos==null) articulos = this.findByProveedor(proveedor);
            TipoArticulo tipoArticulo = tipoArticuloService.checkIfExistsAndSave(nameTipoArticulo);
            Articulo articulo=null;
            for(Articulo a: articulos){
                if(a.getCodigo().equals(codigo)){
                    articulo = a;
                    break;
                }
            }

            if(articulo==null){
                articulo = new Articulo(codigo, nombre, precio, moneda, proveedor, tipoArticulo);
                this.save(articulo);
            }else{
                if(!articulo.getPrecio().equals(precio) || !articulo.getMoneda().equals(moneda)){
                    articulo.setPrecio(precio);
                    articulo.setMoneda(moneda);
                    this.update(articulo.getId(), articulo);
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    public Articulo findByCodigoAndProveedor(String codigo, Proveedor proveedor) throws Exception {
        try{
            return repository.findByCodigoAndProveedor(codigo, proveedor);
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    public Specification<Articulo> findSpecByCodigo(String codigo) throws Exception {
        try{
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("codigo"), codigo);
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public Specification<Articulo> findSpecByNombre(String nombre) throws Exception {
        try{
            return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("nombre"), "%" + nombre + "%");
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public Specification<Articulo> findSpecByTipoArticulo(TipoArticulo tipoArticulo) throws Exception{
        try{
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tipoArticulo"), tipoArticulo);
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public Specification<Articulo> findSpecByMoneda(Moneda moneda) throws Exception{
        try{
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("moneda"), moneda);
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public Specification<Articulo> findSpecByProveedor(Proveedor proveedor) throws Exception{
        try{
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("proveedor"), proveedor);
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public List<Articulo> findByProveedor(Proveedor proveedor) throws Exception{
        try{
            return this.repository.findByProveedor(proveedor);
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public Page<Articulo> findArticulos(ArticuloFiltro filtro, Optional<Integer> page) throws Exception {
        Pageable pageable = PaginationUtil.getPageable(page, Sort.by(Sort.Direction.ASC, "nombre"));

        Specification<Articulo> spec = null;
        boolean entre=false;
        if(filtro.getCodigo()!=null && !filtro.getCodigo().equals("")){
            return repository.findAll(where(findSpecByCodigo(filtro.getCodigo())),pageable );
        }else{
            String nombre = filtro.getNombre();
            if(nombre!=null && !nombre.equals("")){
                spec = where(findSpecByNombre(nombre));
                entre=true;
            }
            Moneda moneda = filtro.getMoneda();
            if(moneda!=null){
                spec = (entre) ? spec.and(findSpecByMoneda(moneda)) : where(findSpecByMoneda(moneda));
                entre=true;
            }
            Proveedor proveedor = filtro.getProveedor();
            if(proveedor!=null){
                spec = (entre) ? spec.and(findSpecByProveedor(proveedor)) : where(findSpecByProveedor(proveedor));
                entre=true;
            }
            TipoArticulo tipoArticulo = filtro.getTipoArticulo();
            if(tipoArticulo!=null){
                spec = (entre) ? spec.and(findSpecByTipoArticulo(tipoArticulo)) : where(findSpecByTipoArticulo(tipoArticulo));
                entre=true;
            }
            return (entre) ? this.repository.findAll(spec, pageable) : this.repository.findAll(pageable);
        }

    }

    @Override
    public BindingResult checkEntity(Articulo articulo, BindingResult result) throws Exception{
        Hashtable<String,String> mensajes = new Hashtable<>();
        try {
            Articulo otro;
            if(articulo.getCodigo()!=null){
                otro = this.findByCodigoAndProveedor(articulo.getCodigo(), articulo.getProveedor());
                if(otro!=null && otro.getId()!=articulo.getId()){
                    mensajes.put("codigo", "Ya existe un codigo igual");
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return HelperCheck.convertHashtableToBindingResult(mensajes, result);
    }
}
