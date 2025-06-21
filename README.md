# Spring Boot Paper Management
## Purpose
This is a Spring Boot-based academic paper management system, using MVC architecture and Controller-Service-DAO pattern. The system integrates Google Scholar crawler, which can automatically search, extract and manage academic papers.
## Main Features
### Thesis Crawling Function
- **Google Scholar auto-crawler**: Support keyword search and paper information extraction.
- **Intelligent Year Filtering**: Filter papers according to a specified year range.
- **Duplicate Paper Detection**: Filter existing papers automatically(auto-check from your database).

### Thesis Management Features
- **Complete paper information**: title, author, journal, publication year, abstract.
- **Database management**: Complete CRUD operation.
- **Modernized interface**:Responsive design, supports various devices

### Containerized Deployment
- **Docker Compose**: one-click deployment with consistent environment.
- **MySQL Database**: Persistent Data Storage.(free to utlize other databases)

## Schema
```plaintext
paper-management-system
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.alex.paper
│   │   │       ├── SpringbootPaperApplication.java      // Spring Boot 主程式
│   │   │       ├── Controller
│   │   │       │   ├── PaperController.java             // 論文管理控制器
│   │   │       │   └── CrawlerController.java           // 爬蟲控制器
│   │   │       ├── Service
│   │   │       │   ├── PaperService.java                // 論文服務介面
│   │   │       │   ├── PaperServiceImpl.java            // 論文服務實作
│   │   │       │   └── CrawlerService.java              // 爬蟲服務
│   │   │       ├── Dao
│   │   │       │   ├── PaperDao.java                    // DAO 介面
│   │   │       │   └── PaperDaoImpl.java                // DAO 實作 (Spring JDBC)
│   │   │       ├── Model
│   │   │       │   └── Paper.java                       // 論文資料模型
│   │   │       ├── DTO
│   │   │       │   └── CrawlerRequest.java              // 爬蟲請求 DTO
│   │   │       └── Crawler
│   │   │           └── GoogleScholarCrawler.java        // Google Scholar 爬蟲
│   │   └── resources
│   │       ├── application.properties                   // 配置檔
│   │       ├── schema.sql                               // 資料表建立腳本
│   │       └── static
│   │           ├── index.html                           // 主頁面
│   │           └── crawler.html                         // 爬蟲介面
├── Dockerfile                                           // Docker 建置檔
├── docker-compose.yml                                   // Docker Compose 配置
└── pom.xml
```
## Configuration
- Language : Java
- Database : MySQL
### DB structure
```sql
CREATE TABLE paper (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(500) NOT NULL,           -- 論文標題
    author VARCHAR(500) NOT NULL,          -- 作者
    journal VARCHAR(500),                  -- 期刊名稱
    year INT,                              -- 出版年份
    abstractText TEXT                      -- 摘要
);
```
## Usage
1. **clone the repo** : ```git clone https://github.com/Alles1212/Springboot-Paper-Management.git```
2. **start services** : ```docker-compose up --build```
3. **access the system** : entry ```http://localhost:8080```
4. **check MySQL** : ```docker exec -it mysql-db mysql -uroot -pspringboot myjdbc``` then ```show tables;```(whether table named `paper` exist) then ```select * from paper```(if no data in it, will be empty)
## Notice
### Law
- follow Google Scholar usage
- merely for academic reserach

### Limit
- Google Scholar may adjust its architecture
- avoid huge crawling

## Pre-requsite
- Docker

## Issue
- still work on selenium on ChromeDriver

## Future Outlook
- support for more acdamic library
- precise year extract
- advanced keyword search
- export for csv(however due to paper management, filtering by yourself might goes well according to your research topic into the database)

## Contact Me
Email : alleszhe1212@gmail.com
Linkedin : linkedin.com/in/竣哲陳alex