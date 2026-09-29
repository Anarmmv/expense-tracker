# Expense Tracker

A small personal expense tracker. Lets you log expenses under categories, view a monthly total, and see a breakdown by category.

## Tech Stack

- **Java 21**, **Spring Boot** (Web, Data JPA, Validation)
- **PostgreSQL** — database
- **Liquibase** — versioned database migrations
- **Lombok**
- **JUnit 5 + Mockito** — unit tests
- **Docker / Docker Compose**
- Plain HTML/CSS/JS frontend (`src/main/resources/static/index.html`)

## Project Structure

```
src/main/java/.../
├── controller/     REST endpoints
├── service/        business logic
├── repository/     Spring Data JPA repositories
├── entity/         JPA entities
├── dto/            request/response objects
└── exception/      centralized exception handling

src/main/resources/
├── application.yml
├── static/index.html
└── db/changelog/   Liquibase changelog files
```

## Running the App

### With Docker Compose (recommended)

```bash
docker compose up --build
```

This starts the app and PostgreSQL together. Open: [http://localhost:8080](http://localhost:8080)

To stop:
```bash
docker compose down
```

### Locally (without Docker)

1. Create the database in PostgreSQL:
   ```sql
   CREATE DATABASE expense_db;
   ```
2. Update the `datasource` settings in `src/main/resources/application.yml` to match your environment.
3. Run the app:
   ```bash
   ./gradlew bootRun
   ```

In both cases, tables are created automatically by Liquibase.

## Tests

```bash
./gradlew test
```

## API

Base path: `/api/v1`

### Categories — `/categories`

| Method | Path | Description |
|---|---|---|
| POST | `/categories` | Create a category (body: `{"name": "..."}`) |
| GET | `/categories` | List all categories |
| GET | `/categories/{id}` | Get a category by id |
| PUT | `/categories/{id}` | Rename a category |
| DELETE | `/categories/{id}` | Delete a category (`409` if it still has expenses) |

### Expenses — `/expenses`

| Method | Path | Description |
|---|---|---|
| POST | `/expenses` | Create an expense (body: `{"amount", "description", "expenseDate", "categoryId"}`) |
| GET | `/expenses` | List all expenses |
| GET | `/expenses/{id}` | Get an expense by id |
| PUT | `/expenses/{id}` | Update an expense |
| DELETE | `/expenses/{id}` | Delete an expense |
| GET | `/expenses/category/{categoryId}` | Filter by category |
| GET | `/expenses/range?from=&to=` | Expenses within a date range |
| GET | `/expenses/total?from=&to=` | Total amount for a date range |
| GET | `/expenses/summary?from=&to=` | Breakdown by category |

Date format: `YYYY-MM-DD` (e.g. `2026-09-28`).

### Example requests

```bash
curl -X POST http://localhost:8080/api/v1/categories \
  -H "Content-Type: application/json" \
  -d '{"name":"Food"}'

curl -X POST http://localhost:8080/api/v1/expenses \
  -H "Content-Type: application/json" \
  -d '{"amount":12.50,"description":"Bread","expenseDate":"2026-09-28","categoryId":1}'

curl "http://localhost:8080/api/v1/expenses/summary?from=2026-09-01&to=2026-09-30"
```

## Error Responses

Validation errors and business rule violations return a clear message:

```json
{"status":400,"error":"Validation failed","fields":{"amount":"must be greater than 0"}}
```

```json
{"status":409,"error":"There are still expenses in this category, delete them first"}
```

## Author

[Anarmmv](https://github.com/Anarmmv)
