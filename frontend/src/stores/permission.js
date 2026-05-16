import { defineStore } from 'pinia'
import { ref } from 'vue'
import api from '@/services/api'

export const usePermissionStore = defineStore('permission', () => {
  const permissions = ref([])
  const currentPermission = ref(null)
  const loading = ref(false)

  const fetchPermissions = async () => {
    loading.value = true
    try {
      const response = await api.permissions.getAll()
      permissions.value = response.data
      return response.data
    } finally {
      loading.value = false
    }
  }

  const getPermissionById = async (id) => {
    loading.value = true
    try {
      const response = await api.permissions.getById(id)
      currentPermission.value = response.data
      return response.data
    } finally {
      loading.value = false
    }
  }

  const getPermissionByCode = async (code) => {
    loading.value = true
    try {
      const response = await api.permissions.getByCode(code)
      currentPermission.value = response.data
      return response.data
    } finally {
      loading.value = false
    }
  }

  const checkIn = async (id) => {
    loading.value = true
    try {
      const response = await api.permissions.checkIn(id)
      return response.data
    } finally {
      loading.value = false
    }
  }

  const checkOut = async (id) => {
    loading.value = true
    try {
      const response = await api.permissions.checkOut(id)
      return response.data
    } finally {
      loading.value = false
    }
  }

  return {
    permissions,
    currentPermission,
    loading,
    fetchPermissions,
    getPermissionById,
    getPermissionByCode,
    checkIn,
    checkOut
  }
})
