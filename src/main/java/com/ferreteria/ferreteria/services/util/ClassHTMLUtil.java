package com.ferreteria.ferreteria.services.util;

import org.springframework.ui.Model;

public class ClassHTMLUtil {

    public static void setModel(Model model){
        model.addAttribute("table_head","table-primary" );
        model.addAttribute("table","table table-hover mt-3");
        model.addAttribute("table_row", "table-default");
        model.addAttribute("table_row_low", "table-danger");
    }
}
