# ✅ SOLUCIÓN RÁPIDA - 3 PASOS PARA ARREGLAR EL 403

## 📚 ¿Qué es @Slf4j?

**`@Slf4j`** = Logging automático con Lombok

```java
@Slf4j  // ← Esto crea automáticamente un 'log' en tu clase
public class MiClase {
    public void metodo() {
        log.info("✅ Mensaje de información");  // ← Ya puedes usar 'log'
        log.error("❌ Mensaje de error");
        log.debug("🔍 Mensaje de debug");
    }
}
```

**Lo uso para que veas en la consola qué está pasando con la autenticación:**
- ✅ Qué usuario se autenticó
- ✅ Qué roles tiene (ROLE_ADMIN, ROLE_USER)
- ❌ Si hay errores de token
- 🔍 Qué peticiones se están procesando

---

## 🔴 TU PROBLEMA: 403 Forbidden

**Causas posibles:**
1. ❌ Token expirado
2. ❌ Aplicación no reiniciada (roles duplicados)
3. ❌ Token mal configurado en Postman

---

## 🚀 SOLUCIÓN EN 3 PASOS

### **PASO 1: REINICIAR LA APLICACIÓN** ⚠️

**En IntelliJ IDEA:**

1. Busca la pestaña de ejecución (abajo)
2. Click en el botón rojo ⬛ (Stop)
3. Click derecho en `MsBancarioApplication.java`
4. Click en ▶️ **Run 'MsBancarioApplication'**

**Verás en la consola:**
```
🚀 Iniciando MS-Bancario Application...
✅ Aplicación iniciada exitosamente en puerto: 9595
📍 URL Base: http://localhost:9595
🔐 Endpoints de autenticación:
   - POST /v1/auth/login
   - POST /v1/auth/register
👤 Endpoints de clientes:
   - GET  /v1/clientes
   - POST /v1/clientes
💡 Usuario por defecto: admin / admin123
```

**Si NO ves estos emojis** → Los cambios no se aplicaron

---

### **PASO 2: HACER LOGIN NUEVO** 🔐

**En Postman:**

```
Carpeta: 🔐 Autenticación
Endpoint: 3. Login - Admin

1. URL: POST http://localhost:9595/v1/auth/login

2. Body (raw - JSON):
{
  "username": "admin",
  "password": "admin123"
}

3. Click en "Send"
```

**Respuesta correcta:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTYzOTUyMDAwMCwiZXhwIjoxNjM5NTIzNjAwfQ.1234567890...",
  "refreshToken": "eyJ...",
  "username": "admin",
  "role": "ROLE_ADMIN"
}
```

**4. COPIA TODO el `accessToken`** (el texto largo que empieza con "eyJ...")

**En la consola de la aplicación deberías ver:**
```
✅ Usuario admin cargado con authorities: [ROLE_ADMIN]
✅ Login exitoso para usuario: admin con rol: ROLE_ADMIN
```

**Si ves `[ROLE_ROLE_ADMIN]`** → Aplicación NO reiniciada, vuelve al PASO 1

---

### **PASO 3: LISTAR CLIENTES CON EL NUEVO TOKEN** 📋

**En Postman:**

```
Carpeta: 👤 Clientes - CRUD Completo
Endpoint: 2. Listar Todos los Clientes

1. URL: GET http://localhost:9595/v1/clientes

2. Pestaña "Authorization":
   - Type: Bearer Token
   - Token: PEGA_EL_ACCESS_TOKEN_AQUÍ (el que copiaste)

3. Click en "Send"
```

**En la consola de la aplicación verás:**
```
🔍 Procesando petición: GET /v1/clientes
Token extraído: eyJhbGciOiJIUzI1...
✅ Token válido para usuario: admin
Usuario cargado: admin con authorities: [ROLE_ADMIN]
✅ Autenticación exitosa para usuario: admin con roles: [ROLE_ADMIN]
```

**Respuesta en Postman (200 OK):**
```json
[
  {
    "id": 1,
    "nombre": "Juan Pérez García",
    "email": "juan.perez@example.com"
  },
  {
    "id": 2,
    "nombre": "María López Rodríguez",
    "email": "maria.lopez@example.com"
  },
  {
    "id": 3,
    "nombre": "Carlos Sánchez Torres",
    "email": "carlos.sanchez@example.com"
  }
]
```

✅ **¡FUNCIONÓ!**

---

## ⚠️ SI SIGUE SIN FUNCIONAR

### Verificación 1: ¿Reiniciaste?

En la consola de IntelliJ, busca:
```
🚀 Iniciando MS-Bancario Application...
```

**Si NO ves el emoji** → No reiniciaste o los cambios no se compilaron

**Solución:**
1. Build → Rebuild Project
2. Reinicia la aplicación

---

### Verificación 2: ¿Token correcto?

El token debe verse así:
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTYzOTUyMDAwMCwiZXhwIjoxNjM5NTIzNjAwfQ.1234567890...
```

**Características:**
- ✅ Empieza con `eyJ`
- ✅ Tiene 2 puntos (`.`)
- ✅ Es un texto muy largo
- ❌ NO tiene espacios
- ❌ NO tiene saltos de línea

---

### Verificación 3: ¿Roles correctos?

En los logs, después del login, busca:
```
authorities: [ROLE_ADMIN]
```

**Si ves:**
- ✅ `[ROLE_ADMIN]` → Correcto
- ❌ `[ROLE_ROLE_ADMIN]` → Aplicación no reiniciada
- ❌ `[]` → Usuario sin roles

---

### Verificación 4: ¿MySQL corriendo?

```sql
-- Verifica que existan roles:
SELECT * FROM rol;

-- Debe mostrar:
-- 1 | ROLE_ADMIN
-- 2 | ROLE_USER
```

---

## 📊 COMPARACIÓN: ANTES vs AHORA

### ❌ ANTES (Sin @Slf4j):
```
Logs vacíos
No sabes qué está pasando
403 Forbidden sin explicación
```

### ✅ AHORA (Con @Slf4j):
```
🔍 Procesando petición: GET /v1/clientes
✅ Token válido para usuario: admin
✅ Autenticación exitosa para usuario: admin con roles: [ROLE_ADMIN]
```

**Ves exactamente qué está pasando** 👀

---

## 🎯 RESUMEN DE 3 PASOS

```
1️⃣ REINICIAR aplicación
   → Ver emojis en logs: 🚀 ✅ 📍

2️⃣ LOGIN nuevo
   → Copiar accessToken
   → Ver en logs: [ROLE_ADMIN] sin duplicar

3️⃣ LISTAR clientes
   → Pegar token en Postman (Authorization: Bearer Token)
   → Ver en logs: ✅ Autenticación exitosa
   → Obtener lista de clientes (200 OK)
```

---

## 💡 CONSEJO PRO

**Usa la colección de Postman que generé:**

1. Importa `MS-Bancario-Complete-Collection.json`
2. Importa `MS-Bancario-Environment.json`
3. Selecciona el environment
4. Ejecuta "3. Login - Admin"
5. ✅ El token se guarda **automáticamente**
6. Ejecuta "2. Listar Todos los Clientes"
7. ✅ El token se usa **automáticamente**

**No necesitas copiar y pegar nada** 🎉

---

**¡AHORA SÍ FUNCIONARÁ!** 🚀

Sigue los 3 pasos en orden y verás en los logs exactamente qué está pasando.

**Fecha:** 2025-12-11  
**Estado:** ✅ Guía Visual Completa

