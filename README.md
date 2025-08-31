# Weather Information Service

A Spring Boot REST API that provides weather data for cities in New Zealand with in-memory storage and external source lookup as fallback.

## 🌟 **Features**

* In-memory storage for up to 3 cities of New Zealand
* External source lookup as fallback for cities not stored in memory
* RESTful JSON API with proper HTTP status codes
* Comprehensive error handling with meaningful error messages
* Input validation and data integrity checks
* Unit tests with high test coverage
* Clean code structure following Spring Boot best practices

## 🚀 **API Endpoints**

**Get Weather for Specific City**

```http
GET /weather/{city}
```

Returns weather data for the specified city. If the city is not in memory, the service automatically fetches data from the external source.

**Response:**

```json
{
  "city": "Auckland",
  "temp": "24",
  "unit": "C",
  "date": "23/10/2023",
  "weather": "cloudy"
}
```

**Add New Weather Data**

```http
POST /weather
Content-Type: application/json

{
  "city": "Queenstown",
  "temp": "20",
  "unit": "C",
  "weather": "sunny"
}
```

Adds new weather data to the in-memory storage.

**Delete Weather Data**

```http
DELETE /weather/{city}
```

Removes weather data for the specified city from memory.

**Health Check**

```http
GET /weather/health
```

Returns service status and confirms the application is running.

**Response:** Weather Service is running

## 🛠️ **Technology Stack**

* Java 17+
* Spring Boot 3.5.5
* Spring Web MVC for REST API
* JUnit 5 & Mockito for testing
* Maven for dependency management
* In-memory storage using HashMap

## 📦 **Installation & Setup**

**Prerequisites**

* Java 17 or higher
* Maven 3.6+
* Git

**Clone the Repository**

```bash
git clone https://github.com/karthikn93/weatherInfoService.git
cd weatherInfoService
```

**Build the Application**

```bash
mvn clean install
```

**Run the Application**

```bash
mvn spring-boot:run
```

The service will start on http://localhost:8080

**Run Tests**

```bash
mvn test
```

## 🎯 **Usage Examples**

Get weather for a stored city

```bash
curl http://localhost:8080/weather?city=Auckland
```

Get weather for a non-stored city (external source lookup as fallback)

```bash
curl http://localhost:8080/weather?city=Nelson
```

Add new weather data

```bash
curl -X POST http://localhost:8080/weather \
  -H "Content-Type: application/json" \
  -d '{
    "city": "Queenstown",
    "temp": "20",
    "unit": "C",
    "weather": "sunny"
  }'
```

## 🔧 **Configuration**

The service comes with sensible defaults. Key configurations:

* In-Memory storage: hardcoded top 3 cities of New Zealand
  * `Auckland, Christchurch, Wellington`
* Server port: 8080
* API base path: /weather

## 🚨 **Error Handling**

The API provides meaningful error responses:

City Not Found (404)

```json
{
  "timestamp": "2025-08-30T10:30:00.12345",
  "message": "Paris city data not found in all the sources",
  "status": 404
}
```

City Already Exist In Memory (409)

```json
{
  "timestamp": "2025-08-30T10:30:00.12345",
  "message": "Auckland already exist in memory, try to add it for new city",
  "status": 400
}
```

Internal Server Error (500)

```json
{
  "timestamp": "2025-08-30T10:30:00.12345",
  "message": "An unexpected error occurred",
  "status": 500
}
```

## 🧪 **Testing**

The project includes comprehensive tests:

* Service layer tests covering business logic
* Controller tests with MockMvc
* Repository tests for covering CRUD operations
* Exception handling tests
* Full end-to-end integration testing with SpringBootTest

Run tests with:

```bash
mvn test
```

## 📁 **Project Structure**

```
src/
├── main/
│   ├── java/com/weather/weatherinfoservice
│   │   ├── controllers/     # REST controllers
│   │   ├── services/        # Business logic
│   │   ├── models/          # Data models
│   │   ├── exceptions/      # Custom exceptions
│   │   ├── repositories/    # CRUD Operations
│   │   ├── util/            # helper funcations
│   │   └── WeatherInfoServiceApplication.java
│   └── resources/           # Configuration files
└── test/
    └── java/com/weather/weatherinfoservice   # Test classes
```
---
Note: Demonstration project created for technical assessment purposes 🥷
