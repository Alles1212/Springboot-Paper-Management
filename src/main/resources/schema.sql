CREATE TABLE IF NOT EXISTS paper (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    abstractText TEXT
    );
