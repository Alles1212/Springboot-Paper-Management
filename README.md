# Spring Boot Paper Management
## Purpose
Aiming to category the paper I read using Spring Boot with MVC & Controller-Service-Dao.
## Schema
```plaintext
paper-management-system
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.example.paper
│   │   │       ├── PaperManagementSystemApplication.java  // Spring Boot 主程式
│   │   │       ├── controller
│   │   │       │   └── PaperController.java               // 控制器層
│   │   │       ├── service
│   │   │       │   ├── PaperService.java                  // 服務層介面
│   │   │       │   └── PaperServiceImpl.java              // 服務層實作
│   │   │       ├── dao
│   │   │       │   ├── PaperDao.java                      // DAO 介面
│   │   │       │   └── PaperDaoImpl.java                  // DAO 實作 (利用 Spring JDBC)
│   │   │       └── model
│   │   │           └── Paper.java                         // 論文的資料模型 (Entity)
│   │   └── resources
│   │       ├── application.properties                     // 配置檔，包含 MySQL 連線設定
│   │       └── schema.sql                                 // 資料表建立腳本(optional)
└── pom.xml
```
## Usage
- Language : Java
- Database : MySQL