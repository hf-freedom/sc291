<template>
  <div class="page-container">
    <div class="content-wrapper">
      <div class="header">
        <button class="btn btn-back" @click="goBack">← 返回</button>
        <h1 class="page-title">黑名单管理</h1>
        <button class="btn btn-primary" @click="showAddDialog = true">
          添加到黑名单
        </button>
      </div>

      <div class="card fade-in">
        <div v-if="loading" class="loading">加载中...</div>
        
        <div v-else-if="blacklist.length === 0" class="empty-state">
          <p>暂无黑名单记录</p>
        </div>

        <div v-else class="blacklist-table">
          <table>
            <thead>
              <tr>
                <th>访客ID</th>
                <th>原因</th>
                <th>有效期至</th>
                <th>添加时间</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in blacklist" :key="item.id">
                <td>{{ item.visitorId }}</td>
                <td>{{ item.reason }}</td>
                <td>{{ item.expireAt ? formatDate(item.expireAt) : '永久' }}</td>
                <td>{{ formatDate(item.createdAt) }}</td>
                <td>
                  <button 
                    class="btn btn-danger btn-sm"
                    @click="removeFromBlacklist(item.visitorId)"
                  >
                    移除
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div v-if="showAddDialog" class="dialog-overlay" @click="showAddDialog = false">
        <div class="dialog" @click.stop>
          <h3>添加到黑名单</h3>
          <div class="form-group">
            <label>访客</label>
            <select v-model="newEntry.visitorId">
              <option value="">请选择访客</option>
              <option v-for="v in visitors" :key="v.id" :value="v.id">
                {{ v.name }} ({{ v.id }})
              </option>
            </select>
          </div>
          <div class="form-group">
            <label>原因</label>
            <textarea v-model="newEntry.reason" rows="3"></textarea>
          </div>
          <div class="form-group">
            <label>有效期至（留空为永久）</label>
            <input type="date" v-model="newEntry.expireAt" />
          </div>
          <div class="dialog-actions">
            <button class="btn btn-secondary" @click="showAddDialog = false">取消</button>
            <button class="btn btn-primary" @click="addToBlacklist">确认</button>
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
import api from '@/services/api'
import { formatDate } from '@/utils/date'

const router = useRouter()
const authStore = useAuthStore()

const blacklist = ref([])
const visitors = ref([])
const loading = ref(false)
const showAddDialog = ref(false)

const newEntry = ref({
  visitorId: '',
  reason: '',
  expireAt: ''
})

const goBack = () => {
  router.push('/security')
}

const loadBlacklist = async () => {
  loading.value = true
  try {
    const response = await api.blacklist.getAll()
    blacklist.value = response.data
  } catch (error) {
    ElMessage.error('加载黑名单失败')
  } finally {
    loading.value = false
  }
}

const loadVisitors = async () => {
  try {
    const response = await api.visitors.getAll()
    visitors.value = response.data
  } catch (error) {
    console.error('加载访客列表失败', error)
  }
}

const addToBlacklist = async () => {
  if (!newEntry.value.visitorId || !newEntry.value.reason) {
    ElMessage.warning('请填写完整信息')
    return
  }

  try {
    const data = {
      visitorId: newEntry.value.visitorId,
      reason: newEntry.value.reason,
      expireAt: newEntry.value.expireAt ? new Date(newEntry.value.expireAt) : null
    }
    await api.blacklist.add(data)
    ElMessage.success('已添加到黑名单')
    showAddDialog.value = false
    newEntry.value = { visitorId: '', reason: '', expireAt: '' }
    loadBlacklist()
  } catch (error) {
    ElMessage.error(error.message || '添加失败')
  }
}

const removeFromBlacklist = async (visitorId) => {
  try {
    await ElMessageBox.confirm('确定要从黑名单移除吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await api.blacklist.remove(visitorId)
    ElMessage.success('已从黑名单移除')
    loadBlacklist()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('移除失败')
    }
  }
}

onMounted(() => {
  if (!authStore.user || authStore.user.role !== 'security') {
    router.push('/login')
    return
  }
  loadBlacklist()
  loadVisitors()
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

.blacklist-table table {
  width: 100%;
  border-collapse: collapse;
}

.blacklist-table th,
.blacklist-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid var(--border-color);
}

.blacklist-table th {
  font-weight: 600;
  color: var(--text-secondary);
}

.btn-sm {
  padding: 6px 16px;
  font-size: 14px;
}

.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.dialog {
  background: white;
  border-radius: 12px;
  padding: 32px;
  width: 500px;
  max-width: 90%;
}

.dialog h3 {
  margin-bottom: 24px;
  color: var(--primary-color);
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: var(--text-primary);
  font-weight: 500;
}

.form-group select,
.form-group textarea,
.form-group input {
  width: 100%;
  padding: 10px;
  border: 1px solid var(--border-color);
  border-radius: 6px;
}

.dialog-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
}
</style>
