package com.ferreteria.ferreteria.services;

import com.ferreteria.ferreteria.entities.domain.Dolar;
import com.ferreteria.ferreteria.repositories.BaseRepository;
import com.ferreteria.ferreteria.repositories.DolarRepository;
import com.ferreteria.ferreteria.services.util.ConversionUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class DolarService extends BaseServiceImpl<Dolar,Long> {

    @Autowired
    DolarRepository dolarRepository;

    public DolarService(BaseRepository<Dolar,Long> baseRepository){
        super(baseRepository);
    }
    public BigDecimal searchDolar(){
        String url = "https://www.google.com/search?q=Dolar+en+Argentina";
        try {
            Document document = Jsoup.connect(url).get();
            Elements elements = document.getElementsByClass("lWzCpb a61j6").tagName("input");
            BigDecimal dolar = new BigDecimal(elements.get(0).attr("value"));
            return dolar;
        } catch (Exception e) {
            return null;
        }
    }

    public BigDecimal getDolar() throws Exception {
        BigDecimal valor = searchDolar();
        if(valor!=null){
            Dolar d= new Dolar(valor, LocalDateTime.now());
            this.save(d);
        }else{
            Dolar d = this.findById(1L);
            valor = d.getValor();
        }
        return valor;
    }
}
