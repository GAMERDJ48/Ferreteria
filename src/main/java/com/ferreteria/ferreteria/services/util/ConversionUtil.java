package com.ferreteria.ferreteria.services.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ConversionUtil {
    public static String bigDecimalToString(BigDecimal numero, boolean escala){
        if(escala){
            numero = setScaleBigDecimal(numero);
        }else{
            numero = setScaleBigDecimalWithThreeDecimals(numero);
        }
        String cadena = "";
        String cadenaNumero = numero.toString();
        int contador=0;
        boolean decimal = true;
        for(int i=(cadenaNumero.length()-1); i>=0; i--){
            if(cadenaNumero.charAt(i)=='.'){
                decimal=false;
                cadena = "," + cadena;
            }else{
                cadena = cadenaNumero.charAt(i) + cadena;
            }
            if(contador==3 && (i-1)>=0){
                contador=0;
                cadena =  "." + cadena;
            }
            if(!decimal){
                contador++;
            }
        }
        return cadena;
    }


    @Contract("null -> new")
    public static @NotNull BigDecimal setScaleBigDecimal (BigDecimal decimal){
        return (decimal==null) ? new BigDecimal(0.00) : decimal.setScale(2, RoundingMode.HALF_UP);
    }

    @Contract("null -> new")
    public static @NotNull BigDecimal setScaleBigDecimalWithThreeDecimals (BigDecimal decimal){
        return (decimal==null) ? new BigDecimal(0.000) : decimal.setScale(3, RoundingMode.HALF_UP);
    }



}
