# Task Manager - Spring Boot REST API

A simple RESTful backend for managing tasks built with Java and Spring Boot.

## Tech Stack

- Java 17
- Spring Boot 4
- PostgreSQL

## Features

- RESTful API - create, read, update, and delete tasks
- Layered architecture (Controller - Service - Repository)
- DTO pattern for API contracts
- Request validation
- Global exception handling
- PostgreSQL database integration

## Getting Started

### Prerequisites

- Java JDK 17+
- Maven 3.5+
- PostgreSQL 9.1+

### Installation

```
git clone https://github.com/erichamk/task-manager-api.git
cd task-manager-api

mvn spring-boot:run
```

## API Endpoints

- `POST /api/tasks`: Create a new task.

```
Request:
{
    "title": "",
    "description": ""
}

Response:
{
    "id": 1,
    "title": "",
    "description": "",
    "completed": false
}
```

- `GET /api/tasks`: Get all tasks.

```
Response:

[
    {
        "id": 1,
        "title": "",
        "description": "",
        "completed": false
    }
]
```

- `GET /api/tasks/{id}`: Get task by ID.

```
Response:
{
    "id": 1,
    "title": "",
    "description": "",
    "completed": false
}
```

- `PUT /api/tasks/{id}`: Update task by ID.

```
Request:
{
    "title": "",
    "description": ""
}
```

- `DELETE /api/tasks/{id}`: Delete task by ID.