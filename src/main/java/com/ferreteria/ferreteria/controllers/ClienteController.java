package com.ferreteria.ferreteria.controllers;

import com.ferreteria.ferreteria.entities.domain.Cliente;
import com.ferreteria.ferreteria.services.ClienteService;
import com.ferreteria.ferreteria.services.LowLogicService;
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
@RequestMapping("/cliente")
public class ClienteController extends LowLogicController<Cliente>{
    @Autowired
    ClienteService clienteService;

    public ClienteController(LowLogicService<Cliente,Long> lowLogicService){
        super(lowLogicService);
    }

    @GetMapping("/listado")
    public String listado(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam(value = "nombre", required = false)String nombre){
        try{
            String nameEntity = "cliente";
            model.addAttribute("nameEntity", nameEntity);


            List<Cliente> clientes = (nombre!=null && !nombre.equals("")) ? this.clienteService.findListByNombre(nombre): this.clienteService.findAll() ;

            PaginationUtil.getModelPaginated(model, page, clientes);

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
            return super.formulario(model, id, null, new Cliente());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "views/error";
        }
    }

    @PostMapping(value ="/formulario/{id}")
    public String guardarFormulario(@Valid @ModelAttribute("entity") Cliente entity,
                                 BindingResult result, @PathVariable("id") long id,
                                 Model model, @ModelAttribute("retorno")String retorno,
                                 @ModelAttribute("nameEntity")String nameEntity) {
        try {
            return super.saveOrAddEntity(model, id, null, result);
        } catch (Exception e) {
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



    @GetMapping("/recover/{id}")
    public String recover (Model model,
                           @PathVariable("id") long id,
                           @ModelAttribute("retorno")String retorno,
                           @ModelAttribute("nameEntity")String nameEntity){
        return super.recover(model, id);
    }
}
