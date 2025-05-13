# Changelog

## [1.0.0] – 2025-05-13
### Añadido
- CRUD completo de clientes (POST, GET, PUT, DELETE)
- Implementación de PUT parcial
- Paginación y filtrado por país
- Validaciones de datos de entrada con Hibernate Validator
- Conexión a API externa para validación de país
- Creación de excepciones personalizadas
- Manejo de excepciones global (captura y mapeo)
- Migraciones de esquema con Flyway (scripts en `db/migration`)
- Seed de datos inicial (`R__seed_customers.sql`)
- Logging en formato JSON para entornos productivos
- Tests unitarios e integración
- Observabilidad: health checks y métricas Prometheus
- Documentación OpenAPI + Swagger UI
- CI/CD con GitHub Actions, SonarQube y Codecov
- Diagramas de componentes y flujos Mermaid
- Configuración de Docker y Docker Compose
- Configuración de H2 para pruebas
- Configuración de PostgreSQL para desarrollo y producción
- Configuración de Lombok y JetBrains Annotations
- Configuración de Jacoco para cobertura de pruebas
- Configuración de Rest Assured para pruebas de API
- Configuración de Mockito para pruebas unitarias
- Configuración de Micrometer para métricas
- Configuración de OpenAPI para documentación
- Configuración de Swagger UI para documentación interactiva
- Implementación de Health Check y Liveness Check para monitoreo
- Documentación de API en Markdown
- Documentación de decisiones arquitectónicas en Markdown
- Documentación de arquitectura en Markdown
- Documentación de diagramas de flujo en Markdown
- Crear este CHANGELOG.md
- Crear README.md
