package com.ferreteria.ferreteria.controllers;

import com.ferreteria.ferreteria.services.ArticuloService;
import com.ferreteria.ferreteria.services.excel.ExcelReader;
import com.ferreteria.ferreteria.entities.enumerations.Proveedor;
import com.ferreteria.ferreteria.services.excel.ExcelService;
import com.ferreteria.ferreteria.services.util.AlertUtil;
import com.ferreteria.ferreteria.services.util.ClassHTMLUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    ArticuloService articuloService;

    @Autowired
    ExcelService excelService;


    @RequestMapping(method = RequestMethod.GET)
    public String excel(Model model) {
        try {
            model.addAttribute("excels", excelService.findAll());
            ClassHTMLUtil.setModel(model);
            return "views/formularios/uploadexcel";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "views/error";
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public String excel(Model model, @RequestParam("archivo") MultipartFile archivo, @RequestParam(value = "proveedor", required = false) Proveedor proveedor) {
        try{
            model.addAttribute("excels", excelService.findAll());
            ClassHTMLUtil.setModel(model);
            new ExcelReader().leer(archivo, proveedor, articuloService,excelService);
            AlertUtil.setAviso(AlertUtil.AlertType.CATALOGO, null);
            return "redirect:/inicio?modoVenta=false";
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("proveedores", Proveedor.values());
            model.addAttribute("mensaje", e.getMessage() );
            return "views/formularios/uploadexcel";
        }

    }
}
