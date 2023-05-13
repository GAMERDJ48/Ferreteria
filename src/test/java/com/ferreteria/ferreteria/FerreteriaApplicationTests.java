package com.ferreteria.ferreteria;

import com.ferreteria.ferreteria.services.util.ConversionUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
class FerreteriaApplicationTests {

    @Test
    void contextLoads() {
        String texto = "0.78";

        System.out.println(ConversionUtil.setScaleBigDecimal(BigDecimal.valueOf(Double.valueOf(texto.toString()))));
    }

}
