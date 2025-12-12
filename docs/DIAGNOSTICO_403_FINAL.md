# 🔴 DIAGNÓSTICO Y SOLUCIÓN - Error 403 Persistente

## 🎯 PROBLEMA

Sigues obteniendo 403 Forbidden al intentar listar clientes, incluso con el token de admin.

---

## ✅ SOLUCIÓN INMEDIATA

### **PASO 1: REINICIAR LA APLICACIÓN**

**IMPORTANTE:** Debes reiniciar la aplicación para que los cambios surtan efecto.

**Desde IntelliJ:**
1. Detén la aplicación (Stop)
2. Click derecho en `MsBancarioApplication.java`
3. Run 'MsBancarioApplication'

**O desde terminal:**
```powershell
# En IntelliJ, abre el terminal y ejecuta:
.\mvnw.cmd spring-boot:run
```

**Verás en los logs al iniciar:**
```
🚀 Iniciando MS-Bancario Application...
✅ Aplicación iniciada exitosamente en puerto: 9595
📍 URL Base: http://localhost:9595
```

---

### **PASO 2: HACER UN LOGIN NUEVO**

El token que tienes puede estar expirado. Haz un login fresco:

**En Postman:**
```
Carpeta: 🔐 Autenticación
Endpoint: 3. Login - Admin

POST http://localhost:9595/v1/auth/login
Body:
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

**COPIA EL NUEVO `accessToken`**

---

### **PASO 3: VERIFICAR LOS LOGS**

Con la aplicación reiniciada y después del login, deberías ver en los logs:

```
✅ Usuario admin cargado con authorities: [ROLE_ADMIN]
✅ Login exitoso para usuario: admin con rol: ROLE_ADMIN
```

**Si ves `[ROLE_ROLE_ADMIN]`** → La aplicación NO se reinició

---

### **PASO 4: LISTAR CLIENTES CON EL NUEVO TOKEN**

En Postman:

```
Carpeta: 👤 Clientes - CRUD Completo
Endpoint: 2. Listar Todos los Clientes

GET http://localhost:9595/v1/clientes
Authorization: Bearer {NUEVO_TOKEN_AQUÍ}
```

**En los logs deberías ver:**
```
🔍 Procesando petición: GET /v1/clientes
✅ Token válido para usuario: admin
Usuario cargado: admin con authorities: [ROLE_ADMIN]
✅ Autenticación exitosa para usuario: admin con roles: [ROLE_ADMIN]
```

**Respuesta esperada:**
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
  }
]
```

✅ **¡Funcionará!**

---

## 🔍 DIAGNÓSTICO DE PROBLEMAS

### ❌ **Problema 1: Token Expirado**

**Síntoma:** 403 Forbidden
**Logs:** No aparece nada sobre autenticación
**Solución:** Haz login nuevamente

### ❌ **Problema 2: Aplicación No Reiniciada**

**Síntoma:** 403 Forbidden
**Logs:** `Usuario admin cargado con authorities: [ROLE_ROLE_ADMIN]`
**Solución:** Reinicia la aplicación

### ❌ **Problema 3: Token Mal Configurado**

**Síntoma:** 403 Forbidden
**Logs:** `⚠️ No se encontró header Authorization`
**Solución:** Verifica que en Postman:
- Authorization Type: `Bearer Token`
- Token: Pega el accessToken completo

### ❌ **Problema 4: Roles No Creados**

**Síntoma:** Error al hacer login
**Logs:** `El rol ingresado no existe`
**Solución:** Ejecuta el script SQL:

```sql
-- En MySQL, ejecuta:
USE bancariodb;

-- Verificar roles
SELECT * FROM rol;

-- Si no existen, crearlos:
INSERT IGNORE INTO rol (id, nombre) VALUES (1, 'ROLE_ADMIN');
INSERT IGNORE INTO rol (id, nombre) VALUES (2, 'ROLE_USER');

-- Verificar usuario admin
SELECT * FROM usuario WHERE username = 'admin';

-- Si existe, eliminar y recrear
DELETE FROM usuario_rol WHERE usuario_id = (SELECT id FROM usuario WHERE username = 'admin');
DELETE FROM usuario WHERE username = 'admin';

-- Crear usuario admin con contraseña encriptada
INSERT INTO usuario (id, username, password, enabled) VALUES 
(1, 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', true);

-- Asignar rol
INSERT INTO usuario_rol (usuario_id, rol_id) VALUES (1, 1);

-- Verificar
SELECT u.username, r.nombre 
FROM usuario u 
JOIN usuario_rol ur ON u.id = ur.usuario_id 
JOIN rol r ON ur.rol_id = r.id 
WHERE u.username = 'admin';
```

---

## 📝 CHECKLIST DE VERIFICACIÓN

Marca cada paso:

- [ ] ✅ Detuve la aplicación
- [ ] ✅ Reinicié la aplicación desde IntelliJ
- [ ] ✅ Vi los logs de inicio con emojis (🚀 ✅ 📍)
- [ ] ✅ Hice un login nuevo
- [ ] ✅ Copié el accessToken completo
- [ ] ✅ Configuré el token en Postman (Authorization: Bearer Token)
- [ ] ✅ Los logs muestran `[ROLE_ADMIN]` sin duplicar
- [ ] ✅ Intenté listar clientes
- [ ] ✅ Revisé los logs para ver qué pasó

---

## 🎯 FLUJO CORRECTO

```
1. REINICIAR aplicación
   ↓
2. Ver logs de inicio con emojis
   ↓
3. LOGIN como admin
   ↓
4. Ver logs: "authorities: [ROLE_ADMIN]"
   ↓
5. COPIAR accessToken nuevo
   ↓
6. CONFIGURAR en Postman (Authorization tab)
   ↓
7. GET /v1/clientes
   ↓
8. Ver logs: "✅ Autenticación exitosa"
   ↓
9. ✅ Recibir lista de clientes (200 OK)
```

---

## 💡 EXPLICACIÓN: ¿Por qué @Slf4j?

`@Slf4j` crea automáticamente un logger llamado `log` que usamos para mostrar mensajes:

**Sin @Slf4j:**
```java
private static final Logger log = LoggerFactory.getLogger(MiClase.class);
log.info("Mensaje");
```

**Con @Slf4j:**
```java
@Slf4j
public class MiClase {
    // El 'log' ya está disponible
    log.info("Mensaje");
}
```

**Lo usamos para:**
- ✅ Mostrar qué usuario se autenticó
- ✅ Mostrar qué roles tiene
- ✅ Identificar errores fácilmente
- ✅ Debugging más rápido

**Ejemplo en los logs:**
```
✅ Usuario admin cargado con authorities: [ROLE_ADMIN]
🔍 Procesando petición: GET /v1/clientes
✅ Autenticación exitosa para usuario: admin
```

---

## 🔧 SI NADA FUNCIONA

1. **Limpia el proyecto:**
   ```
   Maven → Lifecycle → clean
   Maven → Lifecycle → compile
   ```

2. **Verifica application.properties:**
   ```properties
   server.port=9595
   logging.level.pe.edu.idat.msbiblioteca.security=DEBUG
   ```

3. **Verifica que MySQL esté corriendo**

4. **Reinicia IntelliJ IDEA**

---

## 📞 ÚLTIMO RECURSO

Si después de todo esto sigue sin funcionar:

1. Detén la aplicación
2. En IntelliJ: `Build → Rebuild Project`
3. Reinicia la aplicación
4. Verifica que los logs muestren los emojis al iniciar
5. Si no ves emojis → Los cambios no se aplicaron

---

**¡REINICIA LA APLICACIÓN Y PRUEBA AHORA!** 🚀

Los logs te dirán exactamente qué está pasando.

**Versión:** 2.3  
**Fecha:** 2025-12-11  
**Estado:** ✅ Guía de Diagnóstico Completa

