# 📚 Sistema de Gestión de Biblioteca Universitaria - API REST

## 📋 Descripción del Proyecto

API RESTful desarrollada con **Java** y **Spring Boot** para la gestión integral de bibliotecas universitarias. El sistema permite administrar libros, usuarios, préstamos y devoluciones, implementando autenticación JWT y control de acceso basado en roles.

### Características Principales

- ✅ Gestión completa de libros (CRUD)
- ✅ Gestión de usuarios con roles (ADMIN, USUARIO)
- ✅ Sistema de préstamos y devoluciones
- ✅ Autenticación JWT (JSON Web Token)
- ✅ Autorización basada en roles
- ✅ Validación de datos con Bean Validation
- ✅ Manejo centralizado de excepciones
- ✅ Persistencia con JPA/Hibernate
- ✅ Base de datos MySQL

---

## 🛠️ Tecnologías Utilizadas

| Tecnología | Versión | Descripción |
|-----------|---------|-------------|
| Java | 17 | Lenguaje de programación |
| Spring Boot | 3.2.0 | Framework principal |
| Spring Security | 3.2.0 | Seguridad y autenticación |
| Spring Data JPA | 3.2.0 | Persistencia de datos |
| Hibernate | 6.x | ORM |
| MySQL | 8.0+ | Base de datos |
| JWT | 0.12.3 | Tokens de autenticación |
| Maven | 3.8+ | Gestión de dependencias |
| Lombok | - | Reducción de código boilerplate |

---

## 📁 Estructura del Proyecto

```
biblioteca-universitaria-api/
├── src/
│   ├── main/
│   │   ├── java/com/idat/biblioteca/
│   │   │   ├── config/           # Configuraciones (Seguridad, Inicialización)
│   │   │   ├── controller/       # Controladores REST
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── entity/          # Entidades JPA
│   │   │   ├── exception/       # Manejo de excepciones
│   │   │   ├── repository/      # Repositorios Spring Data
│   │   │   ├── security/        # Seguridad JWT
│   │   │   ├── service/         # Lógica de negocio
│   │   │   └── BibliotecaUniversitariaApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── database.sql                 # Script SQL
├── pom.xml                     # Dependencias Maven
└── README.md                   # Este archivo
```

---

## ⚙️ Instalación y Configuración

### Prerrequisitos

- Java JDK 17 o superior
- MySQL 8.0 o superior
- Maven 3.8 o superior
- IDE (IntelliJ IDEA, Eclipse, VS Code)
- Postman (para pruebas)

### Paso 1: Clonar el Repositorio

```bash
git clone <URL_DEL_REPOSITORIO>
cd biblioteca-universitaria-api
```

### Paso 2: Configurar la Base de Datos

**Opción A: Automática (Recomendada)**

Spring Boot creará automáticamente la base de datos con la configuración actual:

```properties
spring.jpa.hibernate.ddl-auto=update
```

Solo necesitas:

1. Crear la base de datos vacía en MySQL:
```sql
CREATE DATABASE biblioteca_universitaria;
```

2. Verificar las credenciales en `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/biblioteca_universitaria
spring.datasource.username=root
spring.datasource.password=root
```

**Opción B: Manual (Opcional)**

Si prefieres crear las tablas manualmente:

```bash
mysql -u root -p < database.sql
```

### Paso 3: Compilar el Proyecto

```bash
mvn clean install
```

### Paso 4: Ejecutar la Aplicación

```bash
mvn spring-boot:run
```

O desde tu IDE, ejecuta la clase `BibliotecaUniversitariaApplication.java`

La aplicación estará disponible en: `http://localhost:8080`

---

## 🔐 Autenticación y Autorización

### Roles del Sistema

| Rol | Permisos |
|-----|----------|
| **ADMIN** | Acceso completo: gestión de libros, usuarios y préstamos |
| **USUARIO** | Consultar libros y ver su historial de préstamos |

### Credenciales de Prueba

**Usuario Administrador:**
- Username: `admin`
- Password: `admin123`

### Flujo de Autenticación

1. **Registrar usuario** → `POST /api/auth/register`
2. **Iniciar sesión** → `POST /api/auth/login`
3. **Recibir JWT token** → Incluir en header de peticiones
4. **Usar token** → `Authorization: Bearer {token}`

---

## 📡 Endpoints de la API

### Autenticación (`/api/auth`)

| Método | Endpoint | Descripción | Acceso |
|--------|----------|-------------|--------|
| POST | `/api/auth/register` | Registrar nuevo usuario | Público |
| POST | `/api/auth/login` | Iniciar sesión | Público |

**Ejemplo - Registro:**
```json
POST /api/auth/register
{
  "username": "juan.perez",
  "email": "juan@universidad.edu",
  "password": "password123",
  "nombreCompleto": "Juan Pérez",
  "telefono": "987654321",
  "roles": ["USUARIO"]
}
```

**Ejemplo - Login:**
```json
POST /api/auth/login
{
  "username": "admin",
  "password": "admin123"
}
```

**Respuesta Login:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "admin",
  "email": "admin@biblioteca.com",
  "roles": ["ADMIN"]
}
```

### Libros (`/api/libros`)

| Método | Endpoint | Descripción | Rol Requerido |
|--------|----------|-------------|---------------|
| GET | `/api/libros` | Listar todos los libros | ADMIN, USUARIO |
| GET | `/api/libros/{id}` | Obtener libro por ID | ADMIN, USUARIO |
| POST | `/api/libros` | Crear nuevo libro | ADMIN |
| PUT | `/api/libros/{id}` | Actualizar libro | ADMIN |
| DELETE | `/api/libros/{id}` | Eliminar libro | ADMIN |
| GET | `/api/libros/buscar/titulo?titulo={texto}` | Buscar por título | ADMIN, USUARIO |
| GET | `/api/libros/buscar/autor?autor={nombre}` | Buscar por autor | ADMIN, USUARIO |
| GET | `/api/libros/buscar/categoria?categoria={cat}` | Buscar por categoría | ADMIN, USUARIO |

**Ejemplo - Crear Libro:**
```json
POST /api/libros
Authorization: Bearer {token}
{
  "isbn": "978-0134685991",
  "titulo": "Effective Java",
  "autor": "Joshua Bloch",
  "editorial": "Addison-Wesley",
  "categoria": "Programación",
  "anioPublicacion": 2018,
  "cantidadDisponible": 5,
  "cantidadTotal": 5,
  "descripcion": "Guía de mejores prácticas en Java"
}
```

### Préstamos (`/api/prestamos`)

| Método | Endpoint | Descripción | Rol Requerido |
|--------|----------|-------------|---------------|
| GET | `/api/prestamos` | Listar todos los préstamos | ADMIN |
| GET | `/api/prestamos/{id}` | Obtener préstamo por ID | ADMIN |
| POST | `/api/prestamos` | Registrar nuevo préstamo | ADMIN |
| PUT | `/api/prestamos/{id}/devolucion` | Registrar devolución | ADMIN |
| DELETE | `/api/prestamos/{id}` | Eliminar préstamo | ADMIN |
| GET | `/api/prestamos/usuario/{usuarioId}` | Préstamos de un usuario | ADMIN |
| GET | `/api/prestamos/mis-prestamos` | Mis préstamos | ADMIN, USUARIO |
| GET | `/api/prestamos/estado/{estado}` | Filtrar por estado | ADMIN |

**Ejemplo - Crear Préstamo:**
```json
POST /api/prestamos
Authorization: Bearer {token}
{
  "usuario": { "id": 2 },
  "libro": { "id": 1 },
  "fechaPrestamo": "2025-01-10",
  "fechaDevolucionPrevista": "2025-01-24",
  "observaciones": "Préstamo regular"
}
```

**Estados de Préstamo:**
- `ACTIVO` - Préstamo vigente
- `DEVUELTO` - Libro devuelto
- `VENCIDO` - Préstamo vencido
- `CANCELADO` - Préstamo cancelado

---

## 🗄️ Modelo de Base de Datos

### Diagrama de Entidades (Simplificado)

```
┌─────────────┐       ┌──────────────┐       ┌─────────────┐
│   Usuario   │       │   Préstamo   │       │    Libro    │
├─────────────┤       ├──────────────┤       ├─────────────┤
│ id          │───┐   │ id           │   ┌───│ id          │
│ username    │   │   │ usuario_id   │   │   │ isbn        │
│ email       │   └──<│ libro_id     │>──┘   │ titulo      │
│ password    │       │ fechaPrestamo│       │ autor       │
│ ...         │       │ fechaDevPrev │       │ ...         │
└─────────────┘       │ estado       │       └─────────────┘
       │              └──────────────┘
       │ M:N
       ▼
┌─────────────┐
│     Rol     │
├─────────────┤
│ id          │
│ nombre      │
│ descripcion │
└─────────────┘
```

### Relaciones Principales

- **Usuario ↔ Rol**: Muchos a Muchos
- **Usuario → Préstamo**: Uno a Muchos
- **Libro → Préstamo**: Uno a Muchos

---

## 🧪 Pruebas con Postman

### Configuración Inicial

1. Importar colección de pruebas (ver carpeta `/postman` si existe)
2. Crear variable de entorno `baseUrl` = `http://localhost:8080`
3. Crear variable de entorno `token` para guardar el JWT

### Flujo de Prueba Completo

**1. Registrar Usuario**
```
POST {{baseUrl}}/api/auth/register
Body: (ver ejemplo arriba)
```

**2. Login**
```
POST {{baseUrl}}/api/auth/login
Body: {"username": "admin", "password": "admin123"}
→ Guardar token de la respuesta
```

**3. Crear Libro (requiere token)**
```
POST {{baseUrl}}/api/libros
Headers: Authorization: Bearer {{token}}
Body: (ver ejemplo arriba)
```

**4. Listar Libros**
```
GET {{baseUrl}}/api/libros
Headers: Authorization: Bearer {{token}}
```

**5. Crear Préstamo**
```
POST {{baseUrl}}/api/prestamos
Headers: Authorization: Bearer {{token}}
Body: (ver ejemplo arriba)
```

**6. Ver Mis Préstamos**
```
GET {{baseUrl}}/api/prestamos/mis-prestamos
Headers: Authorization: Bearer {{token}}
```

---

## ✅ Validaciones Implementadas

### Libro
- ISBN: 10-20 caracteres, único
- Título: Obligatorio, máx 200 caracteres
- Autor: Obligatorio, máx 100 caracteres
- Año: Mayor a 1900
- Cantidad: No negativa

### Usuario
- Username: 3-50 caracteres, único
- Email: Formato válido, único
- Password: Mínimo 6 caracteres
- Nombre completo: Obligatorio

### Préstamo
- Usuario: Obligatorio
- Libro: Obligatorio, con disponibilidad
- Fechas: Validadas

---

## 🔧 Solución de Problemas Comunes

### Error: "Could not create connection to database"

**Solución:**
- Verificar que MySQL esté corriendo
- Revisar usuario/contraseña en `application.properties`
- Confirmar que existe la base de datos `biblioteca_universitaria`

### Error: "Port 8080 is already in use"

**Solución:**
Cambiar el puerto en `application.properties`:
```properties
server.port=8081
```

### Error: "Rol no encontrado"

**Solución:**
Los roles se crean automáticamente al iniciar la app gracias a `DataInitializer.java`. Si persiste, ejecuta:
```sql
INSERT INTO roles (nombre, descripcion) VALUES 
    ('ADMIN', 'Administrador del sistema'),
    ('USUARIO', 'Usuario estándar');
```

### Error: "JWT signature does not match"

**Solución:**
- Token expirado (válido 24 horas)
- Hacer login nuevamente para obtener nuevo token

---

## 📊 Dependencias del Proyecto

```xml
<!-- Principales dependencias en pom.xml -->
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-security
- spring-boot-starter-validation
- mysql-connector-j
- jjwt-api, jjwt-impl, jjwt-jackson (0.12.3)
- lombok
```

---

## 👥 Equipo de Desarrollo

- **Integrante 1**: [Nombre]
- **Integrante 2**: [Nombre]
- **Integrante 3**: [Nombre]

**Institución**: IDAT  
**Curso**: Desarrollo de los Componentes del Negocio  
**Fecha**: Enero 2025

---

## 📝 Licencia

Este proyecto es desarrollado con fines educativos para la Universidad IDAT.

---

## 📞 Soporte

Para reportar problemas o sugerencias:
- Crear un Issue en GitHub
- Contactar al equipo de desarrollo

---

## 🚀 Próximas Mejoras

- [ ] Implementar paginación en listados
- [ ] Agregar filtros avanzados
- [ ] Sistema de multas por retraso
- [ ] Notificaciones por email
- [ ] Reportes en PDF
- [ ] Dashboard administrativo

---

**¡Gracias por usar nuestro sistema! 📚✨**
