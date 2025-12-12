# 📚 Sistema de Gestión de Biblioteca Universitaria - MS-Biblioteca

[![Java](https://img.shields.io/badge/Java-21-orange?style=flat&logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen?style=flat&logo=spring)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=flat&logo=mysql)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Sistema completo de gestión de biblioteca universitaria desarrollado con Spring Boot, que permite administrar libros, préstamos y usuarios con autenticación JWT.

## 🚀 Características

- ✅ **Autenticación y Autorización** con Spring Security + JWT
- 📚 **Gestión completa de libros** (CRUD, búsquedas, categorías)
- 📖 **Sistema de préstamos** con control de fechas y multas
- 👥 **Control de usuarios** con roles (ADMIN, USUARIO)
- 🔍 **Búsquedas avanzadas** por título, autor, categoría, ISBN
- 📊 **Estadísticas y reportes** del sistema
- 🗄️ **Base de datos relacional** con MySQL
- 🧪 **Colección Postman** lista para pruebas
- 📝 **Documentación completa** del sistema

## 🛠️ Tecnologías

- **Backend:** Spring Boot 3.5.7
- **Seguridad:** Spring Security + JWT
- **Base de Datos:** MySQL 8.0
- **ORM:** JPA/Hibernate
- **Validación:** Jakarta Validation
- **Mapeo:** MapStruct
- **Build:** Maven 3.9+
- **Java:** JDK 21
- **Logging:** SLF4J + Logback

## 📋 Requisitos Previos

- JDK 21 o superior
- Maven 3.9+
- MySQL 8.0+
- Postman (opcional, para pruebas)
- Git

## 🔧 Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/vansfanelx/ms-biblioteca.git
cd ms-biblioteca
```

### 2. Configurar Base de Datos

Ejecutar el script SQL de inicialización:

```sql
SOURCE init_database_biblioteca.sql
```

O desde MySQL Workbench, abrir y ejecutar el archivo `init_database_biblioteca.sql`.

### 3. Configurar application.properties

El archivo ya está configurado, pero puedes ajustar:

```properties
# Puerto del servidor
server.port=9595

# Configuración de base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/bd_biblioteca_universitaria
spring.datasource.username=root
spring.datasource.password=

# JWT
jwt.secret=biblioteca2025BackendJavaFullStackSecureKeyForJWTTokenGeneration
jwt.expiration=3600000
```

### 4. Compilar y Ejecutar

```bash
# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run
```

O desde tu IDE:
- Ejecutar la clase `MsBibliotecaApplication.java`

## 🌐 Endpoints API

### URL Base
```
http://localhost:9595/v1
```

### 🔐 Autenticación

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/v1/auth/login` | Iniciar sesión |
| POST | `/v1/auth/register` | Registrar usuario |

### 📚 Libros

| Método | Endpoint | Descripción | Rol |
|--------|----------|-------------|-----|
| GET | `/v1/libros` | Listar todos los libros | USUARIO/ADMIN |
| GET | `/v1/libros/{id}` | Obtener libro por ID | USUARIO/ADMIN |
| GET | `/v1/libros/isbn/{isbn}` | Obtener por ISBN | USUARIO/ADMIN |
| GET | `/v1/libros/disponibles` | Libros disponibles | USUARIO/ADMIN |
| GET | `/v1/libros/buscar?keyword=` | Búsqueda general | USUARIO/ADMIN |
| GET | `/v1/libros/categorias` | Listar categorías | USUARIO/ADMIN |
| POST | `/v1/libros` | Crear libro | ADMIN |
| PUT | `/v1/libros/{id}` | Actualizar libro | ADMIN |
| DELETE | `/v1/libros/{id}` | Eliminar libro | ADMIN |

### 📖 Préstamos

| Método | Endpoint | Descripción | Rol |
|--------|----------|-------------|-----|
| GET | `/v1/prestamos` | Listar préstamos | ADMIN |
| GET | `/v1/prestamos/{id}` | Obtener por ID | ADMIN/USUARIO |
| GET | `/v1/prestamos/vencidos` | Préstamos vencidos | ADMIN |
| POST | `/v1/prestamos` | Crear préstamo | ADMIN |
| PUT | `/v1/prestamos/{id}/devolver` | Registrar devolución | ADMIN |

**Ver documentación completa:** [SISTEMA_ACTUALIZADO_POSTMAN.md](SISTEMA_ACTUALIZADO_POSTMAN.md)

## 🔑 Credenciales por Defecto

### Administrador
```
Username: admin
Password: admin123
Rol: ROLE_ADMIN
```

### Usuario de Prueba
```
Username: usuario1
Password: usuario123
Rol: ROLE_USUARIO
```

## 🧪 Pruebas con Postman

1. Importar la colección: `MS-Biblioteca-Collection.json`
2. La URL base ya está configurada: `http://localhost:9595/v1`
3. Hacer login para obtener el token JWT
4. El token se guarda automáticamente en las variables
5. Probar los endpoints disponibles

**Ver guía completa:** [GUIA_PRUEBAS_POSTMAN.md](GUIA_PRUEBAS_POSTMAN.md)

## 📊 Estructura del Proyecto

```
ms-biblioteca/
├── src/
│   ├── main/
│   │   ├── java/pe/edu/idat/msbiblioteca/
│   │   │   ├── config/          # Configuración (Security, etc)
│   │   │   ├── controller/      # Controladores REST
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── entity/          # Entidades JPA
│   │   │   ├── exception/       # Manejo de excepciones
│   │   │   ├── mappers/         # MapStruct mappers
│   │   │   ├── repository/      # Repositorios JPA
│   │   │   ├── security/        # JWT y seguridad
│   │   │   ├── service/         # Lógica de negocio
│   │   │   └── MsBibliotecaApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/                    # Tests unitarios
├── init_database_biblioteca.sql # Script de BD
├── MS-Biblioteca-Collection.json # Colección Postman
├── pom.xml                      # Configuración Maven
└── README.md
```

## 📖 Documentación

- **[INDICE_DOCUMENTACION.md](INDICE_DOCUMENTACION.md)** - Índice completo de documentación
- **[SISTEMA_ACTUALIZADO_POSTMAN.md](SISTEMA_ACTUALIZADO_POSTMAN.md)** - Documentación técnica
- **[GUIA_PRUEBAS_POSTMAN.md](GUIA_PRUEBAS_POSTMAN.md)** - Guía de pruebas
- **[RESUMEN_ACTUALIZACION_BIBLIOTECA.md](RESUMEN_ACTUALIZACION_BIBLIOTECA.md)** - Resumen del sistema

## 🗄️ Base de Datos

La base de datos incluye:
- ✅ 4 tablas principales (rol, usuario, libros, prestamos)
- ✅ 15 libros pre-cargados en 7 categorías
- ✅ 2 usuarios de prueba (admin y usuario1)
- ✅ Vistas SQL para reportes
- ✅ Procedimientos almacenados para estadísticas

**Diagrama ER y más detalles:** Ver [init_database_biblioteca.sql](init_database_biblioteca.sql)

## 🔒 Seguridad

- Autenticación basada en JWT (JSON Web Tokens)
- Passwords encriptados con BCrypt
- Control de acceso basado en roles (RBAC)
- Tokens con expiración de 1 hora
- Endpoints protegidos por rol

## 📈 Próximas Mejoras

- [ ] Implementar refresh tokens
- [ ] Agregar paginación en listados
- [ ] Sistema de notificaciones por email
- [ ] Dashboard con gráficas
- [ ] Exportación de reportes PDF/Excel
- [ ] API de reservas de libros
- [ ] Integración con frontend React/Angular

## 🤝 Contribuir

Las contribuciones son bienvenidas. Por favor:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add: AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📝 Licencia

Este proyecto está bajo la Licencia MIT. Ver archivo [LICENSE](LICENSE) para más detalles.

## 👤 Autor

**Jonathan Jiménez**

- GitHub: [@vansfanelx](https://github.com/vansfanelx)

## 🙏 Agradecimientos

- Spring Boot Team por el excelente framework
- Comunidad de desarrolladores Java
- Todos los contribuidores del proyecto

## 📞 Soporte

Si tienes problemas o preguntas:

1. Revisa la [documentación](INDICE_DOCUMENTACION.md)
2. Busca en [Issues](https://github.com/vansfanelx/ms-biblioteca/issues)
3. Crea un nuevo Issue si es necesario

---

⭐ Si te gusta el proyecto, no olvides darle una estrella en GitHub

**Hecho con ❤️ usando Spring Boot**

