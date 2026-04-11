# home-thermostat-api

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
