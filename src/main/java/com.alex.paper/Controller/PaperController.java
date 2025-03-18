package com.alex.paper.Controller;

import com.alex.paper.Model.Paper;
import com.alex.paper.Service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/papers")
public class PaperController {

    @Autowired
    private PaperService paperService;

    // 取得所有論文
    @GetMapping
    public ResponseEntity<List<Paper>> getAllPapers() {
        List<Paper> papers = paperService.getAllPapers();
        return ResponseEntity.ok(papers);
    }

    // 依 ID 取得論文
    @GetMapping("/{id}")
    public ResponseEntity<Paper> getPaperById(@PathVariable Long id) {
        Paper paper = paperService.getPaperById(id);
        return paper != null ? ResponseEntity.ok(paper) : ResponseEntity.notFound().build();
    }

    // 新增論文
    @PostMapping
    public ResponseEntity<String> createPaper(@RequestBody Paper paper) {
        if (paperService.createPaper(paper)) {
            return ResponseEntity.ok("論文新增成功");
        } else {
            return ResponseEntity.badRequest().body("論文新增失敗");
        }
    }

    // 更新論文
    @PutMapping("/{id}")
    public ResponseEntity<String> updatePaper(@PathVariable Long id, @RequestBody Paper paper) {
        paper.setId(id);
        if (paperService.updatePaper(paper)) {
            return ResponseEntity.ok("論文更新成功");
        } else {
            return ResponseEntity.badRequest().body("論文更新失敗");
        }
    }

    // 刪除論文
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePaper(@PathVariable Long id) {
        if (paperService.deletePaper(id)) {
            return ResponseEntity.ok("論文刪除成功");
        } else {
            return ResponseEntity.badRequest().body("論文刪除失敗");
        }
    }
}
