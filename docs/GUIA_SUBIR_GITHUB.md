# 🚀 GUÍA: SUBIR MS-BIBLIOTECA A GITHUB

## 📋 PREPARACIÓN COMPLETADA ✅

Los siguientes archivos ya han sido creados:
- ✅ `.gitignore` - Configurado para proyectos Java/Spring Boot
- ✅ `README_GITHUB.md` - Documentación completa del proyecto
- ✅ `LICENSE` - Licencia MIT

---

## 🔑 PASO 1: CONFIGURAR GIT LOCAL

### 1.1 Verificar instalación de Git

```powershell
git --version
```

Si no está instalado, descargarlo de: https://git-scm.com/download/win

### 1.2 Configurar usuario Git (si es primera vez)

```powershell
git config --global user.name "Tu Nombre"
git config --global user.email "tu-email@ejemplo.com"
```

### 1.3 Verificar configuración

```powershell
git config --global user.name
git config --global user.email
```

---

## 🌐 PASO 2: CREAR REPOSITORIO EN GITHUB

### Opción A: Desde GitHub Web (Recomendado)

1. Ir a: https://github.com/new
2. **Repository name:** `ms-biblioteca`
3. **Description:** `Sistema de Gestión de Biblioteca Universitaria con Spring Boot`
4. **Visibilidad:** Elegir Public o Private
5. **NO marcar:** "Add a README file" (ya lo tenemos)
6. **NO marcar:** "Add .gitignore" (ya lo tenemos)
7. **NO marcar:** "Choose a license" (ya lo tenemos)
8. Click en **"Create repository"**

### Opción B: Desde GitHub CLI

```powershell
gh repo create ms-biblioteca --public --description "Sistema de Gestión de Biblioteca Universitaria con Spring Boot"
```

---

## 💻 PASO 3: INICIALIZAR REPOSITORIO LOCAL

Abrir PowerShell en la carpeta del proyecto:

```powershell
cd D:\java_aplicaciones\ms-biblioteca
```

### 3.1 Inicializar Git

```powershell
git init
```

### 3.2 Renombrar README.md

```powershell
# Respaldar el README actual (tiene documentación vieja)
Move-Item README.md README_OLD.md -Force

# Usar el nuevo README para GitHub
Move-Item README_GITHUB.md README.md -Force
```

### 3.3 Agregar todos los archivos

```powershell
git add .
```

### 3.4 Verificar archivos a subir

```powershell
git status
```

**Deberías ver en verde:**
- Código fuente (src/)
- Archivos de configuración (pom.xml, application.properties)
- Scripts SQL (init_database_biblioteca.sql)
- Colección Postman (MS-Biblioteca-Collection.json)
- Documentación (*.md)
- LICENSE

**NO deberías ver (ignorados por .gitignore):**
- target/
- .idea/
- *.class
- *.log

### 3.5 Hacer el primer commit

```powershell
git commit -m "Initial commit: Sistema MS-Biblioteca v1.0

- Sistema completo de gestión de biblioteca universitaria
- Spring Boot 3.5.7 + Spring Security + JWT
- 29 endpoints REST (Auth, Libros, Préstamos)
- Base de datos MySQL con 15 libros pre-cargados
- Colección Postman lista para pruebas
- Documentación completa
- Sistema de roles (ADMIN, USUARIO)
- Control de préstamos con multas
"
```

---

## 🔗 PASO 4: CONECTAR CON GITHUB

Reemplaza `TU_USUARIO` con tu usuario de GitHub:

```powershell
git remote add origin https://github.com/TU_USUARIO/ms-biblioteca.git
```

**Ejemplo:**
```powershell
git remote add origin https://github.com/vansfanelx/ms-biblioteca.git
```

### 4.1 Verificar remote

```powershell
git remote -v
```

Deberías ver:
```
origin  https://github.com/TU_USUARIO/ms-biblioteca.git (fetch)
origin  https://github.com/TU_USUARIO/ms-biblioteca.git (push)
```

---

## 📤 PASO 5: SUBIR AL REPOSITORIO

### 5.1 Cambiar a rama main

```powershell
git branch -M main
```

### 5.2 Hacer push

```powershell
git push -u origin main
```

**Se te pedirá autenticación:**
- **Usuario:** Tu usuario de GitHub
- **Password:** Tu Personal Access Token (PAT) de GitHub

### 5.3 Crear Personal Access Token (si no tienes)

1. Ir a: https://github.com/settings/tokens
2. Click en **"Generate new token"** → **"Generate new token (classic)"**
3. **Note:** `Token para ms-biblioteca`
4. **Expiration:** Elegir duración
5. **Scopes:** Marcar `repo` (acceso completo a repositorios)
6. Click en **"Generate token"**
7. **¡IMPORTANTE!** Copiar el token (solo se muestra una vez)
8. Usar este token como password al hacer push

---

## ✅ PASO 6: VERIFICAR EN GITHUB

1. Ir a: `https://github.com/TU_USUARIO/ms-biblioteca`
2. Verificar que aparezcan:
   - ✅ README.md con toda la documentación
   - ✅ Código fuente
   - ✅ Archivos de configuración
   - ✅ Documentación (.md)
   - ✅ License badge
   - ✅ Sin archivos de target/ o .idea/

---

## 🎨 PASO 7: PERSONALIZAR GITHUB (OPCIONAL)

### 7.1 Agregar Topics

En GitHub, ir a tu repo → About (rueda dentada) → Topics:
- `spring-boot`
- `java`
- `mysql`
- `jwt`
- `rest-api`
- `library-management`
- `spring-security`

### 7.2 Crear archivo CONTRIBUTING.md

```powershell
# En el proyecto
git add CONTRIBUTING.md
git commit -m "docs: Add CONTRIBUTING guide"
git push
```

### 7.3 Agregar Issues Templates

GitHub → Settings → Features → Issues → Set up templates

---

## 🔄 COMANDOS ÚTILES PARA EL FUTURO

### Actualizar cambios

```powershell
# Ver cambios
git status

# Agregar cambios
git add .

# Commit
git commit -m "descripción de cambios"

# Subir
git push
```

### Ver historial

```powershell
git log --oneline
```

### Crear rama nueva

```powershell
git checkout -b feature/nueva-funcionalidad
```

### Cambiar entre ramas

```powershell
git checkout main
git checkout feature/nueva-funcionalidad
```

---

## 📊 ESTRUCTURA FINAL EN GITHUB

```
github.com/TU_USUARIO/ms-biblioteca/
├── 📄 README.md (principal, se ve en la página)
├── 📄 LICENSE
├── 📄 .gitignore
├── 📁 src/
│   ├── main/java/pe/edu/idat/msbiblioteca/
│   └── main/resources/
├── 📁 Documentación/
│   ├── INDICE_DOCUMENTACION.md
│   ├── SISTEMA_ACTUALIZADO_POSTMAN.md
│   ├── GUIA_PRUEBAS_POSTMAN.md
│   └── ...
├── 📄 pom.xml
├── 📄 init_database_biblioteca.sql
├── 📄 MS-Biblioteca-Collection.json
└── 📄 mvnw / mvnw.cmd
```

---

## 🎯 CHECKLIST FINAL

Antes de hacer push, verificar:

- [ ] `.gitignore` está presente
- [ ] README.md está actualizado
- [ ] LICENSE está presente
- [ ] No hay archivos sensibles (passwords, tokens)
- [ ] No hay archivos compilados (.class)
- [ ] No hay carpeta target/
- [ ] No hay carpeta .idea/
- [ ] Colección Postman incluida
- [ ] Script SQL incluido
- [ ] Documentación completa

---

## 🚨 PROBLEMAS COMUNES

### Error: "fatal: remote origin already exists"

```powershell
git remote remove origin
git remote add origin https://github.com/TU_USUARIO/ms-biblioteca.git
```

### Error: "failed to push some refs"

```powershell
# Primero hacer pull
git pull origin main --rebase

# Luego push
git push -u origin main
```

### Olvidé agregar .gitignore antes del primer commit

```powershell
# Limpiar caché
git rm -r --cached .

# Re-agregar todo
git add .

# Commit
git commit -m "fix: Update .gitignore"
```

### Subí archivos sensibles por error

```powershell
# Remover del historial
git filter-branch --force --index-filter \
"git rm --cached --ignore-unmatch ruta/archivo.txt" \
--prune-empty --tag-name-filter cat -- --all

# Force push
git push origin --force --all
```

---

## 🎉 ¡LISTO!

Tu proyecto **MS-Biblioteca** ahora está en GitHub y listo para:

✅ **Compartir** con otros desarrolladores  
✅ **Colaborar** en equipo  
✅ **Mostrar** en tu portafolio  
✅ **Clonar** en otras máquinas  
✅ **Contribuir** con la comunidad  

**URL de tu repo:**
```
https://github.com/TU_USUARIO/ms-biblioteca
```

---

## 📚 RECURSOS ADICIONALES

- **Git Docs:** https://git-scm.com/doc
- **GitHub Guides:** https://guides.github.com/
- **Git Cheat Sheet:** https://education.github.com/git-cheat-sheet-education.pdf

---

**Fecha:** 2025-12-11  
**Estado:** ✅ GUÍA COMPLETA  
**Siguiente:** ¡A subir el código! 🚀

