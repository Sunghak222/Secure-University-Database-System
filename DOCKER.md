# Docker Setup for Secure University Database System

## Architecture

- **Database**: Percona MySQL 8.0
- **Backend**: Spring Boot (Java 17) with embedded Tomcat
- **Frontend**: Static HTML/CSS/JS files served by Spring Boot from `src/main/resources/static`

## Quick Start

### 1. Build and Start All Services

```powershell
docker-compose up --build
```

This will:
- Build the Spring Boot application
- Start Percona MySQL database
- Start the backend server (which serves the frontend)

### 2. Access the Application

- **Frontend**: http://localhost:8080
- **API**: http://localhost:8080/api/*
- **Database**: localhost:3306

### 3. Stop All Services

```powershell
docker-compose down
```

### 4. Stop and Remove Volumes (Clean Database)

```powershell
docker-compose down -v
```

## Docker Commands Reference

### Development Workflow

```powershell
# Start in detached mode
docker-compose up -d

# View logs
docker-compose logs -f backend

# Rebuild after code changes
docker-compose up --build backend

# Restart a specific service
docker-compose restart backend

# Execute commands in backend container
docker-compose exec backend sh
```

### Database Access

```powershell
# Connect to MySQL
docker-compose exec percona mysql -u comp3335 -psecure_password securedb

# View database logs
docker-compose logs percona
```

### Troubleshooting

```powershell
# Check service status
docker-compose ps

# View all logs
docker-compose logs

# Remove all containers and networks
docker-compose down

# Rebuild from scratch
docker-compose down -v
docker-compose up --build
```

## Environment Variables

You can override default values in `docker-compose.yml`:

```yaml
environment:
  SPRING_DATASOURCE_URL: jdbc:mysql://percona:3306/securedb
  SPRING_DATASOURCE_USERNAME: comp3335
  SPRING_DATASOURCE_PASSWORD: secure_password
  SERVER_PORT: 8080
```

## Port Mappings

| Service  | Container Port | Host Port | Description          |
|----------|----------------|-----------|----------------------|
| backend  | 8080           | 8080      | Spring Boot + Frontend |
| percona  | 3306           | 3306      | MySQL Database       |

## Notes

- Frontend files are bundled in the JAR file from `src/main/resources/static`
- Spring Boot automatically serves static files from `/static` at the root URL
- The backend waits for the database to be healthy before starting
- Data persists in the `percona_data` Docker volume
