import React, { useState } from 'react';
import {
  Box,
  Button,
  TextField,
  Alert,
  CircularProgress,
  Stack,
} from '@mui/material';
import { publicationsAPI } from '../services/api';

export default function PublicationForm({ onSuccess }) {
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    content: '',
    authorId: '',
    category: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setSuccess(null);

    // Validaciones
    if (!formData.title.trim() || !formData.authorId || !formData.content.trim()) {
      setError('Título, Autor y Contenido son campos requeridos');
      return;
    }

    const authorId = parseInt(formData.authorId, 10);
    if (isNaN(authorId) || authorId <= 0) {
      setError('ID de Autor debe ser un número válido');
      return;
    }

    setLoading(true);
    try {
      await publicationsAPI.createPublication({
        ...formData,
        authorId: authorId,
      });
      setSuccess('✅ Publicación creada exitosamente');
      setFormData({
        title: '',
        description: '',
        content: '',
        authorId: '',
        category: '',
      });

      // Llamar callback después de 1 segundo
      setTimeout(() => {
        if (onSuccess) onSuccess();
      }, 1000);
    } catch (err) {
      const message = err.response?.data?.message || err.message;
      setError('❌ Error al crear publicación: ' + message);
      console.error('Error:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box component="form" onSubmit={handleSubmit} sx={{ maxWidth: 700 }}>
      <Stack spacing={2}>
        <TextField
          fullWidth
          label="Título *"
          name="title"
          value={formData.title}
          onChange={handleChange}
          disabled={loading}
          variant="outlined"
          required
        />

        <TextField
          fullWidth
          label="Descripción"
          name="description"
          value={formData.description}
          onChange={handleChange}
          disabled={loading}
          variant="outlined"
          multiline
          rows={2}
        />

        <TextField
          fullWidth
          label="Contenido *"
          name="content"
          value={formData.content}
          onChange={handleChange}
          disabled={loading}
          variant="outlined"
          multiline
          rows={4}
          required
        />

        <TextField
          fullWidth
          label="ID del Autor *"
          name="authorId"
          type="number"
          value={formData.authorId}
          onChange={handleChange}
          disabled={loading}
          variant="outlined"
          required
          helperText="Ingresa el ID del autor registrado"
        />

        <TextField
          fullWidth
          label="Categoría"
          name="category"
          value={formData.category}
          onChange={handleChange}
          disabled={loading}
          variant="outlined"
        />

        {error && <Alert severity="error">{error}</Alert>}
        {success && <Alert severity="success">{success}</Alert>}

        <Button
          type="submit"
          variant="contained"
          color="primary"
          disabled={loading}
          sx={{ py: 1.5 }}
        >
          {loading ? <CircularProgress size={24} /> : 'Crear Publicación'}
        </Button>
      </Stack>
    </Box>
  );
}