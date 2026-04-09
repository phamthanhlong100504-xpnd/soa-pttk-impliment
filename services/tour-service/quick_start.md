# Tour Service Quickstart (Docker)

## 1) Build image

Run from repository root:

```bash
docker build -t tour-service:local services/tour-service
```

Optional: set build-time defaults for DB (can still be overridden at runtime):

```bash
docker build -t tour-service:local services/tour-service \
  --build-arg DB_HOST=host.docker.internal \
  --build-arg DB_PORT=3306 \
  --build-arg DB_NAME=tour_db \
  --build-arg DB_USER=root \
  --build-arg DB_PASSWORD=12345678
```

## 2) Run container

### Option A: pass env directly

```bash
docker run --name tour-service \
  -p 5002:5002 \
  -e DB_HOST=host.docker.internal \
  -e DB_PORT=3306 \
  -e DB_NAME=tour_db \
  -e DB_USER=root \
  -e DB_PASSWORD=12345678 \
  tour-service:local
```

### Option B: use env file

```bash
docker run --name tour-service \
  -p 5002:5002 \
  --env-file services/tour-service/.env \
  tour-service:local
```

Notes:

- On Windows, if MySQL runs on host machine, prefer `DB_HOST=host.docker.internal`.
- Runtime env (`-e`, `--env-file`) overrides build-time `--build-arg` defaults.

## 3) Quick API test

### Health check

```bash
curl http://localhost:5002/health
```

Expected:

```json
{ "status": "ok" }
```

### Get tours

```bash
curl "http://localhost:5002/tours"
```

Expected shape:

```json
{
  "success": true,
  "data": [],
  "message": "OK"
}
```

### Filter tours

```bash
curl "http://localhost:5002/tours?duration_days=3&min_price=1000000&max_price=5000000"
```

## 4) Useful commands

```bash
# View logs
docker logs -f tour-service

# Stop and remove container
docker stop tour-service && docker rm tour-service

# If container name already exists
docker rm -f tour-service
```

## 5) Common errors

- `Access denied for user ...`: DB_USER/DB_PASSWORD is wrong or user has no permission on `tour_db`.
- `Communications link failure`: DB_HOST/DB_PORT is wrong or MySQL is not reachable.
- `Public Key Retrieval is not allowed`: ensure JDBC URL includes `allowPublicKeyRetrieval=true` (already configured in this service).
