# Hotel Management Web Application

A Java-based hotel management web application built with Spring Framework (without Spring Boot), Hibernate, MySQL, JWT-based security, and Docker support.

---

## Table of Contents

* [Overview](#overview)
* [Functionality](#functionality)
* [Technology Stack](#technology-stack)
* [Project Structure](#project-structure)
* [Modules](#modules)
* [Configuration](#configuration)

    * [Environment Variables](#environment-variables)
    * [Application Properties](#application-properties)
    * [SQL Initialization Scripts](#sql-initialization-scripts)
* [Building and Running](#building-and-running)

    * [Local Build](#local-build)
    * [Running with Docker Compose](#running-with-docker-compose)
* [Testing](#testing)
* [Checkstyle and Build Tools](#checkstyle-and-build-tools)
* [Contacts](#contacts)

---

## Overview

This project is a multi-module Java web application for hotel management, implemented using the Spring Framework without Spring Boot. It uses:

* **Spring Web MVC** for controllers and REST endpoints.
* **Spring Security** with JWT-based authentication and role-based access control.
* **Hibernate ORM** (configured via `LocalSessionFactoryBean`) for persistence.
* **MySQL** as the relational database.
* **Maven** for build and dependency management in a multi-module setup.
* **Docker & Docker Compose** to containerize the application and the MySQL database.
* **Checkstyle** via a separate build-tools module to enforce coding standards.
* **JUnit 5 / Mockito** for unit and integration testing.

Key points:

* **No Spring Boot**: all configurations are done manually via Java config and property files.
* Modules are separated by responsibility: core (business logic), infra (DAO/repositories), common (utilities), app (web module), build-tools (code style checks).
* Database initialization is done through SQL scripts located in `sql/`, executed by the MySQL container on startup.

---

## Functionality

Below is an example list of features. Customize based on your actual implementation:

### Authentication and Authorization

* **User Registration and Login**: register users with roles (e.g., ROLE\_USER, ROLE\_ADMIN).
* **JWT-based Authentication**: issue and validate JWT tokens.
* **Role-Based Access Control**:

    * Endpoints under `/auth/**` are public (registration, login).
    * Import/export endpoints (`/guests/export`, `/rooms/import`, etc.) restricted to ADMIN or users with specific privileges.
    * Main resources (`/rooms`, `/services`, `/guests`, etc.) accessible to USER or ADMIN as appropriate.
    * Administrative endpoints restricted to ADMIN.

### Guest Management

* **CRUD operations** for guests: create, read, update, delete.
* **Search and Filtering**: find guests by name, registration date, status.
* **Import/Export**: bulk import and export of guest data (CSV/JSON).

### Room Management

* **CRUD operations** for rooms: add, modify, delete, view.
* **Search/Filter**: filter rooms by type, availability, price.
* **Import/Export**: bulk operations for room data.

### Service Management

* **CRUD operations** for services: create additional services (e.g., meals, cleaning, transfers).
* **Pricing and Details**: configure price, descriptions, and constraints.
* **Import/Export**: bulk operations for service data.

### Booking Management

* **Create bookings**: link guest and room for a specific period.
* **View and manage bookings**.
* **Validation**: check room availability for the requested dates.
* **Cancel bookings**.

### REST API

* All functionality is exposed via REST controllers.
* JSON for request and response bodies.
* Document endpoints with examples (e.g., curl, Postman collection).

### Validation and Error Handling

* **Jakarta Bean Validation** (`@Valid`, validation annotations) for DTOs and incoming data.
* **Global Exception Handling** (`@ControllerAdvice`) to return consistent error responses.

---

## Technology Stack

* **Java**: 17
* **Spring Framework (without Boot)**:

    * Spring Context, Spring Web MVC, Spring Security, Spring ORM, Spring TX, Spring Validation.
* **Hibernate ORM**: configured via Spring `LocalSessionFactoryBean`.
* **MySQL**: 8.0
* **JWT**: io.jsonwebtoken (jjwt) for token handling.
* **Maven**: multi-module build.
* **Docker & Docker Compose**: containerization of MySQL and the application.
* **JUnit 5** / **Mockito** for testing.
* **Checkstyle**: code style enforcement in build-tools module.
* **Jackson**: JSON serialization/deserialization.
* **MapStruct** (if used) for mapping between Entities and DTOs.
* **SLF4J + Log4j2** (or alternative) for logging.
* **Reflections** (if used for classpath scanning).
* **Jakarta Servlet API** and **JSTL** (if JSP or view templates are used).
* **Spring Transaction Management** with `HibernateTransactionManager`.
* **Docker Image**: may use an embedded server or external Tomcat depending on packaging.

Adjust versions and dependencies in your POMs accordingly.

---

## Project Structure

Maven multi-module parent project:

```
hotel-parent/
├── hotel-app/             # Web module 
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/      
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   │     
│   └── pom.xml
├── hotel-core/            # Business logic: services, validation, high-level models
│   └── pom.xml
├── hotel-common/          # Utilities: constants, custom exceptions
│   └── pom.xml
├── hotel-infrastructure/  # Data access layer: DAO/repositories with Hibernate
│   └── pom.xml
├── build-tools/           # Checkstyle configuration and other build plugins
│   └── pom.xml
└── pom.xml                # Parent POM with properties and module definitions
```
---

## Modules

* **hotel-app**: Main web module. Contains entry points (controllers), Spring JavaConfig (no Boot), JWT filter, MVC setup.
* **hotel-core**: Business logic and services. Interfaces and implementations for domain rules.
* **hotel-common**: Shared utilities, DTOs, constants, exceptions, MapStruct mappers.
* **hotel-infrastructure**: Data access layer: Hibernate configuration, DAOs/repositories, DataSource bean, transaction manager.
* **build-tools**: Checkstyle rules and possibly other build-related configurations (code generation, etc.).

---

## Configuration

### Environment Variables

When running (especially via Docker Compose), the following environment variables are expected:

* `MYSQL_DATABASE`: name of the database.
* `MYSQL_ROOT_PASSWORD`: root password for MySQL.
* `MYSQL_USER`: database username.
* `MYSQL_PASSWORD`: database user password.
* `DB_HOST`: database host (e.g., `mysql-db` in Docker Compose).
* `DB_PORT`: database port (usually 3306).
* `DB_NAME`: name of the database (same as `MYSQL_DATABASE`).
* `DB_USERNAME`: database username (same as `MYSQL_USER`).
* `DB_PASSWORD`: database password (same as `MYSQL_PASSWORD`).
* `JWT_SECRET_KEY`: secret key for signing JWT tokens.
* Additional variables as needed (e.g., email service configs, external API keys).

Example `.env` file next to `docker-compose.yml`:

```dotenv
# MySQL
MYSQL_DATABASE=hoteldb
MYSQL_ROOT_PASSWORD=secret_root_pwd
MYSQL_USER=hoteluser
MYSQL_PASSWORD=secret_user_pwd

# Application
DB_HOST=mysql-db
DB_PORT=3306
DB_NAME=hoteldb
DB_USERNAME=hoteluser
DB_PASSWORD=secret_user_pwd
JWT_SECRET_KEY=your_jwt_secret_key_here
```

### Application Properties

In `hotel-app/src/main/resources/application.properties`:

```properties
# DataSource settings
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.username=${DB_USERNAME}
db.password=${DB_PASSWORD}

# Hibernate settings
hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
hibernate.hbm2ddl.auto=validate      # or update/none depending on your approach
hibernate.show_sql=true

# JWT settings
jwt.secret=${JWT_SECRET_KEY}
jwt.expirationMs=86400000             # token lifetime in milliseconds

# Server port (if using embedded server)
# server.port=8080
```

> **Note**: Avoid using `hibernate.hbm2ddl.auto=create` or `update` in production. It is recommended to manage schema changes via migrations or manual scripts.

### SQL Initialization Scripts

Place SQL scripts for initial schema and data in a local `sql/` directory. Docker Compose mounts `./sql` into the MySQL container’s `docker-entrypoint-initdb.d`, so scripts run on first startup.

If you prefer migration tools like Flyway or Liquibase, include migration configuration in hotel-infrastructure and document it.

---

## Building and Running

### Local Build

To run the application locally (without Docker):

1. Build the modules:

   ```bash
   mvn clean install
   ```
2. The web module (`hotel-app`) produces a WAR or standalone JAR (if configured with embedded server). Deploy WAR to your servlet container (e.g., Tomcat) or run the JAR:

    * If using embedded: `java -jar hotel-app/target/hotel-app.jar`.
    * If WAR: deploy to Tomcat and configure context path.
3. Ensure a MySQL instance is running locally with matching credentials/environment variables. Update `application.properties` or set environment variables accordingly.
4. Start the application and verify connectivity.

### Running with Docker Compose

A `docker-compose.yml` file in the project root defines services for MySQL and the application.

Example `docker-compose.yml`:

```yaml
version: '3.8'

services:
  mysql-db:
    image: mysql:8.0
    container_name: hotel-mysql
    environment:
      MYSQL_DATABASE: ${MYSQL_DATABASE}
      MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
      MYSQL_USER: ${MYSQL_USER}
      MYSQL_PASSWORD: ${MYSQL_PASSWORD}
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
      - ./sql:/docker-entrypoint-initdb.d
    networks:
      - hotel-network
    command: --default-authentication-plugin=mysql_native_password

  hotel-app:
    build: .
    container_name: hotel-app
    ports:
      - "8080:8080"
    environment:
      - DB_HOST=${DB_HOST}
      - DB_PORT=${DB_PORT}
      - DB_NAME=${DB_NAME}
      - DB_USERNAME=${DB_USERNAME}
      - DB_PASSWORD=${DB_PASSWORD}
      - JWT_SECRET_KEY=${JWT_SECRET_KEY}
    depends_on:
      - mysql-db
    networks:
      - hotel-network
    volumes:
      - ./logs:/usr/local/tomcat/logs  # if deploying WAR in Tomcat

volumes:
  mysql_data:

networks:
  hotel-network:
    driver: bridge
    ipam:
      config:
        - subnet: 172.31.0.0/16
```

#### Steps

1. Create a `.env` file alongside `docker-compose.yml` with required variables (see [Environment Variables](#environment-variables)).
2. Build and start containers:

   ```bash
   mvn clean install -DskipTests
   
   docker-compose up -d --build
   ```
3. Monitor logs:

   ```bash
   docker-compose logs -f hotel-app
   ```
4. After startup, the application should be accessible at `http://localhost:8080/` (or with context path if deployed as WAR).
5. To stop:

   ```bash
   docker-compose down
   ```
6. To reset database (remove volume):

   ```bash
   docker volume rm hotel-parent_mysql_data
   ```

   Adjust volume name as needed.

> Tip: For local development, you can connect your IDE to the MySQL container at `localhost:3306`, and run the application from the IDE with the same environment settings.

---

## Testing

Write tests in each module:

* **Unit Tests**: for services, utilities, mappers (JUnit 5 + Mockito).
* **Security Tests**: verify authentication, authorization, and roles with mock users.
* **Checkstyle**: configured to run on the `validate` phase; build fails on style violations.

Run tests:

```bash
mvn test
```

Optionally configure profiles or test-specific properties (e.g., `application-test.properties`) for integration tests.

---

## Checkstyle and Build Tools

The `build-tools` module contains Checkstyle configuration:

* Maven Checkstyle Plugin runs during `validate` phase.
* Rule file `checkstyle.xml` located in `build-tools/src/main/resources`.

Parent POM snippet:

```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-checkstyle-plugin</artifactId>
  <version>${checkstyle.version}</version>
  <configLocation>
    <configLocation>build-tools/src/main/resources/checkstyle.xml</configLocation>
    <consoleOutput>true</consoleOutput>
    <failsOnError>true</failsOnError>
  </configuration>
</plugin>
```

Include other build plugins as needed (e.g., code generation, documentation).

---

## API Examples

Below are curl examples for common operations. Adjust endpoints and payloads based on your actual API.

```bash
# User Registration
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"user1","password":"password123","email":"user1@example.com"}'

# User Login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user1","password":"password123"}'
# Response contains JWT token: use in subsequent requests as Authorization: Bearer <token>

# Fetch Rooms (ROLE_USER or ROLE_ADMIN)
curl -X GET http://localhost:8080/rooms \
  -H "Authorization: Bearer <token>"

# Import Guests (ROLE_ADMIN)
curl -X POST http://localhost:8080/guests/import \
  -H "Authorization: Bearer <admin-token>" \
  -F file=@guests.csv
```

Include a Postman collection or other documentation if desired.

---

## Contacts

If you have questions or need support:

* **Email**: [antonbut48@gmail.com](mailto:your.email@example.com)