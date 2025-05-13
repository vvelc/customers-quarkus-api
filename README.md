# Backend Developer Test – Customer API

## ✨ Descripción general

API RESTful para la gestión de clientes, construida en Java con Quarkus. Permite crear, consultar, actualizar y eliminar clientes, con validaciones, cobertura de pruebas, observabilidad, y CI/CD completo.

---

## 🔗 Endpoints principales

| Método | Ruta              | Descripción                              |
| ------ | ----------------- | ---------------------------------------- |
| POST   | `/customers`      | Crear cliente                            |
| GET    | `/customers`      | Obtener lista paginada, filtrar por país |
| GET    | `/customers/{id}` | Obtener cliente por ID                   |
| PUT    | `/customers/{id}` | Actualizar parcialmente cliente          |
| DELETE | `/customers/{id}` | Eliminar cliente                         |

### Parametros de consulta

| Parámetro | Tipo    | Descripción                          |
| ---------- | ------- | ------------------------------------ |
| country    | String  | Filtrar clientes por país            |
| page       | Integer | Número de página para paginación      |
| size       | Integer | Tamaño de página para paginación      |

Para más detalles, visistar [Documentacion-API.md](./docs/Documentacion-API.md).

Puedes probar estos endpoints utilizando:
* [Swagger UI](http://localhost:8080/q/swagger-ui), disponible al ejecutar el proyecto. Ver sección [Cómo ejecutar localmente](#rocket-cómo-ejecutar-localmente) para más detalles.
* [Postman](https://www.postman.com/) o [Insomnia](https://insomnia.rest/) con el archivo [Open API](http://localhost:8080/q/openapi.json) generado automáticamente al ejecutar el proyecto.

---

## 🪜 Tecnologías y dependencias clave

* **Quarkus 3.2**
* **Hibernate ORM Panache + Validator**
* **PostgreSQL (producción)**, **H2 (tests)**
* **Flyway** para migraciones
* **Micrometer + Prometheus** para métricas
* **OpenAPI + Swagger UI** para documentación
* **Lombok + JetBrains Annotations** para boilerplate
* **JUnit 5 + Mockito + Rest Assured** para testing
* **Jacoco + Codecov + SonarQube** para cobertura y calidad de código

---

## :rocket: Cómo ejecutar localmente

### Requisitos:

* Java 17
* Maven 3.8+
* Docker + Docker Compose (para modo contenedor)

### Desarrollo local:

```bash
./mvnw quarkus:dev
```

### Docker Compose:

```bash
./mvnw clean package -DskipTests

docker-compose up --build
```

La aplicación estará disponible en: [http://localhost:8080](http://localhost:8080)

**Herramientas y recursos**
* Swagger UI: [http://localhost:8080/q/swagger-ui](http://localhost:8080/q/swagger-ui)
* Open API: [http://localhost:8080/q/openapi.json](http://localhost:8080/q/openapi.json)
* Métricas: [http://localhost:8080/q/metrics](http://localhost:8080/q/metrics)
* Health: [http://localhost:8080/q/health](http://localhost:8080/q/health)
* Health (readiness): [http://localhost:8080/q/health/ready](http://localhost:8080/q/health/ready)
* Health (liveness): [http://localhost:8080/q/health/live](http://localhost:8080/q/health/live)
---

## 🔍 Pruebas

### ✅ Pruebas unitarias
- Crear cliente con datos válidos
- Validación de email invalido
- Validación de país no permitido o no existente
- Cliente duplicado (email)
- Comportamiento del servicio ante cliente inexistente

### ✅ Pruebas de integración
- Crear → Obtener → Eliminar cliente
- Crear múltiples clientes y filtrar por país
- Validación de datos de entrada
- Validación de comportamiento parcial del PUT
- Verificación de headers como `Location`
- Errores esperados: 400, 404, 409
- Asegurar rollback en errores (transacciones)

### Ejecutar pruebas unitarias + integración:

```bash
./mvnw clean test
```

### Ejecutar pruebas unitarias

```bash
./mvnw clean test -Dtest='!*IT' 
```

### Ejecutar pruebas de integración:

```bash
./mvnw clean test -Dtest='*IT' 
```

### Cobertura:

```bash
./mvnw jacoco:report
```

Reporte disponible en: `target/site/jacoco/index.html`

---

## 🔠 Observabilidad

* Health: `/q/health`, `/q/health/ready`, `/q/health/live`
* Métricas Prometheus: `/q/metrics`

---

## 🌐 Arquitectura y organización

### Resumen de arquitectura
* Patrón inspirado en Clean Architecture / Hexagonal
* Separación en `application`, `domain`, `infrastructure`, `interface_`
* Mapper DTO <-> Entity manualizado
* Patrón repositorio para la persistencia
* Excepciones controladas y documentadas
* Enfoque DevOps First

### 📘 Decisiones de diseño

Las decisiones de diseño y justificación tecnológica están documentadas en el archivo [ADR-001.md](./docs/ADR-001.md).

### 📜 Documentación y diagramas

* [Documentación de la API REST](./docs/Documentacion-API.md)
* [Documentación de decisiones arquitectónicas](./docs/ADR-001.md)
* [Diagrama de Arquitectura](docs/diagrams/component.md)
* [Diagrama de Flujo de Datos](docs/diagrams/flow.md)

### 🗂 Estructura de carpetas

```bash
com.vvelc.customers
├── application
│   ├── dto
│   ├── port.outbound
│   └── service
├── domain
│   ├── model
│   ├── exception
│   └── repository
├── infrastructure
│   ├── adapter.rest  ---> connection to external API
│   ├── exception     ---> exception mappers
│   ├── observability
│   ├── persistence
│   │   ├── entity
│   │   ├── mapper
│   │   ├── panache
│   │   └── repository
│   └── metrics
└── interface_
    ├── controller
    ├── dto
    │   └── shared
    └── mapper
```

### 📦 Paquete `com.vvelc.customers`
* `model`: Clases de dominio que representan entidades del negocio.
* `entity`: Clases que representan las entidades de la base de datos.
* `dto`: Data Transfer Objects para la comunicación entre capas.
* `panache`: Clases que extienden PanacheEntityBase para la persistencia.
* `repository`: Interfaces para la persistencia de datos.
* `mapper`: Clases para la conversión entre DTOs y entidades.
* `service`: Implementaciones de la lógica de negocio.
* `controller`: Controladores REST para manejar las solicitudes HTTP.
* `adapter`: Adaptadores para la comunicación con servicios externos.
* `port.outbound`: Interfaces para la comunicación con el dominio.
* `exception`: Excepciones específicas del negocio.
* `metrics`: Clases para la recolección de métricas.
* `observability`: Clases para la configuración de la observabilidad.

---

## Base de datos
## 🗄️ Migraciones y datos iniciales

Para garantizar la **integridad del esquema** y un punto de partida reproducible:

1. **Flyway**
  - Los scripts de migración viven en `src/main/resources/db/migration/`
  - Al arrancar la aplicación con `quarkus.flyway.migrate-at-start=true`, se aplican automáticamente.
    - Ejemplo de script inicial:
      ```sql
      -- V1__init.sql
      CREATE TABLE customers (
        id BIGSERIAL PRIMARY KEY,
        first_name VARCHAR(100) NOT NULL,
        second_name VARCHAR(100),
        first_last_name VARCHAR(100) NOT NULL,
        second_last_name VARCHAR(100),
        email VARCHAR(255) NOT NULL,
        address VARCHAR(255) NOT NULL,
        phone VARCHAR(50) NOT NULL,
        country VARCHAR(50) NOT NULL,
        demonym VARCHAR(100) NOT NULL,
        CONSTRAINT uk_customer_email UNIQUE (email)
      );
    
      CREATE INDEX idx_customer_country ON customers(country);
      CREATE INDEX idx_customer_email ON customers(email);
      ```
2. **Seed de datos**
  - Coloca un script `R__seed_customers.sql` en la misma carpeta con datos de ejemplo:
    ```sql
    -- R__seed_customers.sql
    INSERT INTO customers (first_name, second_name, first_last_name, second_last_name,
        email, address, phone, country, demonym)
    VALUES
    ('Alice',  'Marie',    'Walker',  "Jackson", 'alice.walker@example.com',
        '123 Maple St, Springfield, IL, 62704', '+1-217-555-0101', 'US', 'American'),
    ('Carl',  'Mark',    'Parker',  "Houston", 'carl.parker@example.com',
        '123 Syrup St, Pottsville, AL, 12345', '+1-823-999-0202', 'US', 'American'),
    ```
  - Se aplica siempre después de las migraciones, manteniendo consistencia en todos los entornos.

> 🔧 **Uso**
> ```bash
> ./mvnw clean package -DskipTests
> ./mvnw quarkus:dev
> # Flyway correrá migraciones + seed automáticamente
> ```

---

## 🔨 DevOps / CI

* GitHub Actions:
  * PENDIENTE BADGE DE ACTION
  * Build + Test
  * Verificación de cobertura
  * Análisis estático con SonarQube
* Codecov: \[Badge]
* SonarQube: 
  * [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=vvelc_customers-quarkus-api&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=vvelc_customers-quarkus-api)
  * [![Bugs](https://sonarcloud.io/api/project_badges/measure?project=vvelc_customers-quarkus-api&metric=bugs)](https://sonarcloud.io/summary/new_code?id=vvelc_customers-quarkus-api)
  * [![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=vvelc_customers-quarkus-api&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=vvelc_customers-quarkus-api)
  * [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=vvelc_customers-quarkus-api&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=vvelc_customers-quarkus-api)
  * [![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=vvelc_customers-quarkus-api&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=vvelc_customers-quarkus-api)

> 🔧 **Ejecutar tu pipeline GitHub Actions localmente
act push -j build-and-test**
> ```bash
> act push -j build-and-test
> ```

---

## 📅 Roadmap / mejoras futuras

* Paginación avanzada (ordenamiento, filtros múltiples)
* Tests de contrato (OpenAPI)
* Implementar autorización por API Key o JWT
* Implementar caché (Redis)
* Agregar propiedades createdAt y updatedAt a la entidad para auditoría
* Despliegue automatizado con Railway / Render

---

## 📝 Licencia

[MIT](./LICENSE)

---

## 🧑‍💻 Autor

Victor Velazquez Cid |
[https://github.com/vvelc](https://github.com/vvelc)
