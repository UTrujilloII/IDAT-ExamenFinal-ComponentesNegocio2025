-- ============================================================
-- SISTEMA DE GESTIÓN DE BIBLIOTECA UNIVERSITARIA
-- IDAT - 2025
-- Spring Boot + JWT + MySQL
-- ============================================================

-- Eliminar base de datos si existe
DROP DATABASE IF EXISTS biblioteca_universitaria;

-- Crear base de datos
CREATE DATABASE biblioteca_universitaria
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Usar la base de datos
USE biblioteca_universitaria;

-- ============================================================
-- TABLA: roles
-- ============================================================
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(200),
    INDEX idx_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLA: usuarios
-- ============================================================
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nombre_completo VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    activo BOOLEAN DEFAULT TRUE,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_activo (activo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLA: libros
-- ============================================================
CREATE TABLE libros (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(200) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    editorial VARCHAR(150),
    anio_publicacion INT,
    categoria VARCHAR(100),
    num_paginas INT,
    idioma VARCHAR(50) DEFAULT 'Español',
    ubicacion VARCHAR(100),
    descripcion TEXT,
    cantidad_total INT DEFAULT 1,
    cantidad_disponible INT DEFAULT 1,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_titulo (titulo),
    INDEX idx_autor (autor),
    INDEX idx_isbn (isbn),
    INDEX idx_categoria (categoria),
    INDEX idx_disponible (cantidad_disponible)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLA: prestamos
-- ============================================================
CREATE TABLE prestamos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    libro_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    fecha_prestamo DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_devolucion_esperada DATE NOT NULL,
    fecha_devolucion_real DATE,
    estado VARCHAR(20) DEFAULT 'ACTIVO',
    observaciones TEXT,
    multa DECIMAL(10,2) DEFAULT 0.00,
    FOREIGN KEY (libro_id) REFERENCES libros(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_libro_id (libro_id),
    INDEX idx_usuario_id (usuario_id),
    INDEX idx_estado (estado),
    INDEX idx_fecha_prestamo (fecha_prestamo),
    CONSTRAINT chk_estado CHECK (estado IN ('ACTIVO', 'DEVUELTO', 'VENCIDO'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLA: usuario_roles (Relación Many-to-Many)
-- ============================================================
CREATE TABLE usuario_roles (
    usuario_id BIGINT NOT NULL,
    rol_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, rol_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (rol_id) REFERENCES roles(id) ON DELETE CASCADE,
    INDEX idx_usuario (usuario_id),
    INDEX idx_rol (rol_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- DATOS INICIALES: Roles
-- ============================================================
INSERT INTO roles (nombre, descripcion) VALUES
('ROLE_ADMIN', 'Administrador del sistema con acceso completo'),
('ROLE_USER', 'Usuario normal con permisos de consulta y préstamo');

-- ============================================================
-- DATOS INICIALES: Usuario Admin
-- Password: admin123 (encriptado con BCrypt)
-- ============================================================
INSERT INTO usuarios (username, email, password, nombre_completo, telefono, activo) VALUES
('admin', 'admin@biblioteca.com', 
'$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8ssKQqpZmV0zKJ2jWK',
'Administrador Sistema', '999888777', TRUE);

-- Asignar rol ADMIN al usuario admin
INSERT INTO usuario_roles (usuario_id, rol_id) 
SELECT u.id, r.id 
FROM usuarios u, roles r 
WHERE u.username = 'admin' AND r.nombre = 'ROLE_ADMIN';

-- ============================================================
-- DATOS DE EJEMPLO: Libros
-- ============================================================
INSERT INTO libros (titulo, autor, isbn, editorial, anio_publicacion, categoria, num_paginas, idioma, ubicacion, descripcion, cantidad_total, cantidad_disponible) VALUES
('Effective Java', 'Joshua Bloch', '978-0134685991', 'Addison-Wesley', 2018, 'Programación', 416, 'Inglés', 'Estante A-01', 'Guía definitiva sobre mejores prácticas en Java', 3, 3),
('Clean Code', 'Robert C. Martin', '978-0132350884', 'Prentice Hall', 2008, 'Programación', 464, 'Inglés', 'Estante A-02', 'Manual de estilo para el desarrollo ágil de software', 2, 2),
('Design Patterns', 'Erich Gamma', '978-0201633610', 'Addison-Wesley', 1994, 'Programación', 395, 'Inglés', 'Estante A-03', 'Elementos de software orientado a objetos reutilizable', 2, 2),
('Cien Años de Soledad', 'Gabriel García Márquez', '978-0060883287', 'Harper Perennial', 1967, 'Literatura', 417, 'Español', 'Estante L-10', 'Obra maestra del realismo mágico', 5, 5),
('El Principito', 'Antoine de Saint-Exupéry', '978-0156012195', 'Harcourt', 1943, 'Literatura', 96, 'Español', 'Estante L-15', 'Novela corta y clásico de la literatura universal', 4, 4);

-- ============================================================
-- VERIFICACIÓN
-- ============================================================
SELECT 'Base de datos creada exitosamente' AS mensaje;

-- Mostrar resumen
SELECT 'roles' AS tabla, COUNT(*) AS registros FROM roles
UNION ALL
SELECT 'usuarios' AS tabla, COUNT(*) AS registros FROM usuarios
UNION ALL
SELECT 'libros' AS tabla, COUNT(*) AS registros FROM libros
UNION ALL
SELECT 'usuario_roles' AS tabla, COUNT(*) AS registros FROM usuario_roles;

-- ============================================================
-- CREDENCIALES DE ACCESO
-- ============================================================
-- Usuario Admin:
-- Username: admin
-- Password: admin123
-- Email: admin@biblioteca.com
-- Rol: ROLE_ADMIN
-- ============================================================