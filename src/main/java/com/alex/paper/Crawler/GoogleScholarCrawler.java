package com.alex.paper.Crawler;

import com.alex.paper.Model.Paper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class GoogleScholarCrawler {

    private WebDriver driver;
    private WebDriverWait wait;
    private boolean driverInitialized = false;

    public GoogleScholarCrawler() {
        try {
            initializeDriver();
            driverInitialized = true;
        } catch (Exception e) {
            System.err.println("ChromeDriver 初始化失敗: " + e.getMessage());
            System.err.println("將使用 Jsoup 進行爬取（功能可能受限）");
            driverInitialized = false;
        }
    }

    private void initializeDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // 無頭模式
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public List<Paper> crawlPapers(String keyword, int maxResults) {
        List<Paper> papers = new ArrayList<>();
        
        if (driverInitialized && driver != null) {
            // 使用 Selenium 爬取
            papers = crawlWithSelenium(keyword, maxResults);
        } else {
            // 使用 Jsoup 爬取（備用方案）
            papers = crawlWithJsoup(keyword, maxResults);
        }
        
        return papers;
    }

    private List<Paper> crawlWithSelenium(String keyword, int maxResults) {
        List<Paper> papers = new ArrayList<>();
        
        try {
            // 1. 訪問 Google Scholar
            String searchUrl = buildSearchUrl(keyword);
            driver.get(searchUrl);
            
            // 2. 等待頁面載入
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".gs_r")));
            
            // 3. 解析搜尋結果
            String pageSource = driver.getPageSource();
            Document doc = Jsoup.parse(pageSource);
            
            // 4. 提取論文資訊
            Elements paperElements = doc.select(".gs_r");
            
            for (Element element : paperElements) {
                if (papers.size() >= maxResults) break;
                
                Paper paper = extractPaperInfo(element);
                if (paper != null) {
                    papers.add(paper);
                }
            }
            
        } catch (Exception e) {
            System.err.println("Selenium 爬取失敗: " + e.getMessage());
            // 如果 Selenium 失敗，嘗試使用 Jsoup
            papers = crawlWithJsoup(keyword, maxResults);
        }
        
        return papers;
    }

    private List<Paper> crawlWithJsoup(String keyword, int maxResults) {
        List<Paper> papers = new ArrayList<>();
        
        try {
            // 1. 建立搜尋 URL
            String searchUrl = buildSearchUrl(keyword);
            
            // 2. 使用 Jsoup 直接發送 HTTP 請求
            Document doc = Jsoup.connect(searchUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .timeout(10000)
                    .get();
            
            // 3. 提取論文資訊
            Elements paperElements = doc.select(".gs_r");
            
            for (Element element : paperElements) {
                if (papers.size() >= maxResults) break;
                
                Paper paper = extractPaperInfo(element);
                if (paper != null) {
                    papers.add(paper);
                }
            }
            
        } catch (Exception e) {
            System.err.println("Jsoup 爬取失敗: " + e.getMessage());
        }
        
        return papers;
    }

    private String buildSearchUrl(String keyword) {
        return "https://scholar.google.com/scholar?q=" + keyword.replace(" ", "+");
    }

    private Paper extractPaperInfo(Element element) {
        try {
            // 提取標題
            Element titleElement = element.select(".gs_rt a").first();
            String title = titleElement != null ? titleElement.text() : "";
            
            // 提取作者、期刊、年份
            Element authorElement = element.select(".gs_a").first();
            String author = "";
            String journal = "";
            Integer year = null;
            if (authorElement != null) {
                String authorText = authorElement.text();
                // 通常格式: "作者, 作者 - 期刊, 年份" 或 "作者 - 期刊, 年份"
                String[] parts = authorText.split(" - ");
                if (parts.length > 0) {
                    author = parts[0];
                }
                if (parts.length > 1) {
                    // 期刊與年份通常在 parts[1]
                    String[] journalYear = parts[1].split(",");
                    if (journalYear.length > 0) {
                        journal = journalYear[0].trim();
                    }
                    if (journalYear.length > 1) {
                        // 嘗試從字串中找出四位數年份
                        String yearStr = journalYear[1].replaceAll("[^0-9]", "").trim();
                        if (yearStr.matches("\\d{4}")) {
                            year = Integer.parseInt(yearStr);
                        }
                    }
                }
                
                // 如果上面的方法沒找到年份，嘗試從整個 authorText 中尋找
                if (year == null) {
                    String[] words = authorText.split("\\s+");
                    for (String word : words) {
                        word = word.replaceAll("[^0-9]", "").trim();
                        if (word.matches("\\d{4}")) {
                            int potentialYear = Integer.parseInt(word);
                            // 確保年份在合理範圍內
                            if (potentialYear >= 1900 && potentialYear <= 2030) {
                                year = potentialYear;
                                break;
                            }
                        }
                    }
                }
            }
            
            // 提取摘要
            Element abstractElement = element.select(".gs_rs").first();
            String abstractText = abstractElement != null ? abstractElement.text() : "";
            
            // 建立 Paper 物件
            if (!title.isEmpty()) {
                Paper paper = new Paper();
                paper.setTitle(title);
                paper.setAuthor(author);
                paper.setJournal(journal);
                paper.setYear(year);
                paper.setAbstractText(abstractText);
                return paper;
            }
            
        } catch (Exception e) {
            System.err.println("提取論文資訊時發生錯誤: " + e.getMessage());
        }
        
        return null;
    }

    public void close() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.err.println("關閉 ChromeDriver 時發生錯誤: " + e.getMessage());
            }
        }
    }
} 