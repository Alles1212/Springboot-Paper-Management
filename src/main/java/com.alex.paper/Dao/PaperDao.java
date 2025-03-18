package com.alex.paper.Dao;

import com.alex.paper.Model.Paper;
import java.util.List;

public interface PaperDao {
    List<Paper> findAll();
    Paper findById(Long id);
    int save(Paper paper);
    int update(Paper paper);
    int deleteById(Long id);
}
