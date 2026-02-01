import React, { useState } from 'react';
import { Box, Container, AppBar, Toolbar, Button, Typography, Tabs, Tab } from '@mui/material';
import AuthorList from './components/AuthorList';
import PublicationList from './components/PublicationList';
import Dashboard from './components/Dashboard';

export default function App() {
  const [tabValue, setTabValue] = useState(0);

  const handleTabChange = (event, newValue) => {
    setTabValue(newValue);
  };

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
      {/* Header */}
      <AppBar position="static" sx={{ background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' }}>
        <Toolbar>
          <Typography variant="h6" sx={{ flexGrow: 1, fontWeight: 'bold' }}>
            📖 Editorial Digital - Gestión de Autores y Publicaciones
          </Typography>
          <Typography variant="caption" sx={{ color: '#fff' }}>
            v1.0.0
          </Typography>
        </Toolbar>
      </AppBar>

      {/* Navigation Tabs */}
      <Box sx={{ borderBottom: 1, borderColor: 'divider', backgroundColor: '#f5f5f5' }}>
        <Container maxWidth="lg">
          <Tabs value={tabValue} onChange={handleTabChange}>
            <Tab label="📊 Tablero" />
            <Tab label="✍️ Autores" />
            <Tab label="📚 Publicaciones" />
          </Tabs>
        </Container>
      </Box>

      {/* Content */}
      <Box sx={{ flex: 1, backgroundColor: '#fafafa' }}>
        {tabValue === 0 && <Dashboard />}
        {tabValue === 1 && <AuthorList />}
        {tabValue === 2 && <PublicationList />}
      </Box>

      {/* Footer */}
      <Box
        component="footer"
        sx={{
          py: 3,
          px: 2,
          backgroundColor: '#f5f5f5',
          borderTop: '1px solid #ddd',
          textAlign: 'center',
          marginTop: 'auto',
        }}
      >
        <Typography variant="body2" color="textSecondary">
          © 2024 Editorial Digital. Arquitectura de Microservicios.
        </Typography>
      </Box>
    </Box>
  );
}