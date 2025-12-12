# 🔴 SOLUCIÓN: No Puedo Crear Clientes - Error 403

## ❌ PROBLEMA

Al intentar crear un cliente obtienes **403 Forbidden** porque:
1. El endpoint `/v1/clientes` está protegido
2. Requiere autenticación con token JWT
3. **No estás enviando el token en el header Authorization**

---

## ✅ SOLUCIÓN PASO A PASO

### **Paso 1: Hacer Login Primero**

Antes de crear clientes, **DEBES hacer login** para obtener el token:

**En Postman:**

```
Carpeta: 🔐 Autenticación
Endpoint: 3. Login - Admin

URL: POST http://localhost:9595/v1/auth/login
Headers: Content-Type: application/json
Body (raw JSON):
{
  "username": "admin",
  "password": "admin123"
}

✅ Click en "Send"
```

**Respuesta esperada:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "admin",
  "role": "ROLE_ADMIN"
}
```

**🔑 COPIA EL `accessToken`** (todo el texto largo que empieza con "eyJ...")

---

### **Paso 2: Usar el Token para Crear Cliente**

Ahora usa el token en el header `Authorization`:

**En Postman:**

```
Carpeta: 👤 Clientes - CRUD Completo
Endpoint: 1. Crear Cliente

URL: POST http://localhost:9595/v1/clientes

Headers:
  Content-Type: application/json
  Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
                 ^^^^^^ COPIA TODO EL TOKEN AQUÍ

Body (raw JSON):
{
  "nombre": "Juan Pérez García",
  "email": "juan.perez@example.com"
}

✅ Click en "Send"
```

**Respuesta esperada:**
```json
{
  "id": 1,
  "nombre": "Juan Pérez García",
  "email": "juan.perez@example.com"
}
```

---

## 📝 CONFIGURACIÓN EN POSTMAN

### Opción 1: Usar Variables de Entorno (RECOMENDADO)

Si importaste la colección de Postman que generé:

1. **Ejecuta el endpoint "3. Login - Admin"**
   - El token se guarda automáticamente en la variable `{{accessToken}}`

2. **El token ya está configurado automáticamente** en todos los endpoints
   - Authorization: Bearer `{{accessToken}}`
   - ✅ No necesitas copiar y pegar nada

3. **Crea el cliente** con el endpoint "1. Crear Cliente"
   - ✅ El token se usa automáticamente

---

### Opción 2: Configurar Manualmente

Si no usas las variables, configura así:

1. **Haz Login** y copia el `accessToken`

2. **En el endpoint "Crear Cliente":**
   - Click en la pestaña **"Authorization"**
   - Type: **Bearer Token**
   - Token: **Pega el accessToken aquí**

3. **O usa Headers:**
   - Key: `Authorization`
   - Value: `Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`
     (con "Bearer " al inicio)

---

## 🔍 VERIFICAR QUE EL TOKEN ESTÉ CORRECTO

En Postman Console (`Alt+Ctrl+C`), deberías ver:

```
✅ Token JWT disponible
✅ Login exitoso como ADMIN
Token guardado: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

Si ves:
```
⚠️ No hay token JWT. Ejecuta Login primero.
```

Entonces **NO has hecho login** o el token no se guardó.

---

## ⚠️ ERRORES COMUNES

### ❌ Error: 403 Forbidden

**Causa:** No hay token o el token es inválido

**Solución:**
1. Verifica que el header `Authorization` esté presente
2. Verifica que tenga el formato: `Bearer {token}`
3. Haz login nuevamente para obtener un token fresco

---

### ❌ Error: 401 Unauthorized

**Causa:** Token expirado (dura 1 hora)

**Solución:**
1. Haz login nuevamente
2. O usa el endpoint "Refresh Token"

---

### ❌ Error: UnsupportedJwtException

**Causa:** Token malformado o incorrecto

**Solución:**
1. Verifica que copiaste TODO el token
2. No debe tener espacios extra
3. Debe empezar con "eyJ..."

---

## 🎯 FLUJO CORRECTO

```
1. POST /v1/auth/login
   Body: {"username": "admin", "password": "admin123"}
   ↓
   Respuesta: {"accessToken": "eyJ...", ...}
   ↓
2. COPIAR el accessToken
   ↓
3. POST /v1/clientes
   Header: Authorization: Bearer eyJ...
   Body: {"nombre": "Juan", "email": "juan@example.com"}
   ↓
   ✅ Cliente creado exitosamente
```

---

## 📋 CHECKLIST

Antes de crear un cliente, verifica:

- [ ] ✅ La aplicación está corriendo en puerto 9595
- [ ] ✅ MySQL está corriendo
- [ ] ✅ La base de datos tiene roles creados (ROLE_ADMIN, ROLE_USER)
- [ ] ✅ Ejecutaste el script `FIX_LOGIN_RAPIDO.sql` o `DataInitializer` funcionó
- [ ] ✅ Hiciste login exitosamente
- [ ] ✅ Copiaste el accessToken completo
- [ ] ✅ El header Authorization está configurado: `Bearer {token}`
- [ ] ✅ El body JSON está bien formado

---

## 🧪 PRUEBA RÁPIDA EN POSTMAN

### 1. Login
```
POST http://localhost:9595/v1/auth/login
{
  "username": "admin",
  "password": "admin123"
}
```

### 2. Copiar Token
```
Respuesta > accessToken > Copiar TODO el texto
```

### 3. Crear Cliente
```
POST http://localhost:9595/v1/clientes
Authorization: Bearer {PEGAR_TOKEN_AQUÍ}
{
  "nombre": "Juan Pérez",
  "email": "juan@example.com"
}
```

---

## 🔧 ALTERNATIVA: cURL

Si prefieres usar terminal:

```bash
# 1. Login y guardar token
TOKEN=$(curl -s -X POST http://localhost:9595/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' | jq -r '.accessToken')

# 2. Crear cliente con el token
curl -X POST http://localhost:9595/v1/clientes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "nombre": "Juan Pérez García",
    "email": "juan.perez@example.com"
  }'
```

---

## 💡 CONSEJO PRO

**Usa la colección de Postman que generé:**
- Importa: `MS-Bancario-Complete-Collection.json`
- Importa: `MS-Bancario-Environment.json`
- Selecciona el environment en la esquina superior derecha
- Ejecuta "3. Login - Admin"
- ✅ El token se guarda automáticamente
- ✅ Todos los endpoints lo usan automáticamente

---

**¡AHORA SÍ PODRÁS CREAR CLIENTES!** 🎉

Recuerda:
1. **SIEMPRE** hacer login primero
2. Usar el token en el header Authorization
3. El token expira en 1 hora (vuelve a hacer login)

