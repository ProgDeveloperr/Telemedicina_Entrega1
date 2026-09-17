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
-- Las contraseñas se almacenan mediante hash BCrypt.

INSERT IGNORE INTO usuario
    (id_usuario, nombre, apellido, nombre_usuario, clave, tipo_usuario)
VALUES
    (1, 'Joaquin', 'Gonzalez Garcia', 'recepcion',
     '$2a$10$seYXgwBRrwUr/Mq9XORPOOlk0nma.avTe9PSHJkUmgxJv6x1uAxsq',
     'EMPLEADO'),

    (2, 'Carlos', 'Medico', 'doctor',
     '$2a$10$PW29dhJTQosw9AY1weV1buOQkFDOT6xyCnV/M7gCBGVGI/Z.NvxdO',
     'PROFESIONAL');
    

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
    
    

CREATE TABLE IF NOT EXISTS paciente (
    id_paciente INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    telefono VARCHAR(30) NOT NULL
);