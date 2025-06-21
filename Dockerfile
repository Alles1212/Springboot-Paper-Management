# --- 第一階段: 使用 Maven 映像來建置專案 ---
# 我們使用一個官方的 Maven 映像，它本身基於 openjdk-17
# AS builder 是一個命名，讓我們可以在後面的階段引用它
FROM maven:3.8.5-openjdk-17 AS builder

# 設定工作目錄
WORKDIR /app

# 複製 pom.xml 和原始碼
# (先複製 pom.xml 可以利用 Docker 的快取機制，當原始碼變更但依賴不變時，可以加速建置)
COPY pom.xml .
COPY src ./src

# 執行 Maven 打包指令，跳過測試
RUN mvn clean package -DskipTests


# --- 第二階段: 建立最終的輕量級映像 ---
# 使用一個輕量的 JRE 即可，不需要完整的 JDK
FROM openjdk:17-jdk-slim

# 安裝 Chrome 和 ChromeDriver (支援 ARM64)
RUN apt-get update && apt-get install -y \
    wget \
    gnupg \
    unzip \
    curl \
    ca-certificates \
    && wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update \
    && apt-get install -y google-chrome-stable || \
    (echo "Chrome installation failed, trying alternative method" && \
     wget -O /tmp/chrome.deb https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb && \
     apt-get install -y /tmp/chrome.deb && \
     rm /tmp/chrome.deb) || \
    (echo "Chrome installation failed, using headless Chrome alternative" && \
     apt-get install -y chromium && \
     ln -s /usr/bin/chromium /usr/bin/google-chrome-stable)

# 安裝 ChromeDriver (根據架構選擇正確版本)
RUN if [ "$(uname -m)" = "aarch64" ]; then \
        echo "ARM64 architecture detected, installing ARM64 ChromeDriver" && \
        CHROME_DRIVER_VERSION=$(curl -sS chromedriver.storage.googleapis.com/LATEST_RELEASE) && \
        wget -O /tmp/chromedriver.zip https://chromedriver.storage.googleapis.com/${CHROME_DRIVER_VERSION}/chromedriver_linux64.zip && \
        unzip /tmp/chromedriver.zip chromedriver -d /usr/local/bin/ && \
        chmod +x /usr/local/bin/chromedriver; \
    else \
        echo "x86_64 architecture detected, installing x86_64 ChromeDriver" && \
        CHROME_DRIVER_VERSION=$(curl -sS chromedriver.storage.googleapis.com/LATEST_RELEASE) && \
        wget -O /tmp/chromedriver.zip https://chromedriver.storage.googleapis.com/${CHROME_DRIVER_VERSION}/chromedriver_linux64.zip && \
        unzip /tmp/chromedriver.zip chromedriver -d /usr/local/bin/ && \
        chmod +x /usr/local/bin/chromedriver; \
    fi && \
    rm -f /tmp/chromedriver.zip && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# 從 'builder' 階段複製打包好的 JAR 檔案
# Docker 會自動找到 target/*.jar
COPY --from=builder /app/target/*.jar app.jar

# 暴露應用程式運行的端口
EXPOSE 8080

# 容器啟動時執行的命令
ENTRYPOINT ["java","-jar","/app.jar"] 