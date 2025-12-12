📚 Biblioteca API – Backend con Spring Boot, JPA y JWT

Versión: 2.0
Integrantes: Roman Huaman Josled Luis Antonio  
             Velarde Robles Francisco Xavier Leon  
             Peña Chavez Gissel Melani  
             Osorio Guzman Jose Luis 
             Colina Martin Jesus Gabriel

Tecnologías: Java 21, Spring Boot 3, Spring Security, JPA/Hibernate, MySQL, JWT, Swagger

🚀 Descripción del Proyecto

La Biblioteca API es un backend robusto que gestiona:

Usuarios (con roles ADMIN y USER)

Libros (incluye activación/inactivación con soft delete)

Préstamos de libros (control de stock, fechas y estados)

Autenticación y autorización mediante JWT

Documentación dinámica con Swagger

El sistema está diseñado siguiendo buenas prácticas de arquitectura de software, separación por capas y validaciones basadas en anotaciones.

🧱 Arquitectura del Proyecto

El proyecto está organizado en una arquitectura modular y escalable:

src/main/java
│
├── config/          → Swagger, DataInitializer
├── controllers/     → Controladores REST
├── dtos/            → DTOs de entrada y salida
├── entity/          → Entidades JPA
├── exception/       → Manejo global de errores
├── mappers/         → MapStruct DTO ↔ Entity
├── repository/      → Repositorios JPA
├── security/        → JWT, filtros, reglas de acceso
├── services/        → Interfaces de servicios
└── services/impl/   → Implementaciones de lógica de negocio

🛠️ Tecnologías Utilizadas
Tecnología	   Uso
Spring Boot	   Base del proyecto
Spring Data JPA	   Persistencia ORM
Hibernate	   Implementación de JPA
Spring Security	   Autenticación y autorización
JWT	           Tokens con roles
MySQL	           Base de datos
Swagger / OpenAPI  Documentación interactiva
MapStruct	   Mapeo entre entidades y DTOs
Lombok	           Eliminación de boilerplate code

💾 Requisitos Previos

Antes de ejecutar este proyecto, necesitas:

Java 17+

Maven 3+

MySQL 8+

IDE (IntelliJ, VSCode, Eclipse)

Postman o acceso a Swagger

⚙️ Configuración de la Base de Datos

En el archivo application.properties se define la conexión:

spring.datasource.url=jdbc:mysql://localhost:3306/biblioteca?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true


⚠️ Importante: Modifica usuario y contraseña según tu entorno.

🔐 Seguridad: JWT y Roles

El sistema utiliza JWT para autenticación sin estado.
Los roles disponibles son:

ROLE_ADMIN

ROLE_USUARIO

Flujo de autenticación:

El usuario se registra o inicia sesión.

El backend genera un token JWT con:

token

email

nombre

roles

El token debe enviarse en cada request protegida:

Authorization: Bearer <token>

📘 Documentación con Swagger

Una vez levantado el servidor, ingresa a:

👉 http://localhost:8080/swagger-ui.html

👉 http://localhost:8080/v3/api-docs

Swagger permite:

Probar los endpoints

Agregar token JWT

Ver modelos de datos

Filtrar rutas según grupos

🔥 Endpoints Principales
🧑‍💼 Auth – Autenticación
Método	Ruta	Rol	Descripción
POST	/auth/login	Público	Devuelve JWT
POST	/auth/register	Público	Crea nuevo usuario

Ejemplo Login (JSON):

{
  "email": "admin@gmail.com",
  "password": "123456"
}

📚 Libros
Método	Ruta	Rol	Descripción
GET	/libros	ADMIN / USER	Lista libros (solo activos para USER)
GET	/libros/{id}	ADMIN / USER	Obtener libro
POST	/libros	ADMIN	Crear libro
PUT	/libros/{id}	ADMIN	Actualizar
DELETE	/libros/{id}	ADMIN	Inactivar (soft delete)
PUT	/libros/activar/{id}	ADMIN	Reactivar

Ejemplo Crear Libro:

{
  "titulo": "Clean Code",
  "autor": "Robert C. Martin",
  "isbn": "1234567890",
  "totalEjemplares": 10,
  "disponibles": 10
}

📘 Préstamos
Método	Ruta	Rol	Descripción
POST	/prestamos/admin	ADMIN	Crear préstamo
PUT	/prestamos/admin/{id}/devolver	ADMIN	Registrar devolución
GET	/prestamos/mios	USER/ADMIN	Ver préstamos propios
GET	/prestamos/admin	ADMIN	Ver todos

👤 Usuarios
Método	Ruta	Rol	Descripción
GET	/usuarios	ADMIN	Lista todos
GET	/usuarios/{id}	ADMIN	Obtener usuario
GET	/usuarios/email/{email}	ADMIN	Buscar por email

🧪 Pruebas y Evidencias
✔ Pruebas manuales con Swagger

Se recomienda probar:

Registro de un usuario

Login → copiar token

Probar rutas públicas

Probar rutas protegidas con token

Ver diferencias entre permisos USER y ADMIN

✔ Pruebas automatizadas (opcional)

Ya existe clase base BibliotecaApiApplicationTests.java.

🧩 Modelo Entidad-Relación

Relaciones principales:

Usuario — Rol: ManyToMany

Usuario — Prestamo: OneToMany

Libro — Prestamo: OneToMany

✔ Modelo normalizado
✔ Uso de llaves foráneas
✔ Restricciones de unicidad (email, isbn)

🧰 Ejecución del Proyecto
1️⃣ Clonar el repositorio
git clone https://github.com/tuusuario/biblioteca-api.git

2️⃣ Configurar base de datos

Crear BD llamada biblioteca o dejar que Hibernate la cree automáticamente.

3️⃣ Ejecutar
mvn spring-boot:run

4️⃣ Acceder a la API

http://localhost:8080/swagger-ui.html

🏆 Características Destacadas

✔ Seguridad avanzada con JWT
✔ Control de acceso granular por rol
✔ Soft delete para libros
✔ Relaciones complejas bien implementadas
✔ Manejo global de excepciones
✔ DTOs + Mappers profesionales
✔ Documentación automática con Swagger
✔ Código limpio y mantenible