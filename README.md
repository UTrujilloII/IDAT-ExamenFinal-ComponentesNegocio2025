
## Cómo levantar el proyecto

    Desde Maven:
        mvn clean install
        mvn spring-boot:run


# API Gestión de Biblioteca Universitaria

Microservicio REST desarrollado con **Spring Boot 3 / Java 21** que permite gestionar:

- Libros y sus categorías
- Usuarios y roles (**ADMIN / USUARIO**)
- Préstamos y devoluciones de libros
- Autenticación y autorización con **JWT**
- Documentación interactiva con **Swagger / OpenAPI**

---

##  Tecnologías principales

- Java 21
- Spring Boot (Web, Data JPA, Security, Validation)
- MySQL / MariaDB
- JWT (**io.jsonwebtoken**)
- Lombok
- MapStruct
- Springdoc OpenAPI (Swagger UI)

---

## ⚙️ Configuración del proyecto

### 1. Clonar el repositorio

```bash

git clone <URL_DEL_REPO>
cd ms-gestion-ventas



2. Requisitos

    JDK 21 instalado
    Maven 3.x
    MySQL o (puede ser Docker)



3. Configurar application.properties

Archivo: src/main/resources/application.properties

spring.application.name=ms-gestion-ventas
server.port=9090

# Base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/biblioteca_db?serverTimezone=America/Lima&createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=12345

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# JWT
# Clave secreta (mínimo 32 caracteres, alfanumérica)
    jwt.secret=LIB2025_BIBLIOTECA_BACKEND_IDAT_SEGURA_01
# Duración del token: 5 minutos (en milisegundos)
    jwt.expiration=300000

# Swagger
springdoc.swagger-ui.path=/swagger-ui.html


Nota: la llave jwt.secret debe ser larga y difícil de adivinar.
Puedes dejar la que está o cambiarla por otra alfanumérica de 32+ caracteres.


️Base de datos: modelo y script

La base de datos se llama biblioteca_db y está normalizada con las siguientes tablas:

            categoria
            libros
            usuarios
            roles
            usuario_roles
            prestamos

Relaciones principales:
    usuario_roles (tabla intermedia N:M entre usuario y rol)
    prestamos (relaciona usuario–libro)
    
    Relaciones principales:
        libros.categoria_id → categoria.id_categoria
        prestamos.libro_id → libros.id_libro
        prestamos.usuario_id → usuarios.id_usuario
        usuario_roles.usuario_id → usuarios.id_usuario
        usuario_roles.rol_id → roles.id


Script SQL

En el repositorio debe ir un archivo, por ejemplo:
    database/biblioteca_db.sql
Con el contenido que incluye:
    Creación de tablas (categoria, libros, usuarios, roles, usuario_roles, prestamos)
    Inserción de datos de ejemplo (categorías, libros, usuarios y préstamos)
    Índices, AUTO_INCREMENT y claves foráneas


   El script contiene:

    7 categorías
    Varios libros de ejemplo
    Usuarios y roles con contraseñas encriptadas (BCrypt)
    Préstamos de prueba
    
    Para restaurar la BD con phpMyAdmin:
        Crear la BD vacía biblioteca_db (si no existe).
        Ir a Importar → seleccionar el archivo biblioteca_db.sql.
        Ejecutar.


Para restaurar por consola:
    mysql -u root -p biblioteca_db < biblioteca_db.sql



 Usuarios de prueba

    En la tabla usuarios ya existen algunos registros con la contraseña encriptada con BCrypt.
    Para la documentación se sugiere trabajar con dos usuarios de prueba:

1. Administrador
    Rol: ADMIN
    TablaS:
        usuarios.username = 'admin'
        usuario_roles lo vincula con roles.nombre = 'ADMIN'
    
2. Usuario normal
    Rol: USUARIO
    TablaS:
        usuarios.username = 'usuario'
        usuario_roles lo vincula con roles.nombre = 'USUARIO'
    
    
| Rol      | Username   | Password    | Descripción               |
|----------|------------|-------------|---------------------------|
| ADMIN    | admin      | admin123    | Administrador Biblioteca  |
| USUARIO  | nuevoUser42  | nuevo123  | Usuario de prueba   |

        Las contraseñas en la base de datos están encriptadas con BCrypt,
        pero para probar la API puedes usar los textos planos indicados arriba.

 ¿Cómo obtener un token JWT (Swagger o Postman)?

1. Ir al endpoint de login:

   POST /api/auth/login`

2. En el body JSON enviar, admin:

   **Administrador:**
   ```json
   {
     "username": "admin",
     "password": "admin123"
   }

La respuesta tendrá un campo similar a:

{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}

Usar el token en Swagger
        
       1. Crear una colección para la API de biblioteca.
       2. Pulsar el botón Authorize.
       3. Escribir:
       
       
            Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

Usar el token en Postman

        En cada petición protegida agregar el header:
        
            Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

        Agregar la petición POST http://localhost:9090/api/auth/login
            con el body JSON (por ejemplo, admin o nuevoUser42).

        Copiar el token del response.

        En cada petición protegida (libros, préstamos, etc.), agregar el header:
            Authorization: Bearer <tu_token_jwt>
        
        Probar los distintos escenarios:
            Como ADMIN: crear/editar/eliminar libros, listar todos los préstamos.
            Como USUARIO: crear préstamos, ver mis préstamos, devolver libros.


    Autenticación y JWT
    
    La autenticación se maneja con JWT:
    Generación y validación del token en JwtUtils.
        Filtro de seguridad: JwtAuthenticationFilter (extiende OncePerRequestFilter).
        Configuración de rutas protegidas en SecurityConfig.
        Manejo de errores y mensajes amigables en GlobalExceptionHandler.
    Duración del token:
        Propiedad: jwt.expiration=300000 → 5 minutos
        Si el token expira, se lanza un mensaje del estilo:
            "El token JWT ha expirado. Vuelve a iniciar sesión."



    
    
    Desde IntelliJ / IDE
        Importar como proyecto Maven.
        Esperar a que descargue dependencias.
        Ejecutar la clase:
    com.proyecto.idat.ms_gestion_ventas.MsGestionVentasApplication
        La API quedará disponible en:
        http://localhost:9090


Swagger / OpenAPI

    El proyecto expone la documentación interactiva de la API con Springdoc OpenAPI.
    
    URL:
        http://localhost:9090/swagger-ui.html
    En Swagger puedes:
        Probar el endpoint de login.
        Copiar el token.
        Pulsar el botón Authorize (candadito).
    Pegar:
        Bearer <tu_token_jwt>
    Ejecutar los demás endpoints autenticados.


    Endpoints principales
    
    1. Autenticación (AuthController)
    Base: /api/auth
    POST /api/auth/register
        Registra un nuevo usuario con rol USUARIO.
    POST /api/auth/login
        Recibe username y password y devuelve un JWT.
    POST /api/auth/refresh (si lo tienes implementado)
        Permite renovar el token.



2. Libros (LibroController)

    Base: /api/libros
    GET /api/libros
        Lista todos los libros (cualquier usuario autenticado).
    GET /api/libros/{id}
        Obtiene el detalle de un libro por ID.
    POST /api/libros (solo ADMIN)
        Crea un nuevo libro.
        Usa LibroRequest con validaciones (@NotBlank, @Min, etc.).
    PUT /api/libros/{id} (solo ADMIN)
        Actualiza un libro existente.
        Recalcula ejemplares_disponibles respetando los préstamos activos.
    DELETE /api/libros/{id} (solo ADMIN)
        Elimina un libro solo si no tiene préstamos asociados.


 Validaciones importantes de Libros

    En el DTO LibroRequest se aplican las siguientes validaciones:

        titulo: obligatorio (@NotBlank).
        autor: obligatorio (@NotBlank).
        isbn:
            Obligatorio (@NotBlank).
            Solo números.
            Debe tener exactamente 10 o 13 dígitos.
            Validado con @Pattern(regexp = "^(\\d{10}|\\d{13})$").
        fechaPublicacion:
            No puede ser futura (@PastOrPresent).
        ejemplaresTotales:
            Obligatorio (@NotNull).
            Mínimo 1 (@Min(1)).
        categoriaId:
            Obligatoria (@NotNull).
        En la entidad Libro:
            La columna isbn es única y no nula:
            
    @Column(name = "isbn", nullable = false, unique = true, length = 13)
    private String isbn;
 
 De esta forma evitamos duplicados de ISBN y garantizamos el formato correcto.      
            
3. Categorías (CategoriaController)

    Base: /api/categorias
    GET /api/categorias
    GET /api/categorias/{id}
    POST /api/categorias (ADMIN)
        Crea una nueva categoría.
        Cada libro guarda una FK categoria_id que apunta a categoria.id_categoria.


4. Préstamos (PrestamoController)

    4.1 Préstamos para USUARIO autenticado
        POST /api/prestamos/mis-prestamos
        Crea un préstamo para el usuario autenticado.

            Valida stock.
            Fecha de devolución > fecha actual.
            Estado inicial: ACTIVO.

        GET /api/prestamos/mis-prestamos
        Lista los préstamos del usuario autenticado.

        PUT /api/prestamos/mis-prestamos/{idPrestamo} (NUEVO)
        Actualiza un préstamo ACTIVO del propio usuario:

            Permite cambiar la fecha de devolución.
            Permite cambiar de libro, ajustando ejemplaresDisponibles.

        DELETE /api/prestamos/mis-prestamos/{idPrestamo} (NUEVO)
        Cancela un préstamo ACTIVO del propio usuario:

            Devuelve el ejemplar al stock.
            El préstamo se elimina.

        POST /api/prestamos/mis-prestamos/{idPrestamo}/devolver
        Marca el préstamo del usuario como DEVUELTO y actualiza stock.




4.2 Préstamos para ADMIN

    GET /api/prestamos (ADMIN)
    Lista todos los préstamos.

        POST /api/prestamos (ADMIN)
        Crea un préstamo para cualquier usuario (PrestamoRequest).

    PUT /api/prestamos/{idPrestamo} (ADMIN) (NUEVO)
    Actualiza un préstamo:

        Puede cambiar el usuario asociado.
        Puede cambiar el libro (ajustando stock).
        Puede actualizar la fecha de devolución.

    DELETE /api/prestamos/{idPrestamo} (ADMIN) (NUEVO)
    Elimina un préstamo:

        Si está ACTIVO, devuelve el ejemplar al stock.

    POST /api/prestamos/{idPrestamo}/devolver (ADMIN)
    Marca cualquier préstamo como DEVUELTO.
    
5. Gestión de bloqueos y seguridad adicional (UsuarioAdminController y utilidades)

     Base: /api/usuarios
     5.1 Desbloqueo manual por ADMIN
         POST /api/usuarios/{username}/desbloquear
     Solo accesible por usuarios con rol ADMIN.
     Lógica:
          Si el usuario tiene bloqueo por intentos fallidos, se limpia.
          Si existe bloqueo global por múltiples tokens inválidos, también se resetea.
     
     Respuesta ejemplo cuando sí había bloqueo:
            {
              "mensaje": "Usuario desbloqueado con éxito",
              "username": "nuevoUser42"
            }

      Respuesta ejemplo cuando no había bloqueo activo:.
      
            {
              "mensaje": "El usuario no tenía un bloqueo activo, no fue necesario desbloquearlo",
              "username": "nuevoUser42"
            }
     5.2 Listar usuarios bloqueados 
     
            GET /api/usuarios/bloqueados
            
     Solo accesible por ADMIN.
     Devuelve la lista de usuarios bloqueados con los minutos restantes de bloqueo.
     Respuesta ejemplo:
     
           {
          "cantidad": 2,
          "usuarios": {
            "usuario82": 18,
            "nuevoUser42": 5
            }
           }

     5.3 Endpoint de prueba de autenticación (opcional).
          GET /api/test/me
 
     Devuelve información básica del usuario autenticado (username y roles).
     Útil para probar tokens de ADMIN y USUARIO.
     
     Ejemplo:
        admin -> [ROLE_ADMIN]


6. Gestión de usuarios por ADMIN (UsuarioAdminController)
    
    Base: /api/usuarios
    POST /api/usuarios/admin

    Protegido con hasRole('ADMIN').
    Valida:

        Username único.
        Email único.
        Rol válido: ADMIN o USUARIO.

    Crea un registro en usuarios y vincula el rol en usuario_roles.
    
Body de ejemplo:

                {
          "username": "usuario_test1",
          "password": "usuario123",
          "nombreCompleto": "Usuario de Prueba Uno",
          "email": "usuario.test1@correo.com",
          "rol": "USUARIO"
                    }

Respuesta de ejemplo:

                 {
          "mensaje": "Usuario con rol USUARIO creado con éxito",
          "usuario": {
            "idUsuario": 11,
            "username": "usuario_test1",
            "nombre": "Usuario de Prueba Uno",
            "email": "usuario.test1@correo.com",
            "roles": [
              "USUARIO"
            ]
          }
          }
 

    6.2 Listar todos los usuarios (ADMIN + USUARIO)

            GET /api/usuarios
                Solo accesible por ADMIN.
                Devuelve un objeto con el conteo total y la lista de usuarios.

    Ejemplo: 

                
                       {
              "totalUsuarios": 6,
              "usuarios": [
                {
                  "idUsuario": 1,
                  "username": "admin",
                  "nombre": "Administrador Biblioteca",
                  "email": "admin@biblioteca.com",
                  "roles": ["ADMIN"]
                },
                {
                  "idUsuario": 2,
                  "username": "nuevoUser42",
                  "nombre": "Usuario de Prueba",
                  "email": "nuevo@biblioteca.com",
                  "roles": ["USUARIO"]
                }
              ]
            }
    
    6.3 Listar solo usuarios con rol ADMIN
    
            GET /api/usuarios/admins
 
                    
                    {
          "totalUsuarios": 2,
          "usuarios": [
            {
              "idUsuario": 1,
              "username": "admin",
              "nombre": "Administrador Biblioteca",
              "email": "admin@biblioteca.com",
              "roles": ["ADMIN"]
            },
            {
              "idUsuario": 13,
              "username": "admin_2",
              "nombre": "Admin SEGUNDO",
              "email": "admin.swagger@correo.com",
              "roles": ["ADMIN"]
            }
          ]
        }


    6.4 Listar solo usuarios con rol USUARIO
            
            GET /api/usuarios/usuarios
                      
                        {
          "totalUsuarios": 4,
          "usuarios": [
            {
              "idUsuario": 8,
              "username": "usuario82",
              "nombre": "USUARIO PROM2",
              "email": "usuario@biblioteca40.com",
              "roles": ["USUARIO"]
            }
          ]
        }
    6.5  Actualizar cuentas

        PUT /api/usuarios/mi-cuenta (usuario autenticado) (NUEVO)
        Permite a cualquier usuario (ADMIN o USUARIO) actualizar su propia cuenta:

            Verifica contraseña actual.
            Permite cambiar username, nombreCompleto, email y opcionalmente la contraseña.

        PUT /api/usuarios/{idUsuario} (ADMIN) (NUEVO)
        Actualiza los datos de cualquier usuario:

            Valida que no haya OTRO usuario con el mismo username o email.            
            Password es opcional (solo se cambia si viene).

    6.6  Eliminar usuarios (NUEVO)

        DELETE /api/usuarios/{idUsuario} (ADMIN)
        Reglas de negocio:

        No se permite eliminar la cuenta principal admin.
        No se puede eliminar un usuario que tenga préstamos registrados 
        (se devuelve un error de negocio indicando que primero debe gestionar sus préstamos).

Seguridad adicional: bloqueo por intentos y tokens

    Además de la autenticación básica con JWT, el sistema implementa mecanismos extra de seguridad.

    Bloqueo por tokens JWT inválidos / expirados POR USUARIO (no global) 

        Clase: JwtUtils.
            Lleva un contador por username de errores de token:

                Token expirado.
                Firma inválida.
                Token mal formado / vacío.

    Reglas:

        Después de 3 tokens inválidos/expirados para el mismo usuario:
            Se bloquea el uso de token para ese usuario durante 30 minutos.
        Mientras el bloqueo sigue activo:
            Cualquier token de ese usuario responde con un mensaje indicando cuántos minutos faltan.
        El usuario admin no se bloquea por token.

Listado de bloqueos y desbloqueo por ADMIN 

    GET /api/usuarios/bloqueados (ADMIN)
    Devuelve:

        Usuarios bloqueados por intentos de login.
        Usuarios bloqueados por tokens JWT inválidos (con minutos restantes).

    POST /api/usuarios/{username}/desbloquear (ADMIN)

        Desbloquea al usuario en BloqueoTokenService.
        Desbloquea el bloqueo por token en JwtUtils.


Flujo de prueba 

    Restaurar la BD con el script biblioteca_db.sql.
    Levantar la aplicación (mvn spring-boot:run).
    Abrir Swagger: http://localhost:9090/swagger-ui.html.
    Login:
        Ir a POST /api/auth/login.
        Enviar JSON con uno de tus usuarios (admin o nuevoUser42).
        Copiar el token del response.
    
    Pulsar Authorize → pegar Bearer <token>.
    Probar:
        Como USUARIO:
            Listar libros.
            Crear préstamo.
            Ver “mis préstamos”.
            Devolver un préstamo.
            Actualizar un préstamo ACTIVO. 
            Cancelar un préstamo ACTIVO. 
    
        Como ADMIN:
            Crear, editar y eliminar libros.
            Listar todos los préstamos.
            Actualizar y eliminar préstamos de cualquier usuario.
            Crear usuarios ADMIN/USUARIO (POST /api/usuarios/admin).
            Listar usuarios bloqueados (si se habilita el endpoint).
            Actualizar cuentas de usuario. 
            Eliminar usuarios sin prestamos / cuenta en desuso. 
            Ver usuarios bloqueados y desbloquearlos.
            Desbloquear usuarios con POST /api/usuarios/{username}/desbloquear.
            Listar usuarios:
                        GET /api/usuarios
                        GET /api/usuarios/admins
                        GET /api/usuarios/usuarios

Manejo de errores

    Todos los errores se unifican mediante GlobalExceptionHandler:
    
    400 – Error de validación (MethodArgumentNotValidException)
    400 – Error de negocio (ReglaNegocioException)
    404 – Recurso no encontrado (RecursoNoEncontradoException)
    400 – Integridad de datos (DataIntegrityViolationException)
    401 – Token expirado / inválido (gestionado en JwtUtils + JwtAuthenticationFilter)


La respuesta de error sigue una estructura tipo:


{
  "timestamp": "2025-11-29T01:23:45Z",
  "status": 400,
  "error": "Error de negocio",
  "message": "No se puede eliminar el libro porque tiene préstamos asociados.",
  "errors": null
}


Notas finales

    El token JWT dura 5 minutos y luego obliga a iniciar sesión nuevamente.
    Los datos de ejemplo de la BD permiten probar todos los casos:
        Libros con distintas categorías.
        Usuarios con roles distintos.
        Préstamos pendientes y devueltos.
    El ISBN está validado para que tenga solo números y longitud 10 o 13, además de ser único en base de datos.

    Este README resume la funcionalidad actual del proyecto, incluyendo:
        Gestión de libros, categorías y préstamos.
        Registro y login con JWT.
        Bloqueo de usuarios por intentos fallidos y tokens inválidos.
        Creación y listado de usuarios por ADMIN (ADMIN / USUARIO).
        Validaciones de negocio clave (ej. ISBN, ejemplares, préstamos activos).