package com.ferreteria.ferreteria.services.util;

import com.ferreteria.ferreteria.entities.domain.Base;

import java.util.ArrayList;
import java.util.List;

public class FiltroUtil<E extends Base>{
    public List mergeListAndItem(E item, List<E> entityList){
        if(item==null) {return entityList;}
        else{
            entityList.add(item);
            entityList = deleteMismatches(entityList);
        }
        return entityList;
    }
    public List mergeLists(List<E> dtoList, List<E> entityList){
        if(dtoList.isEmpty()) {return entityList;}
        else{
            for(E f: entityList){
                dtoList.add(f);
            }
            dtoList = deleteMismatches(dtoList);
        }
        return dtoList;
    }

    public List<E> deleteMismatches(List<E> dtoList){
        List<E> dtoListNoCo = new ArrayList<>();
        boolean agregado;
        for(int i=0; i<dtoList.size();i++){
            agregado=false;
            for(int j=1; j<dtoList.size(); j++){
                if(dtoList.get(i).getId()==dtoList.get(j).getId() && i!=j){
                    if(!agregado){
                        dtoListNoCo.add(dtoList.get(j));
                        agregado=true;
                    }
                    dtoList.remove(j);
                    j--;
                }
            }
        }
        return dtoListNoCo;
    }
}
