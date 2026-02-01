#!/usr/bin/env pwsh
# Script para Editorial Digital - Crea archivos faltantes y levanta Docker
# Uso: ./setup.ps1

Write-Host "╔════════════════════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║         Editorial Digital - Setup Automático para Windows                 ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
Write-Host ""

# Verificar que estamos en la carpeta correcta
$requiredFiles = @("docker-compose.yml", "authors-service", "publications-service", "frontend")
$missingFiles = @()

foreach ($file in $requiredFiles) {
    if (-not (Test-Path $file)) {
        $missingFiles += $file
    }
}

if ($missingFiles.Count -gt 0) {
    Write-Host "❌ Error: Falta archivos requeridos:" -ForegroundColor Red
    foreach ($file in $missingFiles) {
        Write-Host "   - $file" -ForegroundColor Red
    }
    Write-Host ""
    Write-Host "⚠️  Ejecuta este script desde la raíz del proyecto (donde está docker-compose.yml)" -ForegroundColor Yellow
    exit 1
}

Write-Host "✓ Detectado proyecto Editorial Digital" -ForegroundColor Green
Write-Host ""

# Función para crear archivo con contenido
function Create-FileIfNotExists {
    param(
        [string]$FilePath,
        [string]$Content
    )
    
    $directory = Split-Path -Parent $FilePath
    
    if (-not (Test-Path $directory)) {
        New-Item -ItemType Directory -Path $directory -Force | Out-Null
        Write-Host "  📁 Creado directorio: $directory" -ForegroundColor Green
    }
    
    if (Test-Path $FilePath) {
        Write-Host "  ⚠️  Ya existe: $FilePath" -ForegroundColor Yellow
    }
    else {
        Set-Content -Path $FilePath -Value $Content -Encoding UTF8
        Write-Host "  ✓ Creado: $FilePath" -ForegroundColor Green
    }
}

# ============================================================================
# CREAR ARCHIVOS DEL FRONTEND
# ============================================================================

Write-Host "📁 Creando archivos del Frontend..." -ForegroundColor Cyan
Write-Host ""

# 1. Dockerfile para Frontend
$dockerfileContent = @'
# Build stage - Compilar la aplicación React
FROM node:18-alpine AS builder

WORKDIR /app

# Copiar package.json y package-lock.json
COPY package*.json ./

# Instalar dependencias
RUN npm install

# Copiar código fuente
COPY src ./src
COPY vite.config.js ./
COPY index.html ./
COPY .gitignore ./

# Compilar la aplicación
RUN npm run build

# Runtime stage - Servir la aplicación compilada
FROM node:18-alpine

WORKDIR /app

# Instalar servidor HTTP simple (serve)
RUN npm install -g serve

# Copiar archivos compilados desde builder
COPY --from=builder /app/dist ./dist

# Exponer puerto
EXPOSE 3000

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:3000 || exit 1

# Servir la aplicación
CMD ["serve", "-s", "dist", "-l", "3000"]
'@

Create-FileIfNotExists "frontend/Dockerfile" $dockerfileContent

# 2. package.json para Frontend
$packageJsonContent = @'
{
  "name": "editorial-frontend",
  "version": "1.0.0",
  "type": "module",
  "description": "Frontend para Editorial Digital - Gestión de Autores y Publicaciones",
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview",
    "start": "vite preview --host"
  },
  "dependencies": {
    "react": "^18.2.0",
    "react-dom": "^18.2.0",
    "@mui/material": "^5.14.0",
    "@emotion/react": "^11.11.0",
    "@emotion/styled": "^11.11.0",
    "axios": "^1.6.0",
    "react-router-dom": "^6.16.0"
  },
  "devDependencies": {
    "@types/react": "^18.2.0",
    "@types/react-dom": "^18.2.0",
    "@vitejs/plugin-react": "^4.1.0",
    "vite": "^5.0.0"
  }
}
'@

Create-FileIfNotExists "frontend/package.json" $packageJsonContent

# 3. vite.config.js
$viteConfigContent = @'
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    host: '0.0.0.0',
    port: 3000,
    watch: {
      usePolling: true,
    }
  },
  preview: {
    host: '0.0.0.0',
    port: 3000
  },
  build: {
    outDir: 'dist',
    sourcemap: false
  }
})
'@

Create-FileIfNotExists "frontend/vite.config.js" $viteConfigContent

# 4. .gitignore
$gitignoreContent = @'
# Logs
logs
*.log
npm-debug.log*
yarn-debug.log*
yarn-error.log*
pnpm-debug.log*
lerna-debug.log*

node_modules
dist
dist-ssr
*.local

# Editor directories and files
.vscode/*
!.vscode/extensions.json
.idea
.DS_Store
*.suo
*.ntvs*
*.njsproj
*.sln
*.sw?

# Environment variables
.env
.env.local
.env.*.local
'@

Create-FileIfNotExists "frontend/.gitignore" $gitignoreContent

# 5. src/main.jsx
$mainJsxContent = @'
import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import './index.css'

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
)
'@

Create-FileIfNotExists "frontend/src/main.jsx" $mainJsxContent

# 6. src/index.css
$indexCssContent = @'
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen',
    'Ubuntu', 'Cantarell', 'Fira Sans', 'Droid Sans', 'Helvetica Neue',
    sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  background-color: #fafafa;
}

code {
  font-family: source-code-pro, Menlo, Monaco, Consolas, 'Courier New',
    monospace;
}

html, body, #root {
  height: 100%;
}
'@

Create-FileIfNotExists "frontend/src/index.css" $indexCssContent

Write-Host ""
Write-Host "✓ Archivos del Frontend creados exitosamente" -ForegroundColor Green
Write-Host ""

# ============================================================================
# CREAR ARCHIVO .env (opcional)
# ============================================================================

Write-Host "📝 Verificando archivo .env..." -ForegroundColor Cyan
Write-Host ""

if (-not (Test-Path ".env")) {
    $envContent = @'
# Base de datos Authors (PostgreSQL)
DB_AUTHORS_NAME=authors_db
DB_AUTHORS_USER=admin
DB_AUTHORS_PASSWORD=admin123
DB_AUTHORS_PORT=5432

# Base de datos Publications (MySQL)
DB_PUBLICATIONS_NAME=publications_db
DB_PUBLICATIONS_USER=admin
DB_PUBLICATIONS_PASSWORD=admin123
MYSQL_ROOT_PASSWORD=root123
DB_PUBLICATIONS_PORT=3306

# Servicios
AUTHORS_SERVICE_PORT=8080
PUBLICATIONS_SERVICE_PORT=8081
FRONTEND_PORT=3000

# URLs internas (Docker network)
AUTHORS_SERVICE_URL=http://authors-service:8080
PUBLICATIONS_SERVICE_URL=http://publications-service:8081
'@
    
    Set-Content -Path ".env" -Value $envContent -Encoding UTF8
    Write-Host "  ✓ Creado: .env" -ForegroundColor Green
}
else {
    Write-Host "  ✓ Ya existe: .env" -ForegroundColor Yellow
}

Write-Host ""

# ============================================================================
# VERIFICAR DOCKER
# ============================================================================

Write-Host "🐳 Verificando Docker..." -ForegroundColor Cyan
Write-Host ""

try {
    $dockerVersion = docker --version
    Write-Host "  ✓ Docker encontrado: $dockerVersion" -ForegroundColor Green
}
catch {
    Write-Host "  ❌ Docker no está instalado o no está en el PATH" -ForegroundColor Red
    Write-Host "  ⚠️  Descargalo desde: https://www.docker.com/products/docker-desktop" -ForegroundColor Yellow
    exit 1
}

try {
    $composeVersion = docker compose version
    Write-Host "  ✓ Docker Compose encontrado" -ForegroundColor Green
}
catch {
    Write-Host "  ❌ Docker Compose no está disponible" -ForegroundColor Red
    exit 1
}

Write-Host ""

# ============================================================================
# OPCIÓN DE LEVANTAR DOCKER
# ============================================================================

Write-Host "🚀 ¿Deseas levantar los servicios ahora?" -ForegroundColor Yellow
Write-Host ""
Write-Host "  1. Sí, levantar todo (docker compose up --build)" -ForegroundColor Green
Write-Host "  2. No, solo crear archivos" -ForegroundColor Yellow
Write-Host "  3. Salir" -ForegroundColor Red
Write-Host ""

$choice = Read-Host "Selecciona una opción (1, 2 o 3)"

switch ($choice) {
    "1" {
        Write-Host ""
        Write-Host "╔════════════════════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
        Write-Host "║                    Levantando servicios Docker                             ║" -ForegroundColor Cyan
        Write-Host "╚════════════════════════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
        Write-Host ""
        
        Write-Host "Limpiando servicios anteriores..." -ForegroundColor Yellow
        docker compose down -v
        
        Write-Host ""
        Write-Host "Construyendo e iniciando servicios..." -ForegroundColor Yellow
        Write-Host "(Esto puede tomar 5-15 minutos la primera vez)" -ForegroundColor Gray
        Write-Host ""
        
        docker compose up --build
        
        Write-Host ""
        Write-Host "╔════════════════════════════════════════════════════════════════════════════╗" -ForegroundColor Green
        Write-Host "║                    ✓ Servicios levantados exitosamente                    ║" -ForegroundColor Green
        Write-Host "╚════════════════════════════════════════════════════════════════════════════╝" -ForegroundColor Green
        Write-Host ""
        Write-Host "Acceso a las aplicaciones:" -ForegroundColor Green
        Write-Host "  🌐 Frontend:        http://localhost:3000" -ForegroundColor Cyan
        Write-Host "  📚 Authors API:     http://localhost:8080/api/authors" -ForegroundColor Cyan
        Write-Host "  📚 Publications:    http://localhost:8081/api/publications" -ForegroundColor Cyan
        Write-Host ""
    }
    "2" {
        Write-Host ""
        Write-Host "✓ Archivos creados exitosamente" -ForegroundColor Green
        Write-Host ""
        Write-Host "Próximos pasos:" -ForegroundColor Yellow
        Write-Host ""
        Write-Host "1. Ejecuta en PowerShell/CMD:" -ForegroundColor Yellow
        Write-Host "   docker compose up --build" -ForegroundColor Cyan
        Write-Host ""
        Write-Host "2. Espera a que los servicios estén 'healthy'" -ForegroundColor Yellow
        Write-Host ""
        Write-Host "3. Abre en tu navegador:" -ForegroundColor Yellow
        Write-Host "   http://localhost:3000" -ForegroundColor Cyan
        Write-Host ""
    }
    "3" {
        Write-Host ""
        Write-Host "✓ Script cancelado" -ForegroundColor Yellow
        exit 0
    }
    default {
        Write-Host ""
        Write-Host "❌ Opción inválida" -ForegroundColor Red
        exit 1
    }
}
