# MANUAL TÉCNICO Y DE USUARIO
## Sistema de Gestión de Bibliotecas (API RESTful)

**Curso:** Desarrollo de los Componentes del Negocio
**Escuela de Tecnología**
**Fecha:** Noviembre 2025
**Profesor:** Eloy Solano
**Grupo:** 9

---

## 1. DESCRIPCIÓN DEL PROYECTO
El presente proyecto consiste en el desarrollo de un backend completo (API RESTful) utilizando **Java** y **Spring Boot** para la gestión integral de bibliotecas universitarias. El sistema permite el registro de libros, usuarios, préstamos y devoluciones, incorporando altos estándares de seguridad mediante **JWT (JSON Web Tokens)** y control de acceso basado en roles (RBAC).

### 1.1 Tecnologías Utilizadas
* **Lenguaje:** Java 17 / 21
* **Framework:** Spring Boot 3.3.x
* **Seguridad:** Spring Security + JJWT (Auth Stateless)
* **Base de Datos:** MySQL 8
* **ORM:** Spring Data JPA + Hibernate
* **Documentación:** OpenAPI (Swagger UI)

---

## 2. ARQUITECTURA Y FUNCIONAMIENTO DEL CÓDIGO
El proyecto sigue una arquitectura en capas para garantizar la escalabilidad y mantenibilidad:

### 2.1 Capa de Modelo (Entities)
Representan las tablas de la base de datos con sus relaciones:
* **Usuario:** Relación *Many-to-Many* con `Rol`. Contiene credenciales encriptadas.
* **Libro:** Contiene el inventario y valida el stock disponible mediante anotaciones (`@PositiveOrZero`).
* **Prestamo:** Relación *Many-to-One* con `Usuario` y `Libro`. Registra la transacción de préstamo, fechas y estado.

### 2.2 Capa de Repositorio (Repository)
Interfaces que extienden de `JpaRepository`. Permiten la comunicación directa con la base de datos MySQL sin escribir SQL manual, utilizando métodos como `save()`, `findById()`, y `findByUsername()`.

### 2.3 Capa de Servicio (Service)
Contiene la lógica de negocio.
* *Ejemplo:* Al registrar un préstamo, el servicio verifica si el libro tiene stock (`stock > 0`). Si es válido, reduce el stock en -1 y crea el registro del préstamo en una sola transacción. Al devolverlo, restaura el stock.

### 2.4 Capa de Seguridad (Security)
* **JwtFilter:** Intercepta cada petición HTTP, extrae el token del header `Authorization`, lo valida y autentica al usuario en el contexto de Spring.
* **SecurityConfig:** Define qué rutas son públicas (Login/Registro) y cuáles requieren roles específicos (ADMIN para crear libros, USUARIO para ver historial).

### 2.5 Manejo de Excepciones (GlobalExceptionHandler)
* Se implementó un `@RestControllerAdvice` para capturar errores de validación (ej. campos vacíos o stock negativo) y devolver respuestas JSON limpias y controladas, evitando exponer trazas de error del servidor.

---

## 3. GUÍA DE INSTALACIÓN Y EJECUCIÓN

1. **Base de Datos:**
   Abrir MySQL Workbench y ejecutar:
   ```sql
   CREATE DATABASE biblioteca_db;
   ```

2. **Configuración:**
   En el archivo `src/main/resources/application.properties`, actualizar:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/biblioteca_db
   spring.datasource.username=TU_USUARIO
   spring.datasource.password=TU_CONTRASEÑA
   ```

3. **Ejecución:**
   Desde la terminal o IDE, ejecutar el comando maven o la clase principal:
   ```bash
   ./mvnw spring-boot:run
   ```

---

## 4. DOCUMENTACIÓN INTERACTIVA (SWAGGER)

La API cuenta con documentación automática bajo el estándar OpenAPI.

* **URL de Acceso:** `http://localhost:8080/swagger-ui.html`
* **Uso:**
    1. Obtener un token desde el endpoint `/login`.
    2. Hacer clic en el botón verde **Authorize**.
    3. Ingresar el valor `Bearer <TU_TOKEN>`.
    4. Probar cualquier endpoint directamente desde el navegador.

---

## 5. PRUEBAS CON POSTMAN (ESCENARIOS)

A continuación, se detallan los flujos de prueba para validar la funcionalidad y seguridad.

### ESCENARIO A: Configuración de Actores (Registro)

**1. Crear Administrador (Bibliotecario)**
* **Método:** `POST`
* **URL:** `http://localhost:8080/api/auth/register-admin`
* **Body:**
    ```json
    {
      "username": "admin",
      "password": "123",
      "email": "admin@idat.edu.pe",
      "nombres": "Jefe",
      "apellidos": "Admin"
    }
    ```

**2. Crear Usuario (Alumno - Jorge)**
* **Método:** `POST`
* **URL:** `http://localhost:8080/api/auth/register`
* **Body:**
    ```json
    {
      "username": "jorge",
      "password": "123",
      "email": "jorge@alumno.edu.pe",
      "nombres": "Jorge",
      "apellidos": "Perez"
    }
    ```

---

### ESCENARIO B: Flujo de Negocio (Ciclo Completo)

**Paso 1: Login como ADMIN**
* **Endpoint:** `POST /api/auth/login`
* **Body:** `{"username": "admin", "password": "123"}`
* **Acción:** Copiar el `token` de la respuesta.

**Paso 2: Registrar Libro (Requiere Token ADMIN)**
* **Método:** `POST`
* **URL:** `http://localhost:8080/api/libros`
* **Header Authorization:** `Bearer <TOKEN_ADMIN>`
* **Body:**
    ```json
    {
      "titulo": "Spring Boot Avanzado",
      "autor": "IDAT",
      "isbn": "111-222",
      "anioPublicacion": 2025,
      "stock": 5
    }
    ```

**Paso 3: Realizar Préstamo (Requiere Token ADMIN)**
* **Método:** `POST`
* **URL:** `http://localhost:8080/api/prestamos/registrar`
* **Header Authorization:** `Bearer <TOKEN_ADMIN>`
* **Body:**
    ```json
    {
      "usuarioId": 1,  
      "libroId": 1
    }
    ```
* **Resultado Esperado (200 OK):** El sistema devuelve el préstamo creado y el stock del libro baja a 4.

**Paso 4: Registrar Devolución (Requiere Token ADMIN)**
* **Método:** `PUT`
* **URL:** `http://localhost:8080/api/prestamos/devolver/1`
* **Header Authorization:** `Bearer <TOKEN_ADMIN>`
* **Resultado Esperado (200 OK):** El estado cambia a "DEVUELTO" y el stock del libro sube a 5 nuevamente.

---

### ESCENARIO C: Consulta de Usuario y Seguridad

**Paso 1: Login como JORGE (USUARIO)**
* **Endpoint:** `POST /api/auth/login`
* **Body:** `{"username": "jorge", "password": "123"}`
* **Acción:** Copiar el `token` de la respuesta.

**Paso 2: Ver Mis Préstamos (Requiere Token USUARIO)**
* **Método:** `GET`
* **URL:** `http://localhost:8080/api/prestamos/mis-prestamos`
* **Header Authorization:** `Bearer <TOKEN_JORGE>`
* **Resultado:**
    ```json
    [
      {
        "id": 1,
        "fechaPrestamo": "2025-11-23",
        "libro": { "titulo": "Spring Boot Avanzado" }
      }
    ]
    ```

**Paso 3: Intento de violación de seguridad (USUARIO)**
* **Acción:** Intentar crear un libro (`POST /api/libros`) usando el Token de **JORGE**.
* **Resultado Esperado:** `403 Forbidden`. (Prueba de seguridad exitosa).

---

### ESCENARIO D: Validación de Datos (Manejo de Errores)

**Paso 1: Enviar datos inválidos (Admin)**
* **Método:** `POST`
* **URL:** `http://localhost:8080/api/libros`
* **Header Authorization:** `Bearer <TOKEN_ADMIN>`
* **Body (Datos Incorrectos):**
    ```json
    { 
      "titulo": "", 
      "stock": -5 
    }
    ```

**Paso 2: Resultado Esperado (400 Bad Request)**
El sistema intercepta el error y devuelve un JSON limpio:
    ```json
    {
        "titulo": "El título no puede estar vacío",
        "stock": "El stock no puede ser negativo",
        "autor": "El autor es obligatorio"
    }
### ESCENARIO E: Gestión de Usuarios (Actualizar y Eliminar)

**Paso 1: Actualizar datos del usuario (Admin) **
* **Método:** `PUT`
* **URL:** `http://localhost:8080/api/usuarios/1`
* **Header Authorization:** `Bearer <TOKEN_ADMIN>`
* **Body (Datos Correctos):**
    ```json
    {
        "nombres": "Jorge",
        "apellidos": "Perez",
        "email": "jorge@alumno.edu.pe"
    }
    ```

**Paso 2: Resultado Esperado (200 OK)**
El sistema devuelve un mensaje indicando que el usuario se actualizó correctamente.

**Paso 3: Eliminar usuario (Admin) **
* **Método:** `DELETE`
* **URL:** `http://localhost:8080/api/usuarios/1`
* **Header Authorization:** `Bearer <TOKEN_ADMIN>`
* **Resultado Esperado (200 OK):** El sistema devuelve un mensaje indicando que el usuario se eliminó correctamente.

---

## 6. CONCLUSIONES
El sistema cumple con todos los requisitos de la rúbrica, implementando correctamente la persistencia de datos relacional, relaciones complejas entre entidades, seguridad perimetral con JWT, validación centralizada de errores y documentación técnica estandarizada.