# Secure-University-Database-System  

A role-based student information system built with **Spring Boot + MySQL**.  
It uses **JWT authentication**, **method-level authorization**, and **field-level encryption (AES at DB level)** to protect sensitive data such as phone numbers, identification numbers, addresses, grades, and disciplinary records.

---

## Tech Stack

- **Backend:** Java, Spring Boot, Spring Security, JPA (Hibernate)
- **Database:** MySQL / Percona MySQL
- **Auth:** JWT (Bearer token)
- **Encryption:** MySQL `AES_ENCRYPT / AES_DECRYPT` (encrypted-at-rest fields)
- **Frontend:** HTML / CSS / JavaScript

---

## Key Features

### Authentication & Authorization
- JWT login via `/api/auth/login`
- Role-based access control using `@PreAuthorize`
- Roles:
  - **STUDENT**: view own profile / grades / disciplinary records
  - **GUARDIAN**: view own profile + view their children’s grades/disciplinary records only
  - **ARO**: manage grades (CRUD)
  - **DRO**: manage disciplinary records (CRUD)

### Data Protection (Encrypted Fields)
Sensitive fields are stored **encrypted inside the DB** and **decrypted only when fetched** via API:
- Students: `identification_number`, `address`, `phone`
- Guardians: `phone`
- Staffs: `identification_number`, `address`, `phone`
- Grades: `grade`, `comments`
- Disciplinary Records: `descriptions`

### Frontend Dashboards
Each role has a separate dashboard page:
- `student-dashboard.html`
- `guardian-dashboard.html`
- `aro-dashboard.html`
- `dro-dashboard.html`

---


---

## How It Works (Flow)

1. User logs in with email/password
2. Backend validates password hash (BCrypt)
3. Backend returns:
   - `token` (JWT)
   - `role`
   - `studentId / guardianId / staffId` (depending on the role)
4. Frontend saves token/role in `localStorage`
5. Frontend calls APIs with:
   - `Authorization: Bearer <token>`
6. Spring Security filter validates JWT and injects authentication context
7. Controllers enforce access via `@PreAuthorize`

---

## Setup & Run

### 1) Prerequisites
- Java 17+
- Gradle (or use wrapper)
- MySQL / Percona MySQL running locally
- A database created (example: `securedb`)

### 2) Configure `application.properties`

Example:
```properties
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/securedb?useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=comp3335
spring.datasource.password=secure_password

spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true
spring.jpa.open-in-view=false
spring.flyway.enabled=false

spring.jpa.defer-datasource-initialization=true
spring.sql.init.mode=always

# encryption key (used to derive AES key in queries)
app.crypto.key=YOUR_SECRET_KEY

server.port=8080

## How to connect Percona (MySQL)  
mysql -u root -p                          # connect to mysql server  
mysql -u root -p -h 127.0.0.1 -P 3306     # connect to mysql server using TCP  
\q                                         # exit  


## Quick Start (Local Percona) 

### A) After downloading Percona, run these commands once
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS securedb; \
CREATE USER IF NOT EXISTS 'comp3335'@'localhost' IDENTIFIED BY 'secure_password'; \
GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,ALTER,INDEX ON securedb.* TO 'comp3335'@'localhost'; \
FLUSH PRIVILEGES;"

mysql -u root -p

### B) Spring Boot Default Setting  
- URL: `jdbc:mysql://127.0.0.1:3306/securedb?useSSL=false&allowPublicKeyRetrieval=true`
- USER: `comp3335`
- PASS: `secure_password`
