package com.ferreteria.ferreteria.services.util;

import org.springframework.data.domain.*;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PaginationUtil {

    public static <T> Page<T> convertListToPage(Pageable pageable, List<T> items){
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<T> list;

        if (items.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, items.size());
            list = items.subList(startItem, toIndex);
        }

        Page<T> listPage = new PageImpl(list, PageRequest.of(currentPage, pageSize), items.size());

        return listPage;
    }

    private static int currentPage;

    public static Pageable getPageable(Optional<Integer> page){
        currentPage = page.orElse(1);
        return PageRequest.of(currentPage - 1, pageSize);
    }

    public static Pageable getPageable(Optional<Integer> page, Sort sort){
        currentPage = page.orElse(1);
        return PageRequest.of(currentPage - 1, pageSize, sort);
    }

    private static final int pageSize = 10;

    public static  <T> Model getModelPaginated(Model model, Optional<Integer> page, Iterable<T> listSource) throws Exception{
        try{
            Page<?> listPage;
            List<?> list;
            if(listSource==null) return model;

            Pageable pageable = getPageable(page);

            listPage = (listSource instanceof  ArrayList) ?
                    convertListToPage(pageable,(ArrayList<T>)listSource) :
                    ((Page<?>)listSource);

            list = listPage.toList();
            int totalPages = listPage.getTotalPages();
            int[] arreglo = getInicioYFinalPagination(page, totalPages);
            int start=arreglo[0], end= arreglo[1], valor=arreglo[2];
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(start, end)
                        .boxed()
                        .collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
            }
            model.addAttribute("valor", valor);
            model.addAttribute("pageFinalNumber", totalPages);
            model.addAttribute("listPage", listPage);
            model.addAttribute("entities", list);
            return model;
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public static int[] getInicioYFinalPagination(Optional<Integer> page,  int totalPages){
        int currentPage = page.orElse(1);
        final int size= 7;
        int start=1, end=1, valor;
        if(totalPages<=size){
            start=1;
            end=totalPages;
            valor=0;
        }else{
            if(currentPage<size){
                start=1;
                valor=1;
                end=size;
            }else{
                if(currentPage>(totalPages-size)){
                    start= totalPages-size;
                    if(start==1){
                        start=2;
                    }
                    end = totalPages;
                    valor=2;
                }else{
                    valor =3;
                    int division = (totalPages/size);
                    for(int i=1; i<division;i++){
                        if(currentPage>=size*i && currentPage<size*(i+1)){
                            start=size*i-1;
                            if((start)==1){
                                start = 2;
                            }
                            if(totalPages>(size*(i+1))){
                                end = size*(i+1);
                            }else{
                                end = totalPages;
                            }
                        }
                    }
                }
            }
        }
        int[] arreglo = {start, end, valor};
        return arreglo;
    }

}
