<template>
  <div class="page-container">
    <div class="content-wrapper">
      <div class="header">
        <button class="btn btn-back" @click="goBack">← 返回</button>
        <h1 class="page-title">提交预约申请</h1>
      </div>

      <div class="form-card fade-in">
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
            <label>被访人</label>
            <select v-model="formData.hostId" required>
              <option value="">请选择被访人</option>
              <option v-for="host in hosts" :key="host.id" :value="host.id">
                {{ host.name }} - {{ host.department }}
              </option>
            </select>
          </div>

          <div class="form-group">
            <label>访问区域</label>
            <select v-model="formData.areaId" required>
              <option value="">请选择访问区域</option>
              <option v-for="area in areas" :key="area.id" :value="area.id">
                {{ area.name }} ({{ getLevelText(area.requiredPermissionLevel) }})
              </option>
            </select>
          </div>

          <div class="form-row">
            <div class="form-group">
              <label>预计入园时间</label>
              <input 
                type="datetime-local" 
                v-model="formData.expectedEntryTime"
                required
              />
            </div>

            <div class="form-group">
              <label>预计离园时间</label>
              <input 
                type="datetime-local" 
                v-model="formData.expectedExitTime"
                required
              />
            </div>
          </div>

          <div class="form-group">
            <label>访问事由</label>
            <textarea 
              v-model="formData.reason"
              rows="4"
              placeholder="请简要描述访问目的"
              required
            ></textarea>
          </div>

          <div class="form-actions">
            <button type="submit" class="btn btn-primary btn-lg" :disabled="loading">
              {{ loading ? '提交中...' : '提交申请' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import { useVisitRequestStore } from '@/stores/visitRequest'
import api from '@/services/api'

const router = useRouter()
const authStore = useAuthStore()
const visitRequestStore = useVisitRequestStore()

const hosts = ref([])
const areas = ref([])
const loading = ref(false)

const formData = ref({
  visitorId: authStore.user?.id || '',
  hostId: '',
  areaId: '',
  expectedEntryTime: '',
  expectedExitTime: '',
  reason: ''
})

const getLevelText = (level) => {
  const map = {
    'BASIC': '基础权限',
    'INTERMEDIATE': '中级权限',
    'ADVANCED': '高级权限'
  }
  return map[level] || level
}

const goBack = () => {
  router.push('/visitor')
}

const handleSubmit = async () => {
  if (!authStore.user) {
    ElMessage.error('请先登录')
    return
  }

  loading.value = true
  try {
    formData.value.visitorId = authStore.user.id
    await visitRequestStore.createRequest(formData.value)
    ElMessage.success('申请提交成功！')
    router.push('/visitor/list')
  } catch (error) {
    ElMessage.error(error.message || '提交失败')
  } finally {
    loading.value = false
  }
}

const loadData = async () => {
  try {
    const [hostsRes, areasRes] = await Promise.all([
      api.hosts.getAll(),
      api.areas.getAll()
    ])
    hosts.value = hostsRes.data
    areas.value = areasRes.data
  } catch (error) {
    console.error('加载数据失败', error)
  }
}

onMounted(() => {
  if (!authStore.user || authStore.user.role !== 'visitor') {
    router.push('/login')
    return
  }
  loadData()
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

.form-card {
  background: white;
  border-radius: 16px;
  padding: 40px;
  max-width: 800px;
  margin: 0 auto;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.form-group {
  margin-bottom: 24px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: var(--text-primary);
  font-weight: 500;
  font-size: 16px;
}

.form-group input,
.form-group select,
.form-group textarea {
  width: 100%;
  padding: 12px;
  border: 1px solid var(--border-color);
  border-radius: 8px;
  font-size: 14px;
  font-family: inherit;
}

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {
  border-color: var(--secondary-color);
  outline: none;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
}

.form-actions {
  text-align: center;
  margin-top: 32px;
}

.btn-lg {
  padding: 14px 48px;
  font-size: 16px;
}
</style>
