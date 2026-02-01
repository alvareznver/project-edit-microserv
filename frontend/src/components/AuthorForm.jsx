import React, { useState } from 'react';
import {
  Box,
  Button,
  TextField,
  Alert,
  CircularProgress,
  Stack,
} from '@mui/material';
import { authorsAPI } from '../services/api';

export default function AuthorForm({ onSuccess }) {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    bio: '',
    country: '',
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

    // Validaciones básicas
    if (!formData.name.trim() || !formData.email.trim() || !formData.country.trim()) {
      setError('Nombre, Email y País son campos requeridos');
      return;
    }

    setLoading(true);
    try {
      await authorsAPI.createAuthor(formData);
      setSuccess('✅ Autor creado exitosamente');
      setFormData({ name: '', email: '', bio: '', country: '' });
      
      // Llamar callback después de 1 segundo
      setTimeout(() => {
        if (onSuccess) onSuccess();
      }, 1000);
    } catch (err) {
      const message = err.response?.data?.message || err.message;
      setError('❌ Error al crear autor: ' + message);
      console.error('Error:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box component="form" onSubmit={handleSubmit} sx={{ maxWidth: 500 }}>
      <Stack spacing={2}>
        <TextField
          fullWidth
          label="Nombre *"
          name="name"
          value={formData.name}
          onChange={handleChange}
          disabled={loading}
          variant="outlined"
          required
        />
        <TextField
          fullWidth
          label="Email *"
          name="email"
          type="email"
          value={formData.email}
          onChange={handleChange}
          disabled={loading}
          variant="outlined"
          required
        />
        <TextField
          fullWidth
          label="Biografía"
          name="bio"
          value={formData.bio}
          onChange={handleChange}
          disabled={loading}
          variant="outlined"
          multiline
          rows={3}
        />
        <TextField
          fullWidth
          label="País *"
          name="country"
          value={formData.country}
          onChange={handleChange}
          disabled={loading}
          variant="outlined"
          required
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
          {loading ? <CircularProgress size={24} /> : 'Crear Autor'}
        </Button>
      </Stack>
    </Box>
  );
}