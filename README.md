# Backend for 69chan

This directory contains the backend services for the "69chan" application. The backend is built using Java, Spring Boot, and Maven.

## Environment Setup

To get the backend running locally, you'll need to set up the following:

### Prerequisites

-   **Java Development Kit (JDK) 21**: Make sure you have JDK 21 installed. You can verify this by running `java -version`.
-   **Maven**: This project uses the Maven wrapper, so you don't need to have Maven installed globally. The wrapper (`mvnw` or `mvnw.cmd`) will download the correct version for you.

### Environment Variables

The application uses a `.env` file to manage environment variables.

1.  Create a `.env` file in the `backend` directory.
2.  Copy the contents from `.env.example` into your new `.env` file.
3.  Update the values in the `.env` file with your local configuration, such as database credentials and other settings.

## Running the Application

You can build and run the application using the included Maven wrapper.

### Build

To compile the source code, run the tests, and package the application, run the following command from the `backend` directory:

```bash
./mvnw clean install
```

### Run

To run the application, use the following command:

```bash
./mvnw spring-boot:run
```

The application will start, and you can access it at `http://localhost:8080` (or the port configured in your `.env` file).

### Running with a Profile

This Spring Boot application can be run with different configuration profiles (e.g., `dev`, `prod`). You can specify the active profile using the `-Dspring-boot.run.profiles` argument.

For example, to run with the `dev` profile:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

## Database Architecture

This project utilizes a polyglot persistence approach, leveraging both a relational and a NoSQL database to best fit the needs of different services.

-   **PostgreSQL**: Used as the primary relational database, managed via Spring Data JPA. It is suitable for structured data that requires strong transactional consistency, such as user information and relationships.
    -   **Full Text Search (FTS)**: PostgreSQL FTS is implemented for efficient user search with ranking support. See [FTS Documentation](./docs/POSTGRESQL_FTS.md) for details.
-   **MongoDB**: Used as the NoSQL database, managed via Spring Data MongoDB. It is ideal for storing unstructured or semi-structured data, such as posts, comments, or activity streams that benefit from a flexible schema.
    -   **Text Search**: MongoDB text indexes are used for post content search.

Make sure you have both PostgreSQL and MongoDB instances running and have configured the connection details in your `.env` file.

## Full Text Search

The application implements PostgreSQL Full Text Search (FTS) for efficient user searching with the following features:

-   **Standard Search**: Simple keyword and phrase search using `plainto_tsquery`
-   **Advanced Search**: Complex queries with AND, OR, and exact phrase matching using `websearch_to_tsquery`
-   **Ranking**: Results ordered by relevance using `ts_rank`
-   **Pagination**: Full pagination support with Spring Data
-   **Performance**: GIN index for optimized query performance

### Quick Start

```bash
# Simple search
curl "http://localhost:8080/api/search?query=john"

# Advanced search with OR operator
curl "http://localhost:8080/api/search?query=john OR jane&filter=advanced"

# Paginated search
curl "http://localhost:8080/api/search/paginated?query=user&page=1&size=10"
```

### Documentation

-   [Full Text Search Documentation](./docs/POSTGRESQL_FTS.md) - Technical details and implementation
-   [FTS Usage Guide](./docs/FTS_USAGE_GUIDE.md) - Complete usage examples and best practices
-   [API Test Script](./docs/test-fts-api.sh) - Automated testing examples

### Testing

```bash
# Run FTS integration tests
./mvnw test -Dtest=UserRepositoryFTS Test

# Run API tests
./docs/test-fts-api.sh
```

## API Documentation

The project includes `springdoc-openapi` to automatically generate API documentation using Swagger UI. Once the application is running, you can access the Swagger UI at:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

This interface allows you to view all available API endpoints, see their request and response structures, and interact with the API directly from your browser.
