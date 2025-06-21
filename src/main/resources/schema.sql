CREATE TABLE IF NOT EXISTS paper (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(500) NOT NULL,
    author VARCHAR(500) NOT NULL,
    journal VARCHAR(500),
    year INT,
    abstractText TEXT
    );
