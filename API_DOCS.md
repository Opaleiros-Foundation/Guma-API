# 📚 GUMA API Documentation

## 📖 Table of Contents
- [Authentication](#-authentication)
- [Rate Limiting](#-rate-limiting)
- [Error Handling](#-error-handling)
- [Chat Review Endpoints](#-chat-review-endpoints)
- [Canvas Integration Endpoints](#-canvas-integration-endpoints)
- [Health & Monitoring](#-health--monitoring)
- [Data Models](#-data-models)
- [Examples](#-examples)
- [SDKs & Tools](#-sdks--tools)

## 🔐 Authentication

### Canvas Token Authentication
Most endpoints require a Canvas LMS access token passed in the `access_token` header.

```bash
curl -H "access_token: your_canvas_access_token" \
     https://api.guma.edu/canvas/courses
```

### Obtaining a Canvas Token
1. Log into your Canvas LMS instance
2. Go to Account → Settings
3. Create a new access token under "Approved Integrations"
4. Copy the generated token

## ⚡ Rate Limiting

- **Rate Limit**: 100 requests per minute per token
- **Burst Limit**: 10 requests per second
- **Headers Returned**:
  - `X-RateLimit-Limit`: Total requests allowed
  - `X-RateLimit-Remaining`: Requests remaining
  - `X-RateLimit-Reset`: Time when rate limit resets

## ❌ Error Handling

### Standard Error Response
```json
{
  "timestamp": "2024-06-16T10:15:30Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed for field 'subject': must not be blank",
  "path": "/chat"
}
```

### HTTP Status Codes
| Code | Description |
|------|-------------|
| `200` | Success |
| `201` | Created |
| `400` | Bad Request - Invalid input |
| `401` | Unauthorized - Invalid or missing token |
| `403` | Forbidden - Insufficient permissions |
| `404` | Not Found - Resource not found |
| `429` | Too Many Requests - Rate limit exceeded |
| `500` | Internal Server Error |
| `503` | Service Unavailable - AI service down |

## 🤖 Chat Review Endpoints

### POST /chat - Verify Assignment
Generate AI-powered feedback for an assignment submission.

**URL**: `POST /chat?courseId={courseId}&assignmentId={assignmentId}`

**Headers**:
```
Content-Type: application/json
access_token: your_canvas_token
```

**Parameters**:
- `courseId` (query, required): Canvas course ID
- `assignmentId` (query, required): Canvas assignment ID

**Request Body**:
```json
{
  "subject": "Database Design",
  "professor": "Dr. Smith", 
  "content": "Here is my database implementation with proper normalization...",
  "heading": [
    {
      "id": 1,
      "points": 10,
      "description": "Database Design Quality",
      "longDescription": "Evaluate the overall quality of the database design including normalization, relationships, and constraints.",
      "ignoreForScoring": false,
      "criterionUseRange": false
    }
  ]
}
```

**Response**: Server-Sent Events (SSE) stream
```
data: {"content": "🔍 Analyzing your database design..."}

data: {"content": "📊 **Database Structure Analysis:**\n\nYour database design shows good understanding of normalization principles..."}

data: {"content": "✅ **Strengths:**\n- Proper primary key definitions\n- Well-defined foreign key relationships\n- Appropriate data types chosen"}

data: {"content": "🔧 **Areas for Improvement:**\n- Consider adding indexes for frequently queried columns\n- Some tables could benefit from additional constraints"}

data: {"content": "🎯 **Overall Score: 85/100**\n\nGreat work on this assignment! Your database design demonstrates solid understanding of relational database principles."}
```

**Example cURL**:
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
        "description": "Design Quality",
        "longDescription": "Evaluate overall design"
      }
    ]
  }' \
  --no-buffer
```

### GET /chat/{lessonId} - Retrieve Review
Get a previously generated assignment review.

**URL**: `GET /chat/{lessonId}`

**Parameters**:
- `lessonId` (path, required): UUID of the lesson review

**Response**:
```json
{
  "lessonId": "123e4567-e89b-12d3-a456-426614174000",
  "subject": "Database Design",
  "professor": "Dr. Smith",
  "feedback": "🔍 **Database Design Analysis**\n\n✅ **Strengths:**\n- Excellent normalization (3NF achieved)\n- Proper primary/foreign key relationships\n- Appropriate data types and constraints\n\n🔧 **Suggestions:**\n- Consider adding indexes for performance\n- Document your design decisions\n\n🎯 **Score: 85/100** - Great work!",
  "model": "tinyllama",
  "responseDate": "2024-06-16T10:15:30"
}
```

**Example cURL**:
```bash
curl -X GET "http://localhost:8080/chat/123e4567-e89b-12d3-a456-426614174000"
```

## 📚 Canvas Integration Endpoints

### GET /canvas/courses - List Enrolled Courses
Retrieve all courses the authenticated user is enrolled in.

**URL**: `GET /canvas/courses`

**Headers**:
```
access_token: your_canvas_token
```

**Response**:
```json
[
  {
    "id": 123,
    "name": "Advanced Database Systems",
    "account_id": 1001,
    "uuid": "abc-123-def-456",
    "course_code": "CS-540",
    "workflow_state": "available",
    "start_at": "2024-01-15T00:00:00Z",
    "end_at": "2024-05-15T23:59:59Z"
  },
  {
    "id": 124,
    "name": "Software Engineering Principles", 
    "account_id": 1001,
    "uuid": "def-456-ghi-789",
    "course_code": "CS-530",
    "workflow_state": "available",
    "start_at": "2024-01-15T00:00:00Z",
    "end_at": "2024-05-15T23:59:59Z"
  }
]
```

### GET /canvas/assignments/{courseId} - List Course Assignments
Get all assignments for a specific course.

**URL**: `GET /canvas/assignments/{courseId}`

**Headers**:
```
access_token: your_canvas_token
```

**Parameters**:
- `courseId` (path, required): Canvas course ID

**Response**:
```json
[
  {
    "id": 456,
    "name": "Database Design Project",
    "description": "Design and implement a relational database for a library management system. Include proper normalization, constraints, and sample data.",
    "due_at": "2024-07-15T23:59:59Z",
    "points_possible": 100,
    "submission_types": ["online_upload", "online_text_entry"],
    "workflow_state": "published",
    "created_at": "2024-06-01T09:00:00Z",
    "updated_at": "2024-06-10T14:30:00Z"
  }
]
```

### GET /canvas/assignments/{courseId}/{assignmentId} - Get Specific Assignment
Retrieve details for a specific assignment.

**URL**: `GET /canvas/assignments/{courseId}/{assignmentId}`

**Headers**:
```
access_token: your_canvas_token
```

**Parameters**:
- `courseId` (path, required): Canvas course ID  
- `assignmentId` (path, required): Canvas assignment ID

**Response**:
```json
{
  "id": 456,
  "name": "Database Design Project",
  "description": "Design and implement a relational database for a library management system...",
  "due_at": "2024-07-15T23:59:59Z",
  "points_possible": 100,
  "submission_types": ["online_upload", "online_text_entry"],
  "workflow_state": "published",
  "rubric": [
    {
      "id": "1",
      "points": 25,
      "description": "Database Design",
      "longDescription": "Quality of database schema design including normalization",
      "criterionUseRange": false,
      "ratings": [
        {
          "id": "1_1",
          "points": 25,
          "description": "Excellent"
        },
        {
          "id": "1_2", 
          "points": 20,
          "description": "Good"
        }
      ]
    }
  ]
}
```

## 🏥 Health & Monitoring

### GET /actuator/health - Health Check
Check the application health status.

**URL**: `GET /actuator/health`

**Response**:
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "H2",
        "validationQuery": "isValid()"
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 499963174912,
        "free": 319407775744,
        "threshold": 10485760,
        "exists": true
      }
    },
    "ping": {
      "status": "UP"
    }
  }
}
```

### GET /actuator - Available Endpoints
List all available actuator endpoints.

**URL**: `GET /actuator`

**Response**:
```json
{
  "_links": {
    "self": {
      "href": "http://localhost:8080/actuator",
      "templated": false
    },
    "health": {
      "href": "http://localhost:8080/actuator/health",
      "templated": false
    },
    "health-path": {
      "href": "http://localhost:8080/actuator/health/{*path}",
      "templated": true
    }
  }
}
```

## 🗄️ Data Models

### ChatDTO (Request Model)
```typescript
interface ChatDTO {
  subject: string;     // Required: Course subject name
  professor: string;   // Required: Professor name
  content: string;     // Required: Assignment submission content
  heading?: Rubric[];  // Optional: Rubric criteria array
}
```

### Rubric (Embedded Model)
```typescript
interface Rubric {
  id: number;                    // Rubric criterion ID
  points: number;                // Maximum points for this criterion
  description: string;           // Short description
  longDescription?: string;      // Detailed description
  ignoreForScoring: boolean;     // Whether to ignore in scoring
  criterionUseRange: boolean;    // Whether criterion uses a range
  ratings?: Rating[];            // Available rating options
}
```

### LessonReviews (Response Model)
```typescript
interface LessonReviews {
  lessonId: string;        // UUID identifier
  subject: string;         // Course subject
  professor: string;       // Professor name  
  feedback: string;        // AI-generated feedback (can be very long)
  model: string;          // AI model used (e.g., "tinyllama")
  responseDate: string;   // ISO 8601 timestamp
}
```

### Course (Canvas Model)
```typescript
interface Course {
  id: number;              // Canvas course ID
  name: string;            // Course name
  account_id: number;      // Canvas account ID
  uuid: string;           // Canvas UUID
  course_code: string;    // Course code (e.g., "CS-540")
  workflow_state?: string; // Course state
  start_at?: string;      // Course start date
  end_at?: string;        // Course end date
}
```

### Assignment (Canvas Model)
```typescript
interface Assignment {
  id: number;                    // Canvas assignment ID
  name: string;                  // Assignment name
  description: string;           // Assignment description
  due_at: string;               // Due date (ISO 8601)
  points_possible: number;       // Maximum points
  submission_types: string[];    // Allowed submission types
  workflow_state: string;        // Assignment state
  created_at: string;           // Creation date
  updated_at: string;           // Last update date
  rubric?: Rubric[];            // Rubric criteria
}
```

## 💡 Examples

### Complete Assignment Review Workflow

1. **Get your courses**:
```bash
curl -H "access_token: your_token" \
     http://localhost:8080/canvas/courses
```

2. **Get assignments for a course**:
```bash
curl -H "access_token: your_token" \
     http://localhost:8080/canvas/assignments/123
```

3. **Submit assignment for review**:
```bash
curl -X POST "http://localhost:8080/chat?courseId=123&assignmentId=456" \
  -H "Content-Type: application/json" \
  -H "access_token: your_token" \
  -d '{
    "subject": "Database Design",
    "professor": "Dr. Smith",
    "content": "CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR(100), email VARCHAR(100) UNIQUE);\nCREATE TABLE orders (id INT PRIMARY KEY, user_id INT, total DECIMAL(10,2), FOREIGN KEY (user_id) REFERENCES users(id));"
  }' \
  --no-buffer
```

4. **Save the lesson ID from the stream and retrieve later**:
```bash
curl http://localhost:8080/chat/123e4567-e89b-12d3-a456-426614174000
```

### JavaScript/TypeScript Example

```typescript
class GumaApiClient {
  constructor(private baseUrl: string, private accessToken: string) {}

  async getCourses(): Promise<Course[]> {
    const response = await fetch(`${this.baseUrl}/canvas/courses`, {
      headers: {
        'access_token': this.accessToken
      }
    });
    return response.json();
  }

  async getAssignments(courseId: number): Promise<Assignment[]> {
    const response = await fetch(`${this.baseUrl}/canvas/assignments/${courseId}`, {
      headers: {
        'access_token': this.accessToken
      }
    });
    return response.json();
  }

  async submitForReview(
    courseId: number, 
    assignmentId: number, 
    submission: ChatDTO
  ): Promise<EventSource> {
    const url = `${this.baseUrl}/chat?courseId=${courseId}&assignmentId=${assignmentId}`;
    
    // For SSE, you'd typically use EventSource or a library
    const eventSource = new EventSource(url, {
      headers: {
        'Content-Type': 'application/json',
        'access_token': this.accessToken
      }
    });

    eventSource.onmessage = (event) => {
      const data = JSON.parse(event.data);
      console.log('Feedback chunk:', data.content);
    };

    return eventSource;
  }

  async getReview(lessonId: string): Promise<LessonReviews> {
    const response = await fetch(`${this.baseUrl}/chat/${lessonId}`);
    return response.json();
  }
}

// Usage
const client = new GumaApiClient('http://localhost:8080', 'your_canvas_token');

// Get courses and submit assignment
const courses = await client.getCourses();
const assignments = await client.getAssignments(courses[0].id);

const submission: ChatDTO = {
  subject: 'Database Design',
  professor: 'Dr. Smith',
  content: 'My database implementation...'
};

const eventSource = await client.submitForReview(
  courses[0].id, 
  assignments[0].id, 
  submission
);
```

### Python Example

```python
import requests
import json
from typing import List, Dict, Any

class GumaApiClient:
    def __init__(self, base_url: str, access_token: str):
        self.base_url = base_url
        self.access_token = access_token
        self.headers = {'access_token': access_token}
    
    def get_courses(self) -> List[Dict[str, Any]]:
        response = requests.get(
            f"{self.base_url}/canvas/courses",
            headers=self.headers
        )
        response.raise_for_status()
        return response.json()
    
    def get_assignments(self, course_id: int) -> List[Dict[str, Any]]:
        response = requests.get(
            f"{self.base_url}/canvas/assignments/{course_id}",
            headers=self.headers
        )
        response.raise_for_status()
        return response.json()
    
    def submit_for_review(self, course_id: int, assignment_id: int, submission: Dict[str, Any]):
        response = requests.post(
            f"{self.base_url}/chat",
            params={'courseId': course_id, 'assignmentId': assignment_id},
            headers={**self.headers, 'Content-Type': 'application/json'},
            json=submission,
            stream=True
        )
        response.raise_for_status()
        
        for line in response.iter_lines():
            if line.startswith(b'data: '):
                data = json.loads(line[6:])
                yield data['content']
    
    def get_review(self, lesson_id: str) -> Dict[str, Any]:
        response = requests.get(f"{self.base_url}/chat/{lesson_id}")
        response.raise_for_status()
        return response.json()

# Usage
client = GumaApiClient('http://localhost:8080', 'your_canvas_token')

# Get courses and submit assignment
courses = client.get_courses()
assignments = client.get_assignments(courses[0]['id'])

submission = {
    'subject': 'Database Design',
    'professor': 'Dr. Smith',
    'content': 'My database implementation...'
}

# Stream the feedback
for feedback_chunk in client.submit_for_review(courses[0]['id'], assignments[0]['id'], submission):
    print(feedback_chunk)
```

## 🛠️ SDKs & Tools

### Postman Collection
A Postman collection is available with pre-configured requests for all endpoints.

**Import URL**: `https://api.guma.edu/postman-collection.json`

### OpenAPI Specification
The complete OpenAPI 3.0 specification is available at:
- **JSON**: `http://localhost:8080/v3/api-docs`
- **YAML**: `http://localhost:8080/v3/api-docs.yaml`
- **Swagger UI**: `http://localhost:8080/docs`

### cURL Scripts
Bash scripts for common operations are available in the `/scripts` directory:

```bash
# Get all courses
./scripts/get-courses.sh your_token

# Submit assignment for review  
./scripts/submit-assignment.sh your_token 123 456 "Database Design" "Dr. Smith" "content.txt"

# Get review by ID
./scripts/get-review.sh lesson_id
```

---

## 📞 Support

- **Documentation**: This file and the interactive Swagger UI
- **Issues**: Report bugs and request features on GitHub
- **Community**: Join our discussions for questions and help

## 🔄 Versioning

This API follows semantic versioning. The current version is `v1`.

Breaking changes will only be introduced in major version updates, with advance notice and migration guides provided.