# Blog API – Spring Boot, Keycloak & Liquibase

A RESTful Blog API built with **Spring Boot**, secured using **Keycloak (JWT)**, and versioned with **Liquibase**.  
The API supports **Articles**, **Comments**, **pagination**, and **keyword search** (title + content).

---

## 🧱 Tech Stack

### Backend
- **Java 21**
- **Spring Boot 4.x**
- **Spring Web (WebMVC)**
- **Spring Data JPA (Hibernate)**
- **Spring Security (OAuth2 Resource Server)**
- **Bean Validation (Jakarta Validation)**
- **MapStruct**
- **Lombok**

### Database
- **PostgreSQL 16**
- **Liquibase** (database migrations)

### Authentication & Security
- **Keycloak** (JWT, roles, users)
- OAuth2 / OpenID Connect
- Role-based access control

### Dev & Tooling
- **Docker & Docker Compose**
- **Postman** (API testing)
- **Maven**

---

## ✨ Features

### Articles
- Create article (**AUTHOR / ADMIN**)
- Update article (**AUTHOR / ADMIN**)
- Delete article (**ADMIN**)
- Public listing of published articles
- Public read by **slug**
- Pagination & sorting
- Keyword search (`q`) on **title OR content**

### Comments
- List comments for an article (public)
- Add comment to an article (authenticated user)

### Security
- JWT authentication via Keycloak
- Role-based authorization
- Stateless API
- Local `users` table used as a **minimal mirror** for Keycloak users (UUID mapping)

---

## 📂 Project Structure (simplified)

src/main/java/com/novelis/blog

├── config/ # SecurityConfig, JWT role mapping

├── controller/ # ArticleController, CommentController

├── domain/ # JPA entities

├── dto/ # Request / Response DTOs

├── mapper/ # MapStruct mappers

├── repository/ # Spring Data JPA repositories

├── service/ # Services & implementations

└── BlogApplication.java


---

## ⚙️ Prerequisites

- Java **21**
- Maven **3.9+**
- Docker & Docker Compose

---

## 🔧 Configuration

### `application.properties`

```properties
spring.application.name=blog
server.port=8080

spring.datasource.url=jdbc:postgresql://localhost:5433/blog_db
spring.datasource.username=blog_user
spring.datasource.password=blog_password

spring.jpa.hibernate.ddl-auto=none
spring.jpa.open-in-view=false

spring.liquibase.enabled=true
spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.yaml

spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8081/realms/blog
```

# optional:
spring.security.oauth2.resourceserver.jwt.audiences=blog-api
🐳 Run Infrastructure (Docker)
Start PostgreSQL & Keycloak
docker compose up -d
Keycloak Admin Console
URL: http://localhost:8081

Login: admin / admin

🔐 Keycloak Setup (Minimum)
## Create a Realm: blog

## Create a Client: blog-api

## Enable Direct Access Grants

## Create Roles:

AUTHOR

ADMIN

## Create a user:

username: author1

password: secret (Temporary = OFF)

Assign role: AUTHOR

▶️ Run the API
mvn spring-boot:run
Liquibase will automatically create:

users

articles

comments

databasechangelog

databasechangeloglock

🔑 Get Access Token (DEV)
Password Grant (Postman / curl)
POST
````
http://localhost:8081/realms/blog/protocol/openid-connect/token
Body (x-www-form-urlencoded):

client_id=blog-api
grant_type=password
username=author1
password=secret
curl example
curl -X POST "http://localhost:8081/realms/blog/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=blog-api" \
  -d "grant_type=password" \
  -d "username=author1" \
  -d "password=secret"
````
Use the returned access_token in API calls:

Authorization: Bearer <ACCESS_TOKEN>
🌐 API Endpoints
Base URL:

http://localhost:8080
Articles (Public)
GET

/api/v1/articles
Query params:

q → keyword search (title OR content)

page, size, sort

Example:
```
GET /api/v1/articles?q=bubbles&page=0&size=10&sort=createdAt,desc

GET /api/v1/articles/{slug}
Articles (Secured)
POST /api/v1/articles (AUTHOR / ADMIN)

{
  "title": "My first article",
  "content": "Hello bubbles world",
  "published": true
}
PUT /api/v1/articles/{id} (AUTHOR / ADMIN)
DELETE /api/v1/articles/{id} (ADMIN)

Comments
GET /api/v1/articles/{slug}/comments
POST (authenticated)
/api/v1/articles/{slug}/comments
{
  "content": "Great article!"
}
```
