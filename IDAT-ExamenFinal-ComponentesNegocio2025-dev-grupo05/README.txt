MANUAL TÉCNICO Y DE USUARIO
Sistema de Gestión de Bibliotecas (API RESTful)
Curso: Desarrollo de los Componentes del Negocio
Profesor: Eloy Ivan Solano Coello
Grupo: 5

- Josmel Montoya Paiva
- Guillermo Arturo Ugaz Montesinos
- Alexander jose illescas flores
- Cris Angelo Rivera Cardenas

1. DESCRIPCIÓN DEL PROYECTO
Las universidades suelen gestionar el préstamo de libros y la administración de sus bibliotecas de forma manual o mediante software limitado, lo que dificulta el control eficiente de los recursos y usuarios. Ante esta necesidad, se requiere una API RESTful robusta y segura que permita a administradores y usuarios consultar el catálogo de libros, registrar préstamos y devoluciones, gestionar cuentas de usuario y controlar el acceso a funcionalidades según los roles asignados (ej. ADMIN, USUARIO).
Por ello, como parte del equipo de desarrollo, se te ha asignado, junto a otros dos integrantes el desarrollo un backend completo utilizando Java y Spring Boot, a fin de facilitar la gestión integral de bibliotecas universitarias, incorporando almacenamiento en una base de datos relacional y mecanismos de seguridad mediante autenticación JWT, que permita el registro de libros, usuarios, préstamos y devoluciones.
1. TECNOLOGÍAS UTILIZADAS
Lenguaje: Java 21
Framework: Spring Boot 3.3.x
Seguridad: Spring Security + JWT (JJWT) – Autenticación Stateless
Base de Datos: MySQL 8
ORM: Spring Data JPA + Hibernate
Documentación: OpenAPI (Swagger UI)

2. ARQUITECTURA Y FUNCIONAMIENTO DEL CÓDIGO
El proyecto está desarrollado bajo una arquitectura en capas, lo que permite una clara separación de responsabilidades, facilitando la escalabilidad, el mantenimiento y la seguridad del sistema.
2.1 Capa de Modelo (Entities)
Representa las entidades persistentes del sistema y su mapeo con la base de datos:
•	Usuarios:
o	Mantiene relación Many-to-One con Roles (rol_id).
o	Mantiene relación One-to-Many con refres_tokens (usuario_id).
o	Almacena credenciales protegidas mediante encriptación.
o	Contiene: ID, nombre, apellido, email, password, rol, activo, fecha registro, ultima actualizacion.
•	Libros:
o	Mantiene relación One to Many con Prestamos (libro_id)
o	Contiene: ID, titulo, autor, editorial, categoría, cantidad_total, cantidad disponible, activo.
•	Préstamos:
o	Mantiene una relación Many-to-One con Usuario y Libro (usuario_id y libro_id).
o	Contiene: ID, usuario_id, libro_id, fecha_prestamo, fecha_devolucion, fecha_devolucion_esperada, fecha_devolucion_real, estado.

2.2 Capa de Repositorio (Repository)
Se implementa mediante interfaces que extienden de JpaRepository.
•	Permite la interacción directa con la base de datos MySQL sin necesidad de escribir consultas SQL manuales.
2.3 Capa de Servicio (Service)
Contiene la lógica de negocio del sistema.
2.4 Capa de Seguridad (Security
Gestiona la autenticación y autorización de los usuarios.
•	JwtFilter:
o	Intercepta cada solicitud HTTP.
o	Extrae el token del encabezado Authorization, lo valida y establece la autenticación en el contexto de Spring.
•	SecurityConfig:
o	Restringe el acceso a recursos según roles:
	ADMIN: gestión de libros y préstamos.
	USUARIO: consulta de historial personal.

2.5 Manejo de Excepciones (GlobalExceptionHandler)
•	Se implementó un @RestControllerAdvice para capturar excepciones de validación y errores de negocio.
•	Devuelve respuestas JSON claras y controladas, evitando la exposición de trazas internas del servidor.

3. GUÍA DE INSTALACIÓN Y EJECUCIÓN
1. Base de Datos
En MySQL Workbench, ejecutar:
CREATE DATABASE biblioteca_db;
2. Configuración
Editar el archivo src/main/resources/application.properties:
spring.datasource.url=jdbc:mysql://localhost:3306/biblioteca_db
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_CONTRASEÑA
3. Ejecución
Desde la terminal o IDE:
./mvnw spring-boot:run

4. DOCUMENTACIÓN INTERACTIVA (SWAGGER)
La API cuenta con documentación automática basada en el estándar OpenAPI.
•	URL: http://localhost:8080/swagger-ui.html
•	Procedimiento de uso:
1.	Obtener un token desde el endpoint /login.
2.	Hacer clic en el botón Authorize.
3.	Ingresar Bearer <TU_TOKEN>.
4.	Probar los endpoints directamente desde el navegador.

5.- ENDPOINTS API REST

I. Acceso Público (PermitAll)
(Rutas que no requieren token JWT)
Método	Ruta (Endpoint)	Descripción
POST	/v1/auth/login	Autenticación del usuario para obtener el token JWT.
POST	/v1/auth/register	Registro de nuevos usuarios en el sistema (asumido).
GET	/v3/api-docs/**	Acceso a la documentación de Swagger/OpenAPI.
GET	/swagger-ui/**	Interfaz gráfica de Swagger UI.
GET	/swagger-ui.html	Página principal de la documentación.
________________________________________
II. Acceso Compartido (ROLE_ADMIN & ROLE_USER)
(Rutas que requieren un token JWT válido con cualquiera de los dos roles)
Método	Ruta (Endpoint)	Componente	Descripción
GET	/v1/libros	Libro	Recuperar la lista completa de libros.
GET	/v1/prestamos/historial	Préstamo	Ver el historial de préstamos del usuario (o global).
GET	/v1/libros/{id}	Libro	Obtener un libro por su ID.
________________________________________
III. Acceso Exclusivo (ROLE_ADMIN)
(Rutas que requieren un token JWT válido con el rol ADMIN)
Método	Ruta (Endpoint)	Componente	Descripción
POST	/v1/libros	Libro	Registrar un nuevo libro en el sistema.
PUT	/v1/libros/{id}	Libro	Actualizar los datos de un libro existente.
DELETE	/v1/libros/{id}	Libro	Eliminar un libro del sistema.
POST	/v1/prestamos	Préstamo	Crear un nuevo registro de préstamo.
PUT	/v1/prestamos/{id}/devolver	Préstamo	Marcar un préstamo como devuelto.

8.- GITHUB

https://github.com/UTrujilloII/IDAT-ExamenFinal-ComponentesNegocio2025/tree/dev-grupo05
