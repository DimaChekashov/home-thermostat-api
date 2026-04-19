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

### User

- GET - `/api/users/{id}` Get user by ID
- GET - `/api/users/{id}/homes` Get all homes for a user
- POST - `/api/users` Create a new user

### Home

- GET - `/api/homes/{id}` Get home by ID
- POST - `/api/homes` Create a new home
- POST - `/api/homes/{id}` Update existing home
- GET - `/api/homes/{id}/rooms` Get all rooms in a home

### Room

- GET - `/api/rooms/{id}` Get room by ID
- GET - `/api/rooms/{id}/temperatures` Get all temperature readings for a room
- POST - `/api/rooms/{id}/target?target={value}` Update room's target temperature
- POST - `/api/rooms?homeId={id}` Create a new room in a home

### TemperatureReading

- GET - `/api/temperatures` Get all temperature readings
- GET - `/api/temperatures/room/{roomId}` Get temperature readings by room ID
- GET - `/api/temperatures/home/{homeId}` Get temperature readings by home ID
- POST - `/api/temperatures?roomId={id}` Create new temperature reading
