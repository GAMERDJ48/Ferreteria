package com.ferreteria.ferreteria.controllers;

import com.ferreteria.ferreteria.entities.enumerations.Moneda;
import com.ferreteria.ferreteria.entities.enumerations.Proveedor;
import com.ferreteria.ferreteria.entities.filtro.ArticuloFiltro;
import com.ferreteria.ferreteria.entities.wrappers.VentaWrapper;
import com.ferreteria.ferreteria.services.*;
import com.ferreteria.ferreteria.services.util.AlertUtil;
import com.ferreteria.ferreteria.services.util.ClassHTMLUtil;
import com.ferreteria.ferreteria.services.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Controller
@RequestMapping("/inicio")
public class InicioController {
    @Autowired
    ArticuloService articuloService;
    @Autowired
    ClienteService clienteService;
    @Autowired
    TipoArticuloService tipoArticuloService;
    @GetMapping()
    public String inicio(Model model, @RequestParam("page") Optional<Integer> page, @ModelAttribute("filtro") ArticuloFiltro filtro, @ModelAttribute(value = "modoVenta")Boolean modoVenta){
        try{
            String nameEntity = "articulo";
            model.addAttribute("nameEntity", nameEntity);
            model.addAttribute("tiposArticulos", tipoArticuloService.findActivas());
            model.addAttribute("proveedores", Proveedor.values());
            model.addAttribute("page", page.orElse(1));
            model.addAttribute("monedas", Moneda.values());
            PaginationUtil.getModelPaginated(model, page, this.articuloService.findArticulos(filtro, page));
            ClassHTMLUtil.setModel(model);
            AlertUtil.SetModelAndResetValues(model);

            if(!modoVenta){
                VentaController.ventaWrapper=null;
                VentaService.dolar=null;
            }
            VentaWrapper ventaWrapper = VentaController.ventaWrapper;
            if(ventaWrapper!=null){
                model.addAttribute("object", ventaWrapper);
                model.addAttribute("clientes", clienteService.findActivas());
                model.addAttribute("dolar", VentaService.dolar);
            }
            return "views/index";
        }catch (Exception e){
            model.addAttribute("error", e.getMessage());
            return "views/error";
        }
    }

    @PostMapping()
    public String postInicio(Model model, @RequestParam("page") Optional<Integer> page, @ModelAttribute("filtro") ArticuloFiltro filtro, @ModelAttribute(value = "modoVenta")Boolean modoVenta){
        try{
            return "redirect:/inicio?modoVenta="+modoVenta+"&page="+page.orElse(1)+"&"+((filtro!=null) ? filtro.toQuerryString() : "" );
        }catch (Exception e){
            model.addAttribute("error", e.getMessage());
            return "views/error";
        }
    }


}
