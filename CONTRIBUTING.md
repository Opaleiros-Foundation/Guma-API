# 🤝 Contributing to GUMA API

Thank you for your interest in contributing to GUMA API! This document provides guidelines and information for contributors.

## 📋 Code of Conduct

By participating in this project, you agree to maintain a respectful and inclusive environment for all contributors.

## 🚀 How to Contribute

### 🐛 Reporting Issues

Before creating a new issue:
1. Check existing issues to avoid duplicates
2. Use the latest version to reproduce the problem
3. Provide clear reproduction steps

**Bug Report Template:**
```markdown
**Bug Description**
A clear description of the bug.

**Steps to Reproduce**
1. Step one
2. Step two
3. See error

**Expected Behavior**
What should happen.

**Environment**
- OS: [e.g., Ubuntu 20.04]
- Java: [e.g., 17.0.2]
- Maven: [e.g., 3.8.1]
```

### 💡 Feature Requests

For new features:
1. Describe the problem you're solving
2. Explain your proposed solution
3. Consider alternative approaches
4. Discuss potential breaking changes

### 🔧 Pull Requests

**Development Workflow:**

1. **Fork the repository**
   ```bash
   git clone https://github.com/your-username/guma-api.git
   cd guma-api
   ```

2. **Create a feature branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **Set up development environment**
   ```bash
   export ACCESS_TOKEN=your_canvas_token
   ./mvnw clean install
   ```

4. **Make your changes**
   - Follow code style guidelines
   - Add tests for new functionality
   - Update documentation

5. **Run tests**
   ```bash
   ./mvnw test
   ```

6. **Commit your changes**
   ```bash
   git add .
   git commit -m "feat: add new feature description"
   ```

7. **Push and create PR**
   ```bash
   git push origin feature/your-feature-name
   ```

## 📝 Development Guidelines

### Code Style

**Java Conventions:**
- Use 4 spaces for indentation
- Line length: 120 characters max
- Use meaningful names
- Add JavaDoc for public methods

**Spring Boot Best Practices:**
- Use constructor injection with `@RequiredArgsConstructor`
- Prefer `@RestController` over `@Controller` for APIs
- Use proper HTTP status codes
- Implement proper error handling

**Example Controller:**
```java
@RestController
@RequestMapping("/api/v1/examples")
@RequiredArgsConstructor
@Tag(name = "Examples")
public class ExampleController {
    
    private final ExampleService exampleService;
    
    @PostMapping
    @Operation(summary = "Create example")
    public ResponseEntity<ExampleResponse> create(@Valid @RequestBody ExampleRequest request) {
        ExampleResponse response = exampleService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
```

### Testing Standards

**Unit Tests:**
- Use JUnit 5 and Mockito
- Test classes end with `Test`
- Cover both success and error scenarios

**Integration Tests:**
- Use `@SpringBootTest`
- Test classes end with `IT`
- Test complete workflows

**Example Test:**
```java
@ExtendWith(MockitoExtension.class)
class ExampleServiceTest {
    
    @Mock
    private ExampleRepository repository;
    
    @InjectMocks
    private ExampleServiceImpl service;
    
    @Test
    void shouldCreateExample_WhenValidInput() {
        // Given
        ExampleRequest request = ExampleRequest.builder()
            .name("Test")
            .build();
        
        // When & Then
        assertThat(service.create(request)).isNotNull();
    }
}
```

### Documentation

- Update README.md for significant changes
- Add OpenAPI annotations for new endpoints
- Include code comments for complex logic
- Update configuration documentation

## 🏗️ Project Structure

```
src/
├── main/java/university/jala/gumaapi/
│   ├── controller/          # REST endpoints
│   ├── service/            # Business logic
│   │   └── impl/          # Service implementations
│   ├── repository/        # Data access
│   ├── entity/           # JPA entities
│   ├── dtos/            # Data transfer objects
│   │   ├── request/     # Request DTOs
│   │   └── response/    # Response DTOs
│   ├── config/          # Configuration
│   ├── utils/           # Utilities
│   └── handler/         # Exception handling
├── resources/
│   ├── application.yml  # Configuration
│   └── static/         # Static files
└── test/java/           # Tests
```

## 🧪 Testing Strategy

### Running Tests

```bash
# All tests
./mvnw test

# Specific test
./mvnw test -Dtest=ChatReviewControllerTest

# With coverage
./mvnw test jacoco:report
```

### Test Categories

1. **Unit Tests**: Individual component testing
2. **Integration Tests**: Component interaction testing
3. **Repository Tests**: Database integration testing
4. **Controller Tests**: HTTP endpoint testing

## 🚀 Local Development

### Prerequisites

- Java 17+
- Maven 3.6+
- Docker (optional)
- Canvas LMS access token

### Setup

1. **Clone and setup**
   ```bash
   git clone <repository-url>
   cd guma-api
   export ACCESS_TOKEN=your_canvas_token
   ```

2. **Run locally**
   ```bash
   ./mvnw spring-boot:run
   ```

3. **Access services**
   - API: http://localhost:8080
   - Swagger: http://localhost:8080/docs
   - H2 Console: http://localhost:8080/h2-console

### Docker Development

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f api

# Stop services
docker-compose down
```

## 🔍 Code Review Process

### Review Checklist

- [ ] Code follows style guidelines
- [ ] Tests are included and passing
- [ ] Documentation updated
- [ ] No breaking changes
- [ ] Performance considered
- [ ] Security reviewed

### Review Criteria

1. **Functionality**: Does it work as intended?
2. **Tests**: Are tests comprehensive?
3. **Performance**: Any performance implications?
4. **Security**: Are there security concerns?
5. **Documentation**: Is documentation adequate?

## 📦 Branch Strategy

- `main`: Production-ready code
- `develop`: Integration branch
- `feature/*`: New features
- `bugfix/*`: Bug fixes
- `hotfix/*`: Critical fixes

## 🎯 Commit Guidelines

Use conventional commits:

- `feat:` New features
- `fix:` Bug fixes
- `docs:` Documentation
- `test:` Tests
- `refactor:` Code refactoring
- `style:` Code style changes
- `chore:` Build/tooling changes

## 🤝 Getting Help

- Open an issue for questions
- Check existing documentation
- Review similar implementations
- Ask in pull request comments

## 🏆 Recognition

Contributors are recognized in:
- README.md contributors section
- Release notes
- GitHub contributor graphs

Thank you for contributing to GUMA API! 🎓