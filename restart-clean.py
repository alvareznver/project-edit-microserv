#!/usr/bin/env python3
"""
Script para Liberar Puertos Específicos y Reiniciar Docker
Mata procesos usando los puertos 5432 y 8080, luego reinicia Docker
"""

import os
import sys
import subprocess
import socket
import time
import platform
from pathlib import Path

class PortKiller:
    """Clase para matar procesos en puertos específicos"""
    
    def __init__(self):
        self.RED = '\033[91m'
        self.GREEN = '\033[92m'
        self.YELLOW = '\033[93m'
        self.BLUE = '\033[94m'
        self.CYAN = '\033[96m'
        self.RESET = '\033[0m'
        self.BOLD = '\033[1m'
        self.os_type = platform.system()
    
    def print_header(self):
        """Imprime encabezado"""
        print(f"\n{self.BOLD}╔════════════════════════════════════════════════════════════════╗{self.RESET}")
        print(f"{self.BOLD}║     Liberador de Puertos - Editorial Microservices            ║{self.RESET}")
        print(f"{self.BOLD}║         (Mata procesos y reinicia Docker)                     ║{self.RESET}")
        print(f"{self.BOLD}╚════════════════════════════════════════════════════════════════╝{self.RESET}\n")
    
    def check_port(self, port: int) -> bool:
        """Verifica si un puerto está en uso"""
        try:
            sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            sock.settimeout(1)
            result = sock.connect_ex(('127.0.0.1', port))
            sock.close()
            return result == 0
        except Exception:
            return False
    
    def kill_port_windows(self, port: int) -> bool:
        """Mata el proceso usando un puerto en Windows"""
        try:
            print(f"\n{self.BLUE}Intentando liberar puerto {port} en Windows...{self.RESET}")
            
            # Encontrar PID
            result = subprocess.run(
                f'netstat -ano | findstr :{port}',
                shell=True,
                capture_output=True,
                text=True
            )
            
            if not result.stdout:
                print(f"  {self.YELLOW}⚠ No se encontró proceso en puerto {port}{self.RESET}")
                return True
            
            # Extraer PID (última columna)
            lines = result.stdout.strip().split('\n')
            for line in lines:
                parts = line.split()
                if parts:
                    pid = parts[-1]
                    print(f"  Encontrado PID: {pid}")
                    
                    # Matar proceso
                    kill_result = subprocess.run(
                        f'taskkill /PID {pid} /F',
                        shell=True,
                        capture_output=True,
                        text=True
                    )
                    
                    if kill_result.returncode == 0:
                        print(f"  {self.GREEN}✓ Proceso {pid} terminado{self.RESET}")
                    else:
                        print(f"  {self.RED}✗ Error al terminar proceso {pid}{self.RESET}")
                        return False
            
            return True
        except Exception as e:
            print(f"  {self.RED}✗ Error: {e}{self.RESET}")
            return False
    
    def kill_port_unix(self, port: int) -> bool:
        """Mata el proceso usando un puerto en Linux/Mac"""
        try:
            print(f"\n{self.BLUE}Intentando liberar puerto {port} en {self.os_type}...{self.RESET}")
            
            # Encontrar PID con lsof
            result = subprocess.run(
                f'lsof -t -i :{port}',
                shell=True,
                capture_output=True,
                text=True
            )
            
            if not result.stdout.strip():
                print(f"  {self.YELLOW}⚠ No se encontró proceso en puerto {port}{self.RESET}")
                return True
            
            pid = result.stdout.strip().split('\n')[0]
            print(f"  Encontrado PID: {pid}")
            
            # Matar proceso
            kill_result = subprocess.run(
                f'kill -9 {pid}',
                shell=True,
                capture_output=True,
                text=True
            )
            
            if kill_result.returncode == 0:
                print(f"  {self.GREEN}✓ Proceso {pid} terminado{self.RESET}")
                return True
            else:
                print(f"  {self.RED}✗ Error al terminar proceso{self.RESET}")
                return False
        
        except Exception as e:
            print(f"  {self.RED}✗ Error: {e}{self.RESET}")
            return False
    
    def kill_port(self, port: int) -> bool:
        """Mata el proceso usando un puerto (detecta SO)"""
        if self.os_type == 'Windows':
            return self.kill_port_windows(port)
        else:
            return self.kill_port_unix(port)
    
    def stop_docker_compose(self) -> bool:
        """Detiene Docker Compose"""
        print(f"\n{self.BLUE}Deteniendo Docker Compose...{self.RESET}")
        try:
            result = subprocess.run(
                'docker-compose down',
                shell=True,
                capture_output=True,
                text=True,
                timeout=30
            )
            if result.returncode == 0:
                print(f"  {self.GREEN}✓ Docker Compose detenido{self.RESET}")
                return True
            else:
                print(f"  {self.RED}✗ Error al detener Docker Compose{self.RESET}")
                print(f"    {result.stderr[:200]}")
                return False
        except subprocess.TimeoutExpired:
            print(f"  {self.YELLOW}⚠ Docker Compose tardó demasiado en detener{self.RESET}")
            return False
        except Exception as e:
            print(f"  {self.RED}✗ Error: {e}{self.RESET}")
            return False
    
    def clean_docker(self) -> bool:
        """Limpia imágenes y contenedores de Docker"""
        print(f"\n{self.BLUE}Limpiando Docker (esto puede tardar)...{self.RESET}")
        try:
            result = subprocess.run(
                'docker system prune -a -f',
                shell=True,
                capture_output=True,
                text=True,
                timeout=120
            )
            if result.returncode == 0:
                print(f"  {self.GREEN}✓ Docker limpiado{self.RESET}")
                return True
            else:
                print(f"  {self.YELLOW}⚠ Advertencia al limpiar Docker{self.RESET}")
                return True  # No fallar si hay advertencias
        except subprocess.TimeoutExpired:
            print(f"  {self.YELLOW}⚠ Limpieza de Docker tardó demasiado{self.RESET}")
            return True
        except Exception as e:
            print(f"  {self.RED}✗ Error: {e}{self.RESET}")
            return False
    
    def start_docker_compose(self) -> bool:
        """Inicia Docker Compose"""
        print(f"\n{self.BLUE}Iniciando Docker Compose...{self.RESET}")
        try:
            result = subprocess.run(
                'docker-compose up -d',
                shell=True,
                capture_output=True,
                text=True,
                timeout=120
            )
            if result.returncode == 0:
                print(f"  {self.GREEN}✓ Docker Compose iniciado{self.RESET}")
                return True
            else:
                print(f"  {self.RED}✗ Error al iniciar Docker Compose{self.RESET}")
                print(f"    {result.stderr[:500]}")
                return False
        except subprocess.TimeoutExpired:
            print(f"  {self.YELLOW}⚠ Docker Compose tardó demasiado en iniciar{self.RESET}")
            return True
        except Exception as e:
            print(f"  {self.RED}✗ Error: {e}{self.RESET}")
            return False
    
    def verify_ports(self) -> bool:
        """Verifica que los puertos estén libres"""
        print(f"\n{self.BLUE}Verificando puertos...{self.RESET}")
        
        ports_to_check = [5432, 8080]
        all_free = True
        
        for port in ports_to_check:
            if self.check_port(port):
                print(f"  {self.RED}✗ Puerto {port} aún está ocupado{self.RESET}")
                all_free = False
            else:
                print(f"  {self.GREEN}✓ Puerto {port} está libre{self.RESET}")
        
        return all_free
    
    def show_summary(self):
        """Muestra resumen final"""
        print(f"\n{self.BOLD}╔════════════════════════════════════════════════════════════════╗{self.RESET}")
        print(f"{self.BOLD}║                     ✓ Proceso Completado                       ║{self.RESET}")
        print(f"{self.BOLD}╚════════════════════════════════════════════════════════════════╝{self.RESET}\n")
        
        print(f"{self.GREEN}Próximos pasos:{self.RESET}\n")
        print("  1. Espera 30-60 segundos para que los servicios inicien")
        print("  2. Verifica los logs:")
        print(f"     {self.CYAN}docker-compose logs -f{self.RESET}")
        print("  3. Abre en el navegador:")
        print(f"     {self.CYAN}http://localhost:3000{self.RESET}")
        
        print(f"\n{self.CYAN}Recursos:{self.RESET}\n")
        print("  Frontend:              http://localhost:3000")
        print("  Authors Service:       http://localhost:8080")
        print("  Publications Service:  http://localhost:8081")
        
        print(f"\n{self.CYAN}Comandos útiles:{self.RESET}\n")
        print("  docker-compose logs -f       # Ver logs en tiempo real")
        print("  docker-compose restart       # Reiniciar servicios")
        print("  docker-compose down          # Detener todo")
        
        print(f"\n{self.BOLD}╚════════════════════════════════════════════════════════════════╝{self.RESET}\n")
    
    def run(self):
        """Ejecuta el proceso completo"""
        try:
            self.print_header()
            
            # Paso 1: Detener Docker Compose
            print(f"{self.BOLD}PASO 1/5: Detener Docker Compose{self.RESET}")
            if not self.stop_docker_compose():
                print(f"{self.YELLOW}⚠ Continuando de todas formas...{self.RESET}")
            
            time.sleep(2)
            
            # Paso 2: Matar procesos en puertos
            print(f"\n{self.BOLD}PASO 2/5: Liberar Puertos{self.RESET}")
            ports = [5432, 8080]
            for port in ports:
                if self.check_port(port):
                    if not self.kill_port(port):
                        print(f"{self.YELLOW}⚠ No se pudo liberar puerto {port}, continuando...{self.RESET}")
                    time.sleep(1)
            
            # Paso 3: Verificar puertos
            print(f"\n{self.BOLD}PASO 3/5: Verificar que puertos estén libres{self.RESET}")
            if not self.verify_ports():
                print(f"{self.YELLOW}⚠ Algunos puertos aún están ocupados, pero continuando...{self.RESET}")
            
            time.sleep(2)
            
            # Paso 4: Limpiar Docker
            print(f"\n{self.BOLD}PASO 4/5: Limpiar Docker{self.RESET}")
            self.clean_docker()
            
            # Paso 5: Iniciar Docker Compose
            print(f"\n{self.BOLD}PASO 5/5: Iniciar Docker Compose{self.RESET}")
            if not self.start_docker_compose():
                print(f"{self.RED}✗ Error al iniciar Docker Compose{self.RESET}")
                return False
            
            time.sleep(5)
            
            # Resumen
            self.show_summary()
            return True
        
        except KeyboardInterrupt:
            print(f"\n{self.YELLOW}Abortado por el usuario{self.RESET}")
            return False
        except Exception as e:
            print(f"\n{self.RED}Error: {e}{self.RESET}")
            return False


def main():
    """Función principal"""
    killer = PortKiller()
    success = killer.run()
    sys.exit(0 if success else 1)


if __name__ == "__main__":
    main()
