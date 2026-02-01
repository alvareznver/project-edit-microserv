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
