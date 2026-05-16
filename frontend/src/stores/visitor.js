import { defineStore } from 'pinia'
import { ref } from 'vue'
import api from '@/services/api'

export const useVisitorStore = defineStore('visitor', () => {
  const visitors = ref([])
  const currentVisitor = ref(null)
  const loading = ref(false)

  const fetchVisitors = async () => {
    loading.value = true
    try {
      const response = await api.visitors.getAll()
      visitors.value = response.data
    } finally {
      loading.value = false
    }
  }

  const getVisitorById = async (id) => {
    loading.value = true
    try {
      const response = await api.visitors.getById(id)
      currentVisitor.value = response.data
      return response.data
    } finally {
      loading.value = false
    }
  }

  const createVisitor = async (data) => {
    loading.value = true
    try {
      const response = await api.visitors.create(data)
      visitors.value.push(response.data)
      return response.data
    } finally {
      loading.value = false
    }
  }

  return {
    visitors,
    currentVisitor,
    loading,
    fetchVisitors,
    getVisitorById,
    createVisitor
  }
})
