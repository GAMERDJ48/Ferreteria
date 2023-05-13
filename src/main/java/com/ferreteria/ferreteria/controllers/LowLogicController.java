package com.ferreteria.ferreteria.controllers;

import com.ferreteria.ferreteria.entities.domain.LowLogic;
import com.ferreteria.ferreteria.services.LowLogicService;
import com.ferreteria.ferreteria.services.util.AlertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

public class LowLogicController<E extends LowLogic> extends BaseController<E>{
    @Autowired
    protected LowLogicService<E, Long> lowLogicService;

    public LowLogicController(LowLogicService<E,Long> lowLogicService){
        super(lowLogicService);
    }

    public String recover (Model model, Long id){
        try{
            String retorno = model.getAttribute("retorno").toString();
            String nameEntity = model.getAttribute("nameEntity").toString();
            lowLogicService.recover(id);
            AlertUtil.setAviso(AlertUtil.AlertType.RECOVER, nameEntity);
            return "redirect:"+retorno;
        }catch(Exception e){
            model.addAttribute("error", e.getClass());
            return "views/error";
        }
    }

    @Override
    public String eliminar (Model model, Long id){
        try{
            String retorno = model.getAttribute("retorno").toString();
            String nameEntity = model.getAttribute("nameEntity").toString();
            lowLogicService.delete(id);
            AlertUtil.setAviso(AlertUtil.AlertType.LOW, nameEntity);
            return "redirect:"+retorno;
        }catch(Exception e){
            model.addAttribute("error", e.getClass());
            return "views/error";
        }
    }
}
