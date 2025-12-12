# 🔒 Solución: Mensajes de Error en Autenticación y Autorización

## 📋 Problema
Cuando se intenta acceder a un endpoint protegido sin token de autenticación, la API retornaba `403 Forbidden` o `401 Unauthorized` **sin ningún mensaje descriptivo** en el cuerpo de la respuesta.

## ✅ Solución Implementada

Se han creado manejadores personalizados para interceptar errores de seguridad y retornar respuestas JSON con mensajes claros.

---

## 🆕 Archivos Creados

### 1. CustomAuthenticationEntryPoint.java
**Ubicación:** `security/CustomAuthenticationEntryPoint.java`

**Propósito:** Maneja errores de **autenticación (401 Unauthorized)**

**Cuándo se activa:**
- Usuario intenta acceder sin token
- Token es inválido o malformado
- Token ha expirado

**Respuesta:**
```json
{
  "timestamp": "2025-12-11T03:00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "No se ha proporcionado un token de autenticación válido. Por favor, inicie sesión.",
  "path": "/v1/clientes"
}
```

---

### 2. CustomAccessDeniedHandler.java
**Ubicación:** `security/CustomAccessDeniedHandler.java`

**Propósito:** Maneja errores de **autorización (403 Forbidden)**

**Cuándo se activa:**
- Usuario autenticado pero sin permisos suficientes
- Intenta acceder a recurso que requiere rol ADMIN siendo USER

**Respuesta:**
```json
{
  "timestamp": "2025-12-11T03:00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "No tiene permisos suficientes para acceder a este recurso. Se requiere rol de ADMIN.",
  "path": "/v1/clientes"
}
```

---

## 🔧 Archivos Modificados

### SecurityConfig.java

**Cambios realizados:**

1. **Imports agregados:**

```java


```

2. **Inyección de dependencias:**
```java
private final CustomAuthenticationEntryPoint authenticationEntryPoint;
private final CustomAccessDeniedHandler accessDeniedHandler;
```

3. **Configuración en SecurityFilterChain:**
```java
.exceptionHandling(exception -> exception
        .authenticationEntryPoint(authenticationEntryPoint)
        .accessDeniedHandler(accessDeniedHandler)
)
```

---

## 📊 Comparación Antes vs Después

### ❌ ANTES - Sin Token

**Request:**
```http
GET /v1/clientes
(sin header Authorization)
```

**Response:**
```
Status: 403 Forbidden
Body: (vacío)
```

### ✅ DESPUÉS - Sin Token

**Request:**
```http
GET /v1/clientes
(sin header Authorization)
```

**Response:**
```json
Status: 401 Unauthorized
{
  "timestamp": "2025-12-11T03:00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "No se ha proporcionado un token de autenticación válido. Por favor, inicie sesión.",
  "path": "/v1/clientes"
}
```

---

### ❌ ANTES - Usuario sin Permisos

**Request:**
```http
DELETE /v1/clientes/1
Authorization: Bearer {token_de_usuario_user}
```

**Response:**
```
Status: 403 Forbidden
Body: (vacío o mensaje HTML)
```

### ✅ DESPUÉS - Usuario sin Permisos

**Request:**
```http
DELETE /v1/clientes/1
Authorization: Bearer {token_de_usuario_user}
```

**Response:**
```json
Status: 403 Forbidden
{
  "timestamp": "2025-12-11T03:00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "No tiene permisos suficientes para acceder a este recurso. Se requiere rol de ADMIN.",
  "path": "/v1/clientes/1"
}
```

---

## 🎯 Características Implementadas

✅ **Respuestas JSON consistentes** para todos los errores de seguridad  
✅ **Mensajes descriptivos** en español  
✅ **Timestamp** de cuándo ocurrió el error  
✅ **Path** del endpoint que causó el error  
✅ **Código de estado HTTP** correcto (401 vs 403)  
✅ **Logging** detallado con emojis para fácil identificación

---

## 📝 Tipos de Errores Manejados

| Código | Error | Cuándo Ocurre | Manejador |
|--------|-------|---------------|-----------|
| **401** | Unauthorized | Sin token o token inválido | CustomAuthenticationEntryPoint |
| **403** | Forbidden | Token válido pero sin permisos | CustomAccessDeniedHandler |

---

## 🔍 Logging Implementado

Ambos manejadores incluyen logs con emoji para fácil identificación:

```
🚫 Error de autenticación: Full authentication is required... en endpoint: /v1/clientes
🚫 Acceso denegado: Access Denied en endpoint: /v1/clientes/1
```

---

## 🚀 Cómo Probar

### 1. Probar Sin Token (401)

**Postman:**
```
GET http://localhost:9595/v1/clientes
(sin header Authorization)
```

**Resultado Esperado:**
- Status: `401 Unauthorized`
- Mensaje claro en JSON

### 2. Probar Usuario USER intentando DELETE (403)

**Paso 1:** Login con usuario USER
```http
POST http://localhost:9595/v1/auth/login
Content-Type: application/json

{
  "username": "user",
  "password": "user123"
}
```

**Paso 2:** Intentar eliminar cliente (requiere ADMIN)
```http
DELETE http://localhost:9595/v1/clientes/1
Authorization: Bearer {token_del_user}
```

**Resultado Esperado:**
- Status: `403 Forbidden`
- Mensaje indicando falta de permisos

---

## 🎨 Formato de ErrorResponse

Todos los errores de seguridad usan la clase `ErrorResponse`:

```java
@Builder
public class ErrorResponse {
    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
    private List<ValidationError> validationErrors;
}
```

---

## ✨ Beneficios

1. **Mejor UX:** Usuarios saben exactamente qué salió mal
2. **Debugging más fácil:** Mensajes claros para desarrolladores
3. **Consistencia:** Mismo formato de error en toda la API
4. **Seguridad:** No expone detalles internos sensibles
5. **Internacionalización:** Mensajes en español y personalizables

---

## 🔄 Próximos Pasos

1. **Reiniciar la aplicación** para aplicar cambios
2. **Probar en Postman** los diferentes casos
3. **Verificar logs** para confirmar que se registran correctamente

---

## 📚 Documentación Relacionada

- `GUIA_MENSAJES_RESPUESTA.md` - Mensajes para operaciones exitosas
- `GlobalExceptionHandler.java` - Otros manejadores de excepciones
- `ErrorResponse.java` - Estructura de respuestas de error

---

## ⚙️ Configuración Técnica

**Spring Security versión:** 6.x  
**Patrón usado:** AuthenticationEntryPoint + AccessDeniedHandler  
**Serialización:** Jackson con JavaTimeModule para LocalDateTime  
**Encoding:** UTF-8

---

## 🎉 Resultado Final

Ahora cuando accedas a un endpoint sin autenticación recibirás:

```json
{
  "timestamp": "2025-12-11T03:15:30.123456",
  "status": 401,
  "error": "Unauthorized",
  "message": "No se ha proporcionado un token de autenticación válido. Por favor, inicie sesión.",
  "path": "/v1/clientes"
}
```

¡En lugar de una respuesta vacía! 🚀

