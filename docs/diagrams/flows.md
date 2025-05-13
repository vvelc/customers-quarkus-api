# Diagramas de Flujo – Customer API

Este documento contiene los principales flujos de interacción de la API de clientes, representados con diagramas Mermaid para facilitar el entendimiento de los procesos clave.

---

## ✅ Flujo 1 – Crear cliente (`POST /customers`)

```mermaid
sequenceDiagram
    participant Client
    participant Controller as CustomerController
    participant Service as CustomerService
    participant Adapter as CountryValidationRestAdapter
    participant Repo as CustomerRepository
    participant DB as PostgreSQL

    Client->>Controller: POST /customers
    Controller->>Service: createCustomer(request)
    Service->>Adapter: fetchCountryInfo(code)
    Adapter->>Adapter: call restcountries.com
    Adapter-->>Service: CountryInfo
    Service->>Repo: saveCustomer(entity)
    Repo->>DB: INSERT INTO customers
    DB-->>Repo: OK
    Repo-->>Service: savedCustomer
    Service-->>Controller: CustomerResponse
    Controller-->>Client: 201 Created + Location header
```

---

## ✅ Flujo 2 – Obtener cliente por ID (`GET /customers/{id}`)

```mermaid
sequenceDiagram
    participant Client
    participant Controller
    participant Service
    participant Repo
    participant DB

    Client->>Controller: GET /customers/42
    Controller->>Service: getCustomerById(42)
    Service->>Repo: findById(42)
    Repo->>DB: SELECT * FROM customers WHERE id=42
    DB-->>Repo: Customer entity
    Repo-->>Service: Customer
    Service-->>Controller: CustomerResponse
    Controller-->>Client: 200 OK + Body
```

---

## ✅ Flujo 3 – Listar clientes por país (`GET /customers?country=DO`)

```mermaid
sequenceDiagram
    participant Client
    participant Controller
    participant Service
    participant Repo
    participant DB

    Client->>Controller: GET /customers?country=DO&page=0&size=10
    Controller->>Service: getCustomersFiltered("DO")
    Service->>Repo: findByCountry("DO", PageRequest(0, 10))
    Repo->>DB: SELECT * FROM customers WHERE country='DO' LIMIT 10 OFFSET 0
    DB-->>Repo: Customer[]
    Repo-->>Service: List<Customer>
    Service ->>Repo: countByCountry("DO")
    Repo->>DB: SELECT COUNT(*) FROM customers WHERE country='DO'
    DB-->>Repo: 40
    Repo-->>Service: 40
    Service-->>Controller: PageResponse<CustomerResponse> (
    Controller-->>Client: 200 OK + Paged Response (CustomerResponse[], page 0, size 10, total 40)
```

---

## ✅ Flujo 4 – Actualizar cliente (`PUT /customers/{id}`)

```mermaid
sequenceDiagram
    participant Client
    participant Controller
    participant Service
    participant Adapter as CountryValidationRestAdapter
    participant Repo
    participant DB

    Client->>Controller: PUT /customers/42
    Controller->>Service: updateCustomer(42, dto)
    Service->>Repo: findById(42)
    Repo->>DB: SELECT * FROM customers WHERE id=42
    DB-->>Repo: Customer
    Repo-->>Service: existingCustomer
    Service->>Adapter: fetchCountryInfo(newCountryCode)
    Adapter-->>Service: CountryInfo
    Service->>Repo: update(entity)
    Repo->>DB: UPDATE customers SET ...
    DB-->>Repo: OK
    Repo-->>Service: updatedCustomer
    Service-->>Controller: CustomerResponse
    Controller-->>Client: 200 OK
```

---

## ✅ Flujo 5 – Eliminar cliente (`DELETE /customers/{id}`)

```mermaid
sequenceDiagram
    participant Client
    participant Controller
    participant Service
    participant Repo
    participant DB

    Client->>Controller: DELETE /customers/42
    Controller->>Service: deleteCustomer(42)
    Service->>Repo: findById(42)
    Repo->>DB: SELECT * FROM customers WHERE id=42
    DB-->>Repo: Customer
    Repo-->>Service: customer
    Service->>Repo: delete(customer)
    Repo->>DB: DELETE FROM customers WHERE id=42
    DB-->>Repo: OK
    Repo-->>Service: true
    Service-->>Controller: true
    Controller-->>Client: 204 No Content
```

---

## ✅ Flujo 6 – Manejo de errores (valores inválidos, duplicados, no encontrado)

```mermaid
sequenceDiagram
    participant Client
    participant Controller
    participant Service
    participant Repo
    participant DB
    participant Handler as GlobalExceptionMapper

    Client->>Controller: POST /customers (invalid payload)
    Controller->>Service: createCustomer(invalid)
    Service-->>Controller: throws ConstraintViolationException
    Controller-->>Handler: Exception
    Handler-->>Client: 400 Bad Request + details

    Client->>Controller: POST /customers (duplicate email)
    Controller->>Service: createCustomer(duplicate)
    Service->>Repo: isEmailAlreadyUsed()
    Repo->>DB: SELECT * WHERE email=?
    DB-->>Repo: existing
    Repo-->>Service: exists
    Service-->>Controller: throws ConflictException
    Controller-->>Handler: Exception
    Handler-->>Client: 409 Conflict

    Client->>Controller: GET /customers/999
    Controller->>Service: getCustomerById(999)
    Service->>Repo: findById(999)
    Repo->>DB: SELECT * WHERE id=999
    DB-->>Repo: null
    Repo-->>Service: null
    Service-->>Controller: throws NotFoundException
    Controller-->>Handler: Exception
    Handler-->>Client: 404 Not Found
```

---

## 🧭 Visualización

Para visualizar los diagramas, puedes pegar su codigo en  [https://mermaid.live](https://mermaid.live), o abrirlos directamente en GitHub Pages, VS Code o herramientas compatibles.

---

## ✍️ Autor

Víctor Velázquez Cid [https://github.com/vvelc](https://github.com/vvelc)
