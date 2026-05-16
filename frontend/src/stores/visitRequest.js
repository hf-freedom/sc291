import { defineStore } from 'pinia'
import { ref } from 'vue'
import api from '@/services/api'

export const useVisitRequestStore = defineStore('visitRequest', () => {
  const requests = ref([])
  const currentRequest = ref(null)
  const loading = ref(false)

  const fetchRequests = async (params) => {
    loading.value = true
    try {
      const response = await api.visitRequests.getAll(params)
      requests.value = response.data
      return response.data
    } finally {
      loading.value = false
    }
  }

  const fetchRequestsByVisitor = async (visitorId) => {
    loading.value = true
    try {
      const response = await api.visitRequests.getAll({ visitorId })
      requests.value = response.data
      return response.data
    } finally {
      loading.value = false
    }
  }

  const fetchRequestsByHost = async (hostId) => {
    loading.value = true
    try {
      const response = await api.visitRequests.getAll({ hostId })
      requests.value = response.data
      return response.data
    } finally {
      loading.value = false
    }
  }

  const getRequestById = async (id) => {
    loading.value = true
    try {
      const response = await api.visitRequests.getById(id)
      currentRequest.value = response.data
      return response.data
    } finally {
      loading.value = false
    }
  }

  const createRequest = async (data) => {
    loading.value = true
    try {
      const response = await api.visitRequests.create(data)
      requests.value.push(response.data)
      return response.data
    } finally {
      loading.value = false
    }
  }

  const approveRequest = async (id, approved, comment) => {
    loading.value = true
    try {
      const response = await api.visitRequests.approve(id, { approved, comment })
      const index = requests.value.findIndex(r => r.id === id)
      if (index !== -1) {
        requests.value[index] = response.data
      }
      return response.data
    } finally {
      loading.value = false
    }
  }

  const securityApproveRequest = async (id, approved, comment) => {
    loading.value = true
    try {
      const response = await api.visitRequests.securityApprove(id, { approved, comment })
      const index = requests.value.findIndex(r => r.id === id)
      if (index !== -1) {
        requests.value[index] = response.data
      }
      return response.data
    } finally {
      loading.value = false
    }
  }

  const cancelRequest = async (id) => {
    loading.value = true
    try {
      const response = await api.visitRequests.cancel(id)
      const index = requests.value.findIndex(r => r.id === id)
      if (index !== -1) {
        requests.value[index] = response.data
      }
      return response.data
    } finally {
      loading.value = false
    }
  }

  const fetchPendingSecurityApprovals = async () => {
    loading.value = true
    try {
      const response = await api.visitRequests.getPendingSecurity()
      requests.value = response.data
      return response.data
    } finally {
      loading.value = false
    }
  }

  return {
    requests,
    currentRequest,
    loading,
    fetchRequests,
    fetchRequestsByVisitor,
    fetchRequestsByHost,
    getRequestById,
    createRequest,
    approveRequest,
    securityApproveRequest,
    cancelRequest,
    fetchPendingSecurityApprovals
  }
})
