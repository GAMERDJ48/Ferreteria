package com.ferreteria.ferreteria.services;

import com.ferreteria.ferreteria.entities.domain.*;
import com.ferreteria.ferreteria.entities.enumerations.EstadoVenta;
import com.ferreteria.ferreteria.entities.enumerations.Moneda;
import com.ferreteria.ferreteria.entities.enumerations.Proveedor;
import com.ferreteria.ferreteria.entities.filtro.ArticuloFiltro;
import com.ferreteria.ferreteria.entities.filtro.VentaFiltro;
import com.ferreteria.ferreteria.repositories.BaseRepository;
import com.ferreteria.ferreteria.repositories.DetalleVentaRepository;
import com.ferreteria.ferreteria.repositories.VentaRepository;
import com.ferreteria.ferreteria.services.util.ConversionUtil;
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
import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class VentaService extends BaseServiceImpl<Venta, Long>{
    @Autowired
    VentaRepository ventaRepository;
    @Autowired
    DetalleVentaRepository detalleVentaRepository;
    @Autowired
    ArticuloService articuloService;
    @Autowired
    DolarService dolarService;

    @Autowired
    ClienteService clienteService;

    public static BigDecimal dolar=null;

    public VentaService(BaseRepository<Venta,Long> baseRepository){
        super(baseRepository);
    }

    public Specification<Venta> findSpecByFecha(LocalDateTime fechaDesde, LocalDateTime fechaHasta) throws Exception {
        try{
            return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("fecha"), fechaDesde , fechaHasta);
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public Specification<Venta> findSpecByCliente(Cliente cliente) throws Exception{
        try{
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("cliente"), cliente);
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public Specification<Venta> findSpecByEstadoVenta(EstadoVenta estadoVenta) throws Exception{
        try{
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("estadoVenta"), estadoVenta);
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public Page<Venta> findVentas(VentaFiltro filtro, Optional<Integer> page) throws Exception {
        Pageable pageable = PaginationUtil.getPageable(page);

        Specification<Venta> spec = null;
        boolean entre=false;

            Cliente cliente = filtro.getCliente();
            if(cliente!=null && !cliente.equals("")){
                spec = where(findSpecByCliente(cliente));
                entre=true;
            }

            EstadoVenta estadoVenta = filtro.getEstadoVenta();
            if(estadoVenta!=null){
                spec = (entre) ? spec.and(findSpecByEstadoVenta(estadoVenta)) : where(findSpecByEstadoVenta(estadoVenta));
                entre=true;
            }

            LocalDateTime fechaDesde = filtro.getFechaDesde();
            LocalDateTime fechaHasta = filtro.getFechaHasta();
            if(fechaDesde!=null && fechaHasta!=null){
                spec = (entre) ? spec.and(findSpecByFecha(fechaDesde, fechaHasta)) : where(findSpecByFecha(fechaDesde, fechaHasta));
                entre=true;
            }
            return (entre) ? this.ventaRepository.findAll(spec, pageable) : this.ventaRepository.findAll(pageable);


    }

    public void agregarDetalleVenta(Long idArticulo, Venta venta, List<DetalleVenta> detalleVentas) throws Exception {
        try{
            Articulo articulo = articuloService.findById(idArticulo);
            DetalleVenta detalleVenta=null;

            if(detalleVentas!=null){
                for(DetalleVenta d: detalleVentas){
                    if(d.getArticulo().getId()==articulo.getId()){
                        detalleVenta = d;
                    }
                }
            }

            if(detalleVenta!=null){
                detalleVenta.setCantidad(detalleVenta.getCantidad()+1);
            }else{
                BigDecimal precio = articulo.getPrecio();
                if(articulo.getMoneda().equals(Moneda.DOLAR)) {
                    if(dolar==null) dolar=dolarService.getDolar();
                    precio = precio.multiply(dolar);
                }
                ConversionUtil.setScaleBigDecimal(precio);
                detalleVenta = new DetalleVenta(1, precio, articulo,new BigDecimal(0) ,venta);
                detalleVentas.add(detalleVenta);
            }
            detalleVenta.setSubTotal();
            venta.setImporte(venta.getImporte().add(detalleVenta.getPrecioUnitario()));
            venta.setPagado(venta.getPagado().add(detalleVenta.getPrecioUnitario()));
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public BindingResult checkVentaWithDetalles(Venta venta, List<DetalleVenta> lista, BindingResult result){
        try {
            Hashtable<String,String> mensajes = super.checkEntityWithChilds(venta, lista);
            result = new HelperCheck().convertHashtableToBindingResult(mensajes, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Venta addAllDetalle(List<DetalleVenta> detalleListventa, Venta venta) throws Exception {
        try{
            for(DetalleVenta DetalleVenta : detalleListventa){
                DetalleVenta.setVenta(venta);
            }
            if(venta.getDetalles()==null){
                venta.setDetalles(new ArrayList<>());
            }
            venta.getDetalles().addAll(detalleListventa);
            venta.verificarEstadoVenta();
            return this.save(venta);
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public void establecerPagada(Venta venta){
        venta.setEstadoVenta(EstadoVenta.PAGADO);
        venta.setPagado(venta.getImporte());
        try {
            this.update(venta.getId(), venta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pagarDeudaTotal(Cliente cliente) throws Exception {
        List<Venta> ventas = this.findByCliente(cliente);

        if(ventas!=null){
            for(Venta v: ventas){
                this.establecerPagada(v);
            }
        }

    }


    public void pagarDeuda(Cliente cliente, BigDecimal monto) throws Exception {
        List<Venta> ventas = this.findByCliente(cliente);

        if(ventas!=null){
            for(Venta v: ventas){
                if(monto.compareTo(new BigDecimal(0))==0) break;
                monto = v.pagar(monto);
                this.update(v.getId(), v);
            }
        }

    }

    public BigDecimal getDeuda(Cliente cliente) throws Exception {
        List<Venta> ventas = this.findByCliente(cliente);
        BigDecimal deuda= new BigDecimal(0);
        if(ventas!=null){
            for(Venta v: ventas){
                BigDecimal aux = v.getImporte().subtract(v.getPagado());
                if(aux.compareTo(new BigDecimal(0))>0){ //aux>0
                    deuda = deuda.add(aux);
                }
            }
        }
        return deuda;
    }

    public Hashtable<Cliente, BigDecimal> findDeudores() throws Exception {
        Hashtable<Cliente, BigDecimal> deudores = new Hashtable<>();
        List<Cliente> clientes = clienteService.findActivas();

        for(Cliente c: clientes){
            BigDecimal deuda = this.getDeuda(c);
            if(deuda.compareTo(new BigDecimal(0))>0){ //aux>0
                deudores.put(c, deuda);
            }
        }
        return  deudores;
    }

    public List<Venta> findByCliente(Cliente cliente) throws Exception {
        try{
            return ventaRepository.findByCliente(cliente);
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }


}
