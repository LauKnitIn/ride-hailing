# Ride Hailing API

API REST de una plataforma de transporte tipo *ride hailing* construida con **Spring Boot** y organizada con **arquitectura hexagonal** por módulos de dominio: pasajeros, conductores, viajes, emparejamiento de ofertas, tarifas y calificaciones.

> ⚠️ **Proyecto en desarrollo**
> La aplicación aún está en construcción, por lo que el trabajo activo se realiza sobre la rama **`develop`**.
> La rama `main` puede estar desactualizada. Para ejecutar la versión más reciente, clona y usa `develop`.

---

## Requisitos de ejecución

| Requisito | Versión | Notas |
|---|---|---|
| **JDK** | **25** | El `pom.xml` define `java.version=25`. Verifica con `java -version`. |
| **Maven** | 3.9.x *(opcional)* | No es necesario instalarlo: el proyecto incluye Maven Wrapper (`mvnw` / `mvnw.cmd`), que descarga Maven 3.9.16 automáticamente. |
| **Git** | Cualquier versión reciente | Para clonar el repositorio y cambiar de rama. |
| **Bruno** | *(opcional)* | Cliente de API para probar los endpoints con la colección incluida en `bruno/`. |

No se necesita instalar ninguna base de datos: cada módulo usa su propia base **H2 en memoria**, que se crea al iniciar la aplicación.

### Stack principal

- Spring Boot 4.1.1 (Web MVC, Data JPA, Validation)
- H2 (base en memoria) — driver de PostgreSQL incluido para uso futuro
- Flyway
- Lombok
- JUnit (pruebas unitarias y de integración)

---

## Cómo ejecutar

### 1. Clonar el repositorio y cambiar a `develop`

```bash
git clone https://github.com/LauKnitIn/ride-hailing.git
cd ride-hailing
git checkout develop
```

O directamente clonando la rama:

```bash
git clone -b develop https://github.com/LauKnitIn/ride-hailing.git
```

### 2. Levantar la aplicación

**Linux / macOS**

```bash
./mvnw spring-boot:run
```

**Windows (PowerShell o CMD)**

```bash
mvnw.cmd spring-boot:run
```

> En Linux/macOS, si aparece un error de permisos, ejecuta primero `chmod +x mvnw`.

La API queda disponible en **http://localhost:8080**.

### 3. Generar el JAR (opcional)

```bash
./mvnw clean package
java -jar target/matching_rides-0.0.1-SNAPSHOT.jar
```

### 4. Ejecutar las pruebas

```bash
./mvnw test
```

---

## Probar la API con Bruno

La carpeta [`bruno/`](bruno/) contiene una colección lista para usar:

1. Con la aplicación corriendo, abre Bruno → **Open Collection** → selecciona la carpeta `bruno/`.
2. Elige el entorno **Local** (`baseUrl = http://localhost:8080`).
3. Ejecuta las carpetas **en orden** (clic derecho → **Run**):
   - `1 Flujo completo` — camino feliz: registro, solicitud de viaje, oferta, viaje, tarifa y calificación.
   - `2 Flujos alternativos` — cancelaciones con cargo parcial, rechazo de ofertas, asignación directa, etc.
   - `3 Casos de error` — respuestas 400, 403, 404 y 409 esperadas.

> Como las bases de datos son en memoria, al reiniciar la aplicación se pierden los datos y hay que volver a correr la carpeta 1.

Más detalles en [`bruno/README.md`](bruno/README.md).

---

## Endpoints principales

| Módulo | Ruta base |
|---|---|
| Pasajeros | `/api/pasajeros` |
| Conductores | `/api/conductores` |
| Viajes | `/api/viajes` |
| Emparejamiento / ofertas | `/api/ofertas` |
| Tarifas | `/api/tarifas` |
| Calificaciones | `/api/calificaciones` |

---

## Estructura del proyecto

```
src/main/java/com/ride_hailing/
├── pasajeros/              # Registro y perfil de pasajeros
├── conductores/            # Registro, disponibilidad y ubicación de conductores
├── viajes/                 # Ciclo de vida del viaje y eventos
├── emparejamientoOfertas/  # Emparejamiento conductor–viaje y ofertas
├── tarifas/                # Cálculo de tarifas y cargos parciales
├── calificacion/           # Calificación de conductores
└── shared/                 # Manejo global de errores
```

Cada módulo sigue la separación **dominio → aplicación (puertos) → infraestructura (adaptadores de entrada y salida)**.

---

## Flujo de trabajo con ramas

- **`develop`**: rama de integración donde se trabaja actualmente.
- **`feat/*`** y ramas por módulo: desarrollo de funcionalidades, que se integran a `develop` mediante Pull Request.
- **`main`**: se actualizará con versiones estables cuando el proyecto esté listo.
