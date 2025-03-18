package com.alex.paper.Service;

import com.alex.paper.Model.Paper;
import java.util.List;

public interface PaperService {
    List<Paper> getAllPapers();
    Paper getPaperById(Long id);
    boolean createPaper(Paper paper);
    boolean updatePaper(Paper paper);
    boolean deletePaper(Long id);
}
