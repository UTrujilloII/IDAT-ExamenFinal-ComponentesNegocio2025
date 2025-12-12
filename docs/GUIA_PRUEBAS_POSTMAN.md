# 🧪 GUÍA DE PRUEBAS - MS-BIBLIOTECA CON POSTMAN

## 📋 PREPARACIÓN

### 1. Inicializar Base de Datos

```sql
-- Ejecutar en MySQL
SOURCE D:\java_aplicaciones\ms-biblioteca\init_database_biblioteca.sql
```

### 2. Iniciar Aplicación

```bash
# Desde IntelliJ: Run MsBibliotecaApplication.java
# O desde terminal:
mvn spring-boot:run
```

Verás en consola:
```
🚀 Iniciando MS-Biblioteca Application...
✅ Aplicación iniciada exitosamente en puerto: 9595
📍 URL Base: http://localhost:9595
```

### 3. Importar Colección en Postman

1. Abrir Postman
2. Click en **Import**
3. Seleccionar: `MS-Biblioteca-Collection.json`
4. ✅ La colección se importará con URL base: `http://localhost:9595/v1`

---

## 🔐 PASO 1: AUTENTICACIÓN

### Login como Administrador

```http
POST http://localhost:9595/v1/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**Respuesta esperada (200 OK):**
```json
{
  "success": true,
  "message": "Login exitoso",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "type": "Bearer",
    "username": "admin",
    "roles": ["ROLE_ADMIN"]
  }
}
```

✅ **El token se guarda automáticamente en la variable `{{token}}`**

### Login como Usuario

```http
POST http://localhost:9595/v1/auth/login
Content-Type: application/json

{
  "username": "usuario1",
  "password": "usuario123"
}
```

---

## 📚 PASO 2: GESTIÓN DE LIBROS

### 2.1 Listar Todos los Libros

```http
GET http://localhost:9595/v1/libros
Authorization: Bearer {{token}}
```

**Respuesta esperada (200 OK):**
```json
{
  "success": true,
  "message": "Libros obtenidos exitosamente",
  "data": [
    {
      "id": 1,
      "isbn": "978-0134685991",
      "titulo": "Effective Java",
      "autor": "Joshua Bloch",
      "editorial": "Addison-Wesley",
      "anioPublicacion": 2018,
      "categoria": "Programación",
      "copiasDisponibles": 3,
      "copiasTotales": 3,
      "estado": "DISPONIBLE"
    }
    // ... más libros
  ]
}
```

### 2.2 Buscar Libro por ID

```http
GET http://localhost:9595/v1/libros/1
Authorization: Bearer {{token}}
```

### 2.3 Buscar Libro por ISBN

```http
GET http://localhost:9595/v1/libros/isbn/978-0134685991
Authorization: Bearer {{token}}
```

### 2.4 Buscar Libros Disponibles

```http
GET http://localhost:9595/v1/libros/disponibles
Authorization: Bearer {{token}}
```

### 2.5 Buscar por Palabra Clave

```http
GET http://localhost:9595/v1/libros/buscar?keyword=java
Authorization: Bearer {{token}}
```

### 2.6 Buscar por Título

```http
GET http://localhost:9595/v1/libros/buscar/titulo?titulo=Clean
Authorization: Bearer {{token}}
```

### 2.7 Buscar por Autor

```http
GET http://localhost:9595/v1/libros/buscar/autor?autor=Robert
Authorization: Bearer {{token}}
```

### 2.8 Buscar por Categoría

```http
GET http://localhost:9595/v1/libros/buscar/categoria?categoria=Programación
Authorization: Bearer {{token}}
```

### 2.9 Listar Categorías

```http
GET http://localhost:9595/v1/libros/categorias
Authorization: Bearer {{token}}
```

### 2.10 Crear Libro (Solo ADMIN)

```http
POST http://localhost:9595/v1/libros
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "isbn": "978-1234567890",
  "titulo": "Nuevo Libro de Prueba",
  "autor": "Autor de Prueba",
  "editorial": "Editorial Test",
  "anioPublicacion": 2024,
  "categoria": "Programación",
  "copiasDisponibles": 5,
  "copiasTotales": 5,
  "descripcion": "Libro de prueba para el sistema",
  "ubicacion": "Estante Z1"
}
```

**Respuesta esperada (201 Created):**
```json
{
  "success": true,
  "message": "Libro creado exitosamente",
  "data": {
    "id": 16,
    "isbn": "978-1234567890",
    "titulo": "Nuevo Libro de Prueba",
    "estado": "DISPONIBLE"
    // ... más datos
  }
}
```

### 2.11 Actualizar Libro (Solo ADMIN)

```http
PUT http://localhost:9595/v1/libros/16
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "isbn": "978-1234567890",
  "titulo": "Libro Actualizado",
  "autor": "Autor Actualizado",
  "editorial": "Editorial Test",
  "anioPublicacion": 2024,
  "categoria": "Programación",
  "copiasDisponibles": 3,
  "copiasTotales": 5,
  "descripcion": "Descripción actualizada",
  "ubicacion": "Estante Z2"
}
```

### 2.12 Obtener Estadísticas (Solo ADMIN)

```http
GET http://localhost:9595/v1/libros/estadisticas
Authorization: Bearer {{token}}
```

**Respuesta esperada:**
```json
{
  "success": true,
  "message": "Estadísticas obtenidas: [Total: 15, Disponibles: 15]",
  "data": [15, 15]
}
```

### 2.13 Eliminar Libro (Solo ADMIN)

```http
DELETE http://localhost:9595/v1/libros/16
Authorization: Bearer {{token}}
```

---

## 📖 PASO 3: GESTIÓN DE PRÉSTAMOS

### 3.1 Crear Préstamo (Solo ADMIN)

```http
POST http://localhost:9595/v1/prestamos
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "usuarioId": 2,
  "libroId": 1,
  "diasPrestamo": 15,
  "observaciones": "Préstamo de prueba"
}
```

**Respuesta esperada (201 Created):**
```json
{
  "success": true,
  "message": "Préstamo creado exitosamente",
  "data": {
    "id": 1,
    "usuarioId": 2,
    "nombreUsuario": "Juan Pérez García",
    "libroId": 1,
    "tituloLibro": "Effective Java",
    "fechaPrestamo": "2025-12-11",
    "fechaDevolucionEsperada": "2025-12-26",
    "estado": "ACTIVO",
    "multa": 0.0
  }
}
```

### 3.2 Listar Todos los Préstamos (Solo ADMIN)

```http
GET http://localhost:9595/v1/prestamos
Authorization: Bearer {{token}}
```

### 3.3 Obtener Préstamo por ID

```http
GET http://localhost:9595/v1/prestamos/1
Authorization: Bearer {{token}}
```

### 3.4 Préstamos de un Usuario

```http
GET http://localhost:9595/v1/prestamos/usuario/2
Authorization: Bearer {{token}}
```

### 3.5 Préstamos Activos de un Usuario

```http
GET http://localhost:9595/v1/prestamos/usuario/2/activos
Authorization: Bearer {{token}}
```

### 3.6 Préstamos de un Libro (Solo ADMIN)

```http
GET http://localhost:9595/v1/prestamos/libro/1
Authorization: Bearer {{token}}
```

### 3.7 Registrar Devolución (Solo ADMIN)

```http
PUT http://localhost:9595/v1/prestamos/1/devolver
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "observaciones": "Libro devuelto en buen estado"
}
```

**Respuesta esperada (200 OK):**
```json
{
  "success": true,
  "message": "Devolución registrada exitosamente. Multa: $0.0",
  "data": {
    "id": 1,
    "estado": "DEVUELTO",
    "fechaDevolucionReal": "2025-12-11",
    "multa": 0.0
  }
}
```

### 3.8 Préstamos Vencidos (Solo ADMIN)

```http
GET http://localhost:9595/v1/prestamos/vencidos
Authorization: Bearer {{token}}
```

### 3.9 Préstamos con Multa (Solo ADMIN)

```http
GET http://localhost:9595/v1/prestamos/con-multa
Authorization: Bearer {{token}}
```

### 3.10 Calcular Multas de Usuario (Solo ADMIN)

```http
GET http://localhost:9595/v1/prestamos/usuario/2/multas
Authorization: Bearer {{token}}
```

### 3.11 Obtener Estadísticas (Solo ADMIN)

```http
GET http://localhost:9595/v1/prestamos/estadisticas
Authorization: Bearer {{token}}
```

**Respuesta esperada:**
```json
{
  "success": true,
  "message": "Estadísticas: [Total: 1, Activos: 0, Devueltos: 1, Vencidos: 0]",
  "data": [1, 0, 1, 0]
}
```

### 3.12 Últimos Préstamos (Solo ADMIN)

```http
GET http://localhost:9595/v1/prestamos/ultimos?limit=5
Authorization: Bearer {{token}}
```

### 3.13 Actualizar Estados (Solo ADMIN)

```http
POST http://localhost:9595/v1/prestamos/actualizar-estados
Authorization: Bearer {{token}}
```

---

## ✅ CASOS DE PRUEBA

### Test 1: Flujo Completo de Administrador

1. ✅ Login como admin
2. ✅ Listar todos los libros
3. ✅ Crear un nuevo libro
4. ✅ Crear un préstamo
5. ✅ Ver estadísticas
6. ✅ Registrar devolución
7. ✅ Eliminar libro creado

### Test 2: Flujo de Usuario Normal

1. ✅ Login como usuario1
2. ✅ Listar libros disponibles
3. ✅ Buscar libro por categoría
4. ✅ Ver mis préstamos activos
5. ❌ Intentar crear libro (debería fallar - 403 Forbidden)

### Test 3: Búsquedas

1. ✅ Buscar por palabra clave "Java"
2. ✅ Buscar por título "Clean"
3. ✅ Buscar por autor "Martin"
4. ✅ Buscar por categoría "Programación"
5. ✅ Listar todas las categorías

### Test 4: Validaciones

1. ❌ Login con credenciales incorrectas (debería fallar - 401)
2. ❌ Acceder sin token (debería fallar - 401)
3. ❌ Usuario intentando crear libro (debería fallar - 403)
4. ❌ Crear libro con ISBN duplicado (debería fallar - 400)

---

## 🔍 VERIFICACIÓN DE RESPUESTAS

### Respuesta Exitosa
```json
{
  "success": true,
  "message": "Mensaje descriptivo",
  "data": { /* datos */ }
}
```

### Error de Autenticación (401)
```json
{
  "success": false,
  "message": "Token inválido o expirado",
  "timestamp": "2025-12-11T10:30:00",
  "path": "/v1/libros"
}
```

### Error de Autorización (403)
```json
{
  "success": false,
  "message": "No tiene permisos para realizar esta operación",
  "timestamp": "2025-12-11T10:30:00",
  "path": "/v1/libros"
}
```

### Error de Validación (400)
```json
{
  "success": false,
  "message": "El ISBN ya está registrado",
  "timestamp": "2025-12-11T10:30:00"
}
```

---

## 📊 RESULTADOS ESPERADOS

| Endpoint | Método | Rol Requerido | Código Esperado |
|----------|--------|---------------|-----------------|
| `/v1/auth/login` | POST | Público | 200 |
| `/v1/libros` | GET | USUARIO/ADMIN | 200 |
| `/v1/libros` | POST | ADMIN | 201 |
| `/v1/libros/{id}` | PUT | ADMIN | 200 |
| `/v1/libros/{id}` | DELETE | ADMIN | 200 |
| `/v1/prestamos` | GET | ADMIN | 200 |
| `/v1/prestamos` | POST | ADMIN | 201 |
| `/v1/prestamos/{id}/devolver` | PUT | ADMIN | 200 |

---

## 🎯 TIPS PARA POSTMAN

### Variables de Colección
La colección ya incluye:
- `{{base_url}}` = `http://localhost:9595/v1`
- `{{token}}` = Se guarda automáticamente después del login

### Scripts Pre-request
Los scripts ya están configurados para:
- Guardar el token automáticamente
- Incluir el token en todas las peticiones
- Manejar refresh token

### Tests Automáticos
Cada request tiene tests que verifican:
- Código de respuesta correcto
- Estructura de la respuesta
- Presencia de campos requeridos

---

## ✅ CHECKLIST DE PRUEBAS

### Autenticación
- [ ] Login con credenciales correctas
- [ ] Login con credenciales incorrectas
- [ ] Acceso sin token
- [ ] Token expirado

### Libros
- [ ] Listar todos los libros
- [ ] Buscar por ID
- [ ] Buscar por ISBN
- [ ] Búsqueda por palabra clave
- [ ] Crear libro (ADMIN)
- [ ] Actualizar libro (ADMIN)
- [ ] Eliminar libro (ADMIN)
- [ ] Ver estadísticas (ADMIN)

### Préstamos
- [ ] Listar préstamos (ADMIN)
- [ ] Crear préstamo (ADMIN)
- [ ] Registrar devolución (ADMIN)
- [ ] Ver préstamos vencidos (ADMIN)
- [ ] Ver préstamos con multa (ADMIN)
- [ ] Calcular multas (ADMIN)

### Permisos
- [ ] Usuario puede ver libros
- [ ] Usuario NO puede crear libros
- [ ] Usuario NO puede ver todos los préstamos
- [ ] Admin puede hacer todo

---

## 🎉 RESULTADO ESPERADO

Después de ejecutar todas las pruebas, deberías tener:

✅ **29 endpoints probados exitosamente**  
✅ **Sistema de autenticación funcionando**  
✅ **Roles y permisos verificados**  
✅ **CRUD completo de libros**  
✅ **Sistema de préstamos operativo**  
✅ **Cálculo de multas correcto**  

**Sistema listo para producción** 🚀

---

**Fecha:** 2025-12-11  
**Versión:** 1.0  
**Estado:** ✅ LISTA PARA PRUEBAS

