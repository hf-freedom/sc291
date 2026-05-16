<template>
  <div class="page-container">
    <div class="content-wrapper">
      <div class="header">
        <button class="btn btn-back" @click="goBack">← 返回</button>
        <h1 class="page-title">待审批申请</h1>
      </div>

      <div class="card fade-in">
        <div v-if="loading" class="loading">加载中...</div>
        
        <div v-else-if="requests.length === 0" class="empty-state">
          <p>暂无待审批的申请</p>
        </div>

        <div v-else class="request-list">
          <div 
            v-for="request in requests" 
            :key="request.id"
            class="request-item"
          >
            <div class="request-header">
              <span class="request-id">申请ID: {{ request.id }}</span>
              <span v-if="request.requireSecurityApproval" class="tag tag-warning">
                ⚠️ 高风险需保安审批
              </span>
            </div>

            <div class="request-info">
              <div class="info-row">
                <span class="label">访客:</span>
                <span>{{ request.visitorName || request.visitorId }}</span>
              </div>
              <div class="info-row">
                <span class="label">被访人:</span>
                <span>{{ request.hostName || request.hostId }}</span>
              </div>
              <div class="info-row">
                <span class="label">访问区域:</span>
                <span>{{ request.areaName || request.areaId }} (权限级别: {{ request.areaLevel || 'N/A' }})</span>
              </div>
              <div class="info-row">
                <span class="label">预计入园:</span>
                <span>{{ formatDateTime(request.expectedEntryTime) }}</span>
              </div>
              <div class="info-row">
                <span class="label">预计离园:</span>
                <span>{{ formatDateTime(request.expectedExitTime) }}</span>
              </div>
              <div class="info-row">
                <span class="label">访问事由:</span>
                <span>{{ request.reason || request.purpose }}</span>
              </div>
            </div>

            <div class="request-actions">
              <button 
                class="btn btn-success"
                @click="approveRequest(request.id, true)"
                :disabled="processing === request.id"
              >
                {{ processing === request.id ? '处理中...' : '批准并生成通行权限' }}
              </button>
              <button 
                class="btn btn-danger"
                @click="approveRequest(request.id, false)"
                :disabled="processing === request.id"
              >
                {{ processing === request.id ? '处理中...' : '拒绝' }}
              </button>
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

const router = useRouter()
const authStore = useAuthStore()

const requests = ref([])
const loading = ref(false)
const processing = ref(null)

const goBack = () => {
  router.push('/host')
}

const formatDateTime = (dateStr) => {
  if (!dateStr) return 'N/A'
  try {
    const date = new Date(dateStr.replace(' ', 'T'))
    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    })
  } catch (e) {
    return dateStr
  }
}

const loadRequests = async () => {
  loading.value = true
  try {
    const response = await api.visitRequests.getAll({ hostId: authStore.user.id })
    requests.value = response.data.filter(r => r.status === 'PENDING')
  } catch (error) {
    ElMessage.error('加载申请列表失败')
  } finally {
    loading.value = false
  }
}

const approveRequest = async (id, approved) => {
  if (processing.value) return
  
  processing.value = id
  try {
    const data = {
      approved: approved,
      comment: approved ? '同意访问' : '拒绝访问'
    }
    await api.visitRequests.approve(id, data)
    
    if (approved) {
      ElMessage.success('已批准申请并生成临时通行权限！')
    } else {
      ElMessage.success('已拒绝申请')
    }
    
    loadRequests()
  } catch (error) {
    ElMessage.error(error.message || '操作失败')
  } finally {
    processing.value = null
  }
}

onMounted(() => {
  if (!authStore.user || authStore.user.role !== 'host') {
    router.push('/login')
    return
  }
  loadRequests()
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

.request-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.request-item {
  border: 1px solid var(--border-color);
  border-radius: 12px;
  padding: 24px;
  transition: box-shadow 0.3s;
}

.request-item:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.request-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.request-id {
  font-weight: 600;
  color: var(--text-secondary);
  font-size: 14px;
}

.tag {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.tag-warning {
  background: #fef3c7;
  color: #92400e;
}

.request-info {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 20px;
}

.info-row {
  display: flex;
  gap: 12px;
  font-size: 14px;
  align-items: center;
}

.info-row .label {
  color: var(--text-secondary);
  min-width: 100px;
  font-weight: 500;
}

.info-row span:last-child {
  color: var(--text-primary);
}

.request-actions {
  padding-top: 16px;
  border-top: 1px solid var(--border-color);
  display: flex;
  gap: 12px;
}

.btn {
  padding: 10px 20px;
  border-radius: 8px;
  font-size: 14px;
  cursor: pointer;
  border: none;
  transition: all 0.3s;
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-success {
  background: var(--success-color);
  color: white;
}

.btn-success:hover:not(:disabled) {
  background: #059669;
}

.btn-danger {
  background: var(--danger-color);
  color: white;
}

.btn-danger:hover:not(:disabled) {
  background: #dc2626;
}
</style>
