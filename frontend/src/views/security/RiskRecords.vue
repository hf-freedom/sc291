<template>
  <div class="page-container">
    <div class="content-wrapper">
      <div class="header">
        <button class="btn btn-back" @click="goBack">← 返回</button>
        <h1 class="page-title">风险记录</h1>
      </div>

      <div class="card fade-in">
        <div v-if="loading" class="loading">加载中...</div>
        
        <div v-else-if="records.length === 0" class="empty-state">
          <p>暂无风险记录</p>
        </div>

        <div v-else class="record-list">
          <div 
            v-for="record in records" 
            :key="record.id"
            class="record-item"
          >
            <div class="record-header">
              <span class="record-type">{{ getRiskTypeText(record.riskType) }}</span>
              <span :class="['tag', getSeverityClass(record.severity)]">
                {{ getSeverityText(record.severity) }}
              </span>
            </div>

            <div class="record-info">
              <div class="info-row">
                <span class="label">访客ID:</span>
                <span>{{ record.visitorId }}</span>
              </div>
              <div class="info-row">
                <span class="label">预约ID:</span>
                <span>{{ record.requestId }}</span>
              </div>
              <div class="info-row">
                <span class="label">记录时间:</span>
                <span>{{ formatDate(record.recordedAt) }}</span>
              </div>
              <div class="info-row">
                <span class="label">详情:</span>
                <span>{{ record.description }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import api from '@/services/api'
import { formatDate } from '@/utils/date'

const router = useRouter()
const authStore = useAuthStore()

const records = ref([])
const loading = ref(false)

const goBack = () => {
  router.push('/security')
}

const getRiskTypeText = (type) => {
  const map = {
    'LATE': '迟到',
    'OVERSTAY': '超时离场',
    'NOT_EXITED': '未离场',
    'NO_SHOW': '爽约'
  }
  return map[type] || type
}

const getSeverityClass = (severity) => {
  const map = {
    'LOW': 'tag-approved',
    'MEDIUM': 'tag-warning',
    'HIGH': 'tag-danger'
  }
  return map[severity] || 'tag-pending'
}

const getSeverityText = (severity) => {
  const map = {
    'LOW': '低风险',
    'MEDIUM': '中风险',
    'HIGH': '高风险'
  }
  return map[severity] || severity
}

const loadRecords = async () => {
  loading.value = true
  try {
    const response = await api.riskRecords.getAll()
    records.value = response.data
  } catch (error) {
    ElMessage.error('加载风险记录失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  if (!authStore.user || authStore.user.role !== 'security') {
    router.push('/login')
    return
  }
  loadRecords()
})
</script>

<style scoped>
.header {
  display: flex;
  align-items: center;
  gap: 24px;
  margin-bottom: 32px;
}

.btn-back {
  padding: 10px 20px;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border-radius: 8px;
}

.btn-back:hover {
  background: rgba(255, 255, 255, 0.3);
}

.card {
  background: white;
  border-radius: 16px;
  padding: 32px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: var(--text-secondary);
}

.record-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.record-item {
  border: 1px solid var(--border-color);
  border-radius: 12px;
  padding: 24px;
  transition: box-shadow 0.3s;
}

.record-item:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.record-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.record-type {
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
}

.tag-warning {
  background: #fef3c7;
  color: #92400e;
}

.record-info {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.info-row {
  display: flex;
  gap: 12px;
  font-size: 14px;
}

.info-row .label {
  color: var(--text-secondary);
  min-width: 100px;
}
</style>
