import React from 'react';
import {
  Container,
  Grid,
  Card,
  CardContent,
  Typography,
  Box,
  Alert,
  Paper,
} from '@mui/material';
import { People, Book, CheckCircle, HourglassEmpty } from '@mui/icons-material';

export default function Dashboard() {
  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Typography variant="h4" component="h1" sx={{ mb: 4, fontWeight: 'bold' }}>
        📊 Tablero de Control
      </Typography>

      <Alert severity="info" sx={{ mb: 4 }}>
        Bienvenido al sistema de gestión de autores y publicaciones. Usa las pestañas de navegación
        para gestionar autores y crear/modificar publicaciones.
      </Alert>

      <Grid container spacing={3}>
        {/* Card: Autores */}
        <Grid item xs={12} sm={6} md={3}>
          <Card sx={{ background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)', color: 'white' }}>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <Box>
                  <Typography color="inherit" gutterBottom>
                    Autores Registrados
                  </Typography>
                  <Typography variant="h5">📖</Typography>
                </Box>
                <People sx={{ fontSize: 40, opacity: 0.3 }} />
              </Box>
            </CardContent>
          </Card>
        </Grid>

        {/* Card: Publicaciones */}
        <Grid item xs={12} sm={6} md={3}>
          <Card sx={{ background: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)', color: 'white' }}>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <Box>
                  <Typography color="inherit" gutterBottom>
                    Publicaciones Totales
                  </Typography>
                  <Typography variant="h5">📚</Typography>
                </Box>
                <Book sx={{ fontSize: 40, opacity: 0.3 }} />
              </Box>
            </CardContent>
          </Card>
        </Grid>

        {/* Card: Publicadas */}
        <Grid item xs={12} sm={6} md={3}>
          <Card sx={{ background: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)', color: 'white' }}>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <Box>
                  <Typography color="inherit" gutterBottom>
                    Publicadas
                  </Typography>
                  <Typography variant="h5">✅</Typography>
                </Box>
                <CheckCircle sx={{ fontSize: 40, opacity: 0.3 }} />
              </Box>
            </CardContent>
          </Card>
        </Grid>

        {/* Card: En Revisión */}
        <Grid item xs={12} sm={6} md={3}>
          <Card sx={{ background: 'linear-gradient(135deg, #fa709a 0%, #fee140 100%)', color: 'white' }}>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <Box>
                  <Typography color="inherit" gutterBottom>
                    En Revisión
                  </Typography>
                  <Typography variant="h5">⏳</Typography>
                </Box>
                <HourglassEmpty sx={{ fontSize: 40, opacity: 0.3 }} />
              </Box>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      {/* Información */}
      <Paper sx={{ p: 3, mt: 4, backgroundColor: '#f0f7ff' }}>
        <Typography variant="h6" sx={{ mb: 2, fontWeight: 'bold' }}>
          ℹ️ Información del Sistema
        </Typography>
        <Typography variant="body2" paragraph>
          <strong>Arquitecotura:</strong> Microservicios desacoplados con separación de responsabilidades.
        </Typography>
        <Typography variant="body2" paragraph>
          <strong>Servicios:</strong>
        </Typography>
        <Typography variant="body2" sx={{ ml: 2 }} paragraph>
          • <strong>Authors Service:</strong> http://localhost:8080<br />
          • <strong>Publications Service:</strong> http://localhost:8081
        </Typography>
        <Typography variant="body2" paragraph>
          <strong>Estados de Publicación:</strong>
        </Typography>
        <Typography variant="body2" sx={{ ml: 2 }} paragraph>
          • DRAFT (Borrador) → IN_REVIEW (En Revisión) → APPROVED (Aprobado) → PUBLISHED (Publicado)<br />
          • REJECTED (Rechazado) - Estado final alternativo
        </Typography>
        <Typography variant="body2" paragraph>
          <strong>Patrones Implementados:</strong>
        </Typography>
        <Typography variant="body2" sx={{ ml: 2 }} paragraph>
          • Repository Pattern • Adapter Pattern • Factory Pattern • DTO Pattern • Facade Pattern<br />
          • SOLID Principles • Transacciones ACID • Validación de Datos • Manejo de Errores
        </Typography>
      </Paper>

      {/* API Endpoints */}
      <Paper sx={{ p: 3, mt: 4, backgroundColor: '#f5f5f5' }}>
        <Typography variant="h6" sx={{ mb: 2, fontWeight: 'bold' }}>
          🔌 Endpoints Disponibles
        </Typography>
        <Typography variant="body2" component="div" sx={{ fontFamily: 'monospace', fontSize: '0.85rem' }}>
          <strong>Authors Service:</strong><br />
          POST /api/authors - Crear autor<br />
          GET /api/authors - Listar autores<br />
          GET /api/authors/{{'{id}'}} - Obtener autor<br />
          GET /api/authors/{{'{id}'}}/exists - Verificar existencia<br />
          <br />
          <strong>Publications Service:</strong><br />
          POST /api/publications - Crear publicación<br />
          GET /api/publications - Listar publicaciones<br />
          GET /api/publications/{{'{id}'}} - Obtener publicación<br />
          PATCH /api/publications/{{'{id}'}}/status - Cambiar estado
        </Typography>
      </Paper>
    </Container>
  );
}