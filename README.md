<div align="center">
<a href="https://github.com/Sumonta056/FixHub-Issue-Tracker-Website" target="blank">

<img width="128" height="128" alt="image" src="https://github.com/user-attachments/assets/43d75824-c958-43a3-a500-10daefbb517a" />

</a>

<h1> 69chan </h1>

**“Where Otaku hearts connect — 絆 (Kizuna) through anime.”**

***69chan*** is a social media app for Otakus.  
You can post what you love, chat with other fans, and find people who like the same anime and manga.  

Our goal is simple: make a fun and friendly place where Otakus can connect and enjoy anime culture together. With helping hands from

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)
![Render](https://img.shields.io/badge/Render-%46E3B7.svg?style=for-the-badge&logo=render&logoColor=white)
</div>

## ✨ Features
- **🔐 Authentication:** Secure ***JWT-based*** with ***HS256 Algorithm*** and token refreshing mechanism access levels and ***Attribute-based Access Control*** ensure that only authorized users can manage or view resources.
- **♾️ Infinite Scrolling:** Making users's home feeds looks like never ends with ***cursor-based pagination*** and ***fan-out on write/read*** pattern.
- **☁️ Media Storage:** Managed to handle bulk media file uploading from users up to ***10 files, 1KB-10MB***, at once with latency ***2-4s*** with asynchronous thread executors.
  <img width="819" height="542" alt="image" src="https://github.com/user-attachments/assets/bbe562b7-a1d4-46c4-a7f0-aa5146f61324" />
  
- **🔍 Advanced Searching:** Power searching utizling Full Text Search of Postgres and MongoDB to quickly find out posts and user accounts.
- **📝 Communication:** Real-time chat pipeline built with ***Websocket + STOMP*** with helping of message queue from ***Redis*** gives users who finds their same interests to connect each other.  .
- **📱 Multiplatform:** Access ***69chan*** on any device from *web or mobile* with a responsive design that adapts to various screen sizes.



## 🛠 Tech Stack
``` mermaid
flowchart TD
    %% Nodes Definition
    Client(["<b>Client / Frontend</b><br/>Web / mobile app"])
    SB(["<b>Spring Boot Backend</b><br/>Java 21 · Spring Boot 3.4.1"])

    %% Hierarchy
    Client --> SB

    subgraph BC [<b>Backend core</b>]
        direction TB
        SW[Spring Web - REST]
        SS[Spring Security]
        SV[Spring Validation]
        SA[Spring AOP]
        SM[Spring Mail]
        L[Lombok]
    end

    subgraph DL [<b>Data layer</b>]
        direction TB
        SDJ[Spring Data JPA]
        PG[(PostgreSQL 15<br/>relational db)]
        MG[(MongoDB<br/>document store)]
        RD[(Redis 7<br/>cache / session)]
        SDJ --- PG
        SDJ --- MG
        SDJ --- RD
    end

    subgraph AC [<b>Auth & config</b>]
        direction TB
        JWT[JWT Auth<br/>JWT_SECRET via env]
        PC[Profile Config<br/>dev / local / common]
        ENV[.env import<br/>application.yaml]
    end

    subgraph FS [<b>File storage</b>]
        direction LR
        GCS[Google Cloud Storage<br/>spring-cloud-gcp]
        CLD[Cloudinary<br/>optional · via env]
    end

    subgraph BD [<b>Build & deployment</b>]
        direction TB
        subgraph Process [Build Pipeline]
            direction LR
            MW[Maven Wrapper<br/>Package JAR] --> DM[Docker multi-stage<br/>maven:3.9.6 + temurin:21-jre] --> CI[Container image<br/>Expose :8080]
        end
        subgraph Deploy [Delivery]
            direction LR
            GHA[GitHub Actions<br/>CI/CD pipeline] --> RC[Render.com<br/>Web service host] --> DS[Discord<br/>Webhook notifications]
        end
    end

    %% Global Connections
    SB --> BC
    SB --> DL
    SB --> AC
    AC --> CLD
    BC -.-> MW

    %% Styling - Light Theme Palette
    classDef default fill:#ffffff,stroke:#333,stroke-width:1px;
    classDef client fill:#E8EAF6,stroke:#3F51B5,color:#1A237E,stroke-width:2px;
    classDef main fill:#F3E5F5,stroke:#7B1FA2,color:#4A148C,stroke-width:2px;
    classDef core fill:#E0F2F1,stroke:#00897B,color:#004D40;
    classDef data fill:#E1F5FE,stroke:#0288D1,color:#01579B;
    classDef auth fill:#F3E5F5,stroke:#8E24AA,color:#4A148C;
    classDef storage fill:#FFF3E0,stroke:#FB8C00,color:#E65100;
    classDef build fill:#FBE9E7,stroke:#D84315,color:#3E2723;
    classDef notify fill:#F5F5F5,stroke:#9E9E9E,color:#212121;

    %% Apply Classes
    class Client client;
    class SB main;
    class SW,SS,SV,SA,SM,L core;
    class SDJ,PG,MG,RD data;
    class JWT,PC,ENV auth;
    class GCS,CLD storage;
    class MW,DM,CI,GHA,RC build;
    class DS notify;

    %% Subgraph Styling
    style BC fill:#F1F8F7,stroke:#00897B,stroke-dasharray: 5 5
    style DL fill:#F0F4F8,stroke:#0288D1,stroke-dasharray: 5 5
    style AC fill:#F8F0F8,stroke:#8E24AA,stroke-dasharray: 5 5
    style FS fill:#FFF8F0,stroke:#FB8C00,stroke-dasharray: 5 5
    style BD fill:#FFF5F2,stroke:#D84315,stroke-dasharray: 5 5
```

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


