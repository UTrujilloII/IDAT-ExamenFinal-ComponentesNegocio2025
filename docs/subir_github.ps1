# ================================================
# SCRIPT: Preparar y subir MS-Biblioteca a GitHub
# ================================================

Write-Host "================================================" -ForegroundColor Cyan
Write-Host "  PREPARAR MS-BIBLIOTECA PARA GITHUB" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""

$baseDir = "D:\java_aplicaciones\ms-biblioteca"

# Verificar que estamos en el directorio correcto
if (-not (Test-Path "$baseDir\pom.xml")) {
    Write-Host "❌ Error: No se encuentra pom.xml" -ForegroundColor Red
    Write-Host "   Asegúrate de estar en el directorio correcto" -ForegroundColor Yellow
    exit 1
}

Write-Host "✅ Directorio verificado: $baseDir" -ForegroundColor Green
Write-Host ""

# ==================================================
# PASO 1: Verificar Git
# ==================================================
Write-Host "📋 PASO 1: Verificando Git..." -ForegroundColor Yellow

try {
    $gitVersion = git --version
    Write-Host "✅ Git instalado: $gitVersion" -ForegroundColor Green
} catch {
    Write-Host "❌ Git no está instalado" -ForegroundColor Red
    Write-Host "   Descárgalo de: https://git-scm.com/download/win" -ForegroundColor Yellow
    exit 1
}

Write-Host ""

# ==================================================
# PASO 2: Verificar archivos necesarios
# ==================================================
Write-Host "📋 PASO 2: Verificando archivos necesarios..." -ForegroundColor Yellow

$archivosNecesarios = @(
    ".gitignore",
    "LICENSE",
    "README_GITHUB.md",
    "GUIA_SUBIR_GITHUB.md",
    "pom.xml",
    "init_database_biblioteca.sql",
    "MS-Biblioteca-Collection.json"
)

$todoOK = $true
foreach ($archivo in $archivosNecesarios) {
    if (Test-Path "$baseDir\$archivo") {
        Write-Host "  ✅ $archivo" -ForegroundColor Green
    } else {
        Write-Host "  ❌ $archivo (falta)" -ForegroundColor Red
        $todoOK = $false
    }
}

if (-not $todoOK) {
    Write-Host ""
    Write-Host "❌ Faltan archivos necesarios. Abortando..." -ForegroundColor Red
    exit 1
}

Write-Host ""

# ==================================================
# PASO 3: Preparar README
# ==================================================
Write-Host "📋 PASO 3: Preparando README.md..." -ForegroundColor Yellow

if (Test-Path "$baseDir\README.md") {
    Write-Host "  ℹ️  Respaldando README.md actual como README_OLD.md" -ForegroundColor Cyan
    Move-Item "$baseDir\README.md" "$baseDir\README_OLD.md" -Force
}

if (Test-Path "$baseDir\README_GITHUB.md") {
    Write-Host "  ✅ Renombrando README_GITHUB.md a README.md" -ForegroundColor Green
    Move-Item "$baseDir\README_GITHUB.md" "$baseDir\README.md" -Force
} else {
    Write-Host "  ❌ No se encuentra README_GITHUB.md" -ForegroundColor Red
    exit 1
}

Write-Host ""

# ==================================================
# PASO 4: Inicializar Git
# ==================================================
Write-Host "📋 PASO 4: Inicializando repositorio Git..." -ForegroundColor Yellow

if (Test-Path "$baseDir\.git") {
    Write-Host "  ℹ️  Ya existe un repositorio Git" -ForegroundColor Cyan
    $reinit = Read-Host "  ¿Reinicializar? (s/n)"
    if ($reinit -eq "s") {
        Remove-Item "$baseDir\.git" -Recurse -Force
        git init
        Write-Host "  ✅ Repositorio reinicializado" -ForegroundColor Green
    }
} else {
    git init
    Write-Host "  ✅ Repositorio Git inicializado" -ForegroundColor Green
}

Write-Host ""

# ==================================================
# PASO 5: Agregar archivos
# ==================================================
Write-Host "📋 PASO 5: Agregando archivos al staging..." -ForegroundColor Yellow

git add .

$archivosAgregados = git diff --cached --name-only | Measure-Object -Line
Write-Host "  ✅ Archivos agregados: $($archivosAgregados.Lines)" -ForegroundColor Green

# Mostrar algunos archivos agregados
Write-Host "  📁 Archivos a subir:" -ForegroundColor Cyan
git diff --cached --name-only | Select-Object -First 10 | ForEach-Object {
    Write-Host "     - $_" -ForegroundColor Gray
}
Write-Host "     ... y más" -ForegroundColor Gray

Write-Host ""

# ==================================================
# PASO 6: Verificar archivos ignorados
# ==================================================
Write-Host "📋 PASO 6: Verificando .gitignore..." -ForegroundColor Yellow

$archivosIgnorados = @("target", ".idea", "*.class", "*.log")
$problemas = @()

foreach ($patron in $archivosIgnorados) {
    $encontrados = git status --ignored | Select-String $patron
    if ($encontrados) {
        Write-Host "  ✅ Ignorando: $patron" -ForegroundColor Green
    } else {
        Write-Host "  ⚠️  No se encontró patrón ignorado: $patron" -ForegroundColor Yellow
    }
}

Write-Host ""

# ==================================================
# PASO 7: Crear commit
# ==================================================
Write-Host "📋 PASO 7: Creando commit inicial..." -ForegroundColor Yellow

$commitMessage = @"
Initial commit: Sistema MS-Biblioteca v1.0

- Sistema completo de gestión de biblioteca universitaria
- Spring Boot 3.5.7 + Spring Security + JWT
- 29 endpoints REST (Auth, Libros, Préstamos)
- Base de datos MySQL con 15 libros pre-cargados
- Colección Postman lista para pruebas
- Documentación completa
- Sistema de roles (ADMIN, USUARIO)
- Control de préstamos con multas
"@

git commit -m $commitMessage

if ($LASTEXITCODE -eq 0) {
    Write-Host "  ✅ Commit creado exitosamente" -ForegroundColor Green
} else {
    Write-Host "  ❌ Error al crear commit" -ForegroundColor Red
    exit 1
}

Write-Host ""

# ==================================================
# PASO 8: Configurar remote
# ==================================================
Write-Host "📋 PASO 8: Configurando remote..." -ForegroundColor Yellow

Write-Host ""
Write-Host "  Para continuar, necesitas:" -ForegroundColor Cyan
Write-Host "  1. Tu usuario de GitHub" -ForegroundColor White
Write-Host "  2. Haber creado el repositorio en GitHub" -ForegroundColor White
Write-Host ""

$usuario = Read-Host "  Ingresa tu usuario de GitHub"

if ([string]::IsNullOrWhiteSpace($usuario)) {
    Write-Host "  ❌ Usuario vacío. Abortando..." -ForegroundColor Red
    exit 1
}

$remoteUrl = "https://github.com/$usuario/ms-biblioteca.git"

# Verificar si ya existe el remote
$existeRemote = git remote get-url origin 2>$null

if ($existeRemote) {
    Write-Host "  ℹ️  Ya existe un remote origin" -ForegroundColor Cyan
    git remote remove origin
}

git remote add origin $remoteUrl

Write-Host "  ✅ Remote configurado: $remoteUrl" -ForegroundColor Green
Write-Host ""

# ==================================================
# PASO 9: Preparar para push
# ==================================================
Write-Host "📋 PASO 9: Preparando rama main..." -ForegroundColor Yellow

git branch -M main

Write-Host "  ✅ Rama main configurada" -ForegroundColor Green
Write-Host ""

# ==================================================
# RESUMEN FINAL
# ==================================================
Write-Host "================================================" -ForegroundColor Cyan
Write-Host "  ✅ PREPARACIÓN COMPLETADA" -ForegroundColor Green
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "📊 RESUMEN:" -ForegroundColor Yellow
Write-Host "  • Repositorio: $remoteUrl" -ForegroundColor White
Write-Host "  • Rama: main" -ForegroundColor White
Write-Host "  • Commit: Creado" -ForegroundColor White
Write-Host "  • Archivos: Listos para subir" -ForegroundColor White
Write-Host ""

Write-Host "🚀 SIGUIENTE PASO:" -ForegroundColor Yellow
Write-Host ""
Write-Host "  Ejecuta el siguiente comando para subir a GitHub:" -ForegroundColor White
Write-Host ""
Write-Host "  git push -u origin main" -ForegroundColor Green
Write-Host ""
Write-Host "  Se te pedirá:" -ForegroundColor Cyan
Write-Host "  • Usuario: $usuario" -ForegroundColor White
Write-Host "  • Password: Tu Personal Access Token (PAT)" -ForegroundColor White
Write-Host ""
Write-Host "  📝 Para crear un PAT:" -ForegroundColor Cyan
Write-Host "     1. https://github.com/settings/tokens" -ForegroundColor White
Write-Host "     2. Generate new token (classic)" -ForegroundColor White
Write-Host "     3. Marcar scope: repo" -ForegroundColor White
Write-Host "     4. Copiar el token generado" -ForegroundColor White
Write-Host ""

$hacerPush = Read-Host "¿Deseas hacer push ahora? (s/n)"

if ($hacerPush -eq "s") {
    Write-Host ""
    Write-Host "Ejecutando: git push -u origin main" -ForegroundColor Yellow
    Write-Host ""

    git push -u origin main

    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "🎉 ¡ÉXITO! Proyecto subido a GitHub" -ForegroundColor Green
        Write-Host ""
        Write-Host "Ver en: $remoteUrl" -ForegroundColor Cyan
        Write-Host ""
    } else {
        Write-Host ""
        Write-Host "❌ Error al hacer push" -ForegroundColor Red
        Write-Host "   Verifica tus credenciales y vuelve a intentar" -ForegroundColor Yellow
        Write-Host ""
    }
} else {
    Write-Host ""
    Write-Host "⏸️  Push cancelado. Ejecuta manualmente cuando estés listo:" -ForegroundColor Yellow
    Write-Host "   git push -u origin main" -ForegroundColor White
    Write-Host ""
}

Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""

