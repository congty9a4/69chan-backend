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

<a href="https://69chan.atlassian.net/jira/core/projects/CHAN/board" target="blank">
<img src="https://img.shields.io/badge/jira-%230A0FFF.svg?style=for-the-badge&logo=jira&logoColor=white" />
</a>
</div>

## ✨ Features
- **🔐 Authentication:** Secure ***JWT-based*** with ***HS256 Algorithm*** and token refreshing mechanism access levels and ***Attribute-based Access Control*** ensure that only authorized users can manage or view resources.
- **♾️ Infinite Scrolling:** Making users's home feeds looks like never ends with ***cursor-based pagination*** and ***fan-out on write/read*** pattern.
- **☁️ Media Storage:** Managed to handle bulk media file uploading from users up to ***10 files, 1KB-10MB***, at once with latency ***2-4s*** with asynchronous thread executors.
  <img width="819" height="542" alt="image" src="https://github.com/user-attachments/assets/bbe562b7-a1d4-46c4-a7f0-aa5146f61324" />
  
- **🔍 Advanced Searching:** Power searching utizling Full Text Search of Postgres and MongoDB to quickly find out posts and user accounts.
- **📝 Communication:** Real-time chat pipeline built with ***Websocket + STOMP*** with helping of message queue from ***Redis*** gives users who finds their same interests to connect each other.  .
- **📱 Multiplatform:** Access ***69chan*** on any device from *web or mobile* with a responsive design that adapts to various screen sizes.
- **👨‍👨‍👦 Task Assignment :** Apply ***Scrum*** workflow for feature building with team.
<img width="1358" height="894" alt="image" src="https://github.com/user-attachments/assets/8a9418da-9f1d-4661-a100-c4b738c29fde" />

- **🚀 Continuous Deployment :** Automated build and deploy pipeline on Render, including build, release, and environment configuration steps. 

## 📖 API Documentation

When the application is running, Swagger UI is available at:

- `http://localhost:8080/swagger-ui.html`

Use it to inspect endpoints, request schemas, and test calls interactively.

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

This backend follows a layered Spring Boot structure to keep API, business logic, and persistence concerns isolated and maintainable.

### Package Structure
```text
src/main/java/com/congty9a4/backend/
├── annotation/      # Custom annotations
├── config/          # Security, async, docs, app-level configuration
├── constant/        # Shared constants and enums
├── controller/      # REST endpoints
├── dto/             # Request/response payload models
├── entity/          # PostgreSQL entities and MongoDB documents
├── event/           # Domain/application events
├── exception/       # Exception types and global handlers
├── mapper/          # Mapping between entity <-> DTO models
├── repository/      # Spring Data repositories
├── script/          # Helper scripts/utilities related to data flows
├── service/         # Business logic and orchestration
└── util/            # Reusable utility helpers
```

### Design Principles
- **Layered architecture**: Controller -> Service -> Repository flow for clear separation of concerns.
- **DTO boundary**: API contracts are separated from persistence models.
- **Polyglot persistence**: PostgreSQL + MongoDB + Redis based on data access patterns.
- **Stateless auth**: JWT-based authentication and request-level authorization.
- **Config by profile**: `application-*.yaml` files for environment-specific behavior.

## 💾 Database Design

### PostgreSQL
Main relational data lives in PostgreSQL.

- `userchans`: account-level identity/auth data.
- `profiles`: profile metadata tied to users.
- `friendships` and `relationships`: social graph and connection state.
- Flyway migrations are stored under `src/main/resources/db/migration`.

### MongoDB
Document-style content data is stored in MongoDB.

- `posts`: feed content, media references, and interaction metadata.
- `comments`: threaded comment data.
- Text and supporting indexes are used to improve feed/search queries.

### Redis
Redis is used for fast-access/ephemeral workloads (for example caching and real-time support paths where configured).

## 📦 Getting Started

Run the backend locally with Java 21 and your database services.

### Prerequisites
- **JDK 21**
- **Maven Wrapper** (`./mvnw` is included)
- **PostgreSQL** instance
- **MongoDB** instance
- **Redis** instance (recommended for full feature support)

### Installation 
1. **Configure Environment**
Create a `.env` file in the project root and provide values used by your environment.

```env
# PostgreSQL
POSTGRE_DB_HOST=localhost
POSTGRE_DB_PORT=5432
POSTGRE_DB_USERNAME=your_user
POSTGRE_DB_PASSWORD=your_password

# MongoDB
MONGO_DB_HOST=localhost
MONGO_DB_NAME=69chan
MONGO_DB_USERNAME=your_mongo_user
MONGO_DB_PASSWORD=your_mongo_password

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379

# JWT
JWT_SECRET=your_super_secret_key_min_256_bits

# Swagger / app URL
SWAGGER_SERVER_URL=http://localhost:8080

# Optional storage providers
STORAGE_PROVIDER=gcs
GOOGLE_APPLICATION_CREDENTIALS=path/to/gcs_credentials.json

# Or Cloudinary
# STORAGE_PROVIDER=cloudinary
# CLOUDINARY_CLOUD_NAME=your_cloud
# CLOUDINARY_API_KEY=your_key
# CLOUDINARY_API_SECRET=your_secret
```

2. **Run with Maven Wrapper**
```bash
./mvnw clean install
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

3. **Verify**
- API base URL: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

### Available Profiles
- `local`: local development defaults
- `dev`: shared development environment
- `common`: shared base configuration

## 🐳 Deployment

### Docker Build
```bash
docker build -t 69chan-backend .
```

### Docker Compose
```bash
docker-compose up -d
```

### Runtime Notes
- App listens on port `8080`.
- Container startup uses `entrypoint.sh`.
- Environment variables should be supplied by your host/CI/CD platform.


## 🔮 Roadmap

### High Priority
- [ ] Complete cursor-based pagination rollout for feed-heavy endpoints.
- [ ] Expand real-time capabilities for notifications and messaging flows.
- [ ] Harden authorization policies for role/scoped access.

### Medium Priority
- [ ] Improve media pipeline (transformations and optimization).
- [ ] Strengthen search relevance and autocomplete.
- [ ] Add targeted caching for feed and profile hotspots.
- [ ] Add request throttling/rate limiting.
- [ ] Increase integration and repository test coverage.

### Low Priority
- [ ] Add metrics/observability dashboards.
- [ ] Evaluate GraphQL as a complementary API interface.
- [ ] Add i18n support for multi-language user experience.
- [ ] Add anti-abuse/bot mitigation controls.

## 🤝 Contributing

We welcome contributions to this project. Please follow these steps to contribute:

1. **Fork the repository.**
2. **Create a new branch** (`git checkout -b feature/your-feature-name`).
3. **Make your changes** and commit them (`git commit -m 'Add some feature'`).
4. **Push to the branch** (`git push origin feature/your-feature-name`).
5. **Open a pull request**.

Please make sure to update tests as appropriate.

## 🐛 Issues

If you encounter any issues while using or setting up the project, please check the [Issues]() section to see if it has already been reported. If not, feel free to open a new issue detailing the problem.

When reporting an issue, please include:

- A clear and descriptive title.
- A detailed description of the problem.
- Steps to reproduce the issue.
- Any relevant logs or screenshots.
- The environment in which the issue occurs (OS, browser, Node.js version, etc.).

## 📜 License

Distributed under the MIT License. See [License](/LICENSE) for more information.
