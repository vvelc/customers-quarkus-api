# Diagrama de Componentes – Customer API

Este diagrama presenta la **arquitectura lógica** del servicio siguiendo un enfoque Clean / Hexagonal. Se muestran las **cuatro capas conceptuales** —Interface, Application, Domain y Infrastructure— con sus componentes principales, de modo que la **lógica de negocio permanezca aislada** de la infraestructura y las dependencias fluyan siempre hacia adentro. Las flechas indican **protocolos (HTTP, JDBC, HTTPS)** y la dirección de dependencia, facilitando la comprensión del *flow* y los puntos de inyección.

---

## 🧱 Componentes principales

```mermaid
flowchart TD

    subgraph Domain_Layer [Domain Layer]
        D1["Customer (Model)"]
        D2["CustomerRepository (Port)"]
    end
    
    subgraph Internet [Internet]
        A1["API Consumer"]
    end
    
    subgraph Interface_Layer [Interface Layer]
        B1["CustomerController<br><code>/customers</code>"]
    end
    
    subgraph Application_Layer [Application Layer]
        C1["CustomerService"]
        C2["CountryValidationPort"]
        C3["CustomerRepository (Port)"]
    end

    subgraph Infrastructure_Layer [Infrastructure Layer]
        E1["CustomerPanacheRepository (Adapter)"]
        E3["CountryValidationRestAdapter (Adapter)"]
    end
    
    subgraph Domain_Layer [Domain Layer]
        D1["Customer (Model)"]
        D2["CustomerRepository (Port)"]
    end
    
    subgraph External
        EX1["PostgreSQL Database"]
        EX2["RestCountries API (External)"]
    end
    
    A1 -->|HTTP| B1
    B1 --> C1
    C1 --> C2
    C1 --> C3
    C3 --> |JDBC| EX1
    E1 --> |Inject| C1
    E3 --> |Inject| C1
    C2 -->|HTTPS| EX2
```

> 🔍 La capa de dominio contiene los modelos puros, interfaces y los puertos que definen contratos de persistencia y lógica externa. 

---

## 🔍 Detalles clave

* **CustomerController** expone los endpoints REST de la API.
* **CustomerService** orquesta la lógica de negocio y delega a puertos.
* **CustomerRepository (Port)** define la interfaz de persistencia.
* **CustomerPanacheRepository** es el adapter concreto que implementa la persistencia vía Panache.
* **CountryValidationPort** abstrae la lógica de validación de países (hexagonal).
* **CountryValidationRestAdapter** conecta con la API externa restcountries.com.

---

## 🌐 Conexiones

* HTTP del cliente a la API
* JDBC entre Quarkus y la base de datos PostgreSQL
* HTTPS para la validación externa de país

---

## 🧱 Principios aplicados

* **Inversión de dependencias**: capa de dominio no depende de infraestructura
* **Separación de responsabilidades**: cada capa tiene una única función
* **Abstracción de puertos y adaptadores**: facilita pruebas, mantenibilidad y escalabilidad

---

## ✍️ Autor

Víctor Velázquez Cid [https://github.com/vvelc](https://github.com/vvelc)
