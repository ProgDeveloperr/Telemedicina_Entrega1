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
    
    -- Profesionales vinculados a usuarios del sistema

CREATE TABLE IF NOT EXISTS profesional (
    id_profesional INT AUTO_INCREMENT PRIMARY KEY,
    matricula VARCHAR(50) NOT NULL UNIQUE,
    especialidad VARCHAR(100) NOT NULL,
    usuario_id INT NOT NULL UNIQUE,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id_usuario)
);

INSERT IGNORE INTO profesional
    (id_profesional, matricula, especialidad, usuario_id)
VALUES
    (1, 'MP12345', 'Medicina General', 2);