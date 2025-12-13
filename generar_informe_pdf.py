#!/usr/bin/env python3
"""
Script para convertir README.md a PDF con formato de informe técnico
Requiere: pip install markdown2 weasyprint
"""

import sys
import os
from pathlib import Path

try:
    import markdown2
    from weasyprint import HTML, CSS
except ImportError:
    print("Instalando dependencias necesarias...")
    os.system(f"{sys.executable} -m pip install markdown2 weasyprint --quiet")
    import markdown2
    from weasyprint import HTML, CSS

def crear_html_con_estilo(contenido_md, titulo="MS-Biblioteca - Informe Técnico"):
    """Convierte Markdown a HTML con estilos profesionales"""
    
    # Convertir Markdown a HTML
    html_body = markdown2.markdown(contenido_md, extras=[
        "tables", 
        "fenced-code-blocks", 
        "code-friendly",
        "header-ids"
    ])
    
    # Plantilla HTML completa con estilos CSS profesionales
    html_template = f"""
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>{titulo}</title>
    <style>
        @page {{
            size: A4;
            margin: 2.5cm 2cm;
            @top-center {{
                content: "{titulo}";
                font-size: 10pt;
                color: #666;
                border-bottom: 1px solid #ddd;
                padding-bottom: 5px;
            }}
            @bottom-center {{
                content: "Página " counter(page) " de " counter(pages);
                font-size: 9pt;
                color: #666;
            }}
        }}
        
        body {{
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            font-size: 11pt;
            line-height: 1.6;
            color: #333;
            max-width: 100%;
        }}
        
        h1 {{
            color: #2c3e50;
            font-size: 28pt;
            border-bottom: 3px solid #3498db;
            padding-bottom: 10px;
            margin-top: 30px;
            margin-bottom: 20px;
            page-break-after: avoid;
        }}
        
        h2 {{
            color: #34495e;
            font-size: 20pt;
            border-bottom: 2px solid #95a5a6;
            padding-bottom: 8px;
            margin-top: 25px;
            margin-bottom: 15px;
            page-break-after: avoid;
        }}
        
        h3 {{
            color: #2980b9;
            font-size: 16pt;
            margin-top: 20px;
            margin-bottom: 12px;
            page-break-after: avoid;
        }}
        
        h4 {{
            color: #555;
            font-size: 13pt;
            margin-top: 15px;
            margin-bottom: 10px;
            page-break-after: avoid;
        }}
        
        p {{
            margin-bottom: 10px;
            text-align: justify;
        }}
        
        code {{
            background-color: #f4f4f4;
            border: 1px solid #ddd;
            border-radius: 3px;
            padding: 2px 6px;
            font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
            font-size: 9.5pt;
            color: #c7254e;
        }}
        
        pre {{
            background-color: #f8f8f8;
            border: 1px solid #ddd;
            border-left: 4px solid #3498db;
            border-radius: 4px;
            padding: 12px;
            overflow-x: auto;
            margin: 15px 0;
            page-break-inside: avoid;
        }}
        
        pre code {{
            background-color: transparent;
            border: none;
            padding: 0;
            color: #333;
            font-size: 9pt;
        }}
        
        table {{
            width: 100%;
            border-collapse: collapse;
            margin: 15px 0;
            page-break-inside: avoid;
            font-size: 10pt;
        }}
        
        th {{
            background-color: #3498db;
            color: white;
            padding: 10px;
            text-align: left;
            font-weight: bold;
        }}
        
        td {{
            border: 1px solid #ddd;
            padding: 8px;
        }}
        
        tr:nth-child(even) {{
            background-color: #f9f9f9;
        }}
        
        ul, ol {{
            margin: 10px 0;
            padding-left: 30px;
        }}
        
        li {{
            margin-bottom: 5px;
        }}
        
        blockquote {{
            border-left: 4px solid #3498db;
            padding-left: 15px;
            margin-left: 0;
            color: #555;
            font-style: italic;
            background-color: #f9f9f9;
            padding: 10px 15px;
        }}
        
        a {{
            color: #3498db;
            text-decoration: none;
        }}
        
        img {{
            max-width: 100%;
            height: auto;
            display: block;
            margin: 15px auto;
        }}
        
        .cover-page {{
            text-align: center;
            padding-top: 150px;
            page-break-after: always;
        }}
        
        .cover-title {{
            font-size: 36pt;
            color: #2c3e50;
            margin-bottom: 20px;
            font-weight: bold;
        }}
        
        .cover-subtitle {{
            font-size: 18pt;
            color: #555;
            margin-bottom: 40px;
        }}
        
        .cover-info {{
            font-size: 12pt;
            color: #777;
            margin-top: 60px;
        }}
        
        hr {{
            border: none;
            border-top: 2px solid #ddd;
            margin: 30px 0;
        }}
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
            <p><strong>Fecha:</strong> Diciembre 12, 2025</p>
        </div>
    </div>
    
    {html_body}
</body>
</html>
"""
    return html_template

def main():
    # Rutas
    readme_path = Path("README.md")
    output_pdf = Path("INFORME_TECNICO_MS-Biblioteca.pdf")
    
    print("📄 Generando informe técnico en PDF...")
    print(f"   Leyendo: {readme_path}")
    
    # Leer README.md
    if not readme_path.exists():
        print(f"❌ Error: No se encontró {readme_path}")
        return 1
    
    with open(readme_path, 'r', encoding='utf-8') as f:
        contenido_md = f.read()
    
    print("   Convirtiendo Markdown a HTML...")
    
    # Crear HTML con estilos
    html_content = crear_html_con_estilo(contenido_md)
    
    print("   Generando PDF con formato profesional...")
    
    # Convertir a PDF
    try:
        HTML(string=html_content).write_pdf(
            output_pdf,
            stylesheets=[CSS(string="""
                @page { margin: 2.5cm 2cm; }
            """)]
        )
        
        print(f"✅ Informe técnico generado exitosamente:")
        print(f"   📁 {output_pdf.absolute()}")
        print(f"   📊 Tamaño: {output_pdf.stat().st_size / 1024:.1f} KB")
        return 0
        
    except Exception as e:
        print(f"❌ Error al generar PDF: {e}")
        return 1

if __name__ == "__main__":
    sys.exit(main())
