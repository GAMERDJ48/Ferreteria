package com.ferreteria.ferreteria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
public class FerreteriaApplication {

    public static void main(String[] args) {
        //iniciarNavegador();
        iniciarMysql();

       SpringApplication.run(FerreteriaApplication.class, args);
    }

    public static void iniciarNavegador(){
        String url = "http://localhost:9000/inicio?modoVenta=false";
        if(Desktop.isDesktopSupported()){
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(url));
            } catch (IOException | URISyntaxException e){
                e.printStackTrace();
            }
        }
        else{
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("xdg-open " + url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void iniciarMysql(){
        try {
            String [] cmd = {"C:\\xampp\\xampp_start.exe"};
            Runtime.getRuntime().exec(cmd);
        } catch (IOException ioe) {
            System.out.println (ioe);
        }
    }

}
