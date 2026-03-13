# URL Shortener Service

A Spring Boot web application that shortens long URLs into compact, shareable short links and tracks visit analytics.

## Features

- Shorten any valid URL to an 8-character Base62 short code
- Automatic redirect from short link to the original URL
- Duplicate detection — the same URL always returns the same short code
- Visit tracking: counts clicks and records the last visited timestamp
- Analytics dashboard showing all short URLs ranked by visit count

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 25 |
| Framework | Spring Boot 4.1.0 |
| Persistence | Spring Data JPA + Hibernate 7 |
| Database | MySQL 8 |
| Templating | Thymeleaf |
| Validation | Jakarta Bean Validation (Hibernate Validator) |
| Build tool | Gradle |
| Utilities | Lombok |

## Prerequisites

- Java 25+
- MySQL 8 running locally on port `3306`
- Gradle (or use the included `gradlew` wrapper)

## Database Setup

Create the database in MySQL before starting the app:

```sql
CREATE DATABASE urlshortener;
```

The application uses `spring.jpa.hibernate.ddl-auto=update`, so the `short_urls` table is created automatically on first run.

## Configuration

All settings are in [src/main/resources/application.properties](src/main/resources/application.properties):

```properties
server.port=8081

spring.datasource.url=jdbc:mysql://localhost:3306/urlshortener?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
```

Update `username` and `password` to match your local MySQL credentials.

## Running the App

```bash
# Windows
.\gradlew.bat bootRun

# macOS / Linux
./gradlew bootRun
```

The application starts at **http://localhost:8081**.

## Building a JAR

```bash
.\gradlew.bat build        # Windows
./gradlew build             # macOS / Linux
```

The executable JAR is produced at `build/libs/Urlshortenerservice-0.0.1-SNAPSHOT.jar`.

```bash
java -jar build/libs/Urlshortenerservice-0.0.1-SNAPSHOT.jar
```

## Endpoints

| Method | Path | Description |
|---|---|---|
| `GET` | `/` | Home page — URL shortening form |
| `POST` | `/shorten` | Submit a URL to shorten |
| `GET` | `/{shortCode}` | Redirect to the original URL |
| `GET` | `/analytics` | View all links and their click counts |

## Project Structure

```
src/
└── main/
    ├── java/com/example/urlshortenerservice/
    │   ├── controller/      # HomeController — web routes
    │   ├── dto/             # ShortenUrlRequest / ShortenUrlResponse
    │   ├── entity/          # ShortUrl JPA entity
    │   ├── repository/      # ShortUrlRepository (Spring Data JPA)
    │   ├── service/         # UrlShortenerService — business logic
    │   └── util/            # ShortCodeGenerator (Base62, SecureRandom)
    └── resources/
        ├── application.properties
        └── templates/       # Thymeleaf views (index.html, analytics.html)
```

## How Short Codes Are Generated

The `ShortCodeGenerator` uses `SecureRandom` to pick 8 characters from a 62-character alphabet (`0-9`, `a-z`, `A-Z`), giving 62⁸ ≈ 218 trillion possible codes. Uniqueness is verified against the database before saving.
## pics 
<img width="1920" height="1020" alt="image" src="https://github.com/user-attachments/assets/c2cb16b4-46a3-4625-a197-d120428b9eef" />

<img width="1920" height="1020" alt="image" src="https://github.com/user-attachments/assets/62378b88-60ee-46d4-81a4-6b44a80b600f" />
<img width="1920" height="1020" alt="image" src="https://github.com/user-attachments/assets/1954c3aa-a176-4fb7-8a09-efd33b260f57" />
<img width="1920" height="1020" alt="image" src="https://github.com/user-attachments/assets/1645f55e-a32c-4df0-bdee-7802996bc440" />

