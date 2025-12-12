📚 API Biblioteca 

Proyecto desarrollado con Spring Boot, que implementa una API REST para la gestión de libros, incorporando seguridad con JWT, documentación con Swagger/OpenAPI y persistencia con MySQL.

El proyecto aplica buenas prácticas de arquitectura por capas:
Controller – Service – Repository – DTO – Mapper – Security.

🧑‍🎓 Contexto

Curso: [Desarrollo de los Componente del Negocio]

Tema: Desarrollo de API REST segura con Spring Boot
Tecnologías: Spring Boot, Spring Security, JWT, Swagger, MySQL
Tipo: Proyecto demostrativo / académico

🚀 Tecnologías Utilizadas

Java 21
Spring Boot 3.x
Spring Web
Spring Data JPA
Spring Security
JWT (JSON Web Token)
MySQL
Swagger / OpenAPI (springdoc-openapi)
Maven
Lombok

🏗️ Arquitectura del Proyecto

El proyecto sigue una arquitectura en capas:

Controller: Manejo de peticiones HTTP
Service: Lógica de negocio
Repository: Acceso a datos
DTO: Transferencia de datos
Mapper: Conversión entre entidades y DTOs
Security: Autenticación y autorización con JWT
Config: Configuraciones generales (Swagger)

📂 Estructura del Proyecto
src/main/java/com/biblioteca/api
│
├── controller        → Controladores REST
├── service           → Interfaces de negocio
│   └── impl           → Implementaciones
├── repository        → JPA Repositories
├── model             → Entidades JPA
├── dto               → DTOs de entrada y salida
├── mapper            → Conversión Entity ↔ DTO
├── security           → JWT y Spring Security
├── config             → Swagger / OpenAPI
└── exception          → Manejo global de errores

⚙️ Requisitos Previos

Antes de ejecutar el proyecto, el profesor debe tener instalado:

Java JDK 21
Maven 3.9+
MySQL Server
IDE (IntelliJ IDEA, Eclipse o VS Code)
Postman (opcional, para pruebas)

🗄️ Base de Datos
1️⃣ Crear la base de datos manualmente

La aplicación NO crea la base de datos automáticamente.
Debe crearse previamente en MySQL:

CREATE DATABASE biblioteca_db;


⚠️ Importante:
Las tablas sí se crean automáticamente al iniciar la aplicación (Hibernate).

2️⃣ Configuración de conexión

Archivo: src/main/resources/application.properties

spring.datasource.url=jdbc:mysql://localhost:3306/biblioteca_db
spring.datasource.username=root
spring.datasource.password=Palito2020

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect


📌 Nota académica
El usuario y password pueden cambiarse según el entorno del profesor.
No afecta la lógica del proyecto.

▶️ Ejecución del Proyecto

Desde la raíz del proyecto:

Opción 1: Maven Wrapper (Recomendado)
./mvnw spring-boot:run

Opción 2: Desde el IDE

Ejecutar la clase:

BibliotecaApiApplication.java

🔐 Autenticación (JWT)

La API utiliza JWT para proteger los endpoints.

Endpoints públicos:

POST /api/auth/register
POST /api/auth/login
Swagger (/swagger-ui/index.html)

🧪 Flujo de autenticación

1️⃣ Registrar usuario

POST /api/auth/register

{
"username": "admin",
"password": "admin123"
}


2️⃣ Login

POST /api/auth/login

{
"username": "admin",
"password": "admin123"
}


3️⃣ Se obtiene un token JWT válido por 24 horas

4️⃣ Enviar token en las peticiones protegidas:

Authorization: Bearer <TOKEN>

📘 Swagger / OpenAPI

Swagger permite probar todos los endpoints desde el navegador.

URL de acceso:
http://localhost:8080/swagger-ui/index.html


Desde Swagger se pueden:

Listar libros
Crear libros
Obtener libro por ID
Actualizar libro
Eliminar libro
Autenticarse con JWT

📌 El token se ingresa en el botón Authorize.

📚 Endpoints Principales
Libros
Método	Endpoint	Descripción
GET	/api/libros	Listar libros
GET	/api/libros/{id}	Obtener libro por ID
POST	/api/libros	Crear libro
PUT	/api/libros/{id}	Actualizar libro
DELETE	/api/libros/{id}	Eliminar libro
Ejemplo JSON – Crear Libro
{
"titulo": "La Ciudad y los Perros",
"autor": "Mario Vargas Llosa",
"anioPublicacion": 1963,
"cantidadDisponible": 10
}

⚠️ Manejo de Errores

La aplicación maneja errores comunes:

404 Not Found → Recurso no encontrado
400 Bad Request → Validaciones
401 Unauthorized → Token inválido
403 Forbidden → Acceso no permitido
500 Internal Server Error → Error del servidor

🧪 Pruebas

Swagger (principal)

Postman (alternativo)