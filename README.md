# 🔧 Electrónica Doméstica - API REST

Sistema integral de gestión para talleres de electrónica doméstica. API RESTful desarrollada con **Java 17**, **Javalin**, y **MySQL** siguiendo principios de **Arquitectura Hexagonal** (Puertos y Adaptadores).

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Javalin](https://img.shields.io/badge/Javalin-5.6.3-blue?style=for-the-badge)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql)
![Gradle](https://img.shields.io/badge/Gradle-8.0-green?style=for-the-badge&logo=gradle)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

---

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Arquitectura](#-arquitectura)
- [Tecnologías](#-tecnologías)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [Base de Datos](#-base-de-datos)
- [Ejecución](#-ejecución)
- [API Endpoints](#-api-endpoints)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Testing](#-testing)
- [Contribuir](#-contribuir)
- [Licencia](#-licencia)

---

## 🌟 Características

### Módulos Principales

#### 🔐 Autenticación y Autorización
- ✅ Registro de usuarios (Admin, Técnico, Recepcionista)
- ✅ Login con JWT (JSON Web Tokens)
- ✅ Recuperación de contraseña por email
- ✅ Encriptación de contraseñas con BCrypt

#### 🔧 Gestión de Reparaciones
- ✅ Registro de tarjetas de reparación
- ✅ Asignación a técnicos
- ✅ Seguimiento de estados (En Progreso, Finalizado, Entregado)
- ✅ Historial completo de reparaciones

#### 📦 Inventario de Productos
- ✅ CRUD completo de productos
- ✅ Categorización (Resistencias, Capacitores, Transistores, etc.)
- ✅ Control de stock
- ✅ Alertas de stock bajo
- ✅ Búsqueda por categoría

#### ✅ Registro de Finalizados
- ✅ Registro de trabajos completados
- ✅ Costo de reparación
- ✅ Fecha de entrega
- ✅ Consultas por técnico
- ✅ Reportes de ingresos

---

## 🏗️ Arquitectura

Este proyecto implementa **Arquitectura Hexagonal (Puertos y Adaptadores)**, garantizando:

- **Separación de Responsabilidades**: Lógica de negocio independiente de frameworks
- **Testabilidad**: Fácil creación de tests unitarios
- **Mantenibilidad**: Código organizado y escalable
- **Flexibilidad**: Cambio de tecnologías sin afectar la lógica de negocio

```
┌─────────────────────────────────────────────────────────┐
│                    CONTROLLERS                          │
│              (Infrastructure Layer)                     │
└─────────────────┬───────────────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────────────┐
│                   USE CASES                             │
│              (Application Layer)                        │
└─────────────────┬───────────────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────────────┐
│              DOMAIN MODELS & PORTS                      │
│                (Domain Layer)                           │
└─────────────────┬───────────────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────────────┐
│                  ADAPTERS                               │
│    (Infrastructure: DB, Email, External APIs)           │
└─────────────────────────────────────────────────────────┘
```

---

## 💻 Tecnologías

| Tecnología | Versión | Propósito |
|-----------|---------|-----------|
| **Java** | 17 | Lenguaje principal |
| **Javalin** | 5.6.3 | Framework web ligero |
| **MySQL** | 8.0+ | Base de datos |
| **HikariCP** | 5.1.0 | Connection pooling |
| **BCrypt** | 0.4 | Encriptación de contraseñas |
| **JWT** | 4.4.0 | Autenticación |
| **JavaMail** | 1.6.2 | Envío de emails |
| **Dotenv** | 3.0.0 | Variables de entorno |
| **Jackson** | 2.15.3 | Serialización JSON |
| **SLF4J** | 2.0.9 | Logging |
| **Gradle** | 8.0+ | Build tool |

---

## 📦 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

- ☑️ **Java JDK 17** o superior ([Descargar](https://www.oracle.com/java/technologies/downloads/#java17))
- ☑️ **MySQL 8.0+** ([Descargar](https://dev.mysql.com/downloads/mysql/))
- ☑️ **Gradle 8.0+** ([Descargar](https://gradle.org/install/))
- ☑️ **Git** ([Descargar](https://git-scm.com/downloads))
- ☑️ Una cuenta de **Gmail** con contraseña de aplicación

### Verificar instalaciones:

```bash
java -version    # Debe mostrar Java 17+
mysql --version  # Debe mostrar MySQL 8.0+
gradle --version # Debe mostrar Gradle 8.0+
```

---

## 🚀 Instalación

### 1. Clonar el Repositorio

```bash
git clone https://github.com/tu-usuario/electronica-domestica-api.git
cd electronica-domestica-api
```

### 2. Instalar Dependencias

```bash
gradle clean build
```

---

## ⚙️ Configuración

### 1. Crear Base de Datos

Ejecuta el script SQL incluido:

```bash
mysql -u root -p < database.sql
```

O manualmente en MySQL Workbench:

```sql
CREATE DATABASE electronica_domestica;
USE electronica_domestica;
-- Ejecutar el contenido de database.sql
```

### 2. Configurar Variables de Entorno

Crea un archivo `.env` en la raíz del proyecto:

```bash
cp .env.example .env
```

Edita `.env` con tus credenciales:

```properties
# ---- SERVIDOR ----
SERVER_PORT=7000

# ---- BASE DE DATOS ----
DB_URL=jdbc:mysql://localhost:3306/electronica_domestica?useSSL=false&serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=tu_contraseña_mysql

# ---- JWT ----
JWT_SECRET=tu-clave-secreta-de-minimo-32-caracteres-aleatorios
JWT_EXPIRATION=86400000

# ---- EMAIL (GMAIL) ----
EMAIL_FROM=tu-email@gmail.com
EMAIL_PASSWORD=tu-contraseña-de-aplicacion-google
EMAIL_SMTP_HOST=smtp.gmail.com
EMAIL_SMTP_PORT=587

# ---- APLICACIÓN ----
APP_NAME=Electronica Domestica API
APP_FRONTEND_URL=http://localhost:3000
```

### 3. Configurar Gmail para Envío de Emails

Para enviar emails de recuperación de contraseña:

1. Ve a tu [Cuenta de Google](https://myaccount.google.com/)
2. Navega a **Seguridad** → **Verificación en dos pasos** (actívala si no lo está)
3. Busca **Contraseñas de aplicaciones**
4. Genera una nueva contraseña para "Correo"
5. Copia la contraseña de 16 caracteres
6. Pégala en `EMAIL_PASSWORD` en tu archivo `.env`

📖 **Guía completa**: [Contraseñas de aplicaciones de Google](https://support.google.com/accounts/answer/185833)

---

## 🗄️ Base de Datos

### Tablas Principales

#### 📊 users
```sql
- id (VARCHAR 36) - PK
- nombre_completo (VARCHAR 255)
- correo_electronico (VARCHAR 255) - UNIQUE
- contrasena (VARCHAR 255) - Hasheada con BCrypt
- tipo (VARCHAR 50) - ADMIN | TECNICO | RECEPCIONISTA
- reset_token (VARCHAR 36) - Para recuperación de contraseña
- reset_token_expiry (TIMESTAMP)
```

#### 📊 registro_tarjeta
```sql
- id (VARCHAR 36) - PK
- nombre_cliente (VARCHAR 255)
- numero_celular (VARCHAR 20)
- marca (VARCHAR 100)
- modelo (VARCHAR 100)
- problema_descrito (TEXT)
- tecnico_id (VARCHAR 36) - FK → users
- estado (VARCHAR 50) - EN_PROGRESO | FINALIZADO | ENTREGADO
- fecha_registro, fecha_finalizacion, fecha_entrega
```

#### 📊 productos
```sql
- id (VARCHAR 36) - PK
- nombre_producto (VARCHAR 255)
- categoria (VARCHAR 100) - Resistencia | Capacitor | Transistor | etc.
- cantidad_ohms (DECIMAL 15,4) - Opcional
- unidad (VARCHAR 50) - Ohms | uF | mH | etc.
- cantidad_piezas (INT)
- precio_unitario (DECIMAL 10,2)
```

#### 📊 registro_finalizado
```sql
- id (VARCHAR 36) - PK
- registro_tarjeta_id (VARCHAR 36) - FK → registro_tarjeta
- nombre_cliente, numero_celular, marca, modelo
- problema_cambiado (TEXT)
- tecnico_id (VARCHAR 36) - FK → users
- fecha_entrega (TIMESTAMP)
- costo_reparacion (DECIMAL 10,2)
```

### Diagrama de Relaciones

```
┌──────────┐       ┌─────────────────┐       ┌───────────────────┐
│  users   │◄─────►│registro_tarjeta │◄─────►│registro_finalizado│
└──────────┘       └─────────────────┘       └───────────────────┘
                            │
                            ▼
                     ┌──────────┐
                     │productos │
                     └──────────┘
```

---

## ▶️ Ejecución

### Opción 1: Con Gradle

```bash
# Compilar y ejecutar
gradle run

# O construir JAR y ejecutar
gradle build
java -jar build/libs/electronica-domestica-1.0-SNAPSHOT.jar
```

### Opción 2: Con IDE (IntelliJ IDEA / Eclipse)

1. Importa el proyecto como proyecto Gradle
2. Ejecuta la clase `Main.java`
3. La API estará disponible en `http://localhost:7000`

### Verificar que funciona:

```bash
curl http://localhost:7000/api/health
```

Respuesta esperada:
```json
{
  "status": "OK",
  "message": "API funcionando correctamente",
  "database": "MySQL - electronica_domestica",
  "modules": ["Autenticación ✅", "Registro de Tarjetas ✅", "Productos ✅", "Registro Finalizado ✅"]
}
```

---

## 📡 API Endpoints

### Base URL: `http://localhost:7000/api`

### 🔐 Autenticación

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/auth/register` | Registrar nuevo usuario |
| `POST` | `/auth/login` | Iniciar sesión |
| `POST` | `/auth/request-reset` | Solicitar recuperación de contraseña |
| `POST` | `/auth/reset-password` | Restablecer contraseña |

#### Ejemplo: Registro

```bash
curl -X POST http://localhost:7000/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nombreCompleto": "Juan Pérez",
    "correoElectronico": "juan@example.com",
    "contrasena": "password123",
    "tipo": "TECNICO"
  }'
```

#### Ejemplo: Login

```bash
curl -X POST http://localhost:7000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "correoElectronico": "juan@example.com",
    "contrasena": "password123"
  }'
```

---

### 🔧 Registro de Tarjetas

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/tarjetas` | Crear nueva tarjeta |
| `GET` | `/tarjetas` | Obtener todas las tarjetas |
| `GET` | `/tarjetas/:id` | Obtener tarjeta por ID |
| `PUT` | `/tarjetas/:id` | Actualizar tarjeta |
| `DELETE` | `/tarjetas/:id` | Eliminar tarjeta |

#### Ejemplo: Crear Tarjeta

```bash
curl -X POST http://localhost:7000/api/tarjetas \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TU_JWT_TOKEN" \
  -d '{
    "nombreCliente": "Carlos López",
    "numeroCelular": "9611234567",
    "marca": "Samsung",
    "modelo": "UN55TU8000",
    "problemaDescrito": "No enciende la pantalla",
    "tecnicoId": "tecnico-001",
    "tecnicoNombre": "Juan Pérez"
  }'
```

---

### 📦 Productos

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/productos` | Crear nuevo producto |
| `GET` | `/productos` | Obtener todos los productos |
| `GET` | `/productos/:id` | Obtener producto por ID |
| `PUT` | `/productos/:id` | Actualizar producto |
| `DELETE` | `/productos/:id` | Eliminar producto |
| `GET` | `/productos/categoria/:categoria` | Filtrar por categoría |
| `GET` | `/productos/stock-bajo?threshold=10` | Productos con stock bajo |

#### Ejemplo: Crear Producto

```bash
curl -X POST http://localhost:7000/api/productos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TU_JWT_TOKEN" \
  -d '{
    "nombreProducto": "Resistencia 1K",
    "categoria": "Resistencia",
    "cantidadOhms": 1000,
    "unidad": "Ohms",
    "cantidadPiezas": 500,
    "precioUnitario": 0.50
  }'
```

---

### ✅ Registro Finalizado

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/finalizados` | Crear registro finalizado |
| `GET` | `/finalizados` | Obtener todos |
| `GET` | `/finalizados/:id` | Obtener por ID |
| `PUT` | `/finalizados/:id` | Actualizar |
| `DELETE` | `/finalizados/:id` | Eliminar |
| `GET` | `/finalizados/tecnico/:tecnicoId` | Por técnico |

#### Ejemplo: Finalizar Reparación

```bash
curl -X POST http://localhost:7000/api/finalizados \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TU_JWT_TOKEN" \
  -d '{
    "registroTarjetaId": "tarjeta-001",
    "nombreCliente": "Carlos López",
    "numeroCelular": "9611234567",
    "marca": "Samsung",
    "modelo": "UN55TU8000",
    "problemaCambiado": "Se reemplazó la fuente de poder",
    "tecnicoId": "tecnico-001",
    "tecnicoNombre": "Juan Pérez",
    "fechaEntrega": "15/noviembre/2024",
    "costoReparacion": 850.00
  }'
```

---

## 📁 Estructura del Proyecto

```
electronica-domestica-api/
│
├── src/main/java/com/electronica/
│   ├── Main.java                          # Punto de entrada
│   │
│   ├── auth/                              # Módulo de Autenticación
│   │   ├── application/
│   │   │   ├── dto/
│   │   │   └── usecase/
│   │   ├── domain/
│   │   │   ├── model/
│   │   │   └── port/
│   │   └── infrastructure/
│   │       ├── adapter/
│   │       ├── config/
│   │       └── controller/
│   │
│   ├── tarjeta/                           # Módulo de Tarjetas
│   │   ├── application/
│   │   ├── domain/
│   │   └── infrastructure/
│   │
│   ├── producto/                          # Módulo de Productos
│   │   ├── application/
│   │   ├── domain/
│   │   └── infrastructure/
│   │
│   └── finalizado/                        # Módulo de Finalizados
│       ├── application/
│       ├── domain/
│       └── infrastructure/
│
├── src/main/resources/
│   └── (archivos de configuración)
│
├── .env                                   # Variables de entorno (NO subir a Git)
├── .env.example                           # Ejemplo de variables
├── .gitignore
├── build.gradle                           # Configuración de Gradle
├── database.sql                           # Script de base de datos
├── README.md                              # Este archivo
└── settings.gradle
```

---

## 🧪 Testing

### Ejecutar Tests

```bash
gradle test
```

### Cobertura de Tests

```bash
gradle jacocoTestReport
```

Abre `build/reports/jacoco/test/html/index.html` en tu navegador.

---

## 🤝 Contribuir

¡Las contribuciones son bienvenidas! Por favor sigue estos pasos:

1. **Fork** el proyecto
2. Crea una **rama** para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. **Commit** tus cambios (`git commit -m 'Agregar nueva funcionalidad'`)
4. **Push** a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un **Pull Request**

### Guía de Estilo

- Usa nombres descriptivos en inglés para variables y métodos
- Sigue los principios SOLID
- Escribe tests para nuevas funcionalidades
- Documenta tu código con JavaDoc

---

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver archivo `LICENSE` para más detalles.

---

## 👨‍💻 Autor

**Tu Nombre**
- GitHub: [@tu-usuario](https://github.com/tu-usuario)
- Email: tu-email@ejemplo.com

---

## 🙏 Agradecimientos

- [Javalin](https://javalin.io/) - Framework web minimalista
- [HikariCP](https://github.com/brettwooldridge/HikariCP) - Connection pooling
- [Auth0 JWT](https://github.com/auth0/java-jwt) - Manejo de JWT

---

## 📞 Soporte

Si tienes alguna pregunta o problema:

1. Revisa la sección [Issues](https://github.com/tu-usuario/electronica-domestica-api/issues)
2. Abre un nuevo issue si es necesario
3. Contacta al autor

---

## 🔜 Roadmap

- [ ] Implementar autenticación con roles y permisos
- [ ] Agregar endpoints de reportes
- [ ] Implementar paginación en listados
- [ ] Agregar documentación con Swagger/OpenAPI
- [ ] Crear dashboard de estadísticas
- [ ] Implementar WebSockets para notificaciones en tiempo real
- [ ] Agregar soporte para imágenes de productos
- [ ] Implementar sistema de backups automáticos

---

<div align="center">

**⭐ Si este proyecto te fue útil, considera darle una estrella ⭐**

Hecho con ❤️ y ☕ en México

</div>