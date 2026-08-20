# Employee Service 👥

Manages employee records, assignments, and inter-service validations.

## 🛠️ Tech Stack
- **Java**: 25
- **Spring Boot**: 4.1.0
- **Database**: MySQL

## ✨ Architecture Highlights
- **Inter-service Communication**: Uses a robust `@LoadBalanced RestTemplate` configuration (via `RestTemplateConfig.java`) to communicate with the Department Service, intentionally bypassing `RestClient` to prevent circular dependency bugs.
- **Data Integrity**: Implements strict duplication checks (e.g., throwing a `409 Conflict` if an email is reused).

## 📍 Key Endpoints
- `POST /api/v1/employees` - Create an employee
- `GET /api/v1/employees` - List all employees
- `GET /api/v1/employees/{id}` - Fetch single employee
- `GET /api/v1/employees/department/{id}` - Fetch by department
- `PUT /api/v1/employees/{id}` - Update an employee
- `DELETE /api/v1/employees/{id}` - Delete an employee

## 🚀 Running Locally
- Port: Configured via Config Server.
