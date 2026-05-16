import { defineStore } from 'pinia'
import { ref } from 'vue'
import api from '@/services/api'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(null)
  const token = ref(localStorage.getItem('token'))
  const visitForm = ref(null)

  const setUser = (userData) => {
    user.value = userData
    localStorage.setItem('user', JSON.stringify(userData))
  }

  const setToken = (newToken) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  const setVisitForm = (formData) => {
    visitForm.value = formData
    localStorage.setItem('visitForm', JSON.stringify(formData))
  }

  const logout = () => {
    user.value = null
    token.value = null
    visitForm.value = null
    localStorage.removeItem('user')
    localStorage.removeItem('token')
    localStorage.removeItem('visitForm')
  }

  const initAuth = () => {
    const savedUser = localStorage.getItem('user')
    if (savedUser) {
      user.value = JSON.parse(savedUser)
    }
    const savedToken = localStorage.getItem('token')
    if (savedToken) {
      token.value = savedToken
    }
    const savedVisitForm = localStorage.getItem('visitForm')
    if (savedVisitForm) {
      visitForm.value = JSON.parse(savedVisitForm)
    }
  }

  return {
    user,
    token,
    visitForm,
    setUser,
    setToken,
    setVisitForm,
    logout,
    initAuth
  }
})
