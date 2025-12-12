-- Script SQL para Base de Datos MySQL - Sistema de Biblioteca Universitaria
-- Autor: Equipo de Desarrollo
-- Fecha: 2025

-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS biblioteca_universitaria;
USE biblioteca_universitaria;

-- =====================================================
-- NOTA IMPORTANTE:
-- =====================================================
-- Este script es OPCIONAL. Spring Boot con Hibernate
-- creará automáticamente las tablas cuando ejecutes
-- la aplicación por primera vez con:
-- spring.jpa.hibernate.ddl-auto=update
-- 
-- Sin embargo, si prefieres crear la base de datos
-- manualmente, puedes ejecutar este script.
-- =====================================================

-- Tabla: roles
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(20) NOT NULL UNIQUE,
    descripcion VARCHAR(100)
);

-- Tabla: usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nombre_completo VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: usuario_roles (relación muchos a muchos)
CREATE TABLE IF NOT EXISTS usuario_roles (
    usuario_id BIGINT NOT NULL,
    rol_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, rol_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (rol_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Tabla: libros
CREATE TABLE IF NOT EXISTS libros (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    titulo VARCHAR(200) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    editorial VARCHAR(100),
    categoria VARCHAR(50),
    anio_publicacion INT NOT NULL,
    cantidad_disponible INT NOT NULL,
    cantidad_total INT NOT NULL,
    descripcion VARCHAR(500),
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: prestamos
CREATE TABLE IF NOT EXISTS prestamos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    libro_id BIGINT NOT NULL,
    fecha_prestamo DATE NOT NULL,
    fecha_devolucion_prevista DATE NOT NULL,
    fecha_devolucion_real DATE,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    observaciones VARCHAR(200),
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (libro_id) REFERENCES libros(id) ON DELETE CASCADE
);

-- =====================================================
-- DATOS INICIALES (OPCIONAL)
-- =====================================================

-- Insertar roles predeterminados
INSERT INTO roles (nombre, descripcion) VALUES 
    ('ADMIN', 'Administrador del sistema'),
    ('USUARIO', 'Usuario estándar')
ON DUPLICATE KEY UPDATE descripcion = VALUES(descripcion);

-- Insertar usuario administrador de ejemplo
-- Usuario: admin / Contraseña: admin123
-- NOTA: La contraseña ya está encriptada con BCrypt
INSERT INTO usuarios (username, email, password, nombre_completo, telefono, activo) VALUES
    ('admin', 'admin@biblioteca.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Administrador Sistema', '999888777', TRUE)
ON DUPLICATE KEY UPDATE email = VALUES(email);

-- Asignar rol ADMIN al usuario admin
INSERT INTO usuario_roles (usuario_id, rol_id)
SELECT u.id, r.id
FROM usuarios u, roles r
WHERE u.username = 'admin' AND r.nombre = 'ADMIN'
ON DUPLICATE KEY UPDATE usuario_id = usuario_id;

-- Insertar algunos libros de ejemplo
INSERT INTO libros (isbn, titulo, autor, editorial, categoria, anio_publicacion, cantidad_disponible, cantidad_total, descripcion) VALUES
    ('978-0134685991', 'Effective Java', 'Joshua Bloch', 'Addison-Wesley', 'Programación', 2018, 5, 5, 'Guía completa de mejores prácticas en Java'),
    ('978-0596009205', 'Head First Design Patterns', 'Eric Freeman', 'O''Reilly Media', 'Programación', 2004, 3, 3, 'Patrones de diseño explicados de forma visual'),
    ('978-0132350884', 'Clean Code', 'Robert C. Martin', 'Prentice Hall', 'Programación', 2008, 4, 4, 'Manual de estilo para el desarrollo ágil de software'),
    ('978-0201633610', 'Design Patterns', 'Gang of Four', 'Addison-Wesley', 'Programación', 1994, 2, 2, 'Elements of Reusable Object-Oriented Software'),
    ('978-0321125215', 'Domain-Driven Design', 'Eric Evans', 'Addison-Wesley', 'Arquitectura', 2003, 3, 3, 'Tackling Complexity in the Heart of Software')
ON DUPLICATE KEY UPDATE titulo = VALUES(titulo);

-- =====================================================
-- ÍNDICES PARA MEJOR RENDIMIENTO
-- =====================================================

CREATE INDEX idx_usuarios_username ON usuarios(username);
CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_libros_isbn ON libros(isbn);
CREATE INDEX idx_libros_titulo ON libros(titulo);
CREATE INDEX idx_libros_autor ON libros(autor);
CREATE INDEX idx_prestamos_estado ON prestamos(estado);
CREATE INDEX idx_prestamos_usuario ON prestamos(usuario_id);
CREATE INDEX idx_prestamos_libro ON prestamos(libro_id);

-- =====================================================
-- VERIFICACIÓN
-- =====================================================

SELECT 'Base de datos creada exitosamente!' AS Mensaje;
SELECT COUNT(*) AS Total_Roles FROM roles;
SELECT COUNT(*) AS Total_Usuarios FROM usuarios;
SELECT COUNT(*) AS Total_Libros FROM libros;
