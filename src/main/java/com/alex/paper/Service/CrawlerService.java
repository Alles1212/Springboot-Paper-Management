package com.alex.paper.Service;

import com.alex.paper.Crawler.GoogleScholarCrawler;
import com.alex.paper.DTO.CrawlerRequest;
import com.alex.paper.Model.Paper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CrawlerService {

    @Autowired
    private GoogleScholarCrawler googleScholarCrawler;

    @Autowired
    private PaperService paperService;

    public List<Paper> crawlGoogleScholar(CrawlerRequest request) {
        // 1. 爬取論文
        List<Paper> crawledPapers = googleScholarCrawler.crawlPapers(
            request.getKeyword(), 
            request.getMaxResults()
        );

        // 2. 過濾重複論文
        List<Paper> filteredPapers = filterDuplicatePapers(crawledPapers);

        // 3. 根據年份過濾（如果指定了年份範圍）
        if (request.getYearFrom() != null || request.getYearTo() != null) {
            filteredPapers = filterByYear(filteredPapers, request.getYearFrom(), request.getYearTo());
        }

        return filteredPapers;
    }

    public List<Paper> crawlAndSave(CrawlerRequest request) {
        // 1. 爬取論文
        List<Paper> papers = crawlGoogleScholar(request);

        // 2. 儲存到資料庫
        List<Paper> savedPapers = new ArrayList<>();
        for (Paper paper : papers) {
            if (paperService.createPaper(paper)) {
                savedPapers.add(paper);
            }
        }

        return savedPapers;
    }

    // 批量儲存指定的論文
    public List<Paper> saveSelectedPapers(List<Paper> papers) {
        List<Paper> savedPapers = new ArrayList<>();
        
        for (Paper paper : papers) {
            // 檢查論文是否已存在
            if (!isPaperExists(paper)) {
                if (paperService.createPaper(paper)) {
                    savedPapers.add(paper);
                }
            }
        }
        
        return savedPapers;
    }

    private List<Paper> filterDuplicatePapers(List<Paper> papers) {
        // 簡單的去重邏輯：根據標題和作者
        return papers.stream()
            .filter(paper -> !isPaperExists(paper))
            .collect(Collectors.toList());
    }

    private boolean isPaperExists(Paper paper) {
        // 檢查論文是否已存在於資料庫中
        List<Paper> existingPapers = paperService.getAllPapers();
        return existingPapers.stream()
            .anyMatch(existing -> 
                existing.getTitle().equalsIgnoreCase(paper.getTitle()) &&
                existing.getAuthor().equalsIgnoreCase(paper.getAuthor())
            );
    }

    private List<Paper> filterByYear(List<Paper> papers, Integer yearFrom, Integer yearTo) {
        return papers.stream()
            .filter(paper -> {
                // 使用 Paper 物件的 year 欄位
                Integer year = paper.getYear();
                if (year != null) {
                    if (yearFrom != null && year < yearFrom) return false;
                    if (yearTo != null && year > yearTo) return false;
                    return true;
                }
                // 如果沒有年份資訊，預設包含
                return true;
            })
            .collect(Collectors.toList());
    }
} 