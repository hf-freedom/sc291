<template>
  <div class="page-container">
    <div class="content-wrapper">
      <div class="header">
        <button class="btn btn-back" @click="goBack">← 返回</button>
        <h1 class="page-title">我的申请记录</h1>
      </div>

      <div class="card fade-in">
        <div v-if="loading" class="loading">加载中...</div>
        
        <div v-else-if="requests.length === 0" class="empty-state">
          <p>暂无申请记录</p>
        </div>

        <div v-else class="request-list">
          <div 
            v-for="request in requests" 
            :key="request.id"
            class="request-item"
          >
            <div class="request-header">
              <span class="request-id">申请ID: {{ request.id }}</span>
              <span :class="['tag', getStatusClass(request.status)]">
                {{ getStatusText(request.status) }}
              </span>
            </div>

            <div class="request-info">
              <div class="info-row">
                <span class="label">被访人ID:</span>
                <span>{{ request.hostId }}</span>
              </div>
              <div class="info-row">
                <span class="label">访问区域ID:</span>
                <span>{{ request.areaId }}</span>
              </div>
              <div class="info-row">
                <span class="label">预计入园:</span>
                <span>{{ formatDate(request.expectedEntryTime) }}</span>
              </div>
              <div class="info-row">
                <span class="label">预计离园:</span>
                <span>{{ formatDate(request.expectedExitTime) }}</span>
              </div>
              <div class="info-row">
                <span class="label">访问事由:</span>
                <span>{{ request.reason }}</span>
              </div>
              <div v-if="request.requireSecurityApproval" class="info-row security-notice">
                <span class="label">⚠️ 高风险访客，需保安二次审批</span>
              </div>
            </div>

            <div class="request-actions">
              <button 
                v-if="request.status === 'PENDING'"
                class="btn btn-danger btn-sm"
                @click="cancelRequest(request.id)"
              >
                取消申请
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
import { ElMessage, ElMessageBox } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import { useVisitRequestStore } from '@/stores/visitRequest'
import { formatDate, getStatusClass, getStatusText } from '@/utils/date'

const router = useRouter()
const authStore = useAuthStore()
const visitRequestStore = useVisitRequestStore()

const requests = ref([])
const loading = ref(false)

const goBack = () => {
  router.push('/visitor')
}

const loadRequests = async () => {
  loading.value = true
  try {
    requests.value = await visitRequestStore.fetchRequestsByVisitor(authStore.user.id)
  } catch (error) {
    ElMessage.error('加载申请记录失败')
  } finally {
    loading.value = false
  }
}

const cancelRequest = async (id) => {
  try {
    await ElMessageBox.confirm('确定要取消该申请吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await visitRequestStore.cancelRequest(id)
    ElMessage.success('申请已取消')
    loadRequests()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('取消失败')
    }
  }
}

onMounted(() => {
  if (!authStore.user || authStore.user.role !== 'visitor') {
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

.request-info {
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

.security-notice {
  color: var(--warning-color);
  font-weight: 500;
  margin-top: 8px;
}

.request-actions {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid var(--border-color);
}

.btn-sm {
  padding: 6px 16px;
  font-size: 14px;
}
</style>
