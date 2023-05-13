package com.ferreteria.ferreteria.controllers;


import com.ferreteria.ferreteria.entities.enumerations.Moneda;
import com.ferreteria.ferreteria.entities.enumerations.Proveedor;
import com.ferreteria.ferreteria.services.*;
import com.ferreteria.ferreteria.entities.domain.Articulo;
import com.ferreteria.ferreteria.entities.filtro.ArticuloFiltro;
import com.ferreteria.ferreteria.services.util.AlertUtil;
import com.ferreteria.ferreteria.services.util.ClassHTMLUtil;
import com.ferreteria.ferreteria.services.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/articulo")
public class ArticuloController extends LowLogicController<Articulo> {
    @Autowired
    ArticuloService articuloService;
    @Autowired
    TipoArticuloService tipoArticuloService;
    private static List<Articulo> articuloList;

    @Autowired
    DolarService dolarService;

    public ArticuloController(LowLogicService<Articulo,Long> baseService){
        super(baseService);
    }


    @GetMapping("/listado")
    public String listado(Model model, @RequestParam("page") Optional<Integer> page, @ModelAttribute("filtro")ArticuloFiltro filtro){
        try{

            articuloList= new ArrayList<>();
            String nameEntity = "articulo";
            model.addAttribute("nameEntity", nameEntity);
            model.addAttribute("tiposArticulos", tipoArticuloService.findActivas());
            model.addAttribute("proveedores", Proveedor.values());
            model.addAttribute("monedas", Moneda.values());

            PaginationUtil.getModelPaginated(model, page, this.articuloService.findArticulos(filtro, page));
            AlertUtil.SetModelAndResetValues(model);
            ClassHTMLUtil.setModel(model);

            return "views/listado/"+nameEntity;
        }catch (Exception e){
            model.addAttribute("error", e.getMessage());
            return "views/error";
        }
    }

    @GetMapping(value = "/formulario/{id}")
    public String formulario(@PathVariable("id") long id, Model model,
                             @ModelAttribute("retorno")String retorno,
                             @ModelAttribute("nameEntity")String nameEntity){
        try{
            model.addAttribute("monedas", Moneda.values());
            model.addAttribute("proveedores", Proveedor.values());
            model.addAttribute("tiposArticulos", tipoArticuloService.findActivas());
            return super.formulario(model, id, articuloList, new Articulo());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "views/error";
        }
    }

    @PostMapping(value ="/formulario/{id}")
    public String agregarEntidad(@Valid @ModelAttribute("entity") Articulo entity,
                                    BindingResult result, @PathVariable("id") long id,
                                    Model model, @ModelAttribute("retorno")String retorno,
                                    @ModelAttribute("nameEntity")String nameEntity) {
        try {
            model.addAttribute("monedas", Moneda.values());
            model.addAttribute("proveedores", Proveedor.values());
            model.addAttribute("tiposArticulos", tipoArticuloService.findActivas());
            return super.saveOrAddEntity(model, id, articuloList, result);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "views/error";
        }
    }

    @GetMapping("/formulario/borrar")
    public String borrarArticuloFromList (Model model,
                                          @ModelAttribute("id")Long id,
                                          @ModelAttribute("retorno")String retorno,
                                          @ModelAttribute("nameEntity")String nameEntity){
        return super.deleteEntityFromList(model, id, articuloList);
    }

    @GetMapping("/formulario/guardar")
    public String guardarCambios (Model model, @ModelAttribute("retorno")String retorno,
                                  @ModelAttribute("nameEntity")String nameEntity){
        return super.guardarCambios(model, articuloList);
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar (Model model,
                            @PathVariable("id") long id,
                            @ModelAttribute("retorno")String retorno,
                            @ModelAttribute("nameEntity")String nameEntity){
        return super.eliminar(model, id);
    }



    @GetMapping("/recover/{id}")
    public String recover (Model model,
                           @PathVariable("id") long id,
                           @ModelAttribute("retorno")String retorno,
                           @ModelAttribute("nameEntity")String nameEntity){
        return super.recover(model, id);
    }
}
