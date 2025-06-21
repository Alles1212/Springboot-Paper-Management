package com.alex.paper.DTO;

public class CrawlerRequest {
    private String keyword;
    private int maxResults;
    private Integer yearFrom;
    private Integer yearTo;
    private String sortBy; // "relevance", "date"

    // 建構子
    public CrawlerRequest() {}

    public CrawlerRequest(String keyword, int maxResults) {
        this.keyword = keyword;
        this.maxResults = maxResults;
    }

    // Getter 和 Setter
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public Integer getYearFrom() {
        return yearFrom;
    }

    public void setYearFrom(Integer yearFrom) {
        this.yearFrom = yearFrom;
    }

    public Integer getYearTo() {
        return yearTo;
    }

    public void setYearTo(Integer yearTo) {
        this.yearTo = yearTo;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
} 