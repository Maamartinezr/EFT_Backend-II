# Aplicativo Minimarket plus - Backend II EFT S9

Proyecto backend desarrollado con **Java** y **Spring Boot** para la gestión de un minimarket.  
Incluye módulos de productos, categorías, inventario, ventas, detalle de ventas, usuarios, seguridad y documentación de API.

---

## Descripción del proyecto

Este sistema corresponde a un backend REST orientado a la administración de un minimarket.  
Su objetivo es exponer servicios para la gestión de entidades principales del negocio, con arquitectura por capas y buenas prácticas de mantenibilidad:

- **Controller**
- **Service**
- **Repository**
- **Entity**
- **DTO**
- **Security**
- **Exception Handling**
- **Test**

---

## Tecnologías utilizadas

- **Java 17**
- **Spring Boot 3**
- **Spring Web**
- **Spring Security**
- **Spring Data JPA**
- **Bean Validation**
- **springdoc-openapi (Swagger UI)**
- **JUnit 5**
- **Mockito**
- **Maven**
- **Jenkins**
- **OpenAPI**
- **HATEOAS**

---

## Características principales

- API REST para administración de minimarket
- CRUD de entidades principales
- Seguridad con autenticación JWT
- Autorización por roles en endpoints críticos
- Documentación interactiva con Swagger UI (OpenAPI 3)
- Contratos de entrada/salida mediante DTOs
- Manejo global de excepciones
- Pruebas unitarias y de capa controller
- Estructura preparada para integración continua con Jenkins

---

### Función de `DataInitializer` y consola H2

- La clase `DataInitializer` se ejecuta automáticamente al iniciar la aplicación Spring Boot. Su función es cargar datos iniciales en la base de datos H2 en memoria, permitiendo probar el sistema sin tener que insertar registros manualmente.

- Gracias a esta clase, al levantar el backend se crean roles, usuarios de prueba, categorías, productos, movimientos de inventario y carritos asociados a usuarios. Esto facilita la validación de los endpoints desde Swagger y Postman, ya que la aplicación inicia con información suficiente para consultar productos, probar autenticación JWT, revisar stock bajo, visualizar inventario y comprobar respuestas HATEOAS.

La consola H2 está disponible en: http://localhost:9090/h2-console

## Estructura general del proyecto

```bash
minimarket/
│
├── .idea/                          # Archivos de configuración de IDE
├── .mvn/                           # Archivos de configuración del Maven Wrapper
├── output/                         # Evidencias, informes y reportes generados
│   ├── jacoco/                     # Reporte de cobertura JaCoCo
│   ├── pdf/                        # Informe final en PDF
│   │   └── Informe_EFT_Minimarket_Plus.pdf
│   ├── testing/                    # Resultados de pruebas Surefire
│   └── word/                       # Informe editable en Word
│       └── Informe_EFT_Minimarket_Plus.docx
│
├── src/
│   ├── main/
│   │   ├── java/com/minimarket/
│   │   │   ├── config/             # Configuraciones generales del backend
│   │   │   │   ├── DataInitializer.java
│   │   │   │   └── OpenApiConfig.java
│   │   │   │
│   │   │   ├── controller/         # Controladores REST de la aplicación
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── CarritoController.java
│   │   │   │   ├── CategoriaController.java
│   │   │   │   ├── DetalleVentaController.java
│   │   │   │   ├── HolaMundoController.java
│   │   │   │   ├── InventarioController.java
│   │   │   │   ├── ProductoController.java
│   │   │   │   ├── UsuarioController.java
│   │   │   │   └── VentaController.java
│   │   │   │
│   │   │   ├── dto/                # DTOs para respuestas controladas y documentación OpenAPI
│   │   │   │   ├── CarritoDTO.java
│   │   │   │   ├── InventarioDTO.java
│   │   │   │   ├── ProductoDTO.java
│   │   │   │   └── UsuarioDTO.java
│   │   │   │
│   │   │   ├── entity/             # Entidades del modelo de negocio
│   │   │   │   ├── Carrito.java
│   │   │   │   ├── Categoria.java
│   │   │   │   ├── DetalleVenta.java
│   │   │   │   ├── Inventario.java
│   │   │   │   ├── Producto.java
│   │   │   │   ├── Rol.java
│   │   │   │   ├── Usuario.java
│   │   │   │   └── Venta.java
│   │   │   │
│   │   │   ├── hateoas/            # Ensambladores HATEOAS para EntityModel y CollectionModel
│   │   │   │   ├── CarritoModelAssembler.java
│   │   │   │   ├── InventarioModelAssembler.java
│   │   │   │   ├── ProductoModelAssembler.java
│   │   │   │   └── UsuarioModelAssembler.java
│   │   │   │
│   │   │   ├── repository/         # Acceso a datos mediante Spring Data JPA
│   │   │   │   ├── CarritoRepository.java
│   │   │   │   ├── CategoriaRepository.java
│   │   │   │   ├── DetalleVentaRepository.java
│   │   │   │   ├── InventarioRepository.java
│   │   │   │   ├── ProductoRepository.java
│   │   │   │   ├── RolRepository.java
│   │   │   │   ├── UsuarioRepository.java
│   │   │   │   └── VentaRepository.java
│   │   │   │
│   │   │   ├── security/           # Componentes de seguridad y autenticación
│   │   │   │   ├── config/
│   │   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   │   └── SecurityConfig.java
│   │   │   │   ├── model/
│   │   │   │   │   ├── AuthResponse.java
│   │   │   │   │   ├── CustomUserDetails.java
│   │   │   │   │   ├── LoginRequest.java
│   │   │   │   │   └── RegisterRequest.java
│   │   │   │   ├── service/
│   │   │   │   │   └── CustomUserDetailsService.java
│   │   │   │   └── util/
│   │   │   │       └── JwtUtil.java
│   │   │   │
│   │   │   ├── service/            # Interfaces y lógica de negocio
│   │   │   │   ├── impl/           # Implementación de servicios
│   │   │   │   │   ├── CarritoServiceImpl.java
│   │   │   │   │   ├── CategoriaServiceImpl.java
│   │   │   │   │   ├── DetalleVentaServiceImpl.java
│   │   │   │   │   ├── InventarioServiceImpl.java
│   │   │   │   │   ├── ProductoServiceImpl.java
│   │   │   │   │   ├── RolServiceImpl.java
│   │   │   │   │   ├── UsuarioServiceImpl.java
│   │   │   │   │   └── VentaServiceImpl.java
│   │   │   │   │
│   │   │   │   ├── CarritoService.java
│   │   │   │   ├── CategoriaService.java
│   │   │   │   ├── DetalleVentaService.java
│   │   │   │   ├── InventarioService.java
│   │   │   │   ├── ProductoService.java
│   │   │   │   ├── RolService.java
│   │   │   │   ├── UsuarioService.java
│   │   │   │   └── VentaService.java
│   │   │   │
│   │   │   └── MinimarketApplication.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       └── java/com/minimarket/
│           ├── ApiDocumentationIntegrationTest.java
│           ├── AuthSecurityIntegrationTest.java
│           ├── DataInitializerIntegrationTest.java
│           ├── MinimarketApplicationTests.java
│           ├── ProductoServiceImplTest.java
│           ├── SecurityRoleAccessIntegrationTest.java
│           └── ServiceLayerUnitTest.java
│
├── target/                         # Archivos compilados generados por Maven
├── tmp/                            # Archivos temporales de trabajo
│
├── .gitattributes
├── .gitignore
├── HELP.md
├── mvnw                            # Script Maven Wrapper para Linux/Mac
├── mvnw.cmd                        # Script Maven Wrapper para Windows
└── pom.xml                         # Archivo de configuración y dependencias Maven
└── README                        
```

> Nota: la estructura exacta puede variar según la rama, pero esta representa la organización objetivo actual del proyecto.

---

## Módulos funcionales

- **Productos**
- **Categorías**
- **Inventario**
- **Ventas**
- **Detalle de ventas**
- **Usuarios**
- **Carrito**
- **Autenticación/Autorización**

---

## Seguridad (JWT)

La API utiliza seguridad con Spring Security y autenticación por token JWT.

### Comportamiento principal

- Acceso público a:
  - `/public/**`
  - `/api/auth/**`
  - `/v3/api-docs/**`
  - `/swagger-ui/**`
  - `/swagger-ui.html`
- Endpoints protegidos por autenticación
- Reglas por rol para operaciones críticas (por ejemplo, creación/edición/eliminación en módulos sensibles)

---

## Autenticación

### Endpoint de login

```http
POST /api/auth/login
Content-Type: application/json
```

### Body de ejemplo

```json
{
  "username": "admin",
  "password": "Admin123*"
}
```

### Respuesta esperada (ejemplo)

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9....",
  "tokenType": "Bearer"
}
```

### Uso del token

Incluir el token en el header:

```http
Authorization: Bearer <tu_token_jwt>
```

---

## Documentación de API (OpenAPI / Swagger UI)

Este proyecto usa **springdoc-openapi** para generar documentación automática e interactiva.

### URLs

- Swagger UI: `http://localhost:9090/swagger-ui.html`
- Alternativa: `http://localhost:9090/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:9090/v3/api-docs`
- Contrato versionado: `docs/openapi/openapi.json`

### Evidencias verificables

- Swagger UI local: `http://localhost:9090/swagger-ui.html`
- OpenAPI en ejecución: `http://localhost:9090/v3/api-docs`
- OpenAPI versionado en repositorio: `/docs/openapi/openapi.json`

### Respuestas HATEOAS en endpoints principales

Los endpoints clave de **Producto, Carrito, Inventario y Usuario** ahora incluyen enlaces dinámicos HATEOAS (`_links`) en respuestas de detalle (`EntityModel`) y listado (`CollectionModel`).

Verificación rápida en Swagger UI o Postman:
- `GET /api/productos/{id}`
- `GET /api/productos`
- `GET /api/carrito/{id}`
- `GET /api/carrito`
- `GET /api/inventario/{id}`
- `GET /api/inventario`
- `GET /api/usuarios/{id}`
- `GET /api/usuarios`

Ejemplo breve de respuesta:
```json
{
  "id": 1,
  "nombre": "Arroz 1",
  "_links": {
    "self": { "href": "http://localhost:9090/api/productos/1" },
    "collection": { "href": "http://localhost:9090/api/productos" }
  }
}
```

### Regenerar contrato OpenAPI (`openapi.json`)

1. Levantar la aplicación:
   - Linux/macOS: `mvn spring-boot:run`
   - Windows: `.\mvnw.cmd spring-boot:run`
2. Exportar el contrato:
   - Linux/macOS: `curl -s http://localhost:9090/v3/api-docs -o docs/openapi/openapi.json`
   - Windows PowerShell: `Invoke-WebRequest http://localhost:9090/v3/api-docs -OutFile docs/openapi/openapi.json`

### Qué puedes validar en Swagger

- Endpoints por módulo
- Parámetros de ruta/query
- Esquemas de request/response
- Códigos HTTP esperados
- Pruebas interactivas con **Try it out**
- Autenticación JWT mediante **Authorize**

---

## Validación con Postman

1. Levanta la aplicación.
2. Importa en Postman: `http://localhost:9090/v3/api-docs`.
3. Ejecuta `POST /api/auth/login` para obtener token.
4. Configura autorización tipo **Bearer Token**.
5. Prueba endpoints protegidos y valida:
   - `200 OK`
   - `201 Created`
   - `204 No Content`
   - `400 Bad Request`
   - `401 Unauthorized`
   - `403 Forbidden`
   - `404 Not Found`

---

## Endpoints principales (referenciales)

### Auth
- `POST /api/auth/login`

### Productos
GET /api/productos
GET /api/productos/{id}
POST /api/productos
PUT /api/productos/{id}
DELETE /api/productos/{id}
GET /api/productos/disponibles
GET /api/productos/stock-bajo.
Soporta page y size en listados

### Carrito
GET /api/carrito
GET /api/carrito/{id}
POST /api/carrito
PUT /api/carrito/{id}
DELETE /api/carrito/{id}.
Soporta page y size en listados con CollectionModel

### Categorías
- `GET /api/categorias`
- `GET /api/categorias/{id}`
- `POST /api/categorias`
- `PUT /api/categorias/{id}`
- `DELETE /api/categorias/{id}`

### Inventario
GET /api/inventario
GET /api/inventario/{id}
POST /api/inventario, PUT /api/inventario/{id}
DELETE /api/inventario/{id}.
Soporta page y size en listados con CollectionModel

### Ventas
- `GET /api/ventas`
- `GET /api/ventas/{id}`
- `POST /api/ventas`

### Detalle de ventas
- `GET /api/detalle-ventas`
- `GET /api/detalle-ventas/{id}`
- `POST /api/detalle-ventas`
- `PUT /api/detalle-ventas/{id}`
- `DELETE /api/detalle-ventas/{id}`

### Usuarios
GET /api/usuarios
GET /api/usuarios/{id}
POST /api/usuarios
PUT /api/usuarios/{id}
DELETE /api/usuarios/{id}
/api/auth/login y /api/auth/register
Las respuestas usan UsuarioDTO para no exponer password

### Público
- `GET /public/hola`

---

## Requisitos previos

- **Java 17**
- **Maven**
- **Git**
- IDE (VS Code / IntelliJ / Eclipse)

---

## Instalación y ejecución local

### 1) Clonar repositorio

```bash
git clone https://github.com/Maamartinezr/Backend-II_Sumativa_S6_Grupo5.git
cd Backend-II_Sumativa_S6_Grupo5
```

### 2) Compilar

```bash
.\mvnw.cmd clean install
```

### 3) Ejecutar

```bash
.\mvnw.cmd spring-boot:run
```

> La app corre en `http://localhost:9090`.

---

## Ejecución de pruebas

```bash
.\mvnw.cmd verify
```

o

```bash
.\mvnw.cmd clean test
```

---

## Integración continua (Jenkins)

Flujo sugerido de pipeline:

1. Checkout
2. `.\mvnw.cmd clean install`
3. `.\mvnw.cmd clean test`
4. Publicación de resultados

---

## Estado actual y mejoras implementadas

En la evolución reciente del proyecto se incorporaron/mejoraron:

- Documentación OpenAPI con Swagger UI
- Flujo de autenticación JWT
- Endpoints de autenticación
- Uso de DTOs para desacoplar entidad de contratos API
- Validaciones con Bean Validation
- Mejoras de consistencia de respuestas HTTP
- Fortalecimiento de pruebas en capa controller/servicio

---

## Próximas mejoras sugeridas

- Cobertura de pruebas más amplia (incluyendo seguridad y casos límite)
- Mayor estandarización de respuestas de error
- Versionado de API
- Hardening adicional de seguridad
- Pipeline Jenkins con despliegue automatizado
- Documentación técnica de base de datos y arquitectura

---

## Autor

Proyecto desarrollado por **Maamartinezr**. Jefe de proyecto (JP): María Martínez, Ayudante de desarrollo (AD): Tomás Zúñiga.

---

## Licencia

Este proyecto no incluye licencia definida actualmente.  
Se recomienda agregar una licencia como MIT, Apache 2.0 o GPL-3.0 según el objetivo del repositorio.
