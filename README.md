# Employee Service 👥

| | |
|---|---|
| **Student** | L.K.H. Manuth Lakdiw |
| **Student Number** | 241722018 |
| **Batch** | GDSE-72 |
| **GCP Project** | `workforce-hub-cloud` |

## Project Description

Manages employee records, department assignments and inter-service validations for WorkForceHub. When creating or updating an employee, this service communicates with the Department Service via a `@LoadBalanced RestTemplate` to verify that the assigned department exists, ensuring referential integrity across the microservice boundary.

## 🛠️ Technology Stack

- **Java**: 25
- **Spring Boot**: 4.1.0
- **Spring Cloud**: 2025.1.2
- **Spring Data JPA**
- **MySQL** (via MySQL Connector/J)
- **Netflix Eureka Client**
- **Spring Cloud Config Client**
- **Spring Cloud LoadBalancer**

## ✨ Architecture Highlights

- **Inter-service Communication**: Uses a `@LoadBalanced RestTemplate` configuration (via `RestTemplateConfig.java`) to call the Department Service through Eureka-resolved addresses, intentionally avoiding `RestClient` to prevent circular dependency issues.
- **Data Integrity**: Implements strict duplication checks, returning `409 Conflict` if an email address is already in use.

## 📍 API Endpoints

Base path: `/api/v1/employees`

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/v1/employees` | Create an employee |
| `GET` | `/api/v1/employees` | List all employees |
| `GET` | `/api/v1/employees/{id}` | Fetch a single employee |
| `GET` | `/api/v1/employees/department/{id}` | Fetch employees by department |
| `PUT` | `/api/v1/employees/{id}` | Update an employee |
| `DELETE` | `/api/v1/employees/{id}` | Delete an employee |

## 🚀 Running Locally

- **Port**: `8082`
- Ensure MySQL is running and the Config Server and Eureka Server are available.

```bash
mvn spring-boot:run
```

## ☁️ Production Deployment

- **Runtime**: Regional Managed Instance Group (`workforce-hub-backend-mig`)
- **Region**: `asia-south1`
- **Database**: Cloud SQL (MySQL)
- **Service Discovery**: Department Service is resolved through the local Eureka registry on each MIG instance.
- **Process Manager**: PM2 with systemd automatic startup and recovery
