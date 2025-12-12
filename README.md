# MS-Biblioteca - Sistema de Gestión de Biblioteca Universitaria

![Java](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)
![License](https://img.shields.io/badge/License-MIT-yellow.svg)

## Índice
- [Descripción del Sistema](#-descripción-del-sistema)
- [Características Principales](#-características-principales)
- [Arquitectura](#-arquitectura)
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación y Configuración](#-instalación-y-configuración)
- [Ejecución](#-ejecución)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Modelo de Datos](#-modelo-de-datos)
- [API REST - Endpoints](#-api-rest---endpoints)
- [Seguridad y Autenticación](#-seguridad-y-autenticación)
- [Pruebas](#-pruebas)
- [Documentación API](#-documentación-api)
- [Buenas Prácticas Implementadas](#-buenas-prácticas-implementadas)
- [Contribución](#-contribución)
- [Licencia](#-licencia)

---

## 📋 Descripción del Sistema

**MS-Biblioteca** es un microservicio REST completo desarrollado con **Spring Boot 3** y **Java 21** para la gestión integral de una biblioteca universitaria. El sistema permite administrar el catálogo de libros, gestionar préstamos y devoluciones, controlar usuarios con diferentes roles, y calcular multas por retrasos de forma automática.

Implementa una **API RESTful** robusta y segura utilizando **JWT para autenticación sin estado**, con control de acceso basado en roles (ADMIN y USUARIO), persistencia con **JPA/Hibernate** y base de datos **MySQL**.

### Principales Módulos
- ✅ **Autenticación y Autorización** - JWT / Spring Security
- ✅ **Gestión de Libros** - CRUD completo con búsqueda avanzada
- ✅ **Gestión de Préstamos** - Control de disponibilidad y devoluciones
- ✅ **Sistema de Multas** - Cálculo automático por retrasos
- ✅ **Gestión de Usuarios** - Roles y permisos
- ✅ **Manejo Global de Errores** - Respuestas estandarizadas

---

## 🎯 Características Principales

### Gestión de Libros
- ✔️ Registro completo de libros (ISBN, título, autor, editorial, año, categoría)
- ✔️ Control de inventario (copias disponibles/totales)
- ✔️ Búsqueda avanzada por título, autor, ISBN, categoría
- ✔️ Estados del libro (DISPONIBLE, AGOTADO, EN_MANTENIMIENTO)
- ✔️ Ubicación física en biblioteca

### Gestión de Préstamos
- ✔️ Registro de préstamos con validaciones de negocio
- ✔️ Control de límite de préstamos activos por usuario (máximo 5)
- ✔️ Validación de disponibilidad de libros
- ✔️ Prevención de préstamos si hay multas pendientes
- ✔️ Cálculo automático de fechas de devolución
- ✔️ Registro de devoluciones con cálculo de multas

### Sistema de Multas
- ✔️ Cálculo automático de multas por retraso ($1.00 por día)
- ✔️ Identificación de préstamos vencidos
- ✔️ Reporte de multas por usuario
- ✔️ Actualización automática de estados

### Gestión de Usuarios
- ✔️ Registro de usuarios con información completa
- ✔️ Dos roles: **ADMIN** (administrador) y **USUARIO** (estudiante/profesor)
- ✔️ Historial de préstamos por usuario
- ✔️ Control de préstamos activos

### Seguridad
- ✔️ Autenticación con **JWT (JSON Web Tokens)**
- ✔️ Tokens de acceso con expiración configurable
- ✔️ Autorización basada en roles con Spring Security
- ✔️ Protección de endpoints según permisos
- ✔️ Encriptación de contraseñas con BCrypt

### Validaciones y Manejo de Errores
- ✔️ Validaciones de datos con Bean Validation
- ✔️ Manejo centralizado de excepciones (`GlobalExceptionHandler`)
- ✔️ Respuestas estandarizadas con `ApiResponse`
- ✔️ Mensajes descriptivos de error
- ✔️ Logs profesionales sin datos sensibles

---

## 🏗️ Arquitectura

El sistema implementa una **arquitectura en capas** que separa responsabilidades y facilita el mantenimiento:

```
📦 MS-Biblioteca
│
├── 🎮 Capa de Presentación (Controllers)
│   ├── AuthController         - Autenticación y registro
│   ├── LibroController         - Gestión de libros
│   └── PrestamoController      - Gestión de préstamos
│
├── 💼 Capa de Negocio (Services)
│   ├── UsuarioService          - Lógica de usuarios
│   ├── LibroService            - Lógica de libros
│   └── PrestamoService         - Lógica de préstamos y multas
│
├── 🗄️ Capa de Persistencia (Repositories)
│   ├── UsuarioRepository       - Acceso a datos de usuarios
│   ├── RolRepository           - Acceso a datos de roles
│   ├── LibroRepository         - Acceso a datos de libros
│   └── PrestamoRepository      - Acceso a datos de préstamos
│
├── 🔄 Capa de Mapeo (Mappers)
│   ├── LibroMapper             - Conversión Entity ↔ DTO
│   └── PrestamoMapper          - Conversión Entity ↔ DTO
│
├── 📊 Capa de Datos (Entities)
│   ├── Usuario                 - Entidad de usuario
│   ├── Rol                     - Entidad de rol
│   ├── Libro                   - Entidad de libro
│   └── Prestamo                - Entidad de préstamo
│
├── 📝 Capa de DTOs
│   ├── auth/                   - DTOs de autenticación
│   ├── libro/                  - DTOs de libros
│   ├── prestamo/               - DTOs de préstamos
│   ├── common/                 - DTOs comunes (ApiResponse)
│   └── error/                  - DTOs de errores
│
├── 🔐 Capa de Seguridad
│   ├── JwtTokenProvider        - Generación y validación JWT
│   ├── JwtAuthenticationFilter - Filtro de autenticación
│   ├── SecurityConfig          - Configuración de seguridad
│   └── CustomUserDetailsService- Carga de usuarios para auth
│
└── ⚙️ Configuración
    ├── OpenApiConfig           - Configuración Swagger/OpenAPI
    └── DataInitializer         - Datos iniciales del sistema
```

---

## 🚀 Tecnologías Utilizadas

### Backend
- **Java 21** - Lenguaje de programación (compatible con Java 17+)
- **Spring Boot 3.5.7** - Framework principal
- **Spring Data JPA** - Persistencia de datos
- **Spring Security** - Seguridad y autenticación
- **Spring Web** - API REST
- **Hibernate** - ORM (Object-Relational Mapping)
- **MySQL 8** - Base de datos relacional
- **JWT (jjwt 0.11.5)** - Autenticación sin estado
- **MapStruct 1.6.3** - Mapeo automático de objetos
- **Lombok** - Reducción de código boilerplate
- **Bean Validation** - Validación de datos
- **Maven** - Gestión de dependencias y build

### Herramientas de Desarrollo
- **IntelliJ IDEA / Eclipse** - IDEs recomendados
- **Postman** - Pruebas de API
- **MySQL Workbench** - Administración de base de datos
- **Git** - Control de versiones
- **Swagger UI** - Documentación interactiva de API

---

## 📋 Requisitos Previos

Antes de instalar el sistema, asegúrate de tener instalado:

1. **Java Development Kit (JDK)**
   - JDK 17 o superior (recomendado JDK 21)
   - Verificar: `java -version`

2. **MySQL Server**
   - MySQL 8.0 o superior
   - MySQL debe estar en ejecución

3. **Maven** (opcional)
   - Maven 3.9+ o usar el wrapper incluido (`mvnw`)
   - Verificar: `mvn -version`

4. **Git**
   - Para clonar el repositorio
   - Verificar: `git --version`

---

## ⚙️ Instalación y Configuración

### 1️⃣ Clonar el Repositorio

```bash
git clone https://github.com/vansfanelx/ms-biblioteca.git
cd ms-biblioteca
```

### 2️⃣ Configurar Base de Datos

**Opción A: Ejecución Automática del Script SQL (Recomendado)**

El repositorio incluye el script `init_database_biblioteca.sql` con la estructura completa y datos iniciales.

```bash
# Desde la consola de MySQL
mysql -u root -p < init_database_biblioteca.sql
```

**Opción B: Creación Manual**

```sql
CREATE DATABASE bd_biblioteca_universitaria CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Las tablas se crearán automáticamente al iniciar la aplicación gracias a `ddl-auto=update`.

### 3️⃣ Configurar Propiedades de la Aplicación

Editar el archivo `src/main/resources/application.properties`:

```properties
# Configuración del servidor
server.port=9595

# Configuración de base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/bd_biblioteca_universitaria
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD_AQUI
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Configuración JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Configuración JWT
jwt.secret=TuSecretoJWTSuperSeguroYLargo123456789
jwt.expiration=86400000

# Configuración de logs
logging.level.pe.edu.idat.msbiblioteca=DEBUG
logging.level.org.springframework.security=DEBUG
```

### 4️⃣ Compilar el Proyecto

**Con Maven instalado:**
```bash
mvn clean install
```

**Con Maven Wrapper (Windows PowerShell):**
```powershell
.\mvnw.cmd clean install
```

**Con Maven Wrapper (Linux/Mac):**
```bash
./mvnw clean install
```

---

## 🚀 Ejecución

### Opción 1: Ejecutar con Maven

```bash
mvn spring-boot:run
```

### Opción 2: Ejecutar con Maven Wrapper (Windows)

```powershell
.\mvnw.cmd spring-boot:run
```

### Opción 3: Ejecutar JAR Empaquetado

```bash
# Compilar sin tests
.\mvnw.cmd clean package -DskipTests

# Ejecutar JAR
java -jar target/ms-biblioteca-0.0.1-SNAPSHOT.jar
```

### Opción 4: Desde IDE (IntelliJ IDEA)

1. Abrir el proyecto en IntelliJ IDEA
2. Ejecutar la clase `MsBibliotecaApplication.java`
3. La aplicación iniciará en `http://localhost:9595`

### ✅ Verificar que el Sistema está Activo

Abrir en navegador: `http://localhost:9595`

Si ves un error 403 Forbidden, el sistema está funcionando correctamente (requiere autenticación).

---

## 📁 Estructura del Proyecto

```
ms-biblioteca/
│
├── src/
│   ├── main/
│   │   ├── java/pe/edu/idat/msbiblioteca/
│   │   │   ├── MsBibliotecaApplication.java    # Clase principal
│   │   │   ├── config/                          # Configuraciones
│   │   │   │   ├── DataInitializer.java         # Datos iniciales
│   │   │   │   ├── OpenApiConfig.java           # Config Swagger
│   │   │   │   └── SecurityConfig.java          # Config Seguridad
│   │   │   ├── controller/                      # Controladores REST
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── LibroController.java
│   │   │   │   └── PrestamoController.java
│   │   │   ├── dto/                             # Data Transfer Objects
│   │   │   │   ├── auth/
│   │   │   │   ├── common/
│   │   │   │   ├── error/
│   │   │   │   ├── jwt/
│   │   │   │   ├── libro/
│   │   │   │   └── prestamo/
│   │   │   ├── entity/                          # Entidades JPA
│   │   │   │   ├── Usuario.java
│   │   │   │   ├── Rol.java
│   │   │   │   ├── Libro.java
│   │   │   │   └── Prestamo.java
│   │   │   ├── exception/                       # Excepciones custom
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── mappers/                         # Mappers DTO-Entity
│   │   │   ├── repository/                      # Repositorios JPA
│   │   │   ├── security/                        # Componentes seguridad
│   │   │   │   ├── JwtTokenProvider.java
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   └── CustomUserDetailsService.java
│   │   │   └── service/                         # Servicios de negocio
│   │   │       ├── UsuarioService.java
│   │   │       ├── LibroService.java
│   │   │       └── PrestamoService.java
│   │   └── resources/
│   │       ├── application.properties           # Configuración principal
│   │       ├── application-new.properties       # Config alternativa
│   │       └── static/                          # Recursos estáticos
│   └── test/                                    # Tests unitarios
│
├── docs/                                        # Documentación adicional
├── init_database_biblioteca.sql                 # Script inicialización BD
├── MS-Biblioteca-Collection.json                # Colección Postman
├── pom.xml                                      # Dependencias Maven
├── mvnw & mvnw.cmd                              # Maven Wrapper
└── README.md                                    # Este archivo
```

---

## 🗂️ Modelo de Datos

### Diagrama Entidad-Relación

```
┌─────────────────┐         ┌──────────────────┐
│      ROL        │         │     USUARIO      │
├─────────────────┤         ├──────────────────┤
│ id (PK)         │◄───────┐│ id (PK)          │
│ nombre          │        ││ username (UK)    │
│ descripcion     │        ││ password         │
└─────────────────┘        ││ enabled          │
                           ││ nombre_completo  │
                           ││ email (UK)       │
    usuario_rol (M:N)      ││ telefono         │
                           ││ direccion        │
                           ││ fecha_registro   │
                           │└──────────────────┘
                           │         │
                           │         │ 1:N
                           │         ▼
┌─────────────────┐        │┌──────────────────┐
│     LIBRO       │        ││    PRESTAMO      │
├─────────────────┤        │├──────────────────┤
│ id (PK)         │◄───────┘│ id (PK)          │
│ isbn (UK)       │         │ usuario_id (FK)  │
│ titulo          │         │ libro_id (FK)    │
│ autor           │         │ fecha_prestamo   │
│ editorial       │         │ fecha_devolucion_esperada│
│ anio_publicacion│         │ fecha_devolucion_real   │
│ categoria       │         │ estado           │
│ copias_disponibles│       │ multa            │
│ copias_totales  │         │ observaciones    │
│ descripcion     │         └──────────────────┘
│ ubicacion       │
│ estado          │
│ fecha_registro  │
└─────────────────┘
```

### Entidades Principales

#### Usuario
- **Campos**: id, username, password, enabled, nombreCompleto, email, telefono, direccion, fechaRegistro
- **Relaciones**: ManyToMany con Rol, OneToMany con Prestamo

#### Rol
- **Campos**: id, nombre, descripcion
- **Valores**: ROLE_ADMIN, ROLE_USUARIO

#### Libro
- **Campos**: id, isbn, titulo, autor, editorial, anioPublicacion, categoria, copiasDisponibles, copiasTotales, descripcion, ubicacion, estado, fechaRegistro
- **Estados**: DISPONIBLE, AGOTADO, EN_MANTENIMIENTO

#### Prestamo
- **Campos**: id, usuarioId, libroId, fechaPrestamo, fechaDevolucionEsperada, fechaDevolucionReal, estado, multa, observaciones
- **Estados**: ACTIVO, DEVUELTO, VENCIDO

---

## 📡 API REST - Endpoints

### Base URL
```
http://localhost:9595/v1
```

### 🔐 Autenticación

| Método | Endpoint | Descripción | Acceso |
|--------|----------|-------------|--------|
| POST | `/auth/login` | Iniciar sesión y obtener token JWT | Público |
| POST | `/auth/register` | Registrar nuevo usuario | Público |

#### 📝 Ejemplo: Login

**Request:**
```http
POST /v1/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**Response 200 OK:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer"
}
```

#### 📝 Ejemplo: Registro

**Request:**
```http
POST /v1/auth/register
Content-Type: application/json

{
  "username": "nuevo_usuario",
  "password": "password123",
  "nombreCompleto": "Juan Pérez",
  "email": "juan.perez@universidad.edu",
  "telefono": "987654321",
  "direccion": "Av. Universidad 123"
}
```

**Response 201 Created:**
```json
{
  "id": 5,
  "username": "nuevo_usuario",
  "nombreCompleto": "Juan Pérez",
  "email": "juan.perez@universidad.edu",
  "roles": ["ROLE_USUARIO"]
}
```

---

### 📚 Gestión de Libros

| Método | Endpoint | Descripción | Rol Requerido |
|--------|----------|-------------|---------------|
| GET | `/libros` | Listar todos los libros | USER, ADMIN |
| GET | `/libros/{id}` | Obtener libro por ID | USER, ADMIN |
| POST | `/libros` | Crear nuevo libro | ADMIN |
| PUT | `/libros/{id}` | Actualizar libro | ADMIN |
| DELETE | `/libros/{id}` | Eliminar libro | ADMIN |
| GET | `/libros/buscar?q={query}` | Buscar libros | USER, ADMIN |

#### 📝 Ejemplo: Listar Libros

**Request:**
```http
GET /v1/libros?page=0&size=10
Authorization: Bearer {token}
```

**Response 200 OK:**
```json
{
  "content": [
    {
      "id": 1,
      "isbn": "978-0134685991",
      "titulo": "Effective Java",
      "autor": "Joshua Bloch",
      "editorial": "Addison-Wesley",
      "anioPublicacion": 2018,
      "categoria": "Programación",
      "copiasDisponibles": 3,
      "copiasTotales": 5,
      "estado": "DISPONIBLE"
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "size": 10,
  "number": 0
}
```

#### 📝 Ejemplo: Crear Libro

**Request:**
```http
POST /v1/libros
Authorization: Bearer {token}
Content-Type: application/json

{
  "isbn": "978-0134685991",
  "titulo": "Effective Java",
  "autor": "Joshua Bloch",
  "editorial": "Addison-Wesley",
  "anioPublicacion": 2018,
  "categoria": "Programación",
  "copiasTotales": 5,
  "descripcion": "Guía completa para programadores Java",
  "ubicacion": "Estante A-12"
}
```

**Response 201 Created:**
```json
{
  "id": 10,
  "isbn": "978-0134685991",
  "titulo": "Effective Java",
  "autor": "Joshua Bloch",
  "copiasDisponibles": 5,
  "copiasTotales": 5,
  "estado": "DISPONIBLE",
  "fechaRegistro": "2025-12-12T10:30:00"
}
```

---

### 📖 Gestión de Préstamos

| Método | Endpoint | Descripción | Rol Requerido |
|--------|----------|-------------|---------------|
| GET | `/prestamos` | Listar todos los préstamos | USER, ADMIN |
| GET | `/prestamos/{id}` | Obtener préstamo por ID | USER, ADMIN |
| POST | `/prestamos` | Crear nuevo préstamo | USER, ADMIN |
| PUT | `/prestamos/{id}/devolver` | Registrar devolución | USER, ADMIN |
| GET | `/prestamos/usuario/{id}` | Préstamos de un usuario | USER, ADMIN |
| GET | `/prestamos/vencidos` | Préstamos vencidos | ADMIN |

#### 📝 Ejemplo: Crear Préstamo

**Request:**
```http
POST /v1/prestamos
Authorization: Bearer {token}
Content-Type: application/json

{
  "usuarioId": 2,
  "libroId": 10,
  "fechaDevolucionEsperada": "2026-01-15"
}
```

**Response 201 Created:**
```json
{
  "id": 25,
  "usuarioId": 2,
  "libroId": 10,
  "fechaPrestamo": "2025-12-12",
  "fechaDevolucionEsperada": "2026-01-15",
  "estado": "ACTIVO",
  "multa": 0.00
}
```

#### 📝 Ejemplo: Registrar Devolución

**Request:**
```http
PUT /v1/prestamos/25/devolver
Authorization: Bearer {token}
Content-Type: application/json

{
  "observaciones": "Libro en buen estado"
}
```

**Response 200 OK:**
```json
{
  "id": 25,
  "estado": "DEVUELTO",
  "fechaDevolucionReal": "2025-12-20",
  "multa": 0.00,
  "observaciones": "Libro en buen estado"
}
```

---

## 🔐 Seguridad y Autenticación

### Autenticación JWT

El sistema utiliza **JSON Web Tokens (JWT)** para autenticación sin estado:

1. **Login**: El usuario envía credenciales a `/v1/auth/login`
2. **Token**: El servidor devuelve un token JWT válido
3. **Autorización**: El cliente incluye el token en cada request:
   ```
   Authorization: Bearer {token}
   ```
4. **Validación**: El servidor valida el token en cada request

### Roles y Permisos

| Rol | Descripción | Permisos |
|-----|-------------|----------|
| **ROLE_ADMIN** | Administrador del sistema | Acceso completo a todos los endpoints |
| **ROLE_USUARIO** | Usuario regular | Consultar libros, gestionar sus propios préstamos |

### Credenciales por Defecto

El sistema crea automáticamente estos usuarios al iniciar:

**Administrador:**
- Username: `admin`
- Password: `admin123`
- Rol: ROLE_ADMIN

**Usuario de Prueba:**
- Username: `usuario1`
- Password: `usuario123`
- Rol: ROLE_USUARIO

> ⚠️ **Importante**: Cambiar estas credenciales en producción.

---

## 🧪 Pruebas

### Tests Unitarios y de Integración

**Ejecutar todos los tests:**
```bash
.\mvnw.cmd test
```

**Ejecutar test específico:**
```bash
.\mvnw.cmd -Dtest=LibroServiceTest test
```

**Generar reporte de cobertura:**
```bash
.\mvnw.cmd test jacoco:report
```

### Pruebas con Postman

El repositorio incluye colecciones de Postman listas para usar:

1. **Importar colección**: `MS-Biblioteca-Collection.json`
2. **Configurar variables**:
   - `baseUrl`: `http://localhost:9595/v1`
   - `token`: Se actualiza automáticamente tras login
3. **Ejecutar requests** en orden:
   - Auth → Login
   - Libros → Crear/Listar
   - Préstamos → Crear/Devolver

### Pruebas Manuales

**1. Login y obtener token:**
```bash
curl -X POST http://localhost:9595/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

**2. Listar libros:**
```bash
curl -X GET http://localhost:9595/v1/libros \
  -H "Authorization: Bearer {token}"
```

---

## 📖 Documentación API

### Swagger / OpenAPI

La aplicación incluye **Swagger UI** para documentación interactiva de la API.

**Acceder a Swagger UI:**
```
http://localhost:9595/swagger-ui.html
```

**Especificación OpenAPI (JSON):**
```
http://localhost:9595/v3/api-docs
```

### Usar Autenticación en Swagger

1. Hacer login en `/v1/auth/login` para obtener el token
2. En Swagger UI, clic en botón **"Authorize"**
3. Ingresar: `Bearer {tu-token}`
4. Ejecutar endpoints protegidos directamente desde la UI

---

## ✨ Buenas Prácticas Implementadas

### Código Limpio y Modular
- ✅ Arquitectura en capas bien definida
- ✅ Separación de responsabilidades (SoC)
- ✅ Principios SOLID aplicados
- ✅ DTOs para desacoplar entidades de la API
- ✅ Mappers automáticos con MapStruct

### Seguridad
- ✅ Contraseñas encriptadas con BCrypt
- ✅ Tokens JWT con expiración
- ✅ No se loguean datos sensibles
- ✅ Validación de entrada con Bean Validation
- ✅ Protección CSRF deshabilitada (API REST stateless)

### Manejo de Errores
- ✅ Excepciones centralizadas con `@ControllerAdvice`
- ✅ Respuestas estandarizadas con `ApiResponse<T>`
- ✅ Mensajes descriptivos y profesionales
- ✅ Códigos HTTP apropiados

### Logging
- ✅ Logs estructurados y profesionales
- ✅ No se imprimen contraseñas ni hashes
- ✅ Niveles de log apropiados (INFO, DEBUG, ERROR)

### Documentación
- ✅ Swagger/OpenAPI para documentación interactiva
- ✅ README técnico detallado
- ✅ Comentarios JavaDoc en clases importantes
- ✅ Colecciones Postman incluidas

### Recomendaciones Futuras
- 🔄 Implementar caché con Redis para consultas frecuentes
- 🔄 Añadir Testcontainers para tests de integración
- 🔄 Configurar CI/CD con GitHub Actions
- 🔄 Implementar análisis estático de código (SonarQube)
- 🔄 Añadir métricas y monitoreo con Actuator
- 🔄 Implementar paginación y filtros avanzados
- 🔄 Añadir notificaciones por email/SMS

---

## 🤝 Contribución

Las contribuciones son bienvenidas. Para contribuir:

1. Fork el repositorio
2. Crear rama feature: `git checkout -b feature/nueva-funcionalidad`
3. Commit cambios: `git commit -m 'Agregar nueva funcionalidad'`
4. Push a la rama: `git push origin feature/nueva-funcionalidad`
5. Abrir Pull Request

### Convenciones de Código
- Usar Java Code Conventions
- Escribir tests para nuevas funcionalidades
- Documentar métodos públicos con JavaDoc
- Mantener cobertura de tests >80%

---

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver archivo `LICENSE` para más detalles.

---

## 📞 Contacto

**Autor**: Jonathan Jiménez  
**GitHub**: [https://github.com/vansfanelx/](https://github.com/vansfanelx/)

Para preguntas, sugerencias o reportar problemas, por favor abrir un **issue** en el repositorio.

---

## 📚 Recursos Adicionales

- [Documentación Spring Boot](https://spring.io/projects/spring-boot)
- [Documentación Spring Security](https://spring.io/projects/spring-security)
- [Documentación JWT](https://jwt.io/)
- [Guía de Spring Data JPA](https://spring.io/guides/gs/accessing-data-jpa/)
- [Documentación Swagger/OpenAPI](https://swagger.io/docs/)

---

**¡Gracias por usar MS-Biblioteca!** 🎉

