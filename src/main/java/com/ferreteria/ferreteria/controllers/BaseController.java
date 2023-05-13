package com.ferreteria.ferreteria.controllers;

import com.ferreteria.ferreteria.entities.domain.Articulo;
import com.ferreteria.ferreteria.entities.domain.Base;
import com.ferreteria.ferreteria.services.BaseServiceImpl;
import com.ferreteria.ferreteria.services.util.AlertUtil;
import com.ferreteria.ferreteria.services.util.ClassHTMLUtil;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public class BaseController<E extends Base> {
    BaseServiceImpl<E,Long> baseService;

    public BaseController(BaseServiceImpl<E, Long> baseService){
        this.baseService = baseService;
    }

    public  String formulario(Model model, Long id, List<E> lista, E entity){
        try{
            if(id==0){
                model.addAttribute("entities", lista);
            }else{
                entity = this.baseService.findById(id);
            }
            model.addAttribute("object", entity);
            ClassHTMLUtil.setModel(model);
            AlertUtil.SetModelAndResetValues(model);
            return "views/formularios/"+model.getAttribute("nameEntity");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "views/error";
        }
    }
    public String saveOrAddEntity(Model model, Long id, List<E> lista, BindingResult result) {
        try {
            String nameEntity = model.getAttribute("nameEntity").toString();
            String retorno = model.getAttribute("retorno").toString();
            E entity = (E)model.getAttribute("entity");


            ClassHTMLUtil.setModel(model);
            model.addAttribute("entities", lista);
            model.addAttribute("object",entity);

            result = baseService.checkEntity(entity, result);
            model.addAttribute("result", result.hasErrors());

            if(result.hasErrors()){
                AlertUtil.setAviso(AlertUtil.AlertType.ERRORFORM,nameEntity);
                model.addAttribute("result", result);
                AlertUtil.SetModelAndResetValues(model);
                return "views/formularios/"+nameEntity;
            }
            if (id == 0) {
                AlertUtil.setAviso(AlertUtil.AlertType.ADD,nameEntity);
                if(lista!=null){
                    lista.add(entity);
                    return "redirect:/"+nameEntity+"/formulario/0?retorno="+retorno+"&nameEntity="+nameEntity;
                }else{
                    baseService.save(entity);
                    return "redirect:"+retorno;
                }
            } else {
                this.baseService.update(entity.getId(),entity);
                AlertUtil.setAviso(AlertUtil.AlertType.UPDATE, nameEntity);
                return "redirect:"+retorno;
            }

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "views/error";
        }
    }

    public String deleteEntityFromList (Model model, Long id, List<E> lista){
        try{
            String retorno = model.getAttribute("retorno").toString();
            String nameEntity = model.getAttribute("nameEntity").toString();
            if(lista!=null){
                for(int i=0; i<lista.size(); i++){
                    if(id.equals(lista.get(i).getId())){
                        lista.remove(i);
                        AlertUtil.setAviso(AlertUtil.AlertType.DELETE,nameEntity);
                    }
                }
            }
            return "redirect:/"+nameEntity+"/formulario/0?retorno="+retorno+"&nameEntity="+nameEntity;
        }catch(Exception e){
            model.addAttribute("error", e.getStackTrace());
            return "views/error";
        }
    }

    public String guardarCambios (Model model, List<E> lista){
        try{
            String retorno = model.getAttribute("retorno").toString();
            String nameEntity = model.getAttribute("nameEntity").toString();
            if(lista!=null && lista.size()>0){
                for(E e: lista){
                    baseService.save(e);
                }
                AlertUtil.setAviso(AlertUtil.AlertType.CREATE, nameEntity);
            }
            return "redirect:"+retorno;
        }catch(Exception e){
            model.addAttribute("error", e.getMessage());
            return "views/error";
        }
    }

    public String eliminar (Model model, Long id){
        try{
            String retorno = model.getAttribute("retorno").toString();
            String nameEntity = model.getAttribute("nameEntity").toString();
            baseService.delete(id);
            AlertUtil.setAviso(AlertUtil.AlertType.DELETE, nameEntity);
            return "redirect:"+retorno;
        }catch(Exception e){
            model.addAttribute("error", e.getMessage());
            return "views/error";
        }
    }

}
