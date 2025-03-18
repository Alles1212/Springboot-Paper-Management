package com.alex.paper.Service;

import com.alex.paper.Dao.PaperDao;
import com.alex.paper.Model.Paper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaperServiceImpl implements com.alex.paper.Service.PaperService {

    @Autowired
    private PaperDao paperDao;

    @Override
    public List<Paper> getAllPapers() {
        return paperDao.findAll();
    }

    @Override
    public Paper getPaperById(Long id) {
        return paperDao.findById(id);
    }

    @Override
    public boolean createPaper(Paper paper) {
        return paperDao.save(paper) > 0;
    }

    @Override
    public boolean updatePaper(Paper paper) {
        return paperDao.update(paper) > 0;
    }

    @Override
    public boolean deletePaper(Long id) {
        return paperDao.deleteById(id) > 0;
    }
}
