# Clustered Data Warehouse

## Overview
This project is a clustered data warehouse designed for Bloomberg to analyze FX deals. It includes a REST API to accept and persist deal details into a PostgreSQL database.

## Technologies Used
- Java 17
- Spring Boot
- PostgreSQL
- Docker
- Maven

## Setup and Running
1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd <repository-directory>
   ```

2. **Build the project**:
   ```bash
   ./mvnw clean install
   ```

3. **Run the application using Docker Compose**:
   ```bash
   docker-compose up --build
   ```

4. **Access the API**:
   http://localhost:8080/api/v1/deals

## API Endpoints
- POST /api/v1/deals/save-deal: Create a new deal
- GET /api/v1/deals/all-deals: Retrieve all deals

## Error Handling
Proper error handling is implemented for duplicate deal entries and other exceptions.

## Logging
Application logging is configured for better traceability.

## Testing
Unit tests are included to ensure proper functionality and coverage.
 
## Enhancements

There are many enhancements that can be considered for future work, so I'll keep these APIs in version 1 (v1). Due to time limitations with daily working hours, we couldn't implement some enhancements. These include:

1. Unit test annotations for models and controllers LIKE POJO.
2. Enhancing the base structure with a well-structured design for the repository, service, and controller layers to avoid redundant code for future additions and features.
3. Properly handling currency (I faced some issues that made me keep it simple to meet the deadline).
 (   @ManyToOne)
 (   @JoinColumn&#40;name = "from_currency", referencedColumnName = "currency_code"&#41;)
4. Using a mapper (MapStruct) with DTOs and entities instead of using models directly in the controller.