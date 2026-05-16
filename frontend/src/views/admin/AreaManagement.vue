<template>
  <div class="page-container">
    <div class="content-wrapper">
      <div class="header">
        <button class="btn btn-back" @click="goBack">← 返回</button>
        <h1 class="page-title">区域管理</h1>
        <button class="btn btn-primary" @click="showAddDialog = true">
          添加区域
        </button>
      </div>

      <div class="card fade-in">
        <div v-if="loading" class="loading">加载中...</div>
        
        <div v-else-if="areas.length === 0" class="empty-state">
          <p>暂无区域数据</p>
        </div>

        <div v-else class="area-list">
          <div 
            v-for="area in areas" 
            :key="area.id"
            class="area-item"
          >
            <div class="area-header">
              <div class="area-name">{{ area.name }}</div>
              <span :class="['tag', getLevelClass(area.requiredPermissionLevel)]">
                {{ getLevelText(area.requiredPermissionLevel) }}
              </span>
            </div>

            <div class="area-info">
              <div class="info-row">
                <span class="label">区域ID:</span>
                <span>{{ area.id }}</span>
              </div>
              <div class="info-row">
                <span class="label">描述:</span>
                <span>{{ area.description }}</span>
              </div>
              <div class="info-row">
                <span class="label">状态:</span>
                <span>{{ area.status === 'ACTIVE' ? '启用' : '禁用' }}</span>
              </div>
            </div>

            <div class="area-actions">
              <button class="btn btn-sm btn-warning" @click="editArea(area)">
                编辑
              </button>
              <button class="btn btn-sm btn-danger" @click="deleteArea(area.id)">
                删除
              </button>
            </div>
          </div>
        </div>
      </div>

      <div v-if="showAddDialog || showEditDialog" class="dialog-overlay" @click="closeDialog">
        <div class="dialog" @click.stop>
          <h3>{{ showEditDialog ? '编辑区域' : '添加区域' }}</h3>
          <div class="form-group">
            <label>区域名称</label>
            <input v-model="formData.name" placeholder="请输入区域名称" />
          </div>
          <div class="form-group">
            <label>区域描述</label>
            <textarea v-model="formData.description" rows="3" placeholder="请输入区域描述"></textarea>
          </div>
          <div class="form-group">
            <label>所需权限级别</label>
            <select v-model="formData.requiredPermissionLevel">
              <option value="BASIC">基础权限（一级区域）</option>
              <option value="INTERMEDIATE">中级权限（二级区域）</option>
              <option value="ADVANCED">高级权限（三级区域）</option>
            </select>
          </div>
          <div class="dialog-actions">
            <button class="btn btn-secondary" @click="closeDialog">取消</button>
            <button class="btn btn-primary" @click="submitForm">{{ showEditDialog ? '保存' : '添加' }}</button>
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

const router = useRouter()
const authStore = useAuthStore()

const areas = ref([])
const loading = ref(false)
const showAddDialog = ref(false)
const showEditDialog = ref(false)
const editingId = ref('')

const formData = ref({
  name: '',
  description: '',
  requiredPermissionLevel: 'BASIC'
})

const goBack = () => {
  router.push('/admin')
}

const getLevelText = (level) => {
  const map = {
    'BASIC': '基础权限',
    'INTERMEDIATE': '中级权限',
    'ADVANCED': '高级权限'
  }
  return map[level] || level
}

const getLevelClass = (level) => {
  const map = {
    'BASIC': 'tag-approved',
    'INTERMEDIATE': 'tag-warning',
    'ADVANCED': 'tag-danger'
  }
  return map[level] || 'tag-pending'
}

const loadAreas = async () => {
  loading.value = true
  try {
    const response = await api.areas.getAll()
    areas.value = response.data
  } catch (error) {
    ElMessage.error('加载区域列表失败')
  } finally {
    loading.value = false
  }
}

const editArea = (area) => {
  editingId.value = area.id
  formData.value = {
    name: area.name,
    description: area.description,
    requiredPermissionLevel: area.requiredPermissionLevel
  }
  showEditDialog.value = true
}

const deleteArea = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除该区域吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await api.areas.delete(id)
    ElMessage.success('区域已删除')
    loadAreas()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const closeDialog = () => {
  showAddDialog.value = false
  showEditDialog.value = false
  editingId.value = ''
  formData.value = {
    name: '',
    description: '',
    requiredPermissionLevel: 'BASIC'
  }
}

const submitForm = async () => {
  if (!formData.value.name) {
    ElMessage.warning('请输入区域名称')
    return
  }

  try {
    if (showEditDialog.value) {
      await api.areas.update(editingId.value, formData.value)
      ElMessage.success('区域已更新')
    } else {
      await api.areas.create(formData.value)
      ElMessage.success('区域已添加')
    }
    closeDialog()
    loadAreas()
  } catch (error) {
    ElMessage.error(error.message || '操作失败')
  }
}

onMounted(() => {
  if (!authStore.user || authStore.user.role !== 'admin') {
    router.push('/login')
    return
  }
  loadAreas()
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

.area-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 20px;
}

.area-item {
  border: 1px solid var(--border-color);
  border-radius: 12px;
  padding: 24px;
  transition: box-shadow 0.3s;
}

.area-item:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.area-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.area-name {
  font-size: 20px;
  font-weight: 600;
  color: var(--primary-color);
}

.area-info {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 16px;
}

.info-row {
  display: flex;
  gap: 12px;
  font-size: 14px;
}

.info-row .label {
  color: var(--text-secondary);
  min-width: 80px;
}

.area-actions {
  display: flex;
  gap: 12px;
  padding-top: 16px;
  border-top: 1px solid var(--border-color);
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

.form-group input,
.form-group textarea,
.form-group select {
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
