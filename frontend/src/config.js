const isDevelopment = window.location.port === '3000' || window.location.port === '5173';
const API_BASE_URL = isDevelopment ? 'http://localhost:8081' : '';

export default API_BASE_URL;
