<div align="center">
<a href="https://github.com/Sumonta056/FixHub-Issue-Tracker-Website" target="blank">

<img width="128" height="128" alt="image" src="https://github.com/user-attachments/assets/43d75824-c958-43a3-a500-10daefbb517a" />

</a>

<h1> 69chan </h1>

**“Where Otaku hearts connect — 絆 (Kizuna) through anime.”**

***69chan*** is a social media app for Otakus.  
You can post what you love, chat with other fans, and find people who like the same anime and manga.  

Our goal is simple: make a fun and friendly place where Otakus can connect and enjoy anime culture together. With helping hands from

![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)


</div>

## 📋 Table of Contents
- [Tech Stack](#-tech-stack)
- [Project Architecture](#-project-architecture)
- [Database Design](#-database-design)
- [API Overview](#-api-overview)
- [Features](#-features)
- [Getting Started](#-getting-started)
- [Deployment](#-deployment)
- [Future Roadmap](#-future-roadmap)

## 🛠 Tech Stack

**Core:**
- Java 21
- Spring Boot 3.4.1
- Maven

**Frameworks & Libraries:**
- Spring Security (JWT authentication)
- Spring Data JPA (PostgreSQL)
- Spring Data MongoDB
- Spring Cloud GCP (Google Cloud Storage)
- MapStruct (object mapping)
- Lombok
- Springdoc OpenAPI (Swagger UI)

**Databases:**
- PostgreSQL (relational data, FTS)
- MongoDB (posts & comments)

**Storage:**
- Google Cloud Storage (GCS)
- Cloudinary (alternative)

**DevOps:**
- Docker & Docker Compose
- Maven Wrapper

## 🏗 Project Architecture

### Package Structure
```
com.congty9a4.backend/
├── annotation/          # Custom annotations (@TrackExecutionTime)
├── config/             # Configuration classes
│   ├── security/       # JWT, CORS, filters
│   ├── cloud/          # GCS configuration
│   └── mongodb/        # MongoDB setup
├── controller/         # REST API endpoints
├── dto/               # Request/Response DTOs
├── entity/            # JPA entities & MongoDB documents
├── exception/         # Custom exceptions & error handling
├── mapper/            # MapStruct mappers
├── repository/        # Data access layer
├── service/           # Business logic
│   ├── implement/     # Service implementations
│   ├── storage/       # File storage services
│   └── crawling/      # Reddit data crawling
└── util/              # Utility classes
```

### Key Design Patterns
- **Polyglot Persistence**: PostgreSQL for users/relationships, MongoDB for posts/comments
- **JWT Stateless Authentication**: Token-based auth with refresh tokens
- **DTO Pattern**: Separation of internal entities and API contracts
- **Service Layer**: Business logic isolation from controllers
- **AOP**: Logging and execution time tracking

## 💾 Database Design

### PostgreSQL (Relational Data)
**Tables:**
- `userchans` - User accounts with credentials
- `profiles` - Extended user information (bio, avatar, etc.)
- `friendships` - Friend requests/connections (PENDING, ACCEPTED, BLOCKED)
- `relationships` - User relationships

**Features:**
- UUID primary keys for users
- Full-Text Search (FTS) with GIN indexes
- Email uniqueness constraints
- Automatic timestamps (created_at, updated_at)

### MongoDB (Document Storage)
**Collections:**
- `posts` - User posts with media, tags, likes
- `comments` - Nested comments with parent-child relationships

**Indexes:**
- Text indexes on post captions for search
- User ID indexes for efficient queries

## 🌐 API Overview

**Base URL:** `http://localhost:8080/api`

### Authentication (`/auth`)
- `POST /login` - User login (returns JWT)
- `GET /guest` - Guest access
- `GET /refresh-token` - Refresh JWT token

### Users (`/users`)
- `GET /users` - List all users (paginated)
- `GET /users/{id}` - Get user by ID
- `POST /users/create` - Register new user
- `PUT /users/{id}` - Update user
- `DELETE /users/{id}` - Delete user

### Profiles (`/profiles`)
- `GET /profiles` - List all profiles
- `GET /users/{userId}/profile` - Get user profile
- `POST /profiles` - Create profile with avatar upload
- `PATCH /profiles/{id}` - Update profile
- `GET /profiles/check-keyname` - Check profile key availability

### Posts (`/posts`)
- `POST /posts/create` - Create post (multipart: media files + JSON)
- `GET /posts/feed` - Get post feed (paginated)
- `GET /posts` - List posts
- `GET /posts/{id}` - Get single post
- `PATCH /posts/{id}/like` - Like/unlike post
- `GET /posts/{postId}/comments` - Get comments
- `POST /posts/{postId}/comments` - Add comment
- `POST /posts/{postId}/comments/{commentId}` - Reply to comment

### Search (`/search`)
- `GET /search?query={q}&filter=users|posts` - Full-text search
  - PostgreSQL FTS for users (with ranking)
  - MongoDB text search for posts
  - Pagination & sorting support

**Interactive API Docs:** Swagger UI at `http://localhost:8080/swagger-ui.html`

## ✨ Features

### Implemented
✅ **User Management** - Registration, authentication, profile management  
✅ **JWT Authentication** - Stateless auth with access & refresh tokens (30-day expiry)  
✅ **Profile System** - Bio, avatar, phone, birthday, location  
✅ **Post & Comment System** - Create posts with media, nested comments  
✅ **Like System** - Like/unlike posts with count tracking  
✅ **Friendship System** - Friend requests (PENDING/ACCEPTED/BLOCKED states)  
✅ **Full-Text Search** - PostgreSQL FTS for users, MongoDB text search for posts  
✅ **File Upload** - Multi-file uploads via GCS or Cloudinary  
✅ **Pagination** - Offset-based pagination across all list endpoints  
✅ **CORS** - Configured for frontend (localhost:3000)  
✅ **Logging** - Request logging & execution time tracking (AOP)  
✅ **API Documentation** - Auto-generated Swagger/OpenAPI docs  
✅ **Reddit Crawling** - Service for importing Reddit content  

### Partially Implemented
⚠️ **Database Migrations** - Flyway setup exists but limited migrations  
⚠️ **Email Service** - Spring Mail configured but not integrated  

## 🚀 Getting Started

### Prerequisites
- **JDK 21** - `java -version` to verify
- **Docker** (optional) - for containerized databases
- **PostgreSQL** - Running instance
- **MongoDB** - Running instance

### Environment Setup

1. **Create `.env` file** in project root:
```bash
# Database
POSTGRE_DB_HOST=localhost
POSTGRE_DB_PORT=5432
POSTGRE_DB_USERNAME=your_user
POSTGRE_DB_PASSWORD=your_password

MONGO_DB_HOST=your_mongo_host
MONGO_DB_NAME=69chan
MONGO_DB_USERNAME=your_mongo_user
MONGO_DB_PASSWORD=your_mongo_pass

# JWT
JWT_SECRET=your_super_secret_key_min_256_bits

# Storage (choose one)
STORAGE_PROVIDER=gcs  # or cloudinary

# GCS (if using)
GOOGLE_APPLICATION_CREDENTIALS=path/to/gcs_credentials.json

# Cloudinary (if using)
CLOUDINARY_CLOUD_NAME=your_cloud
CLOUDINARY_API_KEY=your_key
CLOUDINARY_API_SECRET=your_secret

# Swagger
SWAGGER_SERVER_URL=http://localhost:8080
```

2. **Build the project:**
```bash
./mvnw clean install
```

3. **Run locally:**
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

4. **Access the app:**
- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

### Running Tests
```bash
./mvnw test
```

### Profiles
- `local` - Local development
- `dev` - Development environment
- `common` - Shared configuration

## 🐳 Deployment

### Docker Build
```bash
docker build -t 69chan-backend .
```

### Docker Compose
```bash
docker-compose up -d
```

**Dockerfile stages:**
1. Maven build with dependency caching
2. Lightweight JRE runtime (Alpine)
3. Exposes port 8080

**Production Checklist:**
- [ ] Update JWT secret to production value
- [ ] Configure production database URLs
- [ ] Set up GCS credentials securely
- [ ] Configure CORS for production domain
- [ ] Enable HTTPS/TLS
- [ ] Set up monitoring (Spring Actuator available at `/actuator`)

## 🔮 Future Roadmap

### High Priority
- [ ] **Cursor-based Pagination** - Replace offset pagination for feed (noted in PostController)
- [ ] **Real-time Features** - WebSocket support for notifications
- [ ] **Email Verification** - Complete email service integration
- [ ] **Password Reset** - Forgot password flow
- [ ] **Role-based Access Control** - Admin/Moderator roles
- [ ] **Post Privacy** - Public/Friends/Private visibility (enum exists, not enforced)

### Medium Priority
- [ ] **Media Processing** - Image resizing, video transcoding
- [ ] **Search Improvements** - Elasticsearch integration, autocomplete
- [ ] **Caching Layer** - Redis for sessions, feed caching
- [ ] **Rate Limiting** - API throttling
- [ ] **Comprehensive Testing** - Increase test coverage
- [ ] **Database Migrations** - Complete Flyway migration scripts

### Low Priority
- [ ] **GraphQL API** - Alternative to REST
- [ ] **Metrics & Monitoring** - Prometheus/Grafana integration
- [ ] **Multi-language Support** - i18n
- [ ] **Bot Detection** - CAPTCHA integration

## 🤝 Contributing

When working on this project:
1. Use the configured code style (Lombok, MapStruct)
2. Add Swagger annotations to new endpoints
3. Follow the existing package structure
4. Write tests for new features
5. Update this README if adding major features

---

**Note:** This is an active development project. Some features are work-in-progress. Check TODOs in code for specific implementation notes.

## API Documentation

The project includes `springdoc-openapi` to automatically generate API documentation using Swagger UI. Once the application is running, you can access the Swagger UI at:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

This interface allows you to view all available API endpoints, see their request and response structures, and interact with the API directly from your browser.
