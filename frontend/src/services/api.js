import axios from 'axios';

// Configurar URLs base desde variables de entorno
const AUTHORS_API = process.env.REACT_APP_AUTHORS_API || 'http://localhost:8080';
const PUBLICATIONS_API = process.env.REACT_APP_PUBLICATIONS_API || 'http://localhost:8081';

// Crear instancias de axios
const authorsClient = axios.create({
  baseURL: `${AUTHORS_API}/api/authors`,
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json',
  },
});

const publicationsClient = axios.create({
  baseURL: `${PUBLICATIONS_API}/api/publications`,
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// ============================================
// AUTORES API
// ============================================

export const authorsAPI = {
  // Crear autor
  createAuthor: (authorData) => authorsClient.post('/', authorData),

  // Obtener autor por ID
  getAuthor: (id) => authorsClient.get(`/${id}`),

  // Listar autores
  listAuthors: (page = 0, size = 10) =>
    authorsClient.get('/', { params: { page, size } }),

  // Verificar si autor existe
  checkAuthorExists: (id) => authorsClient.get(`/${id}/exists`),
};

// ============================================
// PUBLICACIONES API
// ============================================

export const publicationsAPI = {
  // Crear publicación
  createPublication: (publicationData) => publicationsClient.post('/', publicationData),

  // Obtener publicación por ID
  getPublication: (id) => publicationsClient.get(`/${id}`),

  // Listar publicaciones
  listPublications: (page = 0, size = 10, status = null, authorId = null) => {
    const params = { page, size };
    if (status) params.status = status;
    if (authorId) params.authorId = authorId;
    return publicationsClient.get('/', { params });
  },

  // Cambiar estado de publicación
  updateStatus: (id, status) =>
    publicationsClient.patch(`/${id}/status`, { status }),
};

export default {
  authorsAPI,
  publicationsAPI,
};