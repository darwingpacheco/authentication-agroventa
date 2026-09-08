# Authentication Service (WebFlux + Hexagonal)

Servicio de autenticacion reactivo con arquitectura hexagonal para AWS.

## Arquitectura

- `domain`: modelos y puertos del negocio.
- `application`: casos de uso (`LoginUseCase`).
- `infrastructure`: adaptadores R2DBC, JWT, seguridad y configuracion.
- `interfaces`: controlador REST y manejo de errores.

## API

### POST `/api/v1/auth/login`

Request:

```json
{
  "email": "user@agroventa.com",
  "password": "ChangeMe123!"
}
```

Response:

```json
{
  "accessToken": "<jwt>",
  "tokenType": "Bearer",
  "expiresInSeconds": 3600
}
```

### GET `/api/v1/auth/capabilities`

Retorna un `Flux<String>` con capacidades disponibles.

## Variables de entorno

- `R2DBC_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `FLYWAY_JDBC_URL`
- `JWT_SECRET`
- `JWT_EXPIRATION_SECONDS`
- `JWT_ISSUER`
- `SPRING_PROFILES_ACTIVE` (`local` o `aws`)

## Base de datos (AWS RDS PostgreSQL)

Las migraciones Flyway viven en `src/main/resources/db/migration`.

## Ejecutar local

1. Levanta PostgreSQL local con Docker Compose.
2. Usa el perfil `local` para ejecutar la API.

```powershell
Set-Location "C:\Users\T068163\Downloads\Proyecto DevOps\authentication\authentication"
docker compose up -d
Copy-Item .env.example .env
$env:SPRING_PROFILES_ACTIVE="local"
.\mvnw.cmd clean test
.\mvnw.cmd spring-boot:run
```

Perfiles disponibles:

- `src/main/resources/application-local.properties`: desarrollo local
- `src/main/resources/application-aws.properties`: despliegue AWS

## Docker

```powershell
docker build -t authentication:local .
docker run -p 8080:8080 --env-file .env authentication:local
```

## Kubernetes

- Manifiestos en `k8s/`.
- Ajusta `k8s/configmap.yaml` y crea el secret desde `k8s/secret.example.yaml`.

## CI/CD

Pipeline en `.github/workflows/ci-cd.yml`:

1. Build + tests con Maven.
2. Build/push de imagen a ECR.
3. Deploy en EKS con `kubectl`.

