# Biblioteca Backend - Sistema de Gestión de Biblioteca

API RESTful para la gestión de bibliotecas con autenticación JWT y control de acceso basado en roles.

---

## Descripción del Sistema

El sistema **biblioteca-backend** es una aplicación Spring Boot que proporciona una API REST para la gestión completa de operaciones bibliotecarias. Implementa autenticación sin estado mediante tokens JWT con autorización basada en roles, soportando dos tipos de usuarios: **ADMIN** y **USER**.

---

## Características Principales

* Registro y autenticación de usuarios con emisión de tokens JWT.
* Gestión de catálogo de libros con control automático de inventario.
* Procesamiento de préstamos con gestión de estados (PRESTADO / DEVUELTO).
* Administración basada en roles de usuarios, libros y préstamos.

---

## Tecnologías Utilizadas

* **Framework:** Spring Boot
* **Java:** Versión 17
* **Base de Datos:** MySQL
* **Seguridad:** Spring Security con JWT
* **ORM:** Spring Data JPA con Hibernate
* **Documentación:** SpringDoc OpenAPI (Swagger)

---

## Instalación y Ejecución

### Prerrequisitos

* Java 17 o superior
* Maven 3.6+
* MySQL 8.0+
* Git

### 1. Clonar el Repositorio

```bash
git clone https://github.com/joel-machaca/biblioteca-backend.git
cd biblioteca-backend
```

### 2. Configurar Base de Datos

Crear una base de datos en MySQL:

```sql
CREATE DATABASE biblioteca;
```

### 3. Configurar Aplicación

Editar `src/main/resources/application.properties` con tus credenciales:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/biblioteca
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
```

### 4. Compilar y Ejecutar

```bash
mvn clean install
mvn spring-boot:run
```

La aplicación se iniciará en [http://localhost:8080](http://localhost:8080)

---

## Documentación API

* **Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
* **OpenAPI JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## Endpoints Principales

### Autenticación y Usuarios

| Método | Endpoint         | Descripción                         |
| ------ | ---------------- | ----------------------------------- |
| POST   | /v1/auth/login   | Iniciar sesión                      |
| POST   | /v1/usuario      | Registrar usuario (público)         |
| GET    | /v1/usuario      | Listar usuarios (autenticado)       |
| GET    | /v1/usuario/{id} | Obtener usuario por ID (ADMIN/USER) |
| PUT    | /v1/usuario/{id} | Actualizar usuario (ADMIN/USER)     |
| DELETE | /v1/usuario/{id} | Eliminar usuario (ADMIN)            |

### Gestión de Libros

| Método | Endpoint       | Descripción                           |
| ------ | -------------- | ------------------------------------- |
| GET    | /v1/libro      | Listar todos los libros (autenticado) |
| POST   | /v1/libro      | Crear nuevo libro (ADMIN)             |
| GET    | /v1/libro/{id} | Obtener libro por ID (ADMIN/USER)     |
| PUT    | /v1/libro/{id} | Actualizar libro (ADMIN/USER)         |
| DELETE | /v1/libro/{id} | Eliminar libro (ADMIN)                |

### Gestión de Préstamos

| Método | Endpoint                         | Descripción                               |
| ------ | -------------------------------- | ----------------------------------------- |
| GET    | /v1/prestamo                     | Listar préstamos (ADMIN)                  |
| POST   | /v1/prestamo                     | Crear préstamo (ADMIN)                    |
| GET    | /v1/prestamo/{id}                | Obtener préstamo por ID (ADMIN)           |
| GET    | /v1/prestamo/usuario/{idUsuario} | Listar préstamos por usuario (ADMIN/USER) |
| GET    | /v1/prestamo/devolucion/{id}     | Devolver libro (ADMIN)                    |

### Gestión de Roles

| Método | Endpoint     | Descripción                |
| ------ | ------------ | -------------------------- |
| GET    | /v1/rol      | Listar roles (ADMIN)       |
| POST   | /v1/rol      | Crear rol (ADMIN)          |
| GET    | /v1/rol/{id} | Obtener rol por ID (ADMIN) |
| PUT    | /v1/rol/{id} | Actualizar rol (ADMIN)     |
| DELETE | /v1/rol/{id} | Eliminar rol (ADMIN)       |

---

## Ejemplos de Pruebas con Postman

### Registrar Usuario

```http
POST http://localhost:8080/v1/usuario
Content-Type: application/json

{
  "nombre": "Juan",
  "apellido": "Pérez",
  "telefono": "987654321",
  "email": "juan@example.com",
  "password": "password123"
}
```

### Iniciar Sesión

```http
POST http://localhost:8080/v1/auth/login
Content-Type: application/json

{
  "email": "juan@example.com",
  "password": "password123"
}
```

### Crear Libro (ADMIN)

```http
POST http://localhost:8080/v1/libro
Authorization: Bearer <token>
Content-Type: application/json

{
  "isbn": "978-3-16-148410-0",
  "titulo": "Libro de Ejemplo",
  "descripcion": "Descripción del libro",
  "autor": "Autor Ejemplo",
  "genero": "Ficción",
  "stock": 10
}
```

### Crear Préstamo (ADMIN)

```http
POST http://localhost:8080/v1/prestamo
Authorization: Bearer <token>
Content-Type: application/json

{
  "idUsuario": 1,
  "idLibro": 1
}
```

---

## Estructura del Proyecto

```
src/main/java/pe/edu/idat/biblioteca/
├── config/        # Configuración de seguridad
├── controller/    # Controladores REST
├── dto/           # Objetos de transferencia de datos
├── entity/        # Entidades JPA
├── exception/     # Manejo de excepciones
├── mapper/        # Mapeo entre DTOs y entidades
├── repository/    # Interfaces Spring Data JPA
├── service/       # Lógica de negocio
└── util/          # Utilidades (JWT)
```

---

## Informe Técnico

### Arquitectura

* **Capa API:** Controladores REST que manejan solicitudes HTTP
* **Capa de Seguridad:** Filtros JWT y configuración de Spring Security
* **Capa de Servicio:** Lógica de negocio y gestión de transacciones
* **Capa de Datos:** Repositorios Spring Data JPA para acceso a base de datos

### Configuración de Seguridad

* **Endpoints públicos:** `/swagger-ui/**`, `/v3/api-docs/**`, `/v1/auth/**`, `POST /v1/usuario`
* **Solo ADMIN:** `/v1/rol/**`, `POST /v1/libro`, `POST /v1/prestamo`, operaciones DELETE
* **ADMIN o USER:** operaciones GET por ID y operaciones PUT
* **Cualquier usuario autenticado:** operaciones GET de listado

### Validaciones

* Implementadas a nivel de DTO usando anotaciones de Jakarta Validation.

### Gestión de Stock

* Automática mediante `reducirStock()` y `aumentarStock()` en `LibroServiceImpl`.

### Generación de Tokens

* Registro y login generan tokens JWT con `JwtUtil`.

---

## Enlace al Repositorio

[GitHub: biblioteca-backend](https://github.com/joel-machaca/biblioteca-backend)

---

## Licencia

Este proyecto es desarrollado para fines
