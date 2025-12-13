-- ============================================
-- SISTEMA DE GESTIÓN DE BIBLIOTECA UNIVERSITARIA
-- ============================================
DROP DATABASE IF EXISTS biblioteca;
CREATE DATABASE biblioteca;
USE biblioteca;


-- ============================================
-- TABLA: roles
-- ============================================
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

-- ============================================
-- TABLA: usuarios
-- ============================================
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol_id BIGINT NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ultima_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    CONSTRAINT fk_usuarios_rol FOREIGN KEY (rol_id) 
        REFERENCES roles(id) ON DELETE RESTRICT
);

-- ============================================
-- TABLA: libros
-- ============================================
CREATE TABLE libros (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(255) NOT NULL,
    editorial VARCHAR(150),
    categoria VARCHAR(100),
    cantidad_total INT NOT NULL DEFAULT 1,
    cantidad_disponible INT NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    INDEX idx_titulo (titulo),
    CONSTRAINT chk_cantidad CHECK (cantidad_disponible <= cantidad_total AND cantidad_disponible >= 0)
);

-- ============================================
-- TABLA: prestamos
-- ============================================
CREATE TABLE prestamos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    libro_id BIGINT NOT NULL,
    fecha_prestamo TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_devolucion_esperada DATE NOT NULL,
    fecha_devolucion_real TIMESTAMP NULL,
    estado ENUM('ACTIVO', 'DEVUELTO', 'VENCIDO') NOT NULL DEFAULT 'ACTIVO',
    
    CONSTRAINT fk_prestamos_usuario FOREIGN KEY (usuario_id) 
        REFERENCES usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_prestamos_libro FOREIGN KEY (libro_id) 
        REFERENCES libros(id) ON DELETE RESTRICT,
    
    INDEX idx_usuario_estado (usuario_id, estado),
    INDEX idx_libro_estado (libro_id, estado)
);

-- ============================================
-- TABLA: refresh_tokens
-- ============================================
CREATE TABLE refresh_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    token VARCHAR(500) NOT NULL UNIQUE,
    fecha_expiracion TIMESTAMP NOT NULL,
    revocado BOOLEAN DEFAULT FALSE,
    
    CONSTRAINT fk_refresh_tokens_usuario FOREIGN KEY (usuario_id) 
        REFERENCES usuarios(id) ON DELETE CASCADE,
    
    INDEX idx_token (token)
);

-- ============================================
-- TRIGGERS PARA GESTIÓN DE INVENTARIO
-- ============================================
DELIMITER //

CREATE TRIGGER trg_prestamo_after_insert
AFTER INSERT ON prestamos
FOR EACH ROW
BEGIN
    IF NEW.estado = 'ACTIVO' THEN
        UPDATE libros 
        SET cantidad_disponible = cantidad_disponible - 1 
        WHERE id = NEW.libro_id;
    END IF;
END//

CREATE TRIGGER trg_prestamo_after_update
AFTER UPDATE ON prestamos
FOR EACH ROW
BEGIN
    IF OLD.estado = 'ACTIVO' AND NEW.estado = 'DEVUELTO' THEN
        UPDATE libros 
        SET cantidad_disponible = cantidad_disponible + 1 
        WHERE id = NEW.libro_id;
    END IF;
END//

DELIMITER ;

-- ============================================
-- DATOS INICIALES: ROLES
-- ============================================
INSERT INTO roles (nombre) VALUES ('ADMIN');
INSERT INTO roles (nombre) VALUES ('USUARIO');