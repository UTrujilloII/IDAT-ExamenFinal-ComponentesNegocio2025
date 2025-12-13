# Script PowerShell para convertir README.md a PDF usando Microsoft Word
# Si no tienes Word, usa la conversión HTML con Print to PDF

param(
    [string]$InputFile = "README.md",
    [string]$OutputFile = "INFORME_TECNICO_MS-Biblioteca.pdf"
)

Write-Host "📄 Generando informe técnico en PDF..." -ForegroundColor Cyan
Write-Host "   Leyendo: $InputFile" -ForegroundColor Gray

# Verificar que existe el archivo
if (-not (Test-Path $InputFile)) {
    Write-Host "❌ Error: No se encontró $InputFile" -ForegroundColor Red
    exit 1
}

# Leer contenido del README
$markdown = Get-Content $InputFile -Raw -Encoding UTF8

# Crear HTML con estilos profesionales
$htmlContent = @"
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MS-Biblioteca - Informe Técnico</title>
    <style>
        @media print {
            @page {
                size: A4;
                margin: 2.5cm 2cm;
            }
            body {
                font-size: 11pt;
            }
            h1, h2, h3 {
                page-break-after: avoid;
            }
            pre, table {
                page-break-inside: avoid;
            }
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            font-size: 11pt;
            line-height: 1.6;
            color: #333;
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }
        
        .cover-page {
            text-align: center;
            padding: 150px 20px;
            page-break-after: always;
        }
        
        .cover-title {
            font-size: 48pt;
            color: #2c3e50;
            margin-bottom: 20px;
            font-weight: bold;
        }
        
        .cover-subtitle {
            font-size: 24pt;
            color: #555;
            margin-bottom: 40px;
        }
        
        .cover-info {
            font-size: 14pt;
            color: #777;
            margin-top: 80px;
        }
        
        h1 {
            color: #2c3e50;
            font-size: 28pt;
            border-bottom: 3px solid #3498db;
            padding-bottom: 10px;
            margin-top: 30px;
            margin-bottom: 20px;
        }
        
        h2 {
            color: #34495e;
            font-size: 20pt;
            border-bottom: 2px solid #95a5a6;
            padding-bottom: 8px;
            margin-top: 25px;
            margin-bottom: 15px;
        }
        
        h3 {
            color: #2980b9;
            font-size: 16pt;
            margin-top: 20px;
            margin-bottom: 12px;
        }
        
        h4 {
            color: #555;
            font-size: 13pt;
            margin-top: 15px;
            margin-bottom: 10px;
        }
        
        p {
            margin-bottom: 10px;
            text-align: justify;
        }
        
        code {
            background-color: #f4f4f4;
            border: 1px solid #ddd;
            border-radius: 3px;
            padding: 2px 6px;
            font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
            font-size: 9.5pt;
            color: #c7254e;
        }
        
        pre {
            background-color: #f8f8f8;
            border: 1px solid #ddd;
            border-left: 4px solid #3498db;
            border-radius: 4px;
            padding: 12px;
            overflow-x: auto;
            margin: 15px 0;
        }
        
        pre code {
            background-color: transparent;
            border: none;
            padding: 0;
            color: #333;
            font-size: 9pt;
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 15px 0;
            font-size: 10pt;
        }
        
        th {
            background-color: #3498db;
            color: white;
            padding: 10px;
            text-align: left;
            font-weight: bold;
        }
        
        td {
            border: 1px solid #ddd;
            padding: 8px;
        }
        
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        
        ul, ol {
            margin: 10px 0;
            padding-left: 30px;
        }
        
        li {
            margin-bottom: 5px;
        }
        
        blockquote {
            border-left: 4px solid #3498db;
            padding-left: 15px;
            margin-left: 0;
            color: #555;
            font-style: italic;
            background-color: #f9f9f9;
            padding: 10px 15px;
        }
        
        a {
            color: #3498db;
            text-decoration: none;
        }
        
        hr {
            border: none;
            border-top: 2px solid #ddd;
            margin: 30px 0;
        }
    </style>
</head>
<body>
    <div class="cover-page">
        <div class="cover-title">MS-Biblioteca</div>
        <div class="cover-subtitle">Sistema de Gestión de Biblioteca Universitaria</div>
        <div class="cover-subtitle">Informe Técnico Completo</div>
        <div class="cover-info">
            <p><strong>Proyecto:</strong> IDAT-ExamenFinal-ComponentesNegocio2025</p>
            <p><strong>Rama:</strong> dev-grupo06</p>
            <p><strong>Fecha:</strong> $(Get-Date -Format "dd 'de' MMMM 'de' yyyy")</p>
        </div>
    </div>
    
    <div class="content">
"@

# Convertir markdown básico a HTML
$htmlBody = $markdown `
    -replace '```([^\n]*)\n(.*?)\n```', '<pre><code>$2</code></pre>' `
    -replace '`([^`]+)`', '<code>$1</code>' `
    -replace '^### (.*)', '<h3>$1</h3>' `
    -replace '^## (.*)', '<h2>$1</h2>' `
    -replace '^# (.*)', '<h1>$1</h1>' `
    -replace '^\| (.*)', '<tr><td>$1</td></tr>' `
    -replace '^\* (.*)', '<li>$1</li>' `
    -replace '^\- (.*)', '<li>$1</li>' `
    -replace '\*\*([^*]+)\*\*', '<strong>$1</strong>' `
    -replace '\*([^*]+)\*', '<em>$1</em>' `
    -replace '\[(.*?)\]\((.*?)\)', '<a href="$2">$1</a>'

$htmlContent += $htmlBody
$htmlContent += @"
    </div>
</body>
</html>
"@

# Guardar HTML temporal
$tempHtml = [System.IO.Path]::GetTempFileName() + ".html"
$htmlContent | Out-File -FilePath $tempHtml -Encoding UTF8

Write-Host "   HTML generado: $tempHtml" -ForegroundColor Gray
Write-Host ""
Write-Host "📝 Instrucciones para generar el PDF:" -ForegroundColor Yellow
Write-Host ""
Write-Host "   1. Abriendo el archivo HTML en tu navegador..." -ForegroundColor White
Write-Host "   2. En el navegador, presiona Ctrl+P para imprimir" -ForegroundColor White
Write-Host "   3. Selecciona 'Microsoft Print to PDF' o 'Guardar como PDF'" -ForegroundColor White
Write-Host "   4. Guarda el archivo como: $OutputFile" -ForegroundColor White
Write-Host ""

# Abrir el HTML en el navegador predeterminado
Start-Process $tempHtml

Write-Host "✅ Archivo HTML temporal creado y abierto" -ForegroundColor Green
Write-Host "   📁 Ubicación temporal: $tempHtml" -ForegroundColor Gray
Write-Host ""
Write-Host "   Después de guardar el PDF, puedes eliminar el archivo temporal." -ForegroundColor Gray
