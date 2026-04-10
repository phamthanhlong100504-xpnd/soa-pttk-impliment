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

### 10 curl samples to test the API

#### 1. Health check

```bash
curl http://localhost:5002/health
```

#### 2. Get all tours

```bash
curl "http://localhost:5002/api/v1/tours"
```

Expected shape:

```json
{
  "success": true,
  "data": [],
  "message": "OK"
}
```

#### 3. Search by free text q

```bash
curl "http://localhost:5002/api/v1/tours?q=Da%20Nang"
```

#### 4. Search by normalized slug text

```bash
curl "http://localhost:5002/api/v1/tours?q=da-nang"
```

#### 5. Filter by departure city

```bash
curl "http://localhost:5002/api/v1/tours?departures=Ha%20Noi"
```

#### 6. Filter by start date

```bash
curl "http://localhost:5002/api/v1/tours?start_date=2026-05-10"
```

#### 7. Filter by duration

```bash
curl "http://localhost:5002/api/v1/tours?duration_days=4"
```

#### 8. Filter by price range

```bash
curl "http://localhost:5002/api/v1/tours?min_price=6000000&max_price=8000000"
```

#### 9. Combine search with departure and duration

```bash
curl "http://localhost:5002/api/v1/tours?q=da-nang&departures=Ha%20Noi&duration_days=4"
```

#### 10. Get tour detail by slug with header tour ID

```bash
curl -H "X-Tour-Id: 40000000-0000-0000-0000-000000000001" "http://localhost:5002/api/v1/tours/da-nang-hoi-an-4d3n"
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
