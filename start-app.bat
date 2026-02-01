@echo off
REM Script de Inicio Completo - Microservicios Editorial (Windows)
REM Verifica puertos, dependencias y ejecuta la aplicación

cls
echo.
echo ╔════════════════════════════════════════════════════════════════╗
echo ║   Inicializador de Microservicios - Editorial Application      ║
echo ║                       (Windows Batch)                          ║
echo ╚════════════════════════════════════════════════════════════════╝
echo.

setlocal enabledelayedexpansion

REM Variables de configuración
set FRONTEND_PORT=3000
set AUTHORS_PORT=8080
set PUBLICATIONS_PORT=8081
set DB_AUTHORS_PORT=5432
set DB_PUBLICATIONS_PORT=3306

REM Función para verificar si Docker está instalado
where docker >nul 2>nul
if errorlevel 1 (
    color 0C
    echo.
    echo [ERROR] Docker no está instalado
    echo.
    echo Por favor instala Docker Desktop desde: https://www.docker.com/products/docker-desktop
    echo.
    pause
    exit /b 1
)

where docker-compose >nul 2>nul
if errorlevel 1 (
    color 0C
    echo.
    echo [ERROR] Docker Compose no está instalado
    echo.
    pause
    exit /b 1
)

color 0A
echo [OK] Docker está instalado
echo [OK] Docker Compose está instalado
echo.

REM Verificar si estamos en el directorio correcto
if not exist "docker-compose.yml" (
    color 0C
    echo.
    echo [ERROR] No se encontró docker-compose.yml
    echo Por favor ejecuta este script desde la raíz del proyecto
    echo.
    pause
    exit /b 1
)

echo [INFO] Creando archivo .env si no existe...
if not exist ".env" (
    (
        echo # Frontend
        echo FRONTEND_PORT=%FRONTEND_PORT%
        echo.
        echo # Base de datos - Autores ^(PostgreSQL^)
        echo DB_AUTHORS_NAME=authors_db
        echo DB_AUTHORS_USER=admin
        echo DB_AUTHORS_PASSWORD=admin123
        echo DB_AUTHORS_PORT=%DB_AUTHORS_PORT%
        echo.
        echo # Base de datos - Publicaciones ^(MySQL^)
        echo DB_PUBLICATIONS_NAME=publications_db
        echo DB_PUBLICATIONS_USER=admin
        echo DB_PUBLICATIONS_PASSWORD=admin123
        echo MYSQL_ROOT_PASSWORD=root123
        echo DB_PUBLICATIONS_PORT=%DB_PUBLICATIONS_PORT%
        echo.
        echo # Microservicios
        echo AUTHORS_SERVICE_PORT=%AUTHORS_PORT%
        echo PUBLICATIONS_SERVICE_PORT=%PUBLICATIONS_PORT%
    ) > .env
    echo [OK] Archivo .env creado
) else (
    echo [OK] Archivo .env ya existe
)

echo.
echo [INFO] Iniciando contenedores Docker...
echo.

docker-compose up -d

if errorlevel 1 (
    color 0C
    echo.
    echo [ERROR] Error al iniciar docker-compose
    echo.
    pause
    exit /b 1
)

color 0A
echo.
echo [OK] Contenedores iniciados
echo.

echo [INFO] Esperando a que los servicios estén listos...
echo.

REM Esperar a PostgreSQL
echo [INFO] Esperando PostgreSQL...
timeout /t 5 /nobreak > nul

REM Esperar a MySQL
echo [INFO] Esperando MySQL...
timeout /t 5 /nobreak > nul

REM Esperar a Authors Service
echo [INFO] Esperando Authors Service...
timeout /t 10 /nobreak > nul

REM Esperar a Publications Service
echo [INFO] Esperando Publications Service...
timeout /t 10 /nobreak > nul

echo.
echo.
echo ╔════════════════════════════════════════════════════════════════╗
echo ║          [OK] Aplicación iniciada correctamente                ║
echo ╚════════════════════════════════════════════════════════════════╝
echo.
echo URLs de acceso:
echo.
echo   Frontend:              http://localhost:%FRONTEND_PORT%
echo   Authors Service:       http://localhost:%AUTHORS_PORT%
echo   Publications Service:  http://localhost:%PUBLICATIONS_PORT%
echo.
echo Bases de datos:
echo.
echo   PostgreSQL ^(Autores^):     localhost:%DB_AUTHORS_PORT%
echo     Usuario: admin / Contraseña: admin123
echo.
echo   MySQL ^(Publicaciones^):   localhost:%DB_PUBLICATIONS_PORT%
echo     Usuario: admin / Contraseña: admin123
echo.
echo Comandos útiles:
echo.
echo   Ver logs:              docker-compose logs -f
echo   Detener:               docker-compose down
echo   Detener y limpiar:     docker-compose down -v
echo.
echo ╚════════════════════════════════════════════════════════════════╝
echo.
pause
