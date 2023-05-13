package com.ferreteria.ferreteria.controllers;

import com.ferreteria.ferreteria.entities.domain.Articulo;
import com.ferreteria.ferreteria.entities.domain.Cliente;
import com.ferreteria.ferreteria.entities.domain.DetalleVenta;
import com.ferreteria.ferreteria.entities.domain.Venta;
import com.ferreteria.ferreteria.entities.enumerations.EstadoVenta;
import com.ferreteria.ferreteria.entities.filtro.ArticuloFiltro;
import com.ferreteria.ferreteria.entities.filtro.VentaFiltro;
import com.ferreteria.ferreteria.entities.wrappers.VentaWrapper;
import com.ferreteria.ferreteria.services.BaseServiceImpl;
import com.ferreteria.ferreteria.services.ClienteService;
import com.ferreteria.ferreteria.services.VentaService;
import com.ferreteria.ferreteria.services.util.AlertUtil;
import com.ferreteria.ferreteria.services.util.ClassHTMLUtil;
import com.ferreteria.ferreteria.services.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/venta")
public class VentaController extends BaseController<Venta>{
    @Autowired
    VentaService ventaService;
    @Autowired
    ClienteService clienteService;


    public VentaController(BaseServiceImpl<Venta,Long> baseService){
        super(baseService);
    }

    public static VentaWrapper ventaWrapper;

    @GetMapping("/listado")
    public String listado(Model model, @RequestParam("page") Optional<Integer> page, @ModelAttribute("filtro") VentaFiltro filtro){
        try {
            String nameEntity = "venta";
            model.addAttribute("nameEntity", nameEntity);
            model.addAttribute("clientes", this.clienteService.findActivas());
            model.addAttribute("estados", EstadoVenta.values());

            PaginationUtil.getModelPaginated(model, page, this.ventaService.findVentas(filtro, page));
            AlertUtil.SetModelAndResetValues(model);
            ClassHTMLUtil.setModel(model);
            return "views/listado/"+nameEntity;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/deudor")
    public String deudor(Model model, @RequestParam("page") Optional<Integer> page){
        try {
            model.addAttribute("nameEntity", "deudore");
            model.addAttribute("entities", this.ventaService.findDeudores());


            AlertUtil.SetModelAndResetValues(model);
            ClassHTMLUtil.setModel(model);
            return "views/listado/deudor";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/formulario/{id}")
    public  String formularioEditar(@PathVariable("id") long id, Model model,
                                    @ModelAttribute("retorno")String retorno, @ModelAttribute("nameEntity") String nameEntity){
        try{
            model.addAttribute("clientes", clienteService.findActivas());
            model.addAttribute("estados", EstadoVenta.values());
            Venta entity = this.baseService.findById(id);
            model.addAttribute("object", new VentaWrapper(entity, entity.getDetalles()));
            ClassHTMLUtil.setModel(model);
            AlertUtil.SetModelAndResetValues(model);
        return "views/formularios/editar/"+nameEntity;
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "views/error";
        }
    }

    @GetMapping("/detalle/{id}")
    public String detalle(Model model, @PathVariable("id")Long id, @ModelAttribute("retorno")String retorno, @ModelAttribute("nameEntity")String nameEntity){
        try {
            model.addAttribute("object", this.ventaService.findById(id));
            AlertUtil.SetModelAndResetValues(model);
            ClassHTMLUtil.setModel(model);
            return "views/detalle/"+nameEntity;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/agregar")
    public String agregarDetalleVenta(Model model, @ModelAttribute("id")Long idArticulo,
                                      @ModelAttribute("filtro") ArticuloFiltro filtro,
                                      @ModelAttribute(value = "modoVenta")Boolean modoVenta,
                                      @ModelAttribute(value = "page") Integer page){
        try{
            if(ventaWrapper==null) ventaWrapper = new VentaWrapper();
            ventaService.agregarDetalleVenta(idArticulo, ventaWrapper.getVenta(), ventaWrapper.getDetalles());
            AlertUtil.setAviso(AlertUtil.AlertType.ADD, "articulo al carrito");
            return "redirect:/inicio?page="+page+"&modoVenta="+modoVenta+"&"+((filtro!=null) ? filtro.toQuerryString() : "" );
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "views/error";
        }
    }


    @GetMapping("/formulario/borrar")
    public String deleteEntityFromList (Model model, @ModelAttribute("id")Long id,
                                        @ModelAttribute("retorno")String retorno){
        try{
            List<DetalleVenta> lista = ventaWrapper.getDetalles();
            if(lista!=null){
                for(int i=0; i<lista.size(); i++){
                    if(id.equals(lista.get(i).getArticulo().getId())){
                        DetalleVenta d = lista.get(i);
                        lista.remove(i);
                        AlertUtil.setAviso(AlertUtil.AlertType.DELETE,"Detalle Venta");
                        if(lista==null || lista.size()==0){
                            ventaWrapper=null;
                        }else{
                            Venta venta = ventaWrapper.getVenta();
                            venta.setImporte(venta.getImporte().subtract(d.getSubTotal()));
                            venta.setPagado(venta.getPagado().subtract(d.getSubTotal()));
                        }
                        break;
                    }
                }
            }
            return "redirect:"+retorno;
        }catch(Exception e){
            model.addAttribute("error", e.getStackTrace());
            return "views/error";
        }
    }

    @PostMapping(value = "/formulario/guardar")
    public String guardarVenta(@Valid @ModelAttribute("object") VentaWrapper ventaWrapper,
                                        BindingResult result, Model model,
                                        @ModelAttribute("retorno")String retorno){
        try{
            model.addAttribute("clientes", clienteService.findActivas());
            model.addAttribute("estados", EstadoVenta.values());
            model.addAttribute("dolar", VentaService.dolar);
            model.addAttribute("object", ventaWrapper);
            Venta venta = ventaWrapper.getVenta();
            List<DetalleVenta> lista = ventaWrapper.getDetalles();
            result = ventaService.checkVentaWithDetalles(venta, lista, result);

            if(result.hasErrors()){
                return "views/index";
            }
            if(venta.getId()==0){

                AlertUtil.setAviso(AlertUtil.AlertType.CREATE, "venta");
            }else{
                AlertUtil.setAviso(AlertUtil.AlertType.UPDATE, "venta");
            }
            ventaService.addAllDetalle(lista, venta);
            return "redirect:"+retorno;
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            return "views/error";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar (Model model,
                            @PathVariable("id") long id,
                            @ModelAttribute("retorno")String retorno,
                            @ModelAttribute("nameEntity")String nameEntity){
        return super.eliminar(model, id);
    }

    @GetMapping("/estadoVenta/pagado/{id}")
    public String establecerPagada (Model model, @PathVariable("id") long id, @ModelAttribute("retorno")String retorno){
        try {
            Venta venta = ventaService.findById(id);
            ventaService.establecerPagada(venta);
            return "redirect:"+retorno;
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "views/error";
        }
    }


    @PostMapping(value = "/pagar/{id}")
    public String pagar(@PathVariable("id")Long id, @RequestParam("monto") BigDecimal monto, Model model, @ModelAttribute("retorno")String retorno){
        try{
            Cliente cliente = clienteService.findById(id);
            ventaService.pagarDeuda(cliente, monto);
            return "redirect:"+retorno;
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            return "views/error";
        }
    }

    @GetMapping(value = "/pagar/todo/{id}")
    public String pagarTodo(@PathVariable("id")Long id,  Model model, @ModelAttribute("retorno")String retorno){
        try{
            Cliente cliente = clienteService.findById(id);
            ventaService.pagarDeudaTotal(cliente);
            return "redirect:"+retorno;
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            return "views/error";
        }
    }


}
