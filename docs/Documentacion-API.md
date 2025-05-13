# Documentación Customers API REST

A continuación se presentan los endpoints disponibles en la API REST de clientes, junto con ejemplos de uso y detalles técnicos.

## 📁 Interfaces

En esta sección se describen las interfaces de la API REST, 
que definen los contratos para la comunicación entre el cliente y el servidor.

### 📘 `CustomerCreateRequest`

Objeto enviado para crear un nuevo cliente.

| Campo            | Tipo   | Requerido | Restricciones                                       |
| ---------------- | ------ | --------- | --------------------------------------------------- |
| `firstName`      | string | ✅         | ≤ 50 caracteres, no puede ser solo espacios (`\S`)  |
| `secondName`     | string | ❌         | ≤ 50 caracteres                                     |
| `firstLastName`  | string | ✅         | ≤ 50 caracteres, no puede ser solo espacios (`\S`)  |
| `secondLastName` | string | ❌         | ≤ 50 caracteres                                     |
| `email`          | string | ✅         | ≤ 100 caracteres, no puede ser solo espacios (`\S`) |
| `address`        | string | ✅         | ≤ 255 caracteres, no puede ser solo espacios (`\S`) |
| `phone`          | string | ✅         | 7–20 caracteres, formato: `^\+?[0-9\-\s()]*$`       |
| `country`        | string | ✅         | 2 letras mayúsculas, formato: `^[A-Z]{2}$`          |

---

### 📘 `CustomerUpdateRequest`

Objeto para actualizar parcialmente la información de un cliente.

| Campo     | Tipo   | Requerido | Restricciones                              |
| --------- | ------ | --------- | ------------------------------------------ |
| `email`   | string | ❌         | ≤ 100 caracteres                           |
| `address` | string | ❌         | ≤ 255 caracteres                           |
| `phone`   | string | ❌         | ≤ 20 caracteres                            |
| `country` | string | ❌         | 2 letras mayúsculas, formato: `^[A-Z]{2}$` |

🔹 Todos los campos son opcionales. Solo se actualiza lo que se envíe.

---

### 📘 `CustomerResponse`

Respuesta estándar al consultar un cliente.

| Campo            | Tipo   | Descripción                     |
| ---------------- | ------ | ------------------------------- |
| `id`             | int64  | Identificador único del cliente |
| `firstName`      | string | Primer nombre                   |
| `secondName`     | string | Segundo nombre                  |
| `firstLastName`  | string | Primer apellido                 |
| `secondLastName` | string | Segundo apellido                |
| `email`          | string | Correo electrónico              |
| `address`        | string | Dirección física                |
| `phone`          | string | Número de teléfono              |
| `country`        | string | Código de país (2 letras)       |
| `demonym`        | string | Gentilicio (ej. "American")     |

---

### 📘 `CustomerPageResponse`

Respuesta paginada al consultar una lista de clientes.

| Campo   | Tipo  | Descripción                         |
| ------- | ----- | ----------------------------------- |
| `items` | array | Lista de objetos `CustomerResponse` |
| `page`  | int32 | Número de página actual             |
| `size`  | int32 | Cantidad de elementos por página    |
| `total` | int64 | Total de registros encontrados      |

---

## 🔗 Endpoints

| Método | Ruta              | Descripción                              |
| ------ | ----------------- | ---------------------------------------- |
| POST   | `/customers`      | Crear cliente                            |
| GET    | `/customers`      | Obtener lista paginada, filtrar por país |
| GET    | `/customers/{id}` | Obtener cliente por ID                   |
| PUT    | `/customers/{id}` | Actualizar parcialmente cliente          |
| DELETE | `/customers/{id}` | Eliminar cliente                         |

---

### 📘 Query Parameters

Parámetros opcionales utilizados en las consultas de clientes:

| Parámetro | Tipo   | Requerido | Descripción                                       |
| --------- | ------ | --------- | ------------------------------------------------- |
| `country` | string | ❌         | Filtra los clientes por código de país (2 letras) |
| `page`    | int    | ❌         | Número de página para la paginación               |
| `size`    | int    | ❌         | Cantidad de resultados por página                 |

---

## 🛑 Excepciones

A continuación se detallan las excepciones personalizadas manejadas por la API, 
sus posibles causas y el código de estado HTTP esperado:

| Excepción                        | Código HTTP | Descripción                                                              |
| -------------------------------- | ----------- | ------------------------------------------------------------------------ |
| `BadRequestException`            | 400         | Datos de entrada inválidos o mal formados.                               |
| `ConflictException`              | 409         | Conflicto con el estado actual del recurso (e.g., cliente ya existente). |
| `CountryNotFoundException`       | 404         | El país no fue encontrado en el servicio externo.                        |
| `CountryServiceException`        | 503         | Fallo al comunicarse con el servicio externo de países.                  |
| `CustomerAlreadyExistsException` | 409         | Ya existe un cliente con el mismo correo.                                |
| `CustomerNotFoundException`      | 404         | El cliente especificado no fue encontrado.                               |
| `InternalServerErrorException`   | 500         | Error inesperado del servidor.                                           |
| `NotFoundException`              | 404         | Recurso solicitado no encontrado. Puede ser país o cliente.              |
| `PublicException`                | 400–500     | Excepción genérica usada para errores definidos por el dominio.          |

---

## 📗 Ejemplos

1. **Creación de un nuevo cliente**

```json
POST /customers
{
  "firstName": "Jane",
  "secondName": "Mary",
  "firstLastName": "Doe",
  "secondLastName": "Smith",
  "email": "jane@example.com",
  "address": "123 Main Street",
  "phone": "+123456789",
  "country": "US"
}
```

2. **Obtener todos los clientes existentes**

```http
GET /customers
```

3. **Obtener todos los clientes de un país específico**

```http
GET /customers?country=US
```

4. **Obtener un cliente por su ID**

```http
GET /customers/1
```

5. **Actualizar un cliente existente (solo email, dirección, teléfono o país)**

```json
PUT /customers/1
{
  "email": "new.email@example.com",
  "address": "456 Another Street",
  "phone": "+987654321",
  "country": "CA"
}
```

> **Nota**: El `PUT` permite update parcial. Solo se actualizan los campos enviados.
> 
> **Actualizar un cliente existente (solo email)**
> ```json
> PUT /customers/1
> {
>   "email": "new.email@example.com"
> }
> ```

6. **Eliminar un cliente por su identificador**

```http
DELETE /customers/1
```
