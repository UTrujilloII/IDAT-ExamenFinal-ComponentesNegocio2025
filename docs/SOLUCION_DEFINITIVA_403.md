# 🚨 SOLUCIÓN DEFINITIVA - Error 403 sin Logs

## 🔴 PROBLEMA ACTUAL

No puedes listar clientes, obtienes 403, y **NO VES LOGS**. Esto significa:

1. ❌ La aplicación NO se reinició con los cambios
2. ❌ El logging no está activo
3. ❌ Los cambios de código no se aplicaron

---

## ✅ SOLUCIÓN DEFINITIVA - PASO A PASO

### **PASO 1: DETENER LA APLICACIÓN COMPLETAMENTE**

**En IntelliJ:**

1. Busca en la parte inferior la pestaña **"Run"**
2. Click en el botón **⬛ STOP** (cuadrado rojo)
3. **ESPERA** hasta que veas "Process finished"
4. Si no se detiene, click en el ícono de ☠️ "Stop All"

---

### **PASO 2: REBUILD PROJECT (OBLIGATORIO)**

**En IntelliJ:**

1. Click en menú **Build**
2. Selecciona **Rebuild Project**
3. **ESPERA** a que termine (verás progreso abajo)
4. Debe decir "Build completed successfully"

---

### **PASO 3: REINICIAR LA APLICACIÓN**

**En IntelliJ:**

1. Click derecho en `MsBancarioApplication.java`
2. **Run 'MsBancarioApplication'**
3. **IMPORTANTE:** Espera a que aparezca en la consola:

```
🚀 Iniciando MS-Bancario Application...
✅ Aplicación iniciada exitosamente en puerto: 9595
📍 URL Base: http://localhost:9595
```

**SI NO VES ESTOS EMOJIS:**
- Los cambios NO se aplicaron
- Vuelve al PASO 2

---

### **PASO 4: PROBAR ENDPOINT DE DEBUG (SIN TOKEN)**

Primero, verifica que el servidor está funcionando:

**En Postman:**

```
GET http://localhost:9595/v1/debug/test

NO NECESITAS TOKEN para este endpoint
```

**Respuesta esperada:**
```json
{
  "status": "OK",
  "message": "Servidor funcionando correctamente",
  "timestamp": 1639520000000
}
```

✅ Si funciona → El servidor está activo  
❌ Si falla → El servidor NO está corriendo

---

### **PASO 5: HACER LOGIN**

**En Postman:**

```
POST http://localhost:9595/v1/auth/login
Body (raw - JSON):
{
  "username": "admin",
  "password": "admin123"
}
```

**Respuesta esperada:**
```json
{
  "accessToken": "eyJhbGc...",
  "refreshToken": "...",
  "username": "admin",
  "role": "ROLE_ADMIN"
}
```

**COPIA EL `accessToken` COMPLETO**

**En la consola de IntelliJ DEBES VER:**
```
✅ Usuario admin cargado con authorities: [ROLE_ADMIN]
✅ Login exitoso para usuario: admin con rol: ROLE_ADMIN
```

**SI NO VES ESTO:**
- La aplicación usa código viejo
- Vuelve al PASO 2 (Rebuild)

**SI VES `[ROLE_ROLE_ADMIN]`:**
- Los cambios NO se aplicaron
- Vuelve al PASO 2 (Rebuild)

---

### **PASO 6: VERIFICAR TOKEN CON DEBUG**

Antes de intentar listar clientes, verifica que el token funciona:

**En Postman:**

```
GET http://localhost:9595/v1/debug/auth-info
Authorization: Bearer {PEGA_TU_TOKEN_AQUÍ}
```

**Respuesta esperada:**
```json
{
  "hasAuthHeader": true,
  "authHeaderPrefix": "Bearer",
  "tokenLength": 200,
  "isAuthenticated": true,
  "username": "admin",
  "authorities": "[ROLE_ADMIN]",
  "principal": "User"
}
```

**En la consola verás:**
```
🔍 Verificando información de autenticación
Authorization header recibido: SÍ
✅ Usuario autenticado: admin
✅ Authorities: [ROLE_ADMIN]
```

✅ **Si todo esto funciona** → El token está bien  
❌ **Si `isAuthenticated: false`** → El token es inválido

---

### **PASO 7: AHORA SÍ, LISTAR CLIENTES**

**En Postman:**

```
GET http://localhost:9595/v1/clientes
Authorization: Bearer {TU_TOKEN}
```

**En la consola DEBES VER:**
```
🔍 Procesando petición: GET /v1/clientes
Token extraído: eyJhbGc...
✅ Token válido para usuario: admin
Usuario cargado: admin con authorities: [ROLE_ADMIN]
✅ Autenticación exitosa para usuario: admin con roles: [ROLE_ADMIN]
```

**Respuesta esperada (200 OK):**
```json
[
  {
    "id": 1,
    "nombre": "Juan Pérez García",
    "email": "juan.perez@example.com"
  },
  ...
]
```

✅ **¡FUNCIONÓ!**

---

## 🔍 DIAGNÓSTICO: ¿POR QUÉ NO VEO LOGS?

### Problema 1: Logging no configurado

**Verifica en `application.properties`:**
```properties
logging.level.pe.edu.idat.msbiblioteca=DEBUG
logging.level.pe.edu.idat.msbiblioteca.security=DEBUG
```

**Si no existen estas líneas:**
- Agrégalas manualmente
- Rebuild Project
- Reinicia aplicación

---

### Problema 2: Aplicación no reiniciada

**Síntomas:**
- No ves emojis en los logs al iniciar
- No aparece "🚀 Iniciando MS-Bancario Application..."

**Solución:**
1. Stop completo
2. Rebuild Project
3. Reiniciar

---

### Problema 3: IntelliJ usando cache viejo

**Solución:**
```
1. File → Invalidate Caches
2. Marcar: Clear file system cache and Local History
3. Invalidate and Restart
4. Esperar a que reinicie
5. Rebuild Project
6. Run aplicación
```

---

## 📋 CHECKLIST FINAL

Antes de decir "no funciona", verifica:

- [ ] ✅ Detuve la aplicación COMPLETAMENTE
- [ ] ✅ Hice Rebuild Project
- [ ] ✅ Reinicié la aplicación
- [ ] ✅ Vi los emojis 🚀 ✅ 📍 al iniciar
- [ ] ✅ `/v1/debug/test` funciona (200 OK)
- [ ] ✅ Hice login y copié el token
- [ ] ✅ Vi en logs: `[ROLE_ADMIN]` (sin duplicar)
- [ ] ✅ `/v1/debug/auth-info` muestra `isAuthenticated: true`
- [ ] ✅ Configuré el token en Postman correctamente
- [ ] ✅ Vi logs con emojis al hacer peticiones

**Si TODOS están ✅ y sigue sin funcionar:**
- Hay un problema de configuración más profundo
- Envíame los logs completos de la consola

---

## 🎯 NUEVO FLUJO DE PRUEBA

```
1. DETENER aplicación
   ↓
2. REBUILD Project
   ↓
3. REINICIAR aplicación
   ↓
4. Ver emojis: 🚀 ✅ 📍
   ↓
5. GET /v1/debug/test (sin token)
   → ✅ 200 OK = Servidor funciona
   ↓
6. POST /v1/auth/login
   → Copiar accessToken
   → Ver logs: [ROLE_ADMIN]
   ↓
7. GET /v1/debug/auth-info (con token)
   → ✅ isAuthenticated: true
   → Ver logs con ✅
   ↓
8. GET /v1/clientes (con token)
   → ✅ 200 OK con lista de clientes
   → Ver logs: "Autenticación exitosa"
```

---

## 💡 ENDPOINTS DE DEBUG AGREGADOS

He creado 2 endpoints nuevos para diagnosticar:

### 1. `/v1/debug/test`
- **No requiere token**
- Verifica que el servidor esté corriendo
- Si falla → Aplicación no está activa

### 2. `/v1/debug/auth-info`
- **Requiere token**
- Muestra información del token y autenticación
- Si `isAuthenticated: false` → Token inválido
- Si `authorities: []` → Usuario sin roles

---

## ⚠️ ÚLTIMO RECURSO

Si después de TODO esto sigue sin funcionar:

```
1. Cierra IntelliJ completamente
2. Abre IntelliJ
3. File → Invalidate Caches → Invalidate and Restart
4. Espera a que IntelliJ reinicie
5. Build → Rebuild Project
6. Run aplicación
7. Verifica que veas emojis al iniciar
8. Sigue el flujo de prueba desde PASO 4
```

---

**¡SIGUE ESTOS PASOS EN ORDEN Y FUNCIONARÁ!** 🚀

**Versión:** 3.0  
**Fecha:** 2025-12-11  
**Estado:** ✅ Guía Definitiva con Debug

