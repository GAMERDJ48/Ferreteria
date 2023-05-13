package com.ferreteria.ferreteria.services.excel;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Iterator;

import com.ferreteria.ferreteria.entities.domain.ExcelSave;
import com.ferreteria.ferreteria.entities.enumerations.Moneda;
import com.ferreteria.ferreteria.entities.enumerations.Proveedor;
import com.ferreteria.ferreteria.repositories.BaseRepository;
import com.ferreteria.ferreteria.repositories.ExcelSaveRepository;
import com.ferreteria.ferreteria.services.ArticuloService;
import com.ferreteria.ferreteria.services.BaseServiceImpl;
import com.ferreteria.ferreteria.services.TipoArticuloService;
import com.ferreteria.ferreteria.services.util.ConversionUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;


public class ExcelReader {
    private static int indiceCodigo;
    private static int indiceDescripcion;
    private static int indiceComienzo;
    private static int indicePrecio;
    private static int indiceMoneda;
    private static int indiceTipo;

    private static Proveedor proveedor;

    @Autowired
    private ArticuloService articuloService;
    @Autowired
    private TipoArticuloService tipoArticuloService;


    private final String ruta = "C://ferreteria/excel";

    public Path escribir(String nombreArchivo, MultipartFile archivo) throws Exception {
        Path rutaArchivo = Paths.get(ruta + "//" + nombreArchivo);
        try {
            System.out.println("Escribiendo Archivo...");
            Files.write(rutaArchivo, archivo.getBytes());
        } catch (IOException e) {
            System.out.println("Hubo un error al escribir el archivo");
            e.printStackTrace();
        }
        return rutaArchivo;
    }

    public void eliminarArchivo(Path ruta) throws Exception {
        try{
            System.out.println("Eliminando Archivo...");
            Files.delete(ruta);
        }catch (IOException e){
            System.out.println("Hubo un error al eliminar el archivo");
            e.printStackTrace();
        }
    }

    public boolean leer(MultipartFile archivo, Proveedor proveedorExcel, ArticuloService articuloService1, ExcelService excelService) throws Exception {
        validarArchivo(archivo);

        String nombreArchivo = archivo.getOriginalFilename();

        Path rutaArchivo = escribir(nombreArchivo, archivo);

        articuloService = articuloService1;

        if(proveedorExcel==null){
            proveedorExcel = identificarProveedor(nombreArchivo);
            if(proveedorExcel==null) return false;
        }

        configProveedor(proveedorExcel);

        try (FileInputStream file = new FileInputStream(new File(rutaArchivo.toString()))) {
            // leer archivo excel
            Workbook worbook = WorkbookFactory.create(file);

            //obtener la hoja que se va leer
            Sheet sheet = worbook.getSheetAt(0);

            //obtener todas las filas de la hoja excel
            Iterator<Row> rowIterator = sheet.rowIterator();

            recorrerExcel(rowIterator);

            articuloService.articulos=null;
            tipoArticuloService.tipoArticulos=null;

            excelService.save(new ExcelSave(nombreArchivo, LocalDate.now()));

            eliminarArchivo(rutaArchivo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public void validarArchivo(MultipartFile archivo) throws Exception {
        String nombreArchivo = archivo.getOriginalFilename();

        if(nombreArchivo==null || archivo.isEmpty() || nombreArchivo.equals("")) throw new Exception("No se especifico un archivo valido");
        String extension = FilenameUtils.getExtension(nombreArchivo);
        if(!extension.equalsIgnoreCase("xls") && !extension.equalsIgnoreCase("xlsx")){
            throw new Exception("Error: No se especifico un archivo de excel");
        }

    }

    private static Proveedor identificarProveedor(String nombreArchivo) throws Exception {
        if(nombreArchivo.toLowerCase().contains("listado de productos")) return Proveedor.NUEVO_ENFOQUE;

        for(Proveedor p: Proveedor.values()){
            if(nombreArchivo.toUpperCase().contains(p.name())) return p;
        }

        throw new Exception("No se pudo identificar el proveedor, por favor seleccione uno de la lista");
    }

    private static void configProveedor(Proveedor proveedorExcel) {
        proveedor = proveedorExcel;
        if (proveedorExcel.equals(Proveedor.DAYLIGHT)) {
            indiceCodigo = 0;
            indiceDescripcion = 1;
            indicePrecio = 4;
            indiceMoneda = 3;
            indiceTipo=-1;
        }else{
            indiceCodigo=-1;
            indiceDescripcion=-1;
            indicePrecio=-1;
            indiceMoneda = -1;
            indiceTipo = -1;
        }
    }

    public void recorrerExcel(Iterator<Row> filas) throws Exception {
        Row row;
        int indiceColumna;
        String codigo = null;
        String descripcion = null;
        Moneda moneda = Moneda.PESO;
        BigDecimal precio = null;
        String tipo = null;
        Cell cell;
        boolean entrar=false;
        while (filas.hasNext()) {
            row = filas.next();
            Iterator<Cell> celdas = row.cellIterator();
            indiceColumna = 0;

            while (celdas.hasNext()) {

                cell = celdas.next();
                Object objeto = getValue(cell);
                if (objeto != null) {
                    String objetoCadena = objeto.toString();
                    if(!objetoCadena.equals("") && !objetoCadena.equals(" ")){
                        if((indiceCodigo<0 || indiceDescripcion<0 || indicePrecio<0) || entrar){
                            switch (objetoCadena.toLowerCase()){
                                case "codigo": case "cod": case "cod. barras": indiceCodigo=indiceColumna; entrar=true; break;
                                case "descripcion": case "descripción": indiceDescripcion=indiceColumna; entrar=true; break;
                                case "u$s": case "$": indiceMoneda=indiceColumna; entrar=true; break;
                                case "venta al publico": case "precio de venta" : case "$pxpublico_coniva":  indicePrecio=indiceColumna;  entrar=true; break;
                                case "rubro": case "descrubro":  indiceTipo=indiceColumna; entrar=true; break;
                            }
                        }else{
                            if (indiceColumna == indiceCodigo && !objeto.toString().equalsIgnoreCase("codigo")) {
                                codigo = objetoCadena;
                                precio = null;
                            } else {
                                if (indiceColumna == indiceDescripcion) {
                                    descripcion = objetoCadena;
                                } else {
                                    if (indiceMoneda == indiceColumna) {
                                        moneda = (objetoCadena.equalsIgnoreCase("u$s")) ? Moneda.DOLAR : Moneda.PESO;
                                    } else {
                                        if (indiceColumna == indicePrecio) {
                                            Double aux;
                                            try{
                                                aux = Double.valueOf(objetoCadena);
                                            }catch(NumberFormatException e){
                                                aux = Double.valueOf(cambiarComaPorPunto(objetoCadena));
                                            }
                                            precio = ConversionUtil.setScaleBigDecimal(BigDecimal.valueOf(aux));
                                        } else {
                                            if (indiceTipo == indiceColumna) {
                                                tipo = objetoCadena;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (codigo != null && indiceTipo == -1) {
                        tipo = codigo;
                    }
                }
                indiceColumna++;
                if (precio != null && (indiceTipo == -1 || tipo != null)) {
                    this.articuloService.checkIfExistsAndSave(codigo, descripcion, precio, moneda, tipo, proveedor);
                    //System.out.println("Codigo: " + codigo + " | descripcion: " + descripcion + " | precio: " + precio + " | moneda: " + moneda + " | tipo: " + tipo + " | proveedor: " + proveedor);

                    precio = null;
                    codigo = null;
                    descripcion = null;
                    moneda = Moneda.PESO;
                }

            }

            entrar=false;
        }
    }

    private String cambiarComaPorPunto(String cadena){
        cadena = cadena.replace(",", ".");
        return cadena;
    }


    private static Object getValue(Cell cell) {
        if (cell.getCellType() == CellType.BOOLEAN) {
            return cell.getBooleanCellValue();
        } else {
            if (cell.getCellType() == CellType.NUMERIC) {
                return eliminarNotaciónCientifica(cell.getNumericCellValue());
            } else {
                if (cell.getCellType() == CellType.STRING) {
                    return cell.getStringCellValue();
                } else {
                    if (cell.getCellType() == CellType.ERROR) {
                        return cell.getErrorCellValue();
                    } else {
                        if (cell.getCellType() == CellType.FORMULA) {
                            return cell.getNumericCellValue();
                        } else {
                            return null;
                        }
                    }
                }
            }
        }
    }

    public static String eliminarNotaciónCientifica(@NotNull  Double Número) {
        return new DecimalFormat("#.####################################").format(Número);
    }

}