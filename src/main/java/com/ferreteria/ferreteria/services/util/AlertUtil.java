package com.ferreteria.ferreteria.services.util;

import org.springframework.ui.Model;

public class AlertUtil {
    public static String aviso;
    private static String claseColor;

    public static void SetModelAndResetValues(Model model){
        setModel(model);
        aviso=null;
        claseColor=null;
    }

    public enum AlertType {
        CREATE,
        DELETE,
        UPDATE,
        ADD,
        ERRORFORM,
        ERROR,
        LOW,
        RECOVER,
        CATALOGO,
        ERROR_PERSONALIZADO
    }

    public static void setAviso(AlertType tipo, String objeto){
        if(tipo==null) return;
        if(objeto==null || objeto.equals("")) objeto="objeto";
        switch (tipo){
            case CREATE:  aviso= "Se han agregado nuevos "+objeto+"s"; claseColor="text-bg-primary";
                break;
            case DELETE: aviso= "Se ha eliminado un "+objeto; claseColor="text-bg-warning";
                break;
            case UPDATE: aviso= "Se ha actualizado un "+objeto; claseColor="text-bg-primary";
                break;
            case ADD: aviso="Se ha agregado un "+objeto; claseColor="text-bg-info";
                break;
            case ERRORFORM: aviso="Has introducido informacion invalida, revisa lo que has escrito"; claseColor="text-bg-danger";
                break;
            case ERROR: aviso="Upss ha ocurrido un error"; claseColor="text-bg-danger";
                break;
            case LOW: aviso="Le has dado de baja a un "+objeto+", esto NO implica que lo hayas eliminado, simplemente estas restringiendo su uso"; claseColor="text-bg-warning";
                break;
            case RECOVER: aviso="Le has dado de alta a un "+objeto+", esto implica que se puede usar libremente"; claseColor="text-bg-success";
                break;
            case CATALOGO:  aviso= "Se ha actualizado el catalogo de articulos"; claseColor="text-bg-success";
                break;
            case ERROR_PERSONALIZADO: aviso= objeto; claseColor="text-bg-warning"; break;
            default: aviso="Aviso sin Mensaje"; claseColor="text-bg-primary";
        }
    }

    public static void setModel(Model model){
        model.addAttribute("aviso", aviso);
        model.addAttribute("claseColor", claseColor);
    }
}
