# 📮 Guía de Uso - Postman Collection

## ✅ Importante: Formato de Roles

**Al registrar un usuario, envía el rol SIN el prefijo `ROLE_`:**

### ✅ CORRECTO:
```json
{
  "username": "testuser",
  "password": "password123",
  "role": "USER"
}
```

```json
{
  "username": "newadmin",
  "password": "admin456",
  "role": "ADMIN"
}
```

### ⚠️ También funciona (pero NO es necesario):
```json
{
  "role": "ROLE_USER"
}
```

### ❌ INCORRECTO:
```json
{
  "role": "user"    // minúsculas NO funcionan
}
```

---

## 🔄 Cómo Funciona el Sistema

Cuando envías `"role": "USER"` o `"role": "ADMIN"`:

1. **Tu petición:**
   ```json
   {
     "username": "testuser",
     "password": "password123",
     "role": "USER"
   }
   ```

2. **El sistema automáticamente:**
   - Recibe: `"USER"`
   - Busca en la BD: `"ROLE_USER"`
   - Si existe el rol → Crea el usuario
   - Si NO existe → Error: "El rol ingresado no existe: ROLE_USER"

3. **La respuesta incluye el nombre completo del rol:**
   ```json
   {
     "accessToken": "eyJhbGc...",
     "refreshToken": "eyJhbGc...",
     "username": "testuser",
     "role": "ROLE_USER"
   }
   ```

---

## 📋 Endpoints en la Colección

### Autenticación

#### 1. Register - Nuevo Usuario (rol USER)
```
POST /v1/auth/register
Body: {"username": "testuser", "password": "password123", "role": "USER"}
```

#### 1b. Register - Nuevo Admin (rol ADMIN)
```
POST /v1/auth/register
Body: {"username": "newadmin", "password": "admin456", "role": "ADMIN"}
```

#### 2. Login - Admin
```
POST /v1/auth/login
Body: {"username": "admin", "password": "admin123"}
```

#### 3. Login - User
```
POST /v1/auth/login
Body: {"username": "user1", "password": "user123"}
```

#### 4. Refresh Token
```
POST /v1/auth/refresh-token
Body: {"refreshToken": "{{refreshToken}}"}
```

---

## 🔐 Usuarios Pre-creados

Si ejecutaste el script `init_database_completo.sql`, tienes estos usuarios:

| Username | Password | Rol | Uso |
|----------|----------|-----|-----|
| `admin` | `admin123` | ROLE_ADMIN | Para testing como administrador |
| `user1` | `user123` | ROLE_USER | Para testing como usuario normal |

---

## 🚀 Flujo Recomendado

### Primera Vez:

1. **Ejecutar script SQL** (¡IMPORTANTE!)
   ```bash
   # En HeidiSQL o MySQL:
   # Ejecutar: init_database_completo.sql
   ```

2. **Login con usuario existente**
   ```
   POST /v1/auth/login
   Body: {"username": "admin", "password": "admin123"}
   ```

3. **Usar el token** en endpoints protegidos
   ```
   Authorization: Bearer {{accessToken}}
   ```

### Para crear nuevos usuarios:

1. **Registrar con rol USER**
   ```json
   POST /v1/auth/register
   {
     "username": "juanperez",
     "password": "juan123",
     "role": "USER"
   }
   ```

2. **Registrar con rol ADMIN**
   ```json
   POST /v1/auth/register
   {
     "username": "mariaadmin",
     "password": "maria123",
     "role": "ADMIN"
   }
   ```

---

## 🎯 Scripts Automáticos

La colección incluye scripts que **guardan automáticamente los tokens** en las variables de entorno:

- ✅ `accessToken` → Se guarda al hacer login/register
- ✅ `refreshToken` → Se guarda al hacer login/register
- ✅ Los tokens se usan automáticamente en endpoints protegidos

**Para ver los logs:**
- Abre la consola de Postman: `View > Show Postman Console`
- Verás mensajes como: `✅ Access Token guardado`

---

## ❌ Errores Comunes

### Error: "El rol ingresado no existe: ROLE_USER"

**Causa:** No ejecutaste el script SQL
**Solución:**
```sql
-- Ejecutar en MySQL:
USE bd_sistema_bancario;
INSERT INTO rol (nombre) VALUES ('ROLE_ADMIN'), ('ROLE_USER');
```

O ejecutar el script completo: `init_database_completo.sql`

### Error: 403 Forbidden

**Causa:** La aplicación no se reinició después de los cambios
**Solución:** Reinicia Spring Boot

### Error: Token inválido o expirado

**Causa:** El access token expira en 1 hora
**Solución:** Usa el endpoint "4. Refresh Token"

---

## 📝 Notas Importantes

1. **Los roles en la BD tienen prefijo `ROLE_`:**
   - En BD: `ROLE_ADMIN`, `ROLE_USER`
   - En Postman envías: `ADMIN`, `USER`
   - El sistema hace la conversión automáticamente

2. **Los tokens se guardan automáticamente:**
   - No necesitas copiar y pegar manualmente
   - Los scripts de test lo hacen por ti

3. **Roles disponibles:**
   - Solo existen `ADMIN` y `USER`
   - Si necesitas más roles, agrégalos en la BD con prefijo `ROLE_`

---

## 🔧 Variables de Entorno

La colección usa estas variables (se crean automáticamente):

| Variable | Descripción | Valor por defecto |
|----------|-------------|-------------------|
| `baseUrl` | URL del API | `http://localhost:9595` |
| `accessToken` | Token de acceso | (se guarda automáticamente) |
| `refreshToken` | Token de refresh | (se guarda automáticamente) |

---

## ✨ Actualización 2025-12-10

✅ Corregido formato de roles: ahora envías `"USER"` o `"ADMIN"` sin prefijo
✅ Sistema convierte automáticamente a `"ROLE_USER"` y `"ROLE_ADMIN"`
✅ Agregado ejemplo de registro con rol ADMIN
✅ Mejorados scripts de test con mejor manejo de errores
✅ Documentación actualizada con ejemplos claros

---

**¿Dudas?** Consulta: `SOLUCION_COMPLETA_403.md`

