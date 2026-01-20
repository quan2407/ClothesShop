ClothesShop Backend – Setup Guide
1. Tổng quan

Đây là backend của hệ thống ClothesShop, xây dựng bằng Spring Boot, sử dụng:

PostgreSQL (Database)

Redis (Cache / Session / Lock)

Docker & Docker Compose

JPA / Hibernate

JWT Authentication

2. Yêu cầu môi trường (Prerequisites)
2.1 Phần mềm cần cài
Công cụ	Phiên bản khuyến nghị
Java	17
Maven	3.8+
Docker	24+
Docker Compose	v2
Git	Latest

 Kiểm tra nhanh:


java -version
mvn -version
docker -v
docker compose version

3. Cấu trúc Docker Compose

Hệ thống sử dụng PostgreSQL + Redis được cấu hình trong docker-compose.yml.

3.1 docker-compose.yml
services:
  redis:
    image: redis:8
    container_name: redis
    ports:
      - "6379:6379"

4. Cấu hình Database & Redis trong Spring Boot
4.1 application.properties
spring.application.name=ClothesShop
spring.datasource.url=jdbc:postgresql://localhost:5432/clothesshopdb
spring.datasource.username=postgres
spring.datasource.password=12345
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update


jwt.secret-key=x3QmR7FvB8Wn9uYp5A1C2dE4G6H0JkLZsTqP8oM5rVYwXfK9eS7U6iN+DaRbC4hE2
jwt.access-token-expiration-minutes=30
jwt.refresh-token-expiration-days=14

spring.data.redis.host=localhost
spring.data.redis.port=6379

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=quanvidaia3@gmail.com
spring.mail.password=mdzomjfqaxyznovi

spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.default-encoding=UTF-8


5. Chạy Database & Redis

Tại thư mục chứa docker-compose.yml:

docker compose up -d


Kiểm tra container:

docker ps


6. Seed dữ liệu mẫu

Dữ liệu mẫu được khai báo thông qua:

CommandLineRunner


7. Chạy Backend Server
7.1 Chạy bằng Maven
mvn spring-boot:run

7.2 Hoặc chạy file JAR
mvn clean package
java -jar target/clothes-shop-backend.jar


Server mặc định chạy tại:

http://localhost:8080

8. Chạy Test
8.1 Unit Test & Integration Test
mvn test


Lưu ý:

Integration Test sử dụng Testcontainers

Test sẽ tự động:

**Pull image PostgreSQL
**
Khởi tạo database tạm thời

Không ảnh hưởng dữ liệu thật

9. Redis được dùng cho mục đích gì?

Cache dữ liệu truy vấn

Session / Token blacklist (Logout)


10. Ghi chú

Môi trường test sử dụng Testcontainers

Môi trường dev sử dụng Docker Compose

12. Tác giả

Backend Developer: Quân Nguyễn

Project: ClothesShop Backend System
