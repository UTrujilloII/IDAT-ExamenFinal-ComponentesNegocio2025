# 🧹 Limpieza del Sistema - Eliminar Archivos del Sistema Bancario Anterior

## Archivos y Carpetas a Eliminar

### 📂 Entidades del Sistema Bancario (entity/)
Eliminar los siguientes archivos de: `src/main/java/pe/edu/idat/msbancario/entity/`

- ❌ `Cliente.java` - Entidad del sistema bancario
- ❌ `CuentaBancaria.java` - Entidad del sistema bancario
- ❌ `Transaccion.java` - Entidad del sistema bancario

✅ **MANTENER:**
- ✅ `Libro.java` - Nueva entidad del sistema biblioteca
- ✅ `Prestamo.java` - Nueva entidad del sistema biblioteca
- ✅ `Usuario.java` - Actualizada para sistema biblioteca
- ✅ `Rol.java` - Actualizada para sistema biblioteca

---

### 📂 Controladores del Sistema Bancario (controller/)
Eliminar los siguientes archivos de: `src/main/java/pe/edu/idat/msbancario/controller/`

- ❌ `ClienteController.java` - Controlador del sistema bancario
- ❌ `CuentaBancariaController.java` - Controlador del sistema bancario
- ❌ `DebugController.java` - Controlador de debug (opcional mantener si es útil)

✅ **MANTENER:**
- ✅ `AuthController.java` - Sistema de autenticación
- ✅ `LibroController.java` - Nuevo controlador del sistema biblioteca
- ✅ `PrestamoController.java` - Nuevo controlador del sistema biblioteca

---

### 📂 Servicios del Sistema Bancario (service/)
Eliminar los siguientes archivos y carpetas de: `src/main/java/pe/edu/idat/msbancario/service/`

- ❌ Carpeta `impl/` completa (contiene implementaciones del sistema bancario)
  - `ClienteServiceImpl.java`
  - `CuentaBancariaServiceImpl.java`
  - `TransaccionServiceImpl.java`
  - etc.

✅ **MANTENER:**
- ✅ `LibroService.java` - Nuevo servicio del sistema biblioteca
- ✅ `PrestamoService.java` - Nuevo servicio del sistema biblioteca

---

### 📂 Repositorios del Sistema Bancario (repository/)
Eliminar los siguientes archivos de: `src/main/java/pe/edu/idat/msbancario/repository/`

- ❌ `ClienteRepository.java` - Repositorio del sistema bancario
- ❌ `CuentaBancariaRepository.java` - Repositorio del sistema bancario
- ❌ `TransaccionRepository.java` - Repositorio del sistema bancario

✅ **MANTENER:**
- ✅ `LibroRepository.java` - Nuevo repositorio del sistema biblioteca
- ✅ `PrestamoRepository.java` - Nuevo repositorio del sistema biblioteca
- ✅ `UsuarioRepository.java` - Repositorio actualizado
- ✅ `RolRepository.java` - Repositorio actualizado

---

### 📂 DTOs del Sistema Bancario (dto/)
Eliminar las siguientes carpetas de: `src/main/java/pe/edu/idat/msbancario/dto/`

- ❌ Carpeta `cliente/` completa
  - `ClienteRequestDTO.java`
  - `ClienteResponseDTO.java`
  - etc.

- ❌ Carpeta `cuentaBancaria/` completa
  - `CuentaBancariaRequestDTO.java`
  - `CuentaBancariaResponseDTO.java`
  - etc.

- ❌ Carpeta `transaccion/` completa
  - `TransaccionRequestDTO.java`
  - `TransaccionResponseDTO.java`
  - etc.

✅ **MANTENER:**
- ✅ Carpeta `auth/` - Sistema de autenticación
- ✅ Carpeta `common/` - DTOs comunes (ApiResponse, etc.)
- ✅ Carpeta `error/` - DTOs de manejo de errores
- ✅ Carpeta `jwt/` - DTOs de JWT
- ✅ Carpeta `libro/` - Nuevos DTOs del sistema biblioteca
- ✅ Carpeta `prestamo/` - Nuevos DTOs del sistema biblioteca

---

### 📂 Mappers del Sistema Bancario (mappers/)
Eliminar los siguientes archivos de: `src/main/java/pe/edu/idat/msbancario/mappers/`

- ❌ `ClienteMapper.java` - Mapper del sistema bancario
- ❌ `CuentaBancariaMapper.java` - Mapper del sistema bancario
- ❌ `TransaccionMapper.java` - Mapper del sistema bancario

✅ **MANTENER:**
- ✅ `LibroMapper.java` - Nuevo mapper del sistema biblioteca
- ✅ `PrestamoMapper.java` - Nuevo mapper del sistema biblioteca

---

### 📂 Archivos SQL del Sistema Bancario (raíz del proyecto)
Eliminar los siguientes archivos de: `D:/java_aplicaciones/ms-biblioteca/`

- ❌ `init_database.sql` - Script del sistema bancario
- ❌ `init_database_completo.sql` - Script del sistema bancario
- ❌ `INIT_SOLO_TABLAS.sql` - Script del sistema bancario
- ❌ `FIX_LOGIN_RAPIDO.sql` - Fix del sistema bancario

✅ **MANTENER:**
- ✅ `init_database_biblioteca.sql` - Nuevo script del sistema biblioteca

---

### 📂 Archivos de Documentación Obsoletos (raíz del proyecto)
Eliminar archivos de documentación del sistema bancario:

- ❌ `DOCUMENTACION_PROYECTO.md` - Documentación del sistema bancario
- ❌ Todos los archivos `SOLUCION_*.md` (más de 10 archivos)
- ❌ Archivos `GUIA_*.md` relacionados con el sistema bancario
- ❌ Archivos `EJEMPLOS_CURL.md`, `ELIMINACION_EMOJIS.md`, etc.
- ❌ `MS-Bancario-Complete-Collection.json` - Colección Postman del sistema bancario
- ❌ `MS-Bancario-Environment.json` - Variables Postman del sistema bancario

✅ **MANTENER:**
- ✅ `README_SISTEMA_BIBLIOTECA.md` - Nueva documentación del sistema biblioteca
- ✅ `README.md` - README general (actualizar contenido)
- ✅ `pom.xml` - Configuración Maven
- ✅ `application.properties` - Configuración (ya actualizada)

---

### 📂 Archivos BAT de Ejecución Antiguos (raíz del proyecto)
Opcional - Eliminar si no son necesarios:

- ⚠️ `compilar.bat`
- ⚠️ `ejecutar*.bat` (múltiples archivos)
- ⚠️ `verificar-mysql*.bat`
- ⚠️ `prueba-directa.bat`

---

## 🔧 Script de Limpieza Automática

### PowerShell Script

Crear archivo `limpiar_sistema_antiguo.ps1`:

```powershell
# Script de limpieza del sistema bancario anterior
Write-Host "🧹 Limpiando archivos del sistema bancario anterior..." -ForegroundColor Cyan

# Variables
$baseDir = "D:\java_aplicaciones\ms-biblioteca"
$srcDir = "$baseDir\src\main\java\pe\edu\idat\msbancario"

# Eliminar entidades
Write-Host "`n📂 Eliminando entidades del sistema bancario..." -ForegroundColor Yellow
Remove-Item "$srcDir\entity\Cliente.java" -ErrorAction SilentlyContinue
Remove-Item "$srcDir\entity\CuentaBancaria.java" -ErrorAction SilentlyContinue
Remove-Item "$srcDir\entity\Transaccion.java" -ErrorAction SilentlyContinue

# Eliminar controladores
Write-Host "📂 Eliminando controladores del sistema bancario..." -ForegroundColor Yellow
Remove-Item "$srcDir\controller\ClienteController.java" -ErrorAction SilentlyContinue
Remove-Item "$srcDir\controller\CuentaBancariaController.java" -ErrorAction SilentlyContinue
Remove-Item "$srcDir\controller\DebugController.java" -ErrorAction SilentlyContinue

# Eliminar servicios
Write-Host "📂 Eliminando servicios del sistema bancario..." -ForegroundColor Yellow
Remove-Item "$srcDir\service\impl" -Recurse -ErrorAction SilentlyContinue

# Eliminar repositorios
Write-Host "📂 Eliminando repositorios del sistema bancario..." -ForegroundColor Yellow
Remove-Item "$srcDir\repository\ClienteRepository.java" -ErrorAction SilentlyContinue
Remove-Item "$srcDir\repository\CuentaBancariaRepository.java" -ErrorAction SilentlyContinue
Remove-Item "$srcDir\repository\TransaccionRepository.java" -ErrorAction SilentlyContinue

# Eliminar DTOs
Write-Host "📂 Eliminando DTOs del sistema bancario..." -ForegroundColor Yellow
Remove-Item "$srcDir\dto\cliente" -Recurse -ErrorAction SilentlyContinue
Remove-Item "$srcDir\dto\cuentaBancaria" -Recurse -ErrorAction SilentlyContinue
Remove-Item "$srcDir\dto\transaccion" -Recurse -ErrorAction SilentlyContinue

# Eliminar mappers
Write-Host "📂 Eliminando mappers del sistema bancario..." -ForegroundColor Yellow
Remove-Item "$srcDir\mappers\ClienteMapper.java" -ErrorAction SilentlyContinue
Remove-Item "$srcDir\mappers\CuentaBancariaMapper.java" -ErrorAction SilentlyContinue
Remove-Item "$srcDir\mappers\TransaccionMapper.java" -ErrorAction SilentlyContinue

# Eliminar archivos SQL antiguos
Write-Host "📂 Eliminando archivos SQL del sistema bancario..." -ForegroundColor Yellow
Remove-Item "$baseDir\init_database.sql" -ErrorAction SilentlyContinue
Remove-Item "$baseDir\init_database_completo.sql" -ErrorAction SilentlyContinue
Remove-Item "$baseDir\INIT_SOLO_TABLAS.sql" -ErrorAction SilentlyContinue
Remove-Item "$baseDir\FIX_LOGIN_RAPIDO.sql" -ErrorAction SilentlyContinue

# Eliminar documentación obsoleta
Write-Host "📂 Eliminando documentación obsoleta..." -ForegroundColor Yellow
Get-ChildItem "$baseDir\SOLUCION_*.md" | Remove-Item -ErrorAction SilentlyContinue
Get-ChildItem "$baseDir\GUIA_*.md" | Remove-Item -ErrorAction SilentlyContinue
Remove-Item "$baseDir\DOCUMENTACION_PROYECTO.md" -ErrorAction SilentlyContinue
Remove-Item "$baseDir\EJEMPLOS_CURL.md" -ErrorAction SilentlyContinue
Remove-Item "$baseDir\ELIMINACION_EMOJIS.md" -ErrorAction SilentlyContinue
Remove-Item "$baseDir\MS-Bancario-Complete-Collection.json" -ErrorAction SilentlyContinue
Remove-Item "$baseDir\MS-Bancario-Environment.json" -ErrorAction SilentlyContinue

Write-Host "`n✅ Limpieza completada!" -ForegroundColor Green
Write-Host "📝 Revisa el archivo LIMPIEZA_SISTEMA.md para más detalles" -ForegroundColor Cyan
```

---

## 📝 Instrucciones de Ejecución

### Método 1: Manual (Recomendado para verificar antes de eliminar)

1. Abrir el Explorador de Archivos
2. Navegar a cada carpeta mencionada arriba
3. Verificar los archivos antes de eliminar
4. Eliminar manualmente los archivos marcados con ❌

### Método 2: Automático con PowerShell

1. Abrir PowerShell como Administrador
2. Navegar al directorio del proyecto:
   ```powershell
   cd D:\java_aplicaciones\ms-biblioteca
   ```
3. Ejecutar el script de limpieza:
   ```powershell
   .\limpiar_sistema_antiguo.ps1
   ```

### Método 3: Desde IntelliJ IDEA

1. Seleccionar los archivos en el Project Explorer
2. Clic derecho → Delete
3. Confirmar la eliminación

---

## ⚠️ IMPORTANTE - Antes de Eliminar

1. **Hacer backup del proyecto completo**
2. **Verificar que el sistema de biblioteca funciona correctamente**
3. **Compilar el proyecto para detectar dependencias:**
   ```bash
   mvn clean compile
   ```
4. **Revisar que no haya imports de clases eliminadas**

---

## 🔍 Verificación Post-Limpieza

Después de eliminar, ejecutar:

```bash
# Limpiar y compilar
mvn clean compile

# Si hay errores, revisar imports
# Buscar referencias a clases eliminadas
```

---

## 📊 Resumen de Archivos

| Tipo | A Eliminar | A Mantener |
|------|------------|------------|
| Entidades | 3 | 4 |
| Controladores | 3 | 3 |
| Servicios | Carpeta impl/ | 2 |
| Repositorios | 3 | 4 |
| DTOs (carpetas) | 3 | 5 |
| Mappers | 3 | 2 |
| SQL | 4 | 1 |
| Documentación | 15+ | 2 |

---

**Total aproximado a eliminar:** ~30-40 archivos del sistema bancario anterior

**Espacio liberado estimado:** 500 KB - 1 MB

---

## ✅ Sistema Limpio Final

Después de la limpieza, el sistema contendrá únicamente:

- ✅ Entidades de biblioteca (Libro, Prestamo, Usuario, Rol)
- ✅ Controladores de biblioteca (Libro, Prestamo, Auth)
- ✅ Servicios de biblioteca (Libro, Prestamo)
- ✅ Repositorios de biblioteca
- ✅ DTOs de biblioteca
- ✅ Sistema de seguridad JWT
- ✅ Documentación del sistema biblioteca

---

**Fecha de limpieza:** 2025-12-11  
**Autor:** Jonathan Jiménez  
**GitHub:** https://github.com/vansfanelx/

