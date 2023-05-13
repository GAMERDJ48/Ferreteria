package com.ferreteria.ferreteria.services.excel;

import com.ferreteria.ferreteria.entities.domain.ExcelSave;
import com.ferreteria.ferreteria.repositories.BaseRepository;
import com.ferreteria.ferreteria.repositories.ExcelSaveRepository;
import com.ferreteria.ferreteria.services.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ExcelService extends BaseServiceImpl<ExcelSave,Long> {

    @Autowired
    ExcelSaveRepository repository;

    public ExcelService(BaseRepository<ExcelSave, Long> baseRepository){
        super(baseRepository);
    }

    @Override
    public List<ExcelSave> findAll() throws Exception {
        try{
            return super.findAll(Sort.by(Sort.Direction.DESC, "fecha"));
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }


}
