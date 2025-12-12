# ============================================
# Script de Limpieza del Sistema Bancario
# Sistema de Biblioteca Universitaria
# Autor: Jonathan Jiménez
# ============================================

Write-Host "=======================================" -ForegroundColor Cyan
Write-Host "  LIMPIEZA DEL SISTEMA BANCARIO" -ForegroundColor Cyan
Write-Host "=======================================" -ForegroundColor Cyan
Write-Host ""

# Variables
$baseDir = "D:\java_aplicaciones\ms-biblioteca"
$srcDir = "$baseDir\src\main\java\pe\edu\idat\msbancario"
$deletedCount = 0

# Función para eliminar archivo
function Remove-FileIfExists {
    param([string]$path, [string]$description)

    if (Test-Path $path) {
        try {
            Remove-Item $path -Force -ErrorAction Stop
            Write-Host "  ✓ Eliminado: $description" -ForegroundColor Green
            $script:deletedCount++
        } catch {
            Write-Host "  ✗ Error al eliminar: $description" -ForegroundColor Red
        }
    } else {
        Write-Host "  - No existe: $description" -ForegroundColor Gray
    }
}

# Función para eliminar carpeta
function Remove-FolderIfExists {
    param([string]$path, [string]$description)

    if (Test-Path $path) {
        try {
            Remove-Item $path -Recurse -Force -ErrorAction Stop
            Write-Host "  ✓ Eliminada carpeta: $description" -ForegroundColor Green
            $script:deletedCount++
        } catch {
            Write-Host "  ✗ Error al eliminar carpeta: $description" -ForegroundColor Red
        }
    } else {
        Write-Host "  - No existe carpeta: $description" -ForegroundColor Gray
    }
}

# ==========================
# 1. ENTIDADES
# ==========================
Write-Host "`n[1/7] Eliminando entidades del sistema bancario..." -ForegroundColor Yellow
Remove-FileIfExists "$srcDir\entity\Cliente.java" "Cliente.java"
Remove-FileIfExists "$srcDir\entity\CuentaBancaria.java" "CuentaBancaria.java"
Remove-FileIfExists "$srcDir\entity\Transaccion.java" "Transaccion.java"

# ==========================
# 2. CONTROLADORES
# ==========================
Write-Host "`n[2/7] Eliminando controladores del sistema bancario..." -ForegroundColor Yellow
Remove-FileIfExists "$srcDir\controller\ClienteController.java" "ClienteController.java"
Remove-FileIfExists "$srcDir\controller\CuentaBancariaController.java" "CuentaBancariaController.java"
Remove-FileIfExists "$srcDir\controller\DebugController.java" "DebugController.java"

# ==========================
# 3. SERVICIOS
# ==========================
Write-Host "`n[3/7] Eliminando servicios del sistema bancario..." -ForegroundColor Yellow
Remove-FolderIfExists "$srcDir\service\impl" "Carpeta impl/"

# ==========================
# 4. REPOSITORIOS
# ==========================
Write-Host "`n[4/7] Eliminando repositorios del sistema bancario..." -ForegroundColor Yellow
Remove-FileIfExists "$srcDir\repository\ClienteRepository.java" "ClienteRepository.java"
Remove-FileIfExists "$srcDir\repository\CuentaBancariaRepository.java" "CuentaBancariaRepository.java"
Remove-FileIfExists "$srcDir\repository\TransaccionRepository.java" "TransaccionRepository.java"

# ==========================
# 5. DTOs
# ==========================
Write-Host "`n[5/7] Eliminando DTOs del sistema bancario..." -ForegroundColor Yellow
Remove-FolderIfExists "$srcDir\dto\cliente" "Carpeta cliente/"
Remove-FolderIfExists "$srcDir\dto\cuentaBancaria" "Carpeta cuentaBancaria/"
Remove-FolderIfExists "$srcDir\dto\transaccion" "Carpeta transaccion/"

# ==========================
# 6. MAPPERS
# ==========================
Write-Host "`n[6/7] Eliminando mappers del sistema bancario..." -ForegroundColor Yellow
Remove-FileIfExists "$srcDir\mappers\ClienteMapper.java" "ClienteMapper.java"
Remove-FileIfExists "$srcDir\mappers\CuentaBancariaMapper.java" "CuentaBancariaMapper.java"
Remove-FileIfExists "$srcDir\mappers\TransaccionMapper.java" "TransaccionMapper.java"

# ==========================
# 7. ARCHIVOS SQL Y DOCS
# ==========================
Write-Host "`n[7/7] Eliminando archivos SQL y documentación obsoleta..." -ForegroundColor Yellow

# SQL
Remove-FileIfExists "$baseDir\init_database.sql" "init_database.sql"
Remove-FileIfExists "$baseDir\init_database_completo.sql" "init_database_completo.sql"
Remove-FileIfExists "$baseDir\INIT_SOLO_TABLAS.sql" "INIT_SOLO_TABLAS.sql"
Remove-FileIfExists "$baseDir\FIX_LOGIN_RAPIDO.sql" "FIX_LOGIN_RAPIDO.sql"

# Documentación
Write-Host "`n  Eliminando archivos de documentación obsoleta..." -ForegroundColor DarkYellow
Get-ChildItem "$baseDir\SOLUCION_*.md" -ErrorAction SilentlyContinue | ForEach-Object {
    Remove-FileIfExists $_.FullName $_.Name
}

Get-ChildItem "$baseDir\GUIA_*.md" -ErrorAction SilentlyContinue | ForEach-Object {
    Remove-FileIfExists $_.FullName $_.Name
}

Get-ChildItem "$baseDir\ERROR_*.md" -ErrorAction SilentlyContinue | ForEach-Object {
    Remove-FileIfExists $_.FullName $_.Name
}

Remove-FileIfExists "$baseDir\ARQUITECTURA_SISTEMA.md" "ARQUITECTURA_SISTEMA.md"
Remove-FileIfExists "$baseDir\CONFIGURACION_LARAGON.md" "CONFIGURACION_LARAGON.md"
Remove-FileIfExists "$baseDir\DIAGNOSTICO_403_FINAL.md" "DIAGNOSTICO_403_FINAL.md"
Remove-FileIfExists "$baseDir\DOCUMENTACION_PROYECTO.md" "DOCUMENTACION_PROYECTO.md"
Remove-FileIfExists "$baseDir\EJECUTAR_COMO_ADMIN.md" "EJECUTAR_COMO_ADMIN.md"
Remove-FileIfExists "$baseDir\EJECUTAR_DESDE_INTELLIJ.md" "EJECUTAR_DESDE_INTELLIJ.md"
Remove-FileIfExists "$baseDir\EJEMPLOS_CURL.md" "EJEMPLOS_CURL.md"
Remove-FileIfExists "$baseDir\ELIMINACION_EMOJIS.md" "ELIMINACION_EMOJIS.md"
Remove-FileIfExists "$baseDir\INSTRUCCIONES_INMEDIATAS.md" "INSTRUCCIONES_INMEDIATAS.md"
Remove-FileIfExists "$baseDir\MEJORAS_MANEJO_ERRORES.md" "MEJORAS_MANEJO_ERRORES.md"
Remove-FileIfExists "$baseDir\POSTMAN_ROLES_GUIA.md" "POSTMAN_ROLES_GUIA.md"
Remove-FileIfExists "$baseDir\README_EJECUCION.md" "README_EJECUCION.md"
Remove-FileIfExists "$baseDir\RESUMEN_CAMBIOS.md" "RESUMEN_CAMBIOS.md"
Remove-FileIfExists "$baseDir\RESUMEN_ROLES_POSTMAN.md" "RESUMEN_ROLES_POSTMAN.md"

# Postman
Remove-FileIfExists "$baseDir\MS-Bancario-Complete-Collection.json" "MS-Bancario-Complete-Collection.json"
Remove-FileIfExists "$baseDir\MS-Bancario-Environment.json" "MS-Bancario-Environment.json"

# ==========================
# RESUMEN
# ==========================
Write-Host "`n=======================================" -ForegroundColor Cyan
Write-Host "  RESUMEN DE LIMPIEZA" -ForegroundColor Cyan
Write-Host "=======================================" -ForegroundColor Cyan
Write-Host "  Archivos eliminados: $deletedCount" -ForegroundColor Green
Write-Host "  Estado: Limpieza completada" -ForegroundColor Green
Write-Host "=======================================" -ForegroundColor Cyan

Write-Host "`n📝 Próximos pasos:" -ForegroundColor Yellow
Write-Host "  1. Compilar el proyecto: mvn clean compile" -ForegroundColor White
Write-Host "  2. Verificar que no hay errores de compilación" -ForegroundColor White
Write-Host "  3. Ejecutar la aplicación: mvn spring-boot:run" -ForegroundColor White
Write-Host "  4. Probar endpoints con Postman" -ForegroundColor White

Write-Host "`n✅ Sistema de Biblioteca listo para usar!" -ForegroundColor Green
Write-Host ""

