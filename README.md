# home-thermostat-api

Home Thermostat API — REST API для системы умного термостата, которое позволяет управлять домами, комнатами, термостатами и отслеживать температуру.

## Tech Stack

- Java 21
- Spring Boot 4.0.0
- Spring Security + JWT
- PostgreSQL
- Apache POI (Excel reports)
- Telegram Bot API
- Swagger/OpenAPI

## Quick Start

### 1. Clone and configure

```bash
git clone https://github.com/DimaChekashov/home-thermostat-api.git
cd home-thermostat-api

# Create config from template
cp /src/main/resources/secret.properties.example /src/main/resources/secret.properties
# Edit secret.properties with your values
```

### 2. Start database

```bash
# Start PostgreSQL container
docker compose up -d postgres

# Check if running
docker compose ps
```

### 3. Run app

```bash
mvn spring-boot:run
```

### 4. Open Swagger

http://localhost:8080/swagger-ui/index.html

### 5. Telegram Bot

https://t.me/home_thermostat_bot

## Docker Commands

```bash
# Start database
docker compose up -d postgres

# Stop database
docker compose stop postgres

# Restart database
docker compose restart postgres

# Delete database (with data)
docker compose down -v

# Connect to database
docker exec -it thermostat-db psql -U postgres -d rest_api
```

## File Import Format

### Excel Template (.xlsx)

| value | timestamp | source |
|:---|:---|:---|
| 23.5 | 2026-04-26 10:00:00 | SENSOR |
| 23.3 | 2026-04-26 10:30:00 | SENSOR |
| 23.7 | 2026-04-26 11:00:00 | SENSOR |
| 24.0 | 2026-04-26 11:30:00 | SIMULATOR |
| 23.8 | 2026-04-26 12:00:00 | MANUAL |

## API Endpoints

### Authentication

- POST - `/api/auth/register` Register a new user
- POST - `/api/auth/login` Login with username and password
- POST - `/api/auth/refresh` Refresh access token using refresh token
- POST - `/api/auth/logout` Logout and revoke tokens

### User

- GET - `/api/users/me` Get currently authenticated user info

### Home

- GET - `/api/homes` Get all homes of current user
- GET - `/api/homes/{id}` Get home by ID
- POST - `/api/homes` Create a new home
- PUT - `/api/homes/{id}` Update existing home
- DELETE - `/api/homes/{id}` Delete home

### Room

- GET - `/api/homes/{homeId}/rooms` Get all rooms in a home
- GET - `/api/homes/{homeId}/rooms/{id}` Get room by ID
- POST - `/api/homes/{homeId}/rooms` Create a new room in a home
- PUT - `/api/homes/{homeId}/rooms/{id}` Update room
- DELETE - `/api/homes/{homeId}/rooms/{id}` Delete room

### Thermostat

- GET - `/api/thermostats/{id}` Get thermostat by ID
- GET - `/api/thermostats/room/{roomId}` Get thermostat by room ID
- PUT - `/api/thermostats/{id}/target?value={temp}` Set target temperature
- PUT - `/api/thermostats/{id}/mode?mode={mode}` Set thermostat mode (HEAT/COOL/OFF)
- PUT - `/api/thermostats/{id}/status?status={status}` Set thermostat status (ACTIVE, INACTIVE)

### TemperatureReading

- GET - `/api/thermostats/{thermostatId}/temperatures/readings?minutes={n}` Get temperature readings for period
- GET - `/api/thermostats/{thermostatId}/temperatures/current` Get current temperature

### Device Integration (for IoT sensors)

- POST - `/api/devices/{thermostatId}/temperature?value={temp}` Report temperature from sensor

### Reports (Excel)

- GET - `/api/reports/summary` Download summary Excel report for all homes
- GET - `/api/reports/temperature/{homeId}?days={n}` Download temperature Excel report for a home

### Data Import

- POST - `/api/import/excel/{thermostatId}` Upload Excel file with temperature data

# Сборка и Деплой

### Сборка и запуск

```bash
# 1. Собрать JAR
mvn clean package -DskipTests

# 2. Собрать и запустить все контейнеры
docker compose up -d --build

# 3. Проверить логи
docker compose logs app

# 4. Проверить работу
curl http://localhost:8080/swagger-ui/index.html
```

### Деплоя на сервер

```bash
# На сервере:
git clone https://github.com/DimaChekashov/home-thermostat-api.git
cd home-thermostat-api

# Copy env template
cp .env.example .env

# Edit with real values
nano .env

# Build JAR + Docker image + start containers
docker compose up -d --build
```
