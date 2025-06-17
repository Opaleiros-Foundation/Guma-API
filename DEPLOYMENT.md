# 🚀 GUMA API Deployment Guide

This guide covers different deployment strategies for the GUMA API, from local development to production environments.

## 📋 Table of Contents
- [Prerequisites](#-prerequisites)
- [Local Development](#-local-development)
- [Docker Deployment](#-docker-deployment)
- [Production Deployment](#-production-deployment)
- [Environment Configuration](#-environment-configuration)
- [Monitoring & Maintenance](#-monitoring--maintenance)
- [Troubleshooting](#-troubleshooting)

## 🔧 Prerequisites

### Required Software
- **Java 17** or later (OpenJDK recommended)
- **Maven 3.6+**
- **Docker 20.10+** & **Docker Compose 2.0+** (for containerized deployment)
- **PostgreSQL 13+** (for production)
- **Canvas LMS access token**

### System Requirements

| Environment | CPU | RAM | Storage | Network |
|------------|-----|-----|---------|---------|
| Development | 2 cores | 4GB | 10GB | Basic |
| Staging | 2 cores | 8GB | 20GB | Moderate |
| Production | 4+ cores | 16GB+ | 50GB+ | High |

## 💻 Local Development

### Quick Start
```bash
# Clone repository
git clone https://github.com/Opaleiros-Foundation/guma-api.git
cd guma-api

# Set environment variables
export ACCESS_TOKEN=your_canvas_access_token

# Run application
./mvnw spring-boot:run
```

### Detailed Setup
1. **Clone and configure**:
   ```bash
   git clone https://github.com/Opaleiros-Foundation/guma-api.git
   cd guma-api
   
   # Copy and edit configuration
   cp src/main/resources/application.yml src/main/resources/application-local.yml
   ```

2. **Configure Canvas integration**:
   ```yaml
   # application-local.yml
   canvas:
     acess_token: your_canvas_access_token
     feign:
       url: https://your-institution.instructure.com/api/v1
   ```

3. **Run with custom profile**:
   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=local
   ```

4. **Verify installation**:
   ```bash
   curl http://localhost:8080/actuator/health
   ```

## 🐳 Docker Deployment

### Single Container
```bash
# Build application
./mvnw clean package -DskipTests

# Build Docker image
docker build -t guma-api:latest .

# Run container
docker run -d \
  --name guma-api \
  -p 8080:8080 \
  -e ACCESS_TOKEN=your_canvas_token \
  -e SPRING_PROFILES_ACTIVE=prod \
  guma-api:latest
```

### Docker Compose (Recommended)
```bash
# Create environment file
cat > .env << EOF
ACCESS_TOKEN=your_canvas_access_token
POSTGRES_PASSWORD=secure_password_here
OLLAMA_MODEL=tinyllama
EOF

# Start all services
docker-compose up -d

# Check status
docker-compose ps
docker-compose logs -f api
```

### Custom Docker Compose Override
```yaml
# docker-compose.override.yml
version: '3.8'
services:
  api:
    environment:
      - SPRING_PROFILES_ACTIVE=custom
      - LOGGING_LEVEL_ROOT=DEBUG
    volumes:
      - ./logs:/app/logs
    ports:
      - "8081:8080"  # Different port
  
  db:
    ports:
      - "5433:5432"  # Different port
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  postgres_data:
```

## 🏭 Production Deployment

### Option 1: Traditional Server Deployment

#### 1. Server Preparation
```bash
# Install Java 17
sudo apt update
sudo apt install openjdk-17-jdk

# Create application user
sudo useradd -r -s /bin/false guma
sudo mkdir -p /opt/guma-api
sudo chown guma:guma /opt/guma-api
```

#### 2. Application Deployment
```bash
# Build application
./mvnw clean package -DskipTests

# Copy JAR to server
scp target/gumaapi-0.0.1-SNAPSHOT.jar user@server:/opt/guma-api/

# Create systemd service
sudo tee /etc/systemd/system/guma-api.service << EOF
[Unit]
Description=GUMA API
After=network.target

[Service]
Type=simple
User=guma
ExecStart=/usr/bin/java -jar /opt/guma-api/gumaapi-0.0.1-SNAPSHOT.jar
Restart=always
RestartSec=10
StandardOutput=syslog
StandardError=syslog
SyslogIdentifier=guma-api
Environment=SPRING_PROFILES_ACTIVE=prod
Environment=ACCESS_TOKEN=your_canvas_token
Environment=PGHOST=localhost
Environment=PGDATABASE=guma
Environment=PGUSER=guma
Environment=PGPASSWORD=secure_password

[Install]
WantedBy=multi-user.target
EOF

# Start service
sudo systemctl daemon-reload
sudo systemctl enable guma-api
sudo systemctl start guma-api
```

#### 3. Database Setup
```bash
# Install PostgreSQL
sudo apt install postgresql postgresql-contrib

# Create database and user
sudo -u postgres psql << EOF
CREATE DATABASE guma;
CREATE USER guma WITH PASSWORD 'secure_password';
GRANT ALL PRIVILEGES ON DATABASE guma TO guma;
EOF
```

#### 4. Reverse Proxy (Nginx)
```nginx
# /etc/nginx/sites-available/guma-api
server {
    listen 80;
    server_name api.yourdomain.com;
    
    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # For SSE streaming
        proxy_buffering off;
        proxy_cache off;
        proxy_read_timeout 24h;
    }
}
```

### Option 2: Docker Swarm Deployment

#### 1. Initialize Swarm
```bash
# On manager node
docker swarm init

# On worker nodes (use token from init output)
docker swarm join --token <token> <manager-ip>:2377
```

#### 2. Deploy Stack
```yaml
# docker-stack.yml
version: '3.8'
services:
  api:
    image: matheusvict/gumaapi:latest
    deploy:
      replicas: 3
      placement:
        constraints:
          - node.role == worker
      restart_policy:
        condition: on-failure
        delay: 5s
        max_attempts: 3
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - ACCESS_TOKEN=${ACCESS_TOKEN}
    networks:
      - guma-network
    depends_on:
      - db

  db:
    image: postgres:15
    deploy:
      replicas: 1
      placement:
        constraints:
          - node.role == manager
    environment:
      - POSTGRES_DB=guma
      - POSTGRES_USER=guma
      - POSTGRES_PASSWORD=${POSTGRES_PASSWORD}
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - guma-network

  nginx:
    image: nginx:alpine
    ports:
      - "80:80"
      - "443:443"
    deploy:
      replicas: 1
      placement:
        constraints:
          - node.role == manager
    configs:
      - source: nginx_config
        target: /etc/nginx/nginx.conf
    networks:
      - guma-network

networks:
  guma-network:
    driver: overlay

volumes:
  postgres_data:

configs:
  nginx_config:
    external: true
```

```bash
# Deploy stack
docker stack deploy -c docker-stack.yml guma
```

### Option 3: Kubernetes Deployment

#### 1. Namespace and ConfigMap
```yaml
# k8s/namespace.yaml
apiVersion: v1
kind: Namespace
metadata:
  name: guma-api

---
apiVersion: v1
kind: ConfigMap
metadata:
  name: guma-config
  namespace: guma-api
data:
  SPRING_PROFILES_ACTIVE: "prod"
  PGHOST: "postgres-service"
  PGDATABASE: "guma"
  PGUSER: "guma"
```

#### 2. Database Deployment
```yaml
# k8s/postgres.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: postgres
  namespace: guma-api
spec:
  replicas: 1
  selector:
    matchLabels:
      app: postgres
  template:
    metadata:
      labels:
        app: postgres
    spec:
      containers:
      - name: postgres
        image: postgres:15
        env:
        - name: POSTGRES_DB
          value: "guma"
        - name: POSTGRES_USER
          value: "guma"
        - name: POSTGRES_PASSWORD
          valueFrom:
            secretKeyRef:
              name: postgres-secret
              key: password
        ports:
        - containerPort: 5432
        volumeMounts:
        - name: postgres-storage
          mountPath: /var/lib/postgresql/data
      volumes:
      - name: postgres-storage
        persistentVolumeClaim:
          claimName: postgres-pvc

---
apiVersion: v1
kind: Service
metadata:
  name: postgres-service
  namespace: guma-api
spec:
  selector:
    app: postgres
  ports:
  - port: 5432
    targetPort: 5432
```

#### 3. API Deployment
```yaml
# k8s/api.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: guma-api
  namespace: guma-api
spec:
  replicas: 3
  selector:
    matchLabels:
      app: guma-api
  template:
    metadata:
      labels:
        app: guma-api
    spec:
      containers:
      - name: guma-api
        image: matheusvict/gumaapi:latest
        ports:
        - containerPort: 8080
        env:
        - name: ACCESS_TOKEN
          valueFrom:
            secretKeyRef:
              name: canvas-secret
              key: access-token
        envFrom:
        - configMapRef:
            name: guma-config
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 30
        readinessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10

---
apiVersion: v1
kind: Service
metadata:
  name: guma-api-service
  namespace: guma-api
spec:
  selector:
    app: guma-api
  ports:
  - port: 80
    targetPort: 8080
  type: LoadBalancer
```

## ⚙️ Environment Configuration

### Development Environment
```yaml
# application-dev.yml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    username: sa
    password: 
  h2:
    console:
      enabled: true
  ai:
    ollama:
      chat:
        model: tinyllama

logging:
  level:
    university.jala.gumaapi: DEBUG
```

### Staging Environment
```yaml
# application-staging.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/guma_staging
    username: ${PGUSER:guma}
    password: ${PGPASSWORD:}
  ai:
    ollama:
      chat:
        model: deepseek-r1:1.5b

logging:
  level:
    university.jala.gumaapi: INFO
    org.springframework.web: DEBUG
```

### Production Environment
```yaml
# application-prod.yml
spring:
  datasource:
    url: jdbc:postgresql://${PGHOST:localhost}:${PGPORT:5432}/${PGDATABASE:guma}
    username: ${PGUSER:guma}
    password: ${PGPASSWORD:}
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
  ai:
    ollama:
      chat:
        model: ${OLLAMA_MODEL:tinyllama}

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: when-authorized

logging:
  level:
    university.jala.gumaapi: INFO
    org.springframework.security: WARN
  file:
    name: /var/log/guma-api/application.log
```

### Environment Variables Reference

| Variable | Description | Required | Default |
|----------|-------------|----------|---------|
| `ACCESS_TOKEN` | Canvas LMS access token | Yes | - |
| `SPRING_PROFILES_ACTIVE` | Active Spring profile | No | default |
| `PGHOST` | PostgreSQL host | No | localhost |
| `PGPORT` | PostgreSQL port | No | 5432 |
| `PGDATABASE` | PostgreSQL database | No | guma |
| `PGUSER` | PostgreSQL username | No | postgres |
| `PGPASSWORD` | PostgreSQL password | Yes (prod) | - |
| `OLLAMA_BASE_URL` | Ollama service URL | No | http://localhost:11434 |
| `OLLAMA_MODEL` | AI model to use | No | tinyllama |
| `JAVA_OPTS` | JVM options | No | - |

## 📊 Monitoring & Maintenance

### Health Checks
```bash
# Application health
curl http://localhost:8080/actuator/health

# Database connectivity
curl http://localhost:8080/actuator/health/db

# Disk space
curl http://localhost:8080/actuator/health/diskSpace
```

### Logging Configuration
```yaml
# logback-spring.xml
<configuration>
    <springProfile name="prod">
        <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
            <file>/var/log/guma-api/application.log</file>
            <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                <fileNamePattern>/var/log/guma-api/application.%d{yyyy-MM-dd}.%i.gz</fileNamePattern>
                <maxFileSize>100MB</maxFileSize>
                <maxHistory>30</maxHistory>
                <totalSizeCap>10GB</totalSizeCap>
            </rollingPolicy>
            <encoder>
                <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
            </encoder>
        </appender>
        
        <root level="INFO">
            <appender-ref ref="FILE" />
        </root>
    </springProfile>
</configuration>
```

### Monitoring with Prometheus (Optional)
```yaml
# Add to application-prod.yml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  metrics:
    export:
      prometheus:
        enabled: true
```

### Backup Strategy
```bash
#!/bin/bash
# backup-database.sh

DB_NAME="guma"
DB_USER="guma"
BACKUP_DIR="/opt/backups"
DATE=$(date +%Y%m%d_%H%M%S)

# Create backup
pg_dump -h localhost -U $DB_USER -d $DB_NAME > $BACKUP_DIR/guma_backup_$DATE.sql

# Compress backup
gzip $BACKUP_DIR/guma_backup_$DATE.sql

# Remove backups older than 30 days
find $BACKUP_DIR -name "guma_backup_*.sql.gz" -mtime +30 -delete

echo "Backup completed: guma_backup_$DATE.sql.gz"
```

## 🔧 Troubleshooting

### Common Issues

#### 1. Application Won't Start
```bash
# Check Java version
java -version

# Check if port is in use
netstat -tlnp | grep 8080

# Check logs
docker-compose logs api
# or
journalctl -u guma-api -f
```

#### 2. Database Connection Issues
```bash
# Test database connectivity
psql -h localhost -U guma -d guma -c "SELECT 1;"

# Check environment variables
env | grep PG

# Verify database is running
docker-compose ps db
```

#### 3. Canvas API Integration Issues
```bash
# Test Canvas token
curl -H "Authorization: Bearer your_token" \
     https://your-institution.instructure.com/api/v1/users/self

# Check Canvas URL configuration
curl http://localhost:8080/actuator/configprops | grep canvas
```

#### 4. AI Service Issues
```bash
# Check Ollama service
docker-compose logs ollama

# Test Ollama directly
curl http://localhost:11434/api/generate \
  -d '{"model": "tinyllama", "prompt": "test"}'

# Check AI configuration
curl http://localhost:8080/actuator/configprops | grep ollama
```

### Performance Optimization

#### JVM Tuning
```bash
# For production deployment
export JAVA_OPTS="-Xms2g -Xmx4g -XX:+UseG1GC -XX:G1HeapRegionSize=16m"
```

#### Database Optimization
```sql
-- Add indexes for performance
CREATE INDEX idx_lesson_reviews_response_date ON lesson_reviews(response_date);
CREATE INDEX idx_lesson_reviews_subject ON lesson_reviews(subject);
```

#### Connection Pool Tuning
```yaml
# application-prod.yml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 20000
      idle-timeout: 300000
      max-lifetime: 1200000
```

### Security Considerations

#### 1. Secure Token Storage
```bash
# Use environment variables or secrets management
export ACCESS_TOKEN=$(cat /run/secrets/canvas_token)
```

#### 2. Network Security
```yaml
# docker-compose.yml - Internal network
networks:
  guma-network:
    driver: bridge
    internal: true
```

#### 3. Container Security
```dockerfile
# Dockerfile security improvements
FROM openjdk:17-jre-alpine
RUN addgroup -g 1001 guma && adduser -u 1001 -G guma -s /bin/sh -D guma
USER 1001
```

---

## 📞 Support

For deployment issues:
1. Check the [troubleshooting section](#-troubleshooting)
2. Review application logs
3. Check [GitHub Issues](https://github.com/Opaleiros-Foundation/guma-api/issues)
4. Contact the development team

## 🔄 Updates

### Rolling Updates
```bash
# Docker Compose
docker-compose pull api
docker-compose up -d api

# Kubernetes
kubectl set image deployment/guma-api guma-api=matheusvict/gumaapi:new-version -n guma-api
```

### Database Migrations
Database schema changes are handled automatically by Hibernate in development. For production, consider using Flyway or Liquibase for controlled migrations.

---

*This deployment guide is maintained alongside the GUMA API project. For the latest updates, refer to the main repository.*