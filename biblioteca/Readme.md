# API Gestión de Biblioteca - IDAT

**Evaluación Final: Desarrollo de los Componentes del Negocio**  
**Curso**: Desarrollo de los Componentes del Negocio  
**Carrera**: Desarrollo de Sistemas Front-end y Back-end (EEST Virtual)  
**Docente**: Solano Coello Eloy Ivan  
**Grupo 3**  
**Integrantes**:
- Siri Vergara, María Alejandra
- Valdiviezo Atero, Yoli Jhunior
- Santisteban Manrique, Adrián Daniel
- Villasante Contreras, Jean Paul

**Fecha**: Diciembre 2025  
**Versión**: 1.0

---

## Tecnologías Utilizadas

| Tecnología                  | Versión / Detalle                |
|--------------------------------|----------------------------------|
| Lenguaje                       | Java 21                          |
| Framework                      | Spring Boot 3.5.7                |
| Seguridad                      | Spring Security + JJWT (JWT)     |
| Base de Datos                  | MySQL 8                          |
| ORM                            | Spring Data JPA + Hibernate      |
| Reducción de boilerplate       | Lombok                           |
| Validaciones                   | Spring Validation + Bean Validation |
| Documentación API               | OpenAPI (Swagger UI)             |
| Pruebas                        | Postman                          |

---

## Contexto del Proyecto

Se desarrolló una **API RESTful segura y robusta** para la gestión integral de una biblioteca digital/peruana, permitiendo administrar usuarios, libros, préstamos y devoluciones con reglas de negocio reales y un sistema de autenticación basado en roles (ADMIN y USER).

El sistema resuelve problemas comunes en bibliotecas tradicionales como:
- Pérdida de control de stock de libros
- Préstamos sin seguimiento
- Duplicidad de usuarios
- Acceso no autorizado a funciones administrativas

---

## Logros Alcanzados

- Autenticación segura con JWT y roles (ADMIN / USER)
- Arquitectura en capas limpia: Controller → Service → Repository
- Validaciones de unicidad (DNI, email, teléfono)
- Reglas de negocio avanzadas (no eliminar usuario con préstamos activos/vencidos, control de stock)
- Manejo global de excepciones con respuestas HTTP correctas (409, 400, 404)
- Documentación interactiva con Swagger UI
- Persistencia real con MySQL + JPA

-----------------------------
### 3. GUÍA DE INSTALACIÓN Y EJECUCIÓN

**Base de Datos:**  
Abrir MySQL Workbench y ejecutar:

```sql
CREATE DATABASE biblioteca_db;
Configuración:
En el archivo src/main/resources/application.properties, actualizar:
propertiesspring.datasource.url=jdbc:mysql://localhost:3306/biblioteca_db
spring.datasource.username=TU_USUARIO (root)
spring.datasource.password=TU_CONTRASEÑA (root)

para ejecutar solo darle click en la clase principal

------------------------------

## Estructura del Proyecto
src/main/java/pe/edu/idat/biblioteca/
├── config/              → Configuración de Security, JWT, Swagger
├── controller/          → Controladores REST (Usuario, Libro, Prestamo, Auth)
├── dto/                 → Objetos de transferencia (request/response)
├── exception/           → GlobalExceptionHandler
├── model/               → Entidades JPA (Usuario, Libro, Prestamo)
├── repository/          → Repositorios JPA
├── service/             → Lógica de negocio (impl)
├── security/            → Filtros JWT, configuraciones de autorización
└── BibliotecaApplication.java


---

## Roles del Sistema

| Rol    | Permisos Principales |
|-------|-----------------------|
| `ADMIN` | CRUD completo de usuarios, libros y préstamos<br>Registrar devoluciones<br>Eliminar usuarios (solo si no tiene préstamos pendientes) |
| `USER`  | Buscar libros<br>Ver su historial de préstamos<br>Autenticarse |

---

## Endpoints Principales (Swagger)

Accede a la documentación completa en:  
http://localhost:9596/swagger-ui/index.html

### Autenticación
| Método | Ruta                | Descripción                  |
|--------|---------------------|------------------------------|
| POST   | `/api/auth/login`   | Login → devuelve JWT         |
| POST   | `/api/auth/register`| Registro (solo ADMIN)        |

### Usuarios (ADMIN)
| Método | Ruta                     | Descripción                     |
|--------|--------------------------|---------------------------------|
| GET    | `/api/usuarios`          | Listar todos                    |
| POST   | `/api/usuarios`          | Crear usuario                   |
| DELETE | `/api/usuarios/{id}`     | Eliminar (valida préstamos)     |

### Libros (ADMIN)
| Método | Ruta               | Descripción               |
|--------|--------------------|---------------------------|
| POST   | `/api/libros`      | Crear libro               |
| PUT    | `/api/libros/{id}` | Actualizar                |
| DELETE | `/api/libros/{id}` | Eliminar                  |

### Préstamos
| Método | Ruta                     | Descripción                        |
|--------|--------------------------|------------------------------------|
| POST   | `/api/prestamos`         | Registrar préstamo (valida stock)  |
| PUT    | `/api/prestamos/{id}/devolver` | Devolver libro → repone stock |
| GET    | `/api/prestamos/mis/mis-prestamos` | Historial del usuario logueado |

---

## Reglas de Negocio Implementadas

| Regla                                   | Código HTTP | Mensaje Ejemplo                              |
|-----------------------------------------|-------------|-----------------------------------------------|
| Email/DNI/Teléfono duplicado            | 409 Conflict| "El email ya está registrado"                |
| No hay stock del libro                  | 400 Bad Request | "No hay stock disponible del libro"       |
| Usuario tiene préstamos activos/vencidos| 400 Bad Request | "No se puede eliminar: tiene préstamos pendientes" |
| Token inválido o expirado               | 401 Unauthorized | -                                          |
| Acceso denegado por rol                 | 403 Forbidden | -                                          |
| Recurso no encontrado                    | 404 Not Found | "Usuario no encontrado"                     |

---

## Ejemplos de Pruebas con Postman (Capturas en /docs)

- Login exitoso → token JWT
- Registro duplicado → 409 Conflict
- Préstamo sin stock → 400 Bad Request
- Eliminación de usuario con préstamos → bloqueado
- Devolución exitosa → stock repuesto
- Acceso denegado a USER intentando crear usuario → 403

---

## Configuración de la Aplicación (`application.properties`)

```properties
server.port=9596
spring.datasource.url=jdbc:mysql://localhost:3306/biblioteca_idat
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT
jwt.secret=MjIwNTJkZjI4NTExNGMzMWFjMTM2MjE5NTk0OTI0ZTk2MjI1YjgwMTAxYjRhZjhmYjAwM2Y2ZDJkNjM3MTZjMQ
jwt.expiration=86400000