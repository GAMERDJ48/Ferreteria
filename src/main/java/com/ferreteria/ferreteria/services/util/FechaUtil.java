package com.ferreteria.ferreteria.services.util;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Set;

public class FechaUtil {

    public static final String formatDate="yyyy-MM-dd";

    public static String formatearLocalDateTimeSinAnio(LocalDateTime fecha){
        String cadena = "";
        cadena = controlarCero(fecha.getDayOfMonth()) + "/"+ controlarCero(fecha.getMonthValue()) + "  " + controlarCero(fecha.getHour()) + ":" + controlarCero(fecha.getMinute());
        return cadena;
    }

    public static String formatearLocalDateTime(LocalDateTime fecha){
        String cadena = "";
        cadena = controlarCero(fecha.getDayOfMonth()) + "/"+ controlarCero(fecha.getMonthValue()) +"/"+fecha.getYear()+ "  " + controlarCero(fecha.getHour()) + ":" + controlarCero(fecha.getMinute());
        return cadena;
    }


    private static String controlarCero(int numero){
        if(numero<=9){
            return "0"+numero;
        }else{
            return Integer.toString(numero);
        }
    }

    public static LocalDate convertToLocalDate(Date fecha){
        return Instant.ofEpochMilli(fecha.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static LocalDateTime convertDateToLocalDateTime(Date fecha){
        return Instant.ofEpochMilli(fecha.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }


    public static Date convertToDateViaInstant(LocalDate dateToConvert) {
        return Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public static Date convertToDate(LocalDateTime now){
        Date date = Date.from(
                now.atZone(ZoneId.systemDefault())
                        .toInstant());
        return date;
    }

    public static <T extends Number> LinkedHashMap<String, T> initHashWithMounths(String tipo){
        LinkedHashMap<String, T> result = new LinkedHashMap<>();
        LinkedHashMap<Integer, String> meses = getHashWithMounths();
        T valor = (T) inicializar(tipo);
        Set<Integer> keys = meses.keySet();
        for(Integer clave: keys){
            result.put(meses.get(clave), valor);
        }
        return result;
    }

    public static <T extends Number> LinkedHashMap<Integer, T> getHashWithDays(String tipo, int mes, int anio){
        LinkedHashMap<Integer, T> dias = new LinkedHashMap();
        T valor = (T) inicializar(tipo);
        int cantDias=0;
        switch (mes){
            case 4: case 6: case 9: case 11:
                cantDias = 30; break;
            case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                cantDias = 31; break;
            case 2:
                cantDias = ((anio % 4 == 0 && anio % 100 != 0) || (anio % 100 == 0 && anio % 400 == 0))
                        ? 29 : 28;
        }
        for(int i=1; i<=cantDias; i++){
            dias.put(i, valor);
        }
        return dias;
    }

    public static LinkedHashMap<Integer, String> getHashWithMounths(){
        LinkedHashMap<Integer,String> meses = new LinkedHashMap<>();
        for(int i=1; i<=12; i++){
            meses.put(i, getNameMonth(i));
        }
        return meses;
    }

    public static String getNameMonth(int mes){
        switch (mes){
            case 1: return "Enero";
            case 2: return "Febrero";
            case 3: return "Marzo";
            case 4: return "Abril";
            case 5: return "Mayo";
            case 6: return "Junio";
            case 7: return "Julio";
            case 8: return "Agosto";
            case 9: return "Septiembre";
            case 10: return "Obtubre";
            case 11: return "Noviembre";
            case 12: return "Diciembre";
            default: return "ninguno";
        }
    }

    private static Number inicializar(@NotNull String tipo){
        if(tipo.equals("Integer")){
            return 0;
        }else{
            if(tipo.equals("Float")){
                return 0F;
            }else{
                return new BigDecimal(0);
            }
        }
    }
}
