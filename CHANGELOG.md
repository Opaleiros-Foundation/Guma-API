# 📋 Changelog

All notable changes to the GUMA API project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Comprehensive documentation suite (README, CONTRIBUTING, LICENSE)
- Enhanced project structure documentation
- Detailed API examples and usage guidelines

## [0.0.1-SNAPSHOT] - 2024-06-16

### Added
- **Core Features**
  - AI-powered assignment review system using Ollama
  - Canvas LMS integration for course and assignment management
  - Reactive streaming responses with Server-Sent Events
  - RESTful API with OpenAPI documentation
  - Comprehensive test suite with JUnit 5 and Mockito

- **Controllers**
  - `ChatReviewController`: Assignment verification and review retrieval
  - `CanvasController`: Canvas LMS integration endpoints

- **Services**
  - `ChatReviewService`: Business logic for AI-powered reviews
  - `CanvasService`: Canvas API integration
  - `LessonReviewsService`: Review storage and retrieval

- **Data Layer**
  - `LessonReviews` entity with UUID-based identification
  - `Professor` entity for instructor management
  - H2 in-memory database for development
  - PostgreSQL support for production

- **AI Integration**
  - Spring AI with Ollama support
  - TinyLlama model integration
  - Configurable AI model parameters

- **Infrastructure**
  - Docker Compose setup with PostgreSQL and Ollama
  - Spring Boot Actuator for health monitoring
  - Swagger UI for API documentation
  - Maven build system with Java 17

- **Configuration**
  - Environment-based configuration
  - Canvas LMS API integration
  - Multipart file upload support (10MB limit)
  - Reactive web stack with WebFlux

### Technical Details
- **Spring Boot**: 3.4.3
- **Java**: 17
- **Database**: H2 (dev), PostgreSQL (prod)
- **AI Framework**: Spring AI 1.0.0-M6
- **Build Tool**: Maven
- **Testing**: JUnit 5, Mockito, Spring Boot Test

### Security
- Canvas access token authentication
- Request validation with Bean Validation API
- Secure environment variable handling

### Documentation
- OpenAPI 3.1.0 specification
- Interactive Swagger UI
- Comprehensive README with examples
- Code documentation with JavaDoc

---

## Legend

- **Added**: New features
- **Changed**: Changes in existing functionality
- **Deprecated**: Soon-to-be removed features
- **Removed**: Now removed features
- **Fixed**: Bug fixes
- **Security**: Security improvements