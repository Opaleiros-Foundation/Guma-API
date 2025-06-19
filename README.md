# 🎓 GUMA API

[![Java](https://img.shields.io/badge/Java-17-orange)](https://openjdk.java.net/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-green)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue)](https://maven.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-Supported-blue)](https://docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow)](https://opensource.org/licenses/MIT)
[![Tests](https://img.shields.io/badge/Tests-Passing-brightgreen)](https://github.com/Opaleiros-Foundation/guma-api)

**GUMA API** is an intelligent assignment review system that integrates with Canvas LMS and leverages AI to provide automated feedback on student submissions. Built with Spring Boot and reactive programming principles, it offers real-time streaming responses and comprehensive assignment management.

## 🚀 Features

- **🤖 AI-Powered Reviews**: Automated assignment evaluation using Ollama models (TinyLlama, DeepSeek)
- **📚 Canvas Integration**: Seamless connection with Canvas LMS for course and assignment management
- **⚡ Reactive Streaming**: Real-time responses using Spring WebFlux with Server-Sent Events
- **📊 Assignment Tracking**: Comprehensive lesson review storage and retrieval with UUID-based identification
- **🔍 API Documentation**: Interactive Swagger UI documentation at `/docs`
- **🐳 Docker Ready**: Complete containerization with Docker Compose including PostgreSQL and Ollama
- **🏥 Health Monitoring**: Spring Boot Actuator integration with health checks
- **🧪 Test Coverage**: Comprehensive unit and integration tests with JUnit 5 and Mockito
- **🔒 Validation**: Request validation with Bean Validation API
- **📁 File Upload**: Support for multipart file uploads up to 10MB

## 🏗️ Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │    GUMA API     │    │   Canvas LMS    │
│   (Client)      │◄──►│  (Spring Boot)  │◄──►│  (Instructure)  │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                              │
                              ▼
                       ┌─────────────────┐    ┌─────────────────┐
                       │   Database      │    │   Ollama AI     │
                       │ (H2/PostgreSQL) │    │   (TinyLlama)   │
                       └─────────────────┘    └─────────────────┘
```

### Technology Stack

- **Backend**: Spring Boot 3.4.3, Spring WebFlux, Spring Data JPA
- **AI Integration**: Spring AI with Ollama
- **Database**: H2 (development), PostgreSQL (production)
- **Build Tool**: Maven 3.6+
- **Java Version**: 17
- **Documentation**: OpenAPI 3 with Swagger UI
- **Testing**: JUnit 5, Mockito, TestContainers
- **Containerization**: Docker, Docker Compose

## 📋 Prerequisites

- **Java 17** or later
- **Maven 3.6+**
- **Docker & Docker Compose** (for containerized deployment)
- **Canvas LMS access token** (for Canvas integration)

## 🛠️ Quick Start

### Local Development

1. **Clone the repository**
   ```bash
   git clone https://github.com/Opaleiros-Foundation/guma-api.git
   cd guma-api
   ```

2. **Set environment variables**
   ```bash
   export ACCESS_TOKEN=your_canvas_access_token
   ```

3. **Install dependencies and run tests**
   ```bash
   ./mvnw clean install
   ./mvnw test
   ```

4. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

5. **Access the application**
   - **API Base URL**: http://localhost:8080
   - **Swagger UI**: http://localhost:8080/docs
   - **H2 Console**: http://localhost:8080/h2-console (user: `sa`, password: empty)
   - **Health Check**: http://localhost:8080/actuator/health

### 🐳 Docker Deployment

1. **Start all services**
   ```bash
   docker-compose up -d
   ```

2. **Access the services**
   - **API**: http://localhost:8080
   - **Frontend**: http://localhost:3000
   - **Ollama**: http://localhost:11434
   - **PostgreSQL**: localhost:5431

3. **View logs**
   ```bash
   docker-compose logs -f api
   ```

4. **Stop services**
   ```bash
   docker-compose down
   ```

## 📚 API Endpoints

### 🤖 Chat Review Controller

| Method | Endpoint | Description | Authentication |
|--------|----------|-------------|----------------|
| `POST` | `/chat?courseId={id}&assignmentId={id}` | Verify assignment submission with streaming response | Canvas Token |
| `GET` | `/chat/{lessonId}` | Get assignment review by lesson UUID | None |

#### POST /chat - Assignment Verification

**Request:**
```bash
curl -X POST "http://localhost:8080/chat?courseId=123&assignmentId=456" \
  -H "Content-Type: application/json" \
  -H "access_token: your_canvas_token" \
  -d '{
    "subject": "Database Design",
    "professor": "Dr. Smith",
    "content": "Here is my database implementation...",
    "heading": [
      {
        "id": 1,
        "points": 10,
        "description": "Database Design Quality",
        "longDescription": "Evaluate the overall database design"
      }
    ]
  }'
```

**Response:** (Server-Sent Events stream)
```
data: {"content": "Analyzing your database design..."}
data: {"content": "The schema looks well-structured..."}
data: {"content": "Suggestions for improvement..."}
```

#### GET /chat/{lessonId} - Get Review

**Response:**
```json
{
  "lessonId": "123e4567-e89b-12d3-a456-426614174000",
  "subject": "Database Design",
  "professor": "Dr. Smith",
  "feedback": "Excellent database design with proper normalization...",
  "model": "tinyllama",
  "responseDate": "2024-06-30T10:15:30"
}
```

### 📖 Canvas Controller

| Method | Endpoint | Description | Authentication |
|--------|----------|-------------|----------------|
| `GET` | `/canvas/courses` | Get all enrolled courses | Canvas Token |
| `GET` | `/canvas/assignments/{courseId}` | Get assignments by course | Canvas Token |
| `GET` | `/canvas/assignments/{courseId}/{assignmentId}` | Get specific assignment | Canvas Token |

#### Examples

**Get Courses:**
```bash
curl -X GET "http://localhost:8080/canvas/courses" \
  -H "access_token: your_canvas_token"
```

**Response:**
```json
[
  {
    "id": 123,
    "name": "Advanced Database Systems",
    "account_id": 1001,
    "uuid": "abc-123-def-456",
    "course_code": "CS-540"
  }
]
```

**Get Assignments:**
```bash
curl -X GET "http://localhost:8080/canvas/assignments/123" \
  -H "access_token: your_canvas_token"
```

**Response:**
```json
[
  {
    "id": 456,
    "name": "Database Design Project",
    "description": "Design and implement a relational database...",
    "due_at": "2024-07-15T23:59:59Z",
    "points_possible": 100
  }
]
```

### 🏥 Actuator Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/actuator` | Available actuator endpoints |
| `GET` | `/actuator/health` | Application health status |

## 🗄️ Data Models

### ChatDTO (Request)
```java
{
  "subject": "Database Design",           // Required: Course subject
  "professor": "Dr. Smith",              // Required: Professor name  
  "content": "Implementation details...", // Required: Assignment content
  "heading": [                           // Optional: Rubric criteria
    {
      "id": 1,
      "points": 10,
      "description": "Design Quality",
      "longDescription": "Evaluate overall design",
      "ignoreForScoring": false,
      "criterionUseRange": false
    }
  ]
}
```

### LessonReviews (Entity)
```java
{
  "lessonId": "123e4567-e89b-12d3-a456-426614174000", // Auto-generated UUID
  "subject": "Database Design",
  "professor": "Dr. Smith", 
  "feedback": "Detailed AI-generated feedback...",     // TEXT field for long content
  "model": "tinyllama",                               // AI model used
  "responseDate": "2024-06-30T10:15:30"              // Auto-generated timestamp
}
```

### Course (Canvas DTO)
```java
{
  "id": 123,
  "name": "Advanced Database Systems",
  "account_id": 1001,
  "uuid": "abc-123-def-456", 
  "course_code": "CS-540"
}
```

### Assignment (Canvas DTO)
```java
{
  "id": 456,
  "name": "Database Design Project",
  "description": "Design and implement a relational database...",
  "due_at": "2024-07-15T23:59:59Z",
  "points_possible": 100
}
```

## ⚙️ Configuration

### Application Properties

**Development (application.yml):**
```yaml
spring:
  application:
    name: Guma-api
  datasource:
    url: jdbc:h2:mem:testdb
    username: sa
    password: 
  ai:
    ollama:
      chat:
        model: tinyllama
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB

canvas:
  feign:
    url: https://jalauniversity.instructure.com/api/v1
```

**Production (application-prod.yml):**
```yaml
spring:
  datasource:
    url: jdbc:postgresql://${PGHOST:localhost}:${PGPORT:5432}/${PGDATABASE:guma}
    username: ${PGUSER:postgres}
    password: ${PGPASSWORD:}
```

### Environment Variables

| Variable | Description | Required | Default |
|----------|-------------|----------|---------|
| `ACCESS_TOKEN` | Canvas LMS access token | Yes | - |
| `PGHOST` | PostgreSQL host | No | localhost |
| `PGPORT` | PostgreSQL port | No | 5432 |
| `PGDATABASE` | PostgreSQL database | No | guma |
| `PGUSER` | PostgreSQL username | No | postgres |
| `PGPASSWORD` | PostgreSQL password | No | - |
| `OLLAMA_BASE_URL` | Ollama service URL | No | http://localhost:11434 |

## 🧪 Testing

### Running Tests

```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=ChatReviewControllerTest

# Run with coverage
./mvnw test jacoco:report

# Integration tests only
./mvnw test -Dtest="*IT"
```

### Test Structure

```
src/test/java/
├── university/jala/gumaapi/
│   ├── mock/                    # Mock data and utilities
│   ├── repository/              # Repository tests (@DataJpaTest)
│   ├── service/                 # Service layer tests
│   └── GumaApiApplicationTests.java # Integration tests
```

### Key Test Features

- **Repository Tests**: Testing JPA repositories with embedded H2
- **Service Tests**: Unit tests with Mockito mocking
- **Integration Tests**: Full Spring context testing
- **Mock Data**: Comprehensive test fixtures

## 🔧 Development

### Project Structure

```
src/main/java/university/jala/gumaapi/
├── controller/          # REST endpoints
│   ├── ChatReviewController.java
│   └── CanvasController.java
├── service/            # Business logic
│   ├── impl/          # Service implementations
│   ├── ChatReviewService.java
│   ├── CanvasService.java
│   └── LessonReviewsService.java
├── repository/        # Data access layer
│   └── LessonReviewsRepository.java
├── entity/           # JPA entities
│   ├── LessonReviews.java
│   └── Professor.java
├── dtos/            # Data transfer objects
│   ├── request/     # Request DTOs
│   └── response/    # Response DTOs
├── config/          # Configuration classes
├── utils/           # Utility classes
├── handler/         # Exception handlers
└── swagger/         # API documentation annotations
```

### Key Dependencies

- **Spring Boot Starter Web**: Web MVC support
- **Spring Boot Starter WebFlux**: Reactive web support
- **Spring Boot Starter Data JPA**: Database access
- **Spring AI Ollama**: AI integration
- **Spring Cloud OpenFeign**: HTTP clients for Canvas API
- **SpringDoc OpenAPI**: API documentation
- **Lombok**: Boilerplate code reduction
- **Bean Validation**: Request validation

## 🚀 Deployment

### Local Deployment

1. **Build the application**
   ```bash
   ./mvnw clean package
   ```

2. **Run with production profile**
   ```bash
   java -jar target/gumaapi-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
   ```

### Docker Deployment

1. **Build Docker image**
   ```bash
   ./mvnw spring-boot:build-image
   ```

2. **Run with Docker Compose**
   ```bash
   docker-compose up -d
   ```

### Production Considerations

- Set up proper database with persistent storage
- Configure environment variables securely
- Set up monitoring and logging
- Configure reverse proxy (nginx/Apache)
- Set up SSL/TLS certificates
- Configure backup strategies

## 🔍 Monitoring & Observability

### Health Checks

- **Application Health**: `/actuator/health`
- **Database Connectivity**: Included in health endpoint
- **Custom Health Indicators**: Can be added for external services

### Logging

- **Framework**: Log4j2 with Lombok's `@Log4j2`
- **Levels**: Configurable via application properties
- **Format**: Structured logging for production

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guide](CONTRIBUTING.md) for details.

### Development Workflow

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Update documentation
6. Submit a pull request

### Code Style

- Java 17 features encouraged
- Use Lombok to reduce boilerplate
- Follow Spring Boot best practices
- Maintain test coverage
- Document public APIs

## 📝 License

This project is licensed under the [MIT License](LICENSE).

## 👨‍💻 Author

**Matheus Victor**
- GitHub: [@Opaleiros-Foundation](https://github.com/Opaleiros-Foundation)
- Project: [GUMA API](https://github.com/Opaleiros-Foundation/guma-api)

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Ollama team for AI integration capabilities
- Canvas LMS for the comprehensive API
- Contributors and testers

---

<div align="center">
  <p>Made with ❤️ for educational technology</p>
  <p>🎓 Empowering students and educators through AI-powered feedback</p>
</div>

