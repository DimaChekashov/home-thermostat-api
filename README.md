# home-thermostat-api

## Setup

Run project:

```bash
mvn spring-boot:run
```

Swagger: /swagger-ui/index.html

### Docker:

```bash
# Build docker compose
docker compose up -d

# Start db container
docker start thermostat-db

# Stop db container
docker stop thermostat-db

# Restart db container
docker restart thermostat-db

# Delete db container
docker rm -f thermostat-db

# Connect to db container
docker exec -it thermostat-db psql -U postgres -d rest_api
```

### Docker compose:

```bash
# Start all services
docker-compose up -d

# start postgres service
docker-compose up -d postgres

# Stop all services
docker-compose down

# Stop and delete volumes
docker-compose down -v

# Show postgres service logs
docker-compose logs postgres

# Restart postgres service
docker-compose restart postgres
```

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

- POST - `/api/devices/{thermostatId}/temperature?value={temp}` - Report temperature from sensor
