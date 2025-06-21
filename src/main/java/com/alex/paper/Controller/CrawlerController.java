package com.alex.paper.Controller;

import com.alex.paper.DTO.CrawlerRequest;
import com.alex.paper.Model.Paper;
import com.alex.paper.Service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/crawler")
@CrossOrigin(origins = "*")
public class CrawlerController {

    @Autowired
    private CrawlerService crawlerService;

    // 爬取 Google Scholar 論文（不儲存到資料庫）
    @PostMapping("/google-scholar")
    public ResponseEntity<List<Paper>> crawlGoogleScholar(@RequestBody CrawlerRequest request) {
        try {
            List<Paper> papers = crawlerService.crawlGoogleScholar(request);
            return ResponseEntity.ok(papers);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // 爬取 Google Scholar 論文並儲存到資料庫
    @PostMapping("/google-scholar/save")
    public ResponseEntity<List<Paper>> crawlAndSaveGoogleScholar(@RequestBody CrawlerRequest request) {
        try {
            List<Paper> savedPapers = crawlerService.crawlAndSave(request);
            return ResponseEntity.ok(savedPapers);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // 批量儲存指定的論文
    @PostMapping("/save-selected")
    public ResponseEntity<List<Paper>> saveSelectedPapers(@RequestBody List<Paper> papers) {
        try {
            List<Paper> savedPapers = crawlerService.saveSelectedPapers(papers);
            return ResponseEntity.ok(savedPapers);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // 簡單的爬取測試
    @GetMapping("/test")
    public ResponseEntity<String> testCrawler() {
        try {
            CrawlerRequest request = new CrawlerRequest("blockchain", 5);
            List<Paper> papers = crawlerService.crawlGoogleScholar(request);
            return ResponseEntity.ok("爬取成功！找到 " + papers.size() + " 篇論文");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("爬取失敗：" + e.getMessage());
        }
    }
} 