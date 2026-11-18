# Telemedicina

Sistema de gestión de telemedicina desarrollado en **Java** como proyecto académico de **Programación Avanzada**.

La aplicación permite administrar pacientes, profesionales, disponibilidades, turnos y consultas médicas mediante una interfaz gráfica desarrollada con **Java Swing**, utilizando **JDBC + MySQL/MariaDB** para la persistencia de datos.

<p align="center">
  <img src="src/resources/imagenes/login.gif" alt="Telemedicina" width="190">
</p>

## Descripción

El sistema implementa dos perfiles de acceso:

- **EMPLEADO / Recepción:** administra pacientes, profesionales, turnos e información clínica.
- **PROFESIONAL:** administra su disponibilidad, consulta su agenda, informa retrasos, registra consultas realizadas y accede a su historial de atenciones.

La autenticación utiliza contraseñas almacenadas mediante **BCrypt** y la navegación se realiza mediante ventanas construidas con **Java Swing**.

---

## Funcionalidades principales

### Empleado / Recepción

- Inicio de sesión según rol.
- Registro y modificación de pacientes.
- Validación de DNI.
- Validación de teléfono.
- Control de DNI duplicado.
- Eliminación segura de pacientes sin turnos asociados.
- Registro de profesionales.
- Validación de nombre de usuario duplicado.
- Validación de matrícula duplicada.
- Validación de formato de matrícula.
- Validación de contraseña.
- Consulta de disponibilidad de profesionales.
- Asignación de turnos.
- Modificación de turnos.
- Confirmación de turnos.
- Registro de llegada del paciente.
- Cancelación de turnos.
- Visualización del detalle de turnos.
- Consulta del historial de pacientes.
- Visualización de retrasos informados por profesionales.

### Profesional

- Inicio de sesión con perfil profesional.
- Alta y modificación de disponibilidad.
- Validación de fechas y horarios.
- Control de superposición de disponibilidades.
- Consulta de agenda.
- Registro de retraso estimado.
- Registro de consultas realizadas.
- Carga obligatoria de observaciones.
- Carga opcional de seguimiento.
- Consulta del historial clínico.

---

## Tecnologías utilizadas

- **Java**
- **Java Swing**
- **JDBC**
- **MySQL / MariaDB**
- **XAMPP**
- **BCrypt**
- **Eclipse IDE**
- **Git**
- **GitHub**

---

## Conceptos aplicados

- Programación orientada a objetos.
- Encapsulamiento.
- Interfaces.
- Enumeraciones.
- Excepciones.
- JDBC.
- `PreparedStatement`.
- `ResultSet`.
- `try-with-resources`.
- Transacciones.
- `COMMIT` y `ROLLBACK`.
- `SELECT ... FOR UPDATE`.
- Claves primarias.
- Claves foráneas.
- Restricciones `UNIQUE`.
- Patrón **Singleton** para centralizar la configuración de acceso a la base.
- Conexiones JDBC independientes por controller.
- Separación por capas.
- Interfaces `Repository`.
- BCrypt para almacenamiento seguro de contraseñas.
- Validaciones de negocio en GUI y backend.
- Estrategia **fail-closed** en validaciones críticas.

---

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

---

## Base de datos

La base utilizada por el sistema se llama:

```text
telemedicina
```

El archivo para reconstruir su estructura y datos iniciales se encuentra en:

```text
database/telemedicina.sql
```

El esquema contiene las tablas:

```text
usuario
profesional
paciente
disponibilidad
turno
consulta
```

La integridad de los datos se refuerza mediante:

- claves primarias;
- claves foráneas;
- restricciones `UNIQUE`;
- estados controlados mediante `ENUM`;
- controles de duplicados desde Java;
- validaciones adicionales en backend.

---

## Estados de los turnos

Los turnos utilizan el enum `EstadoTurno`:

```text
PENDIENTE
CONFIRMADO
CANCELADO
PRESENTE
```

Las transiciones son validadas desde el backend para impedir cambios de estado inválidos.

El flujo normal es:

```text
PENDIENTE
    ↓
CONFIRMADO
    ↓
PRESENTE
```

También pueden cancelarse los turnos cuando su estado lo permite.

---

## Control de horarios y concurrencia

Los turnos trabajan con una duración lógica fija de:

```text
30 minutos
```

El sistema valida superposiciones por intervalo.

Por ejemplo:

```text
10:00 - 10:30
```

impide registrar:

```text
10:01
10:15
10:29
```

pero permite:

```text
10:30
```

El registro utiliza transacciones y bloqueo:

```text
SELECT ... FOR UPDATE
```

para reducir condiciones de carrera durante la asignación de turnos.

La base también incorpora la columna generada:

```text
hora_activa
```

y la restricción:

```text
uq_turno_disponibilidad_hora_activa
```

para impedir horarios activos duplicados dentro de una misma disponibilidad.

Los turnos cancelados pueden liberar nuevamente su horario.

---

## Seguridad de contraseñas

Las contraseñas no se almacenan en texto plano.

El proyecto utiliza:

```text
lib/jbcrypt-0.4.jar
```

y BCrypt para generar y verificar los hashes.

Para nuevas credenciales profesionales se valida:

```text
mínimo: 8 bytes
máximo: 72 bytes
```

El hash recuperado desde la base se utiliza únicamente durante la autenticación y **no queda almacenado dentro del objeto `Usuario` autenticado**.

---

## Validaciones principales

### Pacientes

El backend valida:

```text
DNI:
7 a 8 dígitos numéricos

Teléfono:
8 a 15 dígitos numéricos
```

También se impide:

- registrar DNI duplicados;
- modificar un paciente utilizando el DNI de otro;
- eliminar pacientes con turnos asociados.

Las comprobaciones críticas trabajan con estrategia **fail-closed**: ante un error al verificar una restricción, la operación se bloquea.

### Profesionales

Se valida:

```text
Matrícula:
4 a 20 caracteres
letras
números
guiones
sin espacios
```

Las matrículas son normalizadas a mayúsculas.

También se controlan:

- nombre de usuario duplicado;
- matrícula duplicada;
- longitud de contraseña;
- campos obligatorios.

### Consultas

Una consulta solamente puede registrarse cuando:

- el turno está en estado `PRESENTE`;
- las observaciones no están vacías;
- el turno todavía no posee una consulta.

La base refuerza la última condición mediante:

```text
UNIQUE(turno_id)
```

---

# Configuración de la base de datos

## 1. Importar el esquema

Iniciar MySQL/MariaDB desde XAMPP.

Luego ingresar a phpMyAdmin e importar:

```text
database/telemedicina.sql
```

La base debe quedar disponible como:

```text
telemedicina
```

---

## 2. Crear un usuario para la aplicación

La aplicación **no utiliza `root`**.

Se recomienda crear un usuario específico con permisos limitados.

Ejemplo:

```sql
CREATE USER IF NOT EXISTS 'telemedicina_app'@'localhost'
IDENTIFIED BY 'SU_CLAVE_LOCAL';

GRANT SELECT, INSERT, UPDATE, DELETE
ON telemedicina.*
TO 'telemedicina_app'@'localhost';

FLUSH PRIVILEGES;
```

La contraseña utilizada debe permanecer únicamente en el entorno local y no debe incorporarse al repositorio.

---

## 3. Variables de entorno

Las credenciales no se encuentran almacenadas dentro de `Conexion.java`.

La aplicación requiere estas variables:

```text
TELEMEDICINA_DB_URL
TELEMEDICINA_DB_USER
TELEMEDICINA_DB_PASSWORD
```

Ejemplo de configuración:

```text
TELEMEDICINA_DB_URL=jdbc:mysql://localhost:3306/telemedicina
TELEMEDICINA_DB_USER=telemedicina_app
TELEMEDICINA_DB_PASSWORD=SU_CLAVE_LOCAL
```

---

## Configuración en Eclipse

Abrir:

```text
Run
→ Run Configurations...
→ Java Application
→ Main
→ Environment
```

Agregar:

```text
TELEMEDICINA_DB_URL
TELEMEDICINA_DB_USER
TELEMEDICINA_DB_PASSWORD
```

Luego presionar:

```text
Apply
```

y ejecutar la configuración `Main`.

---

## Manejo de conexiones

`Conexion` mantiene una instancia Singleton para centralizar la configuración, pero **no mantiene una única `Connection` JDBC global compartida**.

Cada controller obtiene su propia conexión:

```text
Conexion
   │
   ├── Connection → ControllerPaciente
   ├── Connection → ControllerProfesional
   ├── Connection → ControllerTurno
   ├── Connection → ControllerConsulta
   └── Connection → ControllerUsuario
```

Esto evita que una operación transaccional como:

```java
connection.setAutoCommit(false);
```

afecte accidentalmente a otros controllers.

Antes de abrir el login, `Main` ejecuta un preflight de conexión.

Si MySQL no está disponible o la configuración es incorrecta:

```text
Error de conexión
```

se muestra de forma controlada y el sistema no continúa con una conexión inválida.

---

# Cómo ejecutar el proyecto

## 1. Requisitos

Tener instalado:

- Java.
- Eclipse IDE.
- XAMPP o un servidor MySQL/MariaDB compatible.

## 2. Iniciar MySQL

Desde XAMPP:

```text
MySQL → Start
```

## 3. Importar la base

Importar:

```text
database/telemedicina.sql
```

## 4. Crear el usuario de aplicación

Crear un usuario MySQL/MariaDB con permisos:

```text
SELECT
INSERT
UPDATE
DELETE
```

sobre:

```text
telemedicina.*
```

## 5. Configurar variables de entorno

Configurar:

```text
TELEMEDICINA_DB_URL
TELEMEDICINA_DB_USER
TELEMEDICINA_DB_PASSWORD
```

en la configuración `Main` de Eclipse.

## 6. Verificar las librerías

El proyecto utiliza:

```text
lib/jbcrypt-0.4.jar
lib/mysql-connector-java-5.1.13.jar
```

Si Eclipse no las reconoce:

```text
Project
→ Properties
→ Java Build Path
→ Libraries
→ Add JARs...
```

## 7. Ejecutar

Ejecutar:

```text
src/GUI/Main.java
```

como:

```text
Java Application
```

Si la conexión es correcta, la consola mostrará:

```text
Se conecto correctamente a Telemedicina
```

y se abrirá `LoginFrame`.

---

## Flujo general

```text
Main
 ↓
Preflight de conexión
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

---

## Persistencia y acceso a datos

La aplicación mantiene separadas la interfaz y la persistencia:

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

Las ventanas Swing no ejecutan consultas SQL directamente.

Las consultas utilizan:

```text
PreparedStatement
try-with-resources
ResultSet
```

y las operaciones críticas utilizan transacciones cuando corresponde.

---

## Estado del proyecto

El sistema cuenta con los principales flujos funcionales implementados y validados:

- Login por roles.
- Gestión de pacientes.
- Gestión de profesionales.
- Validaciones en GUI y backend.
- Gestión del ciclo de turnos.
- Control de superposición de horarios.
- Disponibilidad profesional.
- Control de superposición de disponibilidades.
- Retrasos.
- Registro de llegada.
- Registro de consultas.
- Historial clínico.
- Integridad referencial.
- Control de duplicados.
- Transacciones.
- Manejo controlado de fallos de conexión.
- Credenciales externas al código fuente.
- Interfaz gráfica diferenciada para recepción y profesionales.

---

## Autor

**Joaquin Gonzalez Garcia**

Proyecto académico desarrollado para la materia **Programación Avanzada**.
