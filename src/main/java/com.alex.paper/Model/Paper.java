package com.alex.paper.Model;

public class Paper {
    private Long id;
    private String title;
    private String author;
    private String abstractText; // abstract 為保留字，故用 abstractText
    private String journal; // 期刊
    private Integer year;   // 出版年份

    // Constructors
    public Paper() {}

    public Paper(Long id, String title, String author, String abstractText, String journal, Integer year) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.abstractText = abstractText;
        this.journal = journal;
        this.year = year;
    }

    // Getter 與 Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
