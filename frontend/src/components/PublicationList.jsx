import React, { useState, useEffect } from 'react';
import {
  Container,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TablePagination,
  TableRow,
  Button,
  Typography,
  Box,
  Alert,
  CircularProgress,
  Chip,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Stack,
} from '@mui/material';
import { publicationsAPI } from '../services/api';
import PublicationForm from './PublicationForm';

const STATUS_COLORS = {
  DRAFT: 'default',
  IN_REVIEW: 'warning',
  APPROVED: 'info',
  PUBLISHED: 'success',
  REJECTED: 'error',
};

const STATUS_LABELS = {
  DRAFT: 'Borrador',
  IN_REVIEW: 'En Revisión',
  APPROVED: 'Aprobado',
  PUBLISHED: 'Publicado',
  REJECTED: 'Rechazado',
};

export default function PublicationList() {
  const [publications, setPublications] = useState([]);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);
  const [totalElements, setTotalElements] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [statusFilter, setStatusFilter] = useState('');
  const [selectedPublication, setSelectedPublication] = useState(null);
  const [showStatusDialog, setShowStatusDialog] = useState(false);
  const [newStatus, setNewStatus] = useState('');
  const [updatingStatus, setUpdatingStatus] = useState(false);

  // Estados permitidos para cada estado actual
  const VALID_TRANSITIONS = {
    DRAFT: ['IN_REVIEW'],
    IN_REVIEW: ['APPROVED', 'REJECTED', 'DRAFT'],
    APPROVED: ['PUBLISHED', 'REJECTED'],
    PUBLISHED: [],
    REJECTED: [],
  };

  // Cargar publicaciones
  useEffect(() => {
    fetchPublications();
  }, [page, rowsPerPage, statusFilter]);

  const fetchPublications = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await publicationsAPI.listPublications(
        page,
        rowsPerPage,
        statusFilter || null
      );
      setPublications(response.data.content || []);
      setTotalElements(response.data.totalElements || 0);
    } catch (err) {
      setError('Error al cargar publicaciones: ' + (err.response?.data?.message || err.message));
    } finally {
      setLoading(false);
    }
  };

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  const handleStatusFilterChange = (e) => {
    setStatusFilter(e.target.value);
    setPage(0);
  };

  const handleOpenStatusDialog = (publication) => {
    setSelectedPublication(publication);
    setNewStatus('');
    setShowStatusDialog(true);
  };

  const handleCloseStatusDialog = () => {
    setShowStatusDialog(false);
    setSelectedPublication(null);
    setNewStatus('');
  };

  const handleUpdateStatus = async () => {
    if (!newStatus) {
      setError('Selecciona un nuevo estado');
      return;
    }

    setUpdatingStatus(true);
    try {
      await publicationsAPI.updateStatus(selectedPublication.id, newStatus);
      fetchPublications();
      handleCloseStatusDialog();
      setError(null);
    } catch (err) {
      setError('Error al actualizar estado: ' + (err.response?.data?.message || err.message));
    } finally {
      setUpdatingStatus(false);
    }
  };

  const handlePublicationCreated = () => {
    setShowForm(false);
    setPage(0);
    fetchPublications();
  };

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Typography variant="h4" component="h1">
          📚 Gestión de Publicaciones
        </Typography>
        <Button variant="contained" color="primary" onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Cancelar' : '+ Nueva Publicación'}
        </Button>
      </Box>

      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

      {showForm && (
        <Paper sx={{ p: 3, mb: 3 }}>
          <PublicationForm onSuccess={handlePublicationCreated} />
        </Paper>
      )}

      {/* Filtro por estado */}
      <Box sx={{ mb: 3 }}>
        <FormControl sx={{ minWidth: 200 }}>
          <InputLabel>Filtrar por Estado</InputLabel>
          <Select value={statusFilter} onChange={handleStatusFilterChange} label="Filtrar por Estado">
            <MenuItem value="">Todos</MenuItem>
            <MenuItem value="DRAFT">Borrador</MenuItem>
            <MenuItem value="IN_REVIEW">En Revisión</MenuItem>
            <MenuItem value="APPROVED">Aprobado</MenuItem>
            <MenuItem value="PUBLISHED">Publicado</MenuItem>
            <MenuItem value="REJECTED">Rechazado</MenuItem>
          </Select>
        </FormControl>
      </Box>

      {loading ? (
        <Box sx={{ display: 'flex', justifyContent: 'center', py: 4 }}>
          <CircularProgress />
        </Box>
      ) : publications.length === 0 ? (
        <Alert severity="info">No hay publicaciones registradas</Alert>
      ) : (
        <TableContainer component={Paper}>
          <Table>
            <TableHead sx={{ backgroundColor: '#f5f5f5' }}>
              <TableRow>
                <TableCell sx={{ fontWeight: 'bold' }}>ID</TableCell>
                <TableCell sx={{ fontWeight: 'bold' }}>Título</TableCell>
                <TableCell sx={{ fontWeight: 'bold' }}>Estado</TableCell>
                <TableCell sx={{ fontWeight: 'bold' }}>Autor ID</TableCell>
                <TableCell sx={{ fontWeight: 'bold' }}>Categoría</TableCell>
                <TableCell sx={{ fontWeight: 'bold' }} align="center">
                  Acciones
                </TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {publications.map((pub) => (
                <TableRow key={pub.id} hover>
                  <TableCell>{pub.id}</TableCell>
                  <TableCell>{pub.title}</TableCell>
                  <TableCell>
                    <Chip
                      label={STATUS_LABELS[pub.status] || pub.status}
                      color={STATUS_COLORS[pub.status] || 'default'}
                      size="small"
                    />
                  </TableCell>
                  <TableCell>{pub.authorId}</TableCell>
                  <TableCell>{pub.category || '-'}</TableCell>
                  <TableCell align="center">
                    <Button
                      size="small"
                      variant="outlined"
                      onClick={() => handleOpenStatusDialog(pub)}
                      disabled={VALID_TRANSITIONS[pub.status]?.length === 0}
                    >
                      Cambiar Estado
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
          <TablePagination
            rowsPerPageOptions={[5, 10, 25]}
            component="div"
            count={totalElements}
            rowsPerPage={rowsPerPage}
            page={page}
            onPageChange={handleChangePage}
            onRowsPerPageChange={handleChangeRowsPerPage}
          />
        </TableContainer>
      )}

      {/* Diálogo para cambiar estado */}
      <Dialog open={showStatusDialog} onClose={handleCloseStatusDialog} maxWidth="sm" fullWidth>
        <DialogTitle>Cambiar Estado de Publicación</DialogTitle>
        <DialogContent sx={{ pt: 3 }}>
          <Stack spacing={2}>
            {selectedPublication && (
              <>
                <Typography variant="body2" color="textSecondary">
                  <strong>Publicación:</strong> {selectedPublication.title}
                </Typography>
                <Typography variant="body2" color="textSecondary">
                  <strong>Estado actual:</strong> {STATUS_LABELS[selectedPublication.status]}
                </Typography>
              </>
            )}
            <FormControl fullWidth>
              <InputLabel>Nuevo Estado</InputLabel>
              <Select
                value={newStatus}
                onChange={(e) => setNewStatus(e.target.value)}
                label="Nuevo Estado"
                disabled={updatingStatus}
              >
                {selectedPublication &&
                  VALID_TRANSITIONS[selectedPublication.status]?.map((status) => (
                    <MenuItem key={status} value={status}>
                      {STATUS_LABELS[status]}
                    </MenuItem>
                  ))}
              </Select>
            </FormControl>
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseStatusDialog} disabled={updatingStatus}>
            Cancelar
          </Button>
          <Button onClick={handleUpdateStatus} variant="contained" disabled={updatingStatus || !newStatus}>
            {updatingStatus ? <CircularProgress size={24} /> : 'Actualizar'}
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
}