-- Base de datos del sistema Telemedicina
-- Programacion Avanzada

CREATE DATABASE IF NOT EXISTS telemedicina;

USE telemedicina;

CREATE TABLE IF NOT EXISTS usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
    clave VARCHAR(100) NOT NULL,
    tipo_usuario ENUM('EMPLEADO', 'PROFESIONAL') NOT NULL
);

-- Usuarios iniciales para pruebas de autenticacion

INSERT IGNORE INTO usuario
    (id_usuario, nombre, apellido, nombre_usuario, clave, tipo_usuario)
VALUES
    (1, 'Joaquin', 'Gonzalez Garcia', 'recepcion', '1234', 'EMPLEADO'),
    (2, 'Carlos', 'Medico', 'doctor', '1234', 'PROFESIONAL');