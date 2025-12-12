-- =====================================================
-- SISTEMA DE GESTIÓN DE BIBLIOTECA UNIVERSITARIA
-- Script de inicialización de base de datos
-- =====================================================
-- Autor: Jonathan Jiménez
-- GitHub: https://github.com/vansfanelx/
-- Descripción: Crea las tablas y datos iniciales para
--              el sistema de gestión de biblioteca
-- =====================================================

-- Crear base de datos si no existe
CREATE DATABASE IF NOT EXISTS bd_biblioteca_universitaria
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE bd_biblioteca_universitaria;

-- =====================================================
-- TABLA: rol
-- Descripción: Almacena los roles del sistema (ADMIN, USUARIO)
-- =====================================================
CREATE TABLE IF NOT EXISTS rol (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    INDEX idx_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: usuario
-- Descripción: Almacena la información de los usuarios del sistema
-- =====================================================
CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    nombre_completo VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    telefono VARCHAR(20),
    direccion VARCHAR(200),
    fecha_registro DATE NOT NULL,
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: usuario_rol
-- Descripción: Tabla intermedia para la relación muchos a muchos
--              entre usuarios y roles
-- =====================================================
CREATE TABLE IF NOT EXISTS usuario_rol (
    idusuario BIGINT NOT NULL,
    idrol BIGINT NOT NULL,
    PRIMARY KEY (idusuario, idrol),
    FOREIGN KEY (idusuario) REFERENCES usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (idrol) REFERENCES rol(id) ON DELETE CASCADE,
    INDEX idx_usuario (idusuario),
    INDEX idx_rol (idrol)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: libros
-- Descripción: Almacena la información de los libros de la biblioteca
-- =====================================================
CREATE TABLE IF NOT EXISTS libros (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    titulo VARCHAR(200) NOT NULL,
    autor VARCHAR(150) NOT NULL,
    editorial VARCHAR(150),
    anio_publicacion INT,
    categoria VARCHAR(100),
    copias_disponibles INT NOT NULL DEFAULT 0,
    copias_totales INT NOT NULL DEFAULT 0,
    descripcion TEXT,
    ubicacion VARCHAR(50),
    estado VARCHAR(20) NOT NULL DEFAULT 'DISPONIBLE',
    fecha_registro DATE NOT NULL,
    INDEX idx_isbn (isbn),
    INDEX idx_titulo (titulo),
    INDEX idx_autor (autor),
    INDEX idx_categoria (categoria),
    INDEX idx_estado (estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: prestamos
-- Descripción: Almacena los préstamos de libros realizados
-- =====================================================
CREATE TABLE IF NOT EXISTS prestamos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    libro_id BIGINT NOT NULL,
    fecha_prestamo DATE NOT NULL,
    fecha_devolucion_esperada DATE NOT NULL,
    fecha_devolucion_real DATE,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    multa DOUBLE DEFAULT 0.0,
    observaciones TEXT,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE RESTRICT,
    FOREIGN KEY (libro_id) REFERENCES libros(id) ON DELETE RESTRICT,
    INDEX idx_usuario (usuario_id),
    INDEX idx_libro (libro_id),
    INDEX idx_estado (estado),
    INDEX idx_fecha_prestamo (fecha_prestamo),
    INDEX idx_fecha_devolucion (fecha_devolucion_esperada)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- INSERCIÓN DE DATOS INICIALES
-- =====================================================

-- Insertar roles
INSERT INTO rol (nombre) VALUES ('ROLE_ADMIN'), ('ROLE_USUARIO')
ON DUPLICATE KEY UPDATE nombre=nombre;

-- Insertar usuario administrador
-- Username: admin
-- Password: admin123 (cifrado con BCrypt)
INSERT INTO usuario (username, password, enabled, nombre_completo, email, telefono, direccion, fecha_registro)
VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        TRUE, 'Administrador del Sistema', 'admin@biblioteca.edu',
        '999888777', 'Av. Universidad 123', CURDATE())
ON DUPLICATE KEY UPDATE username=username;

-- Insertar usuario de prueba
-- Username: usuario1
-- Password: usuario123 (cifrado con BCrypt)
INSERT INTO usuario (username, password, enabled, nombre_completo, email, telefono, direccion, fecha_registro)
VALUES ('usuario1', '$2a$10$6JhRF2bAMKZMNJ0j0hxRJOVtXVE5rH5b0QLEKwYkR5FN5J8p5bCHO',
        TRUE, 'Juan Pérez García', 'juan.perez@estudiante.edu',
        '987654321', 'Jr. Los Olivos 456', CURDATE())
ON DUPLICATE KEY UPDATE username=username;

-- Asignar roles a usuarios
INSERT INTO usuario_rol (idusuario, idrol)
SELECT u.id, r.id FROM usuario u, rol r
WHERE u.username = 'admin' AND r.nombre = 'ROLE_ADMIN'
ON DUPLICATE KEY UPDATE idusuario=idusuario;

INSERT INTO usuario_rol (idusuario, idrol)
SELECT u.id, r.id FROM usuario u, rol r
WHERE u.username = 'usuario1' AND r.nombre = 'ROLE_USUARIO'
ON DUPLICATE KEY UPDATE idusuario=idusuario;

-- =====================================================
-- INSERCIÓN DE LIBROS DE EJEMPLO
-- =====================================================

INSERT INTO libros (isbn, titulo, autor, editorial, anio_publicacion, categoria,
                    copias_disponibles, copias_totales, descripcion, ubicacion, estado, fecha_registro)
VALUES
-- Programación y Tecnología
('978-0134685991', 'Effective Java', 'Joshua Bloch', 'Addison-Wesley', 2018, 'Programación',
 3, 3, 'Guía esencial de mejores prácticas para programadores Java.', 'Estante A1', 'DISPONIBLE', CURDATE()),

('978-0135957059', 'The Pragmatic Programmer', 'David Thomas, Andrew Hunt', 'Addison-Wesley', 2019, 'Programación',
 2, 2, 'De aprendiz a maestro: tu viaje hacia la maestría en programación.', 'Estante A1', 'DISPONIBLE', CURDATE()),

('978-0132350884', 'Clean Code', 'Robert C. Martin', 'Prentice Hall', 2008, 'Programación',
 4, 4, 'Manual de estilo para el desarrollo ágil de software.', 'Estante A2', 'DISPONIBLE', CURDATE()),

('978-0201633610', 'Design Patterns', 'Gang of Four', 'Addison-Wesley', 1994, 'Programación',
 2, 2, 'Elementos de software orientado a objetos reutilizable.', 'Estante A2', 'DISPONIBLE', CURDATE()),

-- Base de Datos
('978-0136520238', 'Database System Concepts', 'Silberschatz, Korth, Sudarshan', 'McGraw-Hill', 2019, 'Base de Datos',
 3, 3, 'Conceptos fundamentales de sistemas de bases de datos.', 'Estante B1', 'DISPONIBLE', CURDATE()),

('978-0321884497', 'High Performance MySQL', 'Baron Schwartz', 'O\'Reilly Media', 2012, 'Base de Datos',
 2, 2, 'Optimización, copias de seguridad y replicación.', 'Estante B1', 'DISPONIBLE', CURDATE()),

-- Desarrollo Web
('978-1491918661', 'Learning React', 'Alex Banks, Eve Porcello', 'O\'Reilly Media', 2017, 'Desarrollo Web',
 3, 3, 'Crea interfaces de usuario modernas con React.', 'Estante C1', 'DISPONIBLE', CURDATE()),

('978-1484243909', 'Pro Spring Boot 2', 'Felipe Gutierrez', 'Apress', 2019, 'Desarrollo Web',
 2, 2, 'Guía completa de Spring Boot 2 y microservicios.', 'Estante C1', 'DISPONIBLE', CURDATE()),

-- Algoritmos y Estructuras de Datos
('978-0262033848', 'Introduction to Algorithms', 'Cormen, Leiserson, Rivest, Stein', 'MIT Press', 2009, 'Algoritmos',
 4, 4, 'Texto completo sobre el diseño y análisis de algoritmos.', 'Estante D1', 'DISPONIBLE', CURDATE()),

('978-0321573513', 'Algorithms', 'Robert Sedgewick, Kevin Wayne', 'Addison-Wesley', 2011, 'Algoritmos',
 3, 3, 'Cuarta edición del clásico sobre algoritmos.', 'Estante D1', 'DISPONIBLE', CURDATE()),

-- Inteligencia Artificial y Machine Learning
('978-0262035613', 'Deep Learning', 'Ian Goodfellow', 'MIT Press', 2016, 'Inteligencia Artificial',
 2, 2, 'Libro definitivo sobre aprendizaje profundo.', 'Estante E1', 'DISPONIBLE', CURDATE()),

('978-1449369415', 'Hands-On Machine Learning', 'Aurélien Géron', 'O\'Reilly Media', 2019, 'Inteligencia Artificial',
 3, 3, 'Guía práctica con Scikit-Learn, Keras y TensorFlow.', 'Estante E1', 'DISPONIBLE', CURDATE()),

-- Arquitectura de Software
('978-1492043451', 'Fundamentals of Software Architecture', 'Mark Richards, Neal Ford', 'O\'Reilly Media', 2020, 'Arquitectura',
 2, 2, 'Guía completa para arquitectos de software.', 'Estante F1', 'DISPONIBLE', CURDATE()),

('978-1680502091', 'Domain-Driven Design Distilled', 'Vaughn Vernon', 'Addison-Wesley', 2016, 'Arquitectura',
 2, 2, 'Conceptos esenciales de diseño dirigido por el dominio.', 'Estante F1', 'DISPONIBLE', CURDATE()),

-- Seguridad
('978-1118026472', 'Web Application Security', 'Andrew Hoffman', 'O\'Reilly Media', 2020, 'Seguridad',
 2, 2, 'Guía práctica de seguridad en aplicaciones web.', 'Estante G1', 'DISPONIBLE', CURDATE());

-- =====================================================
-- VISTAS ÚTILES PARA REPORTES
-- =====================================================

-- Vista de préstamos activos con información detallada
CREATE OR REPLACE VIEW vista_prestamos_activos AS
SELECT
    p.id AS prestamo_id,
    u.username,
    u.nombre_completo,
    u.email,
    l.isbn,
    l.titulo,
    l.autor,
    p.fecha_prestamo,
    p.fecha_devolucion_esperada,
    DATEDIFF(CURDATE(), p.fecha_devolucion_esperada) AS dias_retraso,
    p.multa,
    p.estado
FROM prestamos p
JOIN usuario u ON p.usuario_id = u.id
JOIN libros l ON p.libro_id = l.id
WHERE p.estado = 'ACTIVO';

-- Vista de estadísticas de libros por categoría
CREATE OR REPLACE VIEW vista_estadisticas_libros AS
SELECT
    categoria,
    COUNT(*) AS total_libros,
    SUM(copias_totales) AS total_copias,
    SUM(copias_disponibles) AS copias_disponibles,
    SUM(copias_totales - copias_disponibles) AS copias_prestadas
FROM libros
GROUP BY categoria
ORDER BY total_libros DESC;

-- Vista de usuarios con préstamos activos
CREATE OR REPLACE VIEW vista_usuarios_con_prestamos AS
SELECT
    u.id,
    u.username,
    u.nombre_completo,
    u.email,
    COUNT(p.id) AS prestamos_activos,
    SUM(p.multa) AS multa_total
FROM usuario u
LEFT JOIN prestamos p ON u.id = p.usuario_id AND p.estado = 'ACTIVO'
GROUP BY u.id, u.username, u.nombre_completo, u.email;

-- =====================================================
-- PROCEDIMIENTOS ALMACENADOS ÚTILES
-- =====================================================

DELIMITER //

-- Procedimiento para actualizar estados de préstamos vencidos
CREATE PROCEDURE sp_actualizar_prestamos_vencidos()
BEGIN
    UPDATE prestamos
    SET estado = 'VENCIDO',
        multa = DATEDIFF(CURDATE(), fecha_devolucion_esperada) * 1.0
    WHERE fecha_devolucion_esperada < CURDATE()
    AND fecha_devolucion_real IS NULL
    AND estado != 'DEVUELTO';
END //

-- Procedimiento para obtener resumen de biblioteca
CREATE PROCEDURE sp_resumen_biblioteca()
BEGIN
    SELECT
        'Libros' AS concepto,
        COUNT(*) AS total,
        SUM(CASE WHEN estado = 'DISPONIBLE' THEN 1 ELSE 0 END) AS disponibles,
        SUM(copias_totales) AS copias_totales,
        SUM(copias_disponibles) AS copias_disponibles
    FROM libros
    UNION ALL
    SELECT
        'Préstamos' AS concepto,
        COUNT(*) AS total,
        SUM(CASE WHEN estado = 'ACTIVO' THEN 1 ELSE 0 END) AS activos,
        SUM(CASE WHEN estado = 'DEVUELTO' THEN 1 ELSE 0 END) AS devueltos,
        SUM(CASE WHEN estado = 'VENCIDO' THEN 1 ELSE 0 END) AS vencidos
    FROM prestamos;
END //

DELIMITER ;

-- =====================================================
-- TRIGGERS PARA AUDITORÍA
-- =====================================================

DELIMITER //

-- Trigger para prevenir eliminación de libros con préstamos activos
CREATE TRIGGER tr_before_delete_libro
BEFORE DELETE ON libros
FOR EACH ROW
BEGIN
    DECLARE prestamos_activos INT;

    SELECT COUNT(*) INTO prestamos_activos
    FROM prestamos
    WHERE libro_id = OLD.id AND estado = 'ACTIVO';

    IF prestamos_activos > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'No se puede eliminar el libro porque tiene préstamos activos';
    END IF;
END //

DELIMITER ;

-- =====================================================
-- INFORMACIÓN FINAL
-- =====================================================

SELECT '=========================================' AS '';
SELECT 'BASE DE DATOS INICIALIZADA CORRECTAMENTE' AS '';
SELECT '=========================================' AS '';
SELECT '' AS '';
SELECT 'Credenciales de acceso:' AS '';
SELECT '------------------------' AS '';
SELECT 'ADMINISTRADOR:' AS '';
SELECT '  Username: admin' AS '';
SELECT '  Password: admin123' AS '';
SELECT '' AS '';
SELECT 'USUARIO DE PRUEBA:' AS '';
SELECT '  Username: usuario1' AS '';
SELECT '  Password: usuario123' AS '';
SELECT '' AS '';
SELECT CONCAT('Total de libros registrados: ', COUNT(*)) AS '' FROM libros;
SELECT '' AS '';
SELECT '=========================================' AS '';

