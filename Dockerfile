FROM maven:3.8.5-openjdk-17 AS builder

# 設定工作目錄
WORKDIR /app

# 複製 pom.xml 和原始碼
# (先複製 pom.xml 可以利用 Docker 的快取機制，當原始碼變更但依賴不變時，可以加速建置)
COPY pom.xml .
COPY src ./src

# 執行 Maven 打包指令，跳過測試
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

# 從 'builder' 階段複製打包好的 JAR 檔案
# Docker 會自動找到 target/*.jar
COPY --from=builder /app/target/*.jar app.jar

# 暴露應用程式運行的端口
EXPOSE 8080

# 容器啟動時執行的命令
ENTRYPOINT ["java","-jar","/app.jar"] 