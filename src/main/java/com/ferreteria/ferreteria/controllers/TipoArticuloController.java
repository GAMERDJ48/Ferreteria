package com.ferreteria.ferreteria.controllers;

import com.ferreteria.ferreteria.entities.domain.TipoArticulo;
import com.ferreteria.ferreteria.services.BaseServiceImpl;
import com.ferreteria.ferreteria.services.LowLogicService;
import com.ferreteria.ferreteria.services.TipoArticuloService;
import com.ferreteria.ferreteria.services.util.AlertUtil;
import com.ferreteria.ferreteria.services.util.ClassHTMLUtil;
import com.ferreteria.ferreteria.services.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tipoarticulo")
public class TipoArticuloController extends LowLogicController<TipoArticulo> {
    @Autowired
    TipoArticuloService tipoArticuloService;

    public TipoArticuloController(LowLogicService<TipoArticulo,Long> baseService){
        super(baseService);
    }

    @GetMapping("/listado")
    public String listado(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam(value = "nombre", required = false)String nombre){
        try{
            String nameEntity = "tipoarticulo";
            model.addAttribute("nameEntity", nameEntity);

            List<TipoArticulo> tipoArticulos = (nombre!=null && !nombre.equals("")) ? this.tipoArticuloService.findListByNombre(nombre.toUpperCase()): this.tipoArticuloService.findAll() ;

            PaginationUtil.getModelPaginated(model, page, tipoArticulos);

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
        return super.formulario(model, id, null, new TipoArticulo());
    }



    @PostMapping(value ="/formulario/{id}")
    public String guardarFormulario(@Valid @ModelAttribute("entity") TipoArticulo entity,
                                    BindingResult result, @PathVariable("id") long id,
                                    Model model, @ModelAttribute("retorno")String retorno,
                                    @ModelAttribute("nameEntity")String nameEntity) {
        return super.saveOrAddEntity(model, id, null, result);
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
