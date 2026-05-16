import axios from 'axios'
import { ElMessage } from 'element-plus'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8004/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

api.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    return res
  },
  error => {
    ElMessage.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)

export default {
  visitors: {
    create: (data) => api.post('/visitors', data),
    getAll: () => api.get('/visitors'),
    getById: (id) => api.get(`/visitors/${id}`)
  },
  
  visitRequests: {
    create: (data) => api.post('/visit-requests', data),
    getAll: (params) => api.get('/visit-requests', { params }),
    getById: (id) => api.get(`/visit-requests/${id}`),
    approve: (id, data) => api.post(`/visit-requests/${id}/approve`, data),
    securityApprove: (id, data) => api.post(`/visit-requests/${id}/security-approve`, data),
    cancel: (id) => api.post(`/visit-requests/${id}/cancel`),
    getPendingSecurity: () => api.get('/visit-requests/pending-security')
  },
  
  permissions: {
    getAll: () => api.get('/permissions'),
    getById: (id) => api.get(`/permissions/${id}`),
    getByCode: (code) => api.get(`/permissions/code/${code}`),
    checkIn: (id) => api.post(`/permissions/${id}/check-in`),
    checkOut: (id) => api.post(`/permissions/${id}/check-out`)
  },
  
  areas: {
    create: (data) => api.post('/areas', data),
    getAll: () => api.get('/areas'),
    getById: (id) => api.get(`/areas/${id}`),
    update: (id, data) => api.put(`/areas/${id}`, data),
    delete: (id) => api.delete(`/areas/${id}`)
  },
  
  riskRecords: {
    getAll: (params) => api.get('/risk-records', { params }),
    getById: (id) => api.get(`/risk-records/${id}`)
  },
  
  blacklist: {
    add: (data) => api.post('/blacklist', data),
    remove: (visitorId) => api.delete(`/blacklist/${visitorId}`),
    getAll: () => api.get('/blacklist'),
    getByVisitor: (visitorId) => api.get(`/blacklist/${visitorId}`)
  },
  
  hosts: {
    getAll: () => api.get('/hosts'),
    getById: (id) => api.get(`/hosts/${id}`)
  },
  
  securityGuards: {
    getAll: () => api.get('/security-guards'),
    getById: (id) => api.get(`/security-guards/${id}`)
  },
  
  statistics: {
    get: () => api.get('/statistics')
  }
}
