package com.ferreteria.ferreteria.services.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Enumeration;
import java.util.Hashtable;

public class HelperCheck {
    public static BindingResult convertHashtableToBindingResult(@NotNull Hashtable<String,String> mensajes, BindingResult result){
        if(mensajes.size()>0){
            Enumeration<String> keys = mensajes.keys();
            while(keys.hasMoreElements()){
                String clave = keys.nextElement();
                result.addError(new FieldError("",clave, mensajes.get(clave)));
            }
        }
        return result;
    }
}
