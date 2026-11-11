# Telemedicina

Sistema de gestión de telemedicina desarrollado en **Java** como proyecto académico de **Programación Avanzada**. La aplicación permite administrar pacientes, profesionales, disponibilidades, turnos y consultas médicas mediante una interfaz gráfica construida con **Java Swing** y persistencia de datos mediante **JDBC + MySQL/MariaDB**.

<p align="center">
  <img src="src/resources/imagenes/login.gif" alt="Telemedicina" width="190">
</p>

## Descripción

El sistema implementa dos perfiles de acceso:

- **EMPLEADO / Recepción:** gestiona pacientes, profesionales, turnos e información de consultas.
- **PROFESIONAL:** administra su disponibilidad, consulta su agenda, informa retrasos, registra consultas realizadas y visualiza el historial correspondiente.

La autenticación utiliza contraseñas almacenadas mediante **BCrypt** y la navegación se realiza íntegramente mediante ventanas `JFrame`.

## Funcionalidades principales

### Empleado / Recepción

- Inicio de sesión según rol.
- Registro y modificación de pacientes.
- Eliminación segura de pacientes sin turnos asociados.
- Registro de nuevos profesionales.
- Validación de usuario y matrícula duplicados.
- Consulta de disponibilidad de profesionales.
- Asignación de turnos.
- Modificación de turnos.
- Cancelación de turnos.
- Confirmación de turnos.
- Registro de llegada del paciente.
- Visualización del detalle de turnos.
- Consulta del historial de pacientes.
- Visualización de retrasos informados por profesionales.

### Profesional

- Inicio de sesión con perfil profesional.
- Alta y modificación de disponibilidad.
- Validación de fechas y horarios.
- Consulta de agenda de turnos.
- Registro de retraso estimado.
- Registro de consultas realizadas.
- Carga de observaciones.
- Carga de seguimiento opcional.
- Consulta del historial de consultas.

## Tecnologías utilizadas

- **Java**
- **Java Swing**
- **JDBC**
- **MySQL / MariaDB**
- **XAMPP**
- **BCrypt**
- **Eclipse IDE**
- **Git / GitHub**

## Conceptos aplicados

- Programación orientada a objetos.
- Encapsulamiento.
- Interfaces.
- Enumeraciones.
- Excepciones.
- JDBC.
- `PreparedStatement`.
- `ResultSet`.
- Relaciones mediante claves foráneas.
- Patrón **Singleton** para la conexión a la base de datos.
- Separación por capas.
- Interfaces `Repository` para desacoplar el acceso a datos.
- Hash seguro de contraseñas mediante BCrypt.
- Interfaz gráfica con `JFrame`.
- Validaciones de negocio antes de persistir información.

## Arquitectura del proyecto

```text
src/
├── BLL/
│   ├── Consulta.java
│   ├── Disponibilidad.java
│   ├── EstadoTurno.java
│   ├── Paciente.java
│   ├── Profesional.java
│   ├── TipoUsuario.java
│   ├── Turno.java
│   └── Usuario.java
│
├── DLL/
│   ├── Conexion.java
│   ├── ControllerConsulta.java
│   ├── ControllerDisponibilidad.java
│   ├── ControllerPaciente.java
│   ├── ControllerProfesional.java
│   ├── ControllerTurno.java
│   └── ControllerUsuario.java
│
├── GUI/
│   ├── Main.java
│   ├── LoginFrame.java
│   ├── DashboardEmpleadoFrame.java
│   ├── DashboardProfesionalFrame.java
│   ├── PacientesFrame.java
│   ├── ProfesionalesFrame.java
│   ├── TurnosFrame.java
│   ├── ConsultasInformacionFrame.java
│   ├── DisponibilidadFrame.java
│   ├── AgendaProfesionalFrame.java
│   ├── RetrasoProfesionalFrame.java
│   ├── RegistrarConsultaFrame.java
│   └── HistorialProfesionalFrame.java
│
├── repository/
│   ├── ConsultaRepository.java
│   ├── DisponibilidadRepository.java
│   ├── Hashing.java
│   ├── PacienteRepository.java
│   ├── ProfesionalRepository.java
│   ├── TurnoRepository.java
│   └── UsuarioRepository.java
│
└── resources/
    └── imagenes/
        ├── icono_app.png
        ├── login.gif
        ├── menu_empleado.png
        ├── menu_profesional.png
        ├── ok_empleado.png
        └── ok_profesional.png

database/
└── telemedicina.sql

lib/
├── jbcrypt-0.4.jar
└── mysql-connector-java-5.1.13.jar
```

## Base de datos

La base utilizada se llama:

```text
telemedicina
```

El archivo completo para crearla se encuentra en:

```text
database/telemedicina.sql
```

El esquema contiene las siguientes tablas:

```text
usuario
profesional
paciente
disponibilidad
turno
consulta
```

Las relaciones principales se implementan mediante claves foráneas para mantener la integridad de los datos. El sistema conserva el historial médico evitando eliminar pacientes que tengan turnos asociados.

## Estados de los turnos

Los turnos utilizan el enum `EstadoTurno` para representar su ciclo de vida dentro del sistema:

```text
PENDIENTE
CONFIRMADO
CANCELADO
PRESENTE
```

Esto permite conservar trazabilidad en lugar de eliminar físicamente los turnos.

## Seguridad de contraseñas

Las contraseñas no se almacenan en texto plano. El proyecto utiliza:

```text
lib/jbcrypt-0.4.jar
```

La interfaz `Hashing` centraliza el hash y la verificación mediante BCrypt.

## Configuración de la conexión

La configuración actual se encuentra en:

```text
src/DLL/Conexion.java
```

Por defecto:

```text
URL: jdbc:mysql://localhost:3306/telemedicina
Usuario: root
Contraseña: vacía
```

Esta configuración corresponde al entorno local utilizado durante el desarrollo con XAMPP. Si la instalación local utiliza otras credenciales, deben modificarse `URL`, `USER` y `PASSWORD` dentro de `Conexion.java`.

> Para un entorno de producción no se recomienda utilizar el usuario `root` ni almacenar credenciales directamente en el código.

## Cómo ejecutar el proyecto

### 1. Requisitos

Tener instalado:

- Java.
- Eclipse IDE.
- XAMPP o un servidor MySQL/MariaDB compatible.

### 2. Iniciar la base de datos

Desde XAMPP iniciar **MySQL**.

### 3. Importar la base

Ingresar a phpMyAdmin e importar:

```text
database/telemedicina.sql
```

La base debe quedar disponible con el nombre:

```text
telemedicina
```

### 4. Verificar las librerías

El proyecto incluye:

```text
lib/jbcrypt-0.4.jar
lib/mysql-connector-java-5.1.13.jar
```

Si Eclipse no las reconoce automáticamente:

```text
Project
→ Properties
→ Java Build Path
→ Libraries
→ Add JARs...
```

y agregar ambos archivos.

### 5. Ejecutar

Ejecutar:

```text
src/GUI/Main.java
```

como **Java Application**.

`Main` inicializa la conexión mediante `Conexion.getInstance()` y abre `LoginFrame`.

## Flujo general

```text
Main
 ↓
Conexion Singleton
 ↓
LoginFrame
 ↓
ControllerUsuario
 ↓
Autenticación BCrypt
 ↓
TipoUsuario
 ├── EMPLEADO
 │    ↓
 │    DashboardEmpleadoFrame
 │    ├── Pacientes
 │    ├── Profesionales
 │    ├── Turnos
 │    └── Consultas e información
 │
 └── PROFESIONAL
      ↓
      DashboardProfesionalFrame
      ├── Disponibilidad
      ├── Agenda de turnos
      ├── Retraso estimado
      ├── Registrar consulta
      └── Historial de consultas
```

## Interfaz gráfica

La interfaz fue desarrollada completamente con **Java Swing**. Las pantallas principales incluyen recursos gráficos diferenciados según el rol:

```text
login.gif
menu_empleado.png
menu_profesional.png
ok_empleado.png
ok_profesional.png
icono_app.png
```

Los módulos de gestión mantienen una interfaz orientada a tablas para priorizar la lectura y manipulación de datos.

## Persistencia y acceso a datos

La aplicación utiliza interfaces dentro del paquete `repository` y clases de implementación dentro de `DLL`:

```text
GUI
 ↓
Controller
 ↓
Repository
 ↓
JDBC
 ↓
MySQL / MariaDB
```

De esta manera, las ventanas no ejecutan consultas SQL directamente y la responsabilidad de persistencia queda separada de la interfaz gráfica.

## Estado del proyecto

El sistema cuenta con los principales flujos funcionales implementados y validados:

- Login por roles.
- Gestión de pacientes.
- Gestión de profesionales.
- Gestión completa del ciclo de turnos.
- Disponibilidad profesional.
- Retrasos.
- Registro de llegada.
- Registro de consultas.
- Historial clínico.
- Integridad referencial en la base de datos.
- Validaciones de fechas, horarios y duplicados.
- Interfaz gráfica diferenciada para empleado y profesional.

## Autor

**Joaquin Gonzalez Garcia**

Proyecto académico desarrollado para la materia **Programación Avanzada**.
