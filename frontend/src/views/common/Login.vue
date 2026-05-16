<template>
  <div class="login-container">
    <div class="login-card fade-in">
      <h1 class="login-title">门禁访客预约系统</h1>
      <p class="login-subtitle">请选择您的角色登录</p>
      
      <div class="role-grid">
        <div 
          v-for="role in roles" 
          :key="role.id"
          class="role-card"
          @click="selectRole(role)"
        >
          <div class="role-icon">{{ role.icon }}</div>
          <div class="role-name">{{ role.name }}</div>
          <div class="role-desc">{{ role.description }}</div>
        </div>
      </div>

      <div v-if="selectedRole && selectedRole.id !== 'visitor'" class="login-form">
        <h3>以{{ selectedRole.name }}身份登录</h3>
        
        <div class="form-group">
          <label>{{ selectedRole.idLabel }}</label>
          <select v-model="selectedId" class="form-control">
            <option value="">请选择{{ selectedRole.idLabel }}</option>
            <option v-for="item in selectableItems" :key="item.id" :value="item.id">
              {{ item.name }}
            </option>
          </select>
        </div>

        <button class="btn btn-primary btn-block" @click="login">
          登录
        </button>
        
        <button class="btn btn-secondary btn-block" @click="selectedRole = null">
          返回
        </button>
      </div>

      <div v-if="selectedRole && selectedRole.id === 'visitor'" class="login-form">
        <h3>以{{ selectedRole.name }}身份登录</h3>
        
        <div class="form-group">
          <label>选择访客</label>
          <select v-model="selectedId" class="form-control" @change="onVisitorChange">
            <option value="">请选择访客</option>
            <option v-for="item in selectableItems" :key="item.id" :value="item.id">
              {{ item.name }}
            </option>
          </select>
        </div>

        <div v-if="selectedId" class="visit-form">
          <div class="form-group">
            <label>访问对象（被访人）</label>
            <select v-model="visitForm.hostId" class="form-control">
              <option value="">请选择被访人</option>
              <option v-for="host in hosts" :key="host.id" :value="host.id">
                {{ host.name }} - {{ host.department }}
              </option>
            </select>
          </div>

          <div class="form-group">
            <label>访问区域</label>
            <select v-model="visitForm.areaId" class="form-control">
              <option value="">请选择访问区域</option>
              <option v-for="area in areas" :key="area.id" :value="area.id">
                {{ area.name }} (权限级别: {{ area.requiredLevel }})
              </option>
            </select>
          </div>

          <div class="form-group">
            <label>预计到达时间</label>
            <input 
              type="datetime-local" 
              v-model="visitForm.expectedEntryTime" 
              class="form-control"
            >
          </div>

          <div class="form-group">
            <label>预计离开时间</label>
            <input 
              type="datetime-local" 
              v-model="visitForm.expectedExitTime" 
              class="form-control"
            >
          </div>

          <div class="form-group">
            <label>访问事由</label>
            <textarea 
              v-model="visitForm.purpose" 
              class="form-control"
              rows="3"
              placeholder="请简要描述访问事由"
            ></textarea>
          </div>
        </div>

        <button class="btn btn-primary btn-block" @click="visitorLogin">
          提交申请并登录
        </button>
        
        <button class="btn btn-secondary btn-block" @click="selectedRole = null">
          返回
        </button>
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

const roles = [
  {
    id: 'visitor',
    name: '访客',
    icon: '👤',
    description: '提交访问申请',
    idLabel: '访客'
  },
  {
    id: 'host',
    name: '被访人',
    icon: '🏢',
    description: '审批访问申请',
    idLabel: '被访人'
  },
  {
    id: 'security',
    name: '保安',
    icon: '🛡️',
    description: '二次审批高风险访客',
    idLabel: '保安'
  },
  {
    id: 'admin',
    name: '管理员',
    icon: '⚙️',
    description: '系统配置管理',
    idLabel: '管理员'
  }
]

const selectedRole = ref(null)
const selectedId = ref('')
const selectableItems = ref([])
const hosts = ref([])
const areas = ref([])

const visitForm = ref({
  hostId: '',
  areaId: '',
  expectedEntryTime: '',
  expectedExitTime: '',
  purpose: ''
})

const selectRole = async (role) => {
  selectedRole.value = role
  selectedId.value = ''
  
  if (role.id === 'visitor') {
    const response = await api.visitors.getAll()
    selectableItems.value = response.data
    
    const hostsResponse = await api.hosts.getAll()
    hosts.value = hostsResponse.data
    
    const areasResponse = await api.areas.getAll()
    areas.value = areasResponse.data
    
    const tomorrow = new Date()
    tomorrow.setDate(tomorrow.getDate() + 1)
    tomorrow.setHours(9, 0, 0, 0)
    visitForm.value.expectedEntryTime = tomorrow.toISOString().slice(0, 16)
    
    const exitTime = new Date(tomorrow)
    exitTime.setHours(18, 0, 0, 0)
    visitForm.value.expectedExitTime = exitTime.toISOString().slice(0, 16)
  } else if (role.id === 'host') {
    const response = await api.hosts.getAll()
    selectableItems.value = response.data
  } else if (role.id === 'security') {
    const response = await api.securityGuards.getAll()
    selectableItems.value = response.data
  } else if (role.id === 'admin') {
    selectableItems.value = [{ id: 'admin-001', name: '系统管理员' }]
  }
}

const onVisitorChange = () => {
  if (selectedId.value) {
    const visitor = selectableItems.value.find(v => v.id === selectedId.value)
    if (visitor && visitor.blacklistUntil) {
      ElMessage.error('您已被加入黑名单，无法申请访问')
      selectedId.value = ''
    }
  }
}

const visitorLogin = async () => {
  if (!selectedId.value) {
    ElMessage.warning('请选择访客')
    return
  }
  
  if (!visitForm.value.hostId) {
    ElMessage.warning('请选择访问对象')
    return
  }
  
  if (!visitForm.value.areaId) {
    ElMessage.warning('请选择访问区域')
    return
  }
  
  if (!visitForm.value.expectedEntryTime) {
    ElMessage.warning('请选择预计到达时间')
    return
  }
  
  if (!visitForm.value.expectedExitTime) {
    ElMessage.warning('请选择预计离开时间')
    return
  }
  
  if (!visitForm.value.purpose) {
    ElMessage.warning('请填写访问事由')
    return
  }
  
  const visitor = selectableItems.value.find(v => v.id === selectedId.value)
  
  if (visitor && visitor.blacklistUntil) {
    ElMessage.error('您已被加入黑名单，无法申请访问')
    return
  }
  
  try {
    const requestData = {
      visitorId: selectedId.value,
      hostId: visitForm.value.hostId,
      areaId: visitForm.value.areaId,
      expectedEntryTime: visitForm.value.expectedEntryTime.replace('T', ' '),
      expectedExitTime: visitForm.value.expectedExitTime.replace('T', ' '),
      purpose: visitForm.value.purpose
    }
    
    await api.visitRequests.create(requestData)
    
    const user = {
      role: 'visitor',
      id: selectedId.value,
      name: visitor.name
    }

    authStore.setUser(user)
    authStore.setToken('token-' + Date.now())
    authStore.setVisitForm(visitForm.value)

    ElMessage.success('访问申请已提交，等待被访人审批！')

    router.push('/visitor')
  } catch (error) {
    ElMessage.error('提交申请失败：' + (error.message || '未知错误'))
  }
}

const login = () => {
  if (!selectedId.value) {
    ElMessage.warning('请选择登录身份')
    return
  }

  const user = {
    role: selectedRole.value.id,
    id: selectedId.value,
    name: selectableItems.value.find(i => i.id === selectedId.value)?.name || '未知'
  }

  authStore.setUser(user)
  authStore.setToken('token-' + Date.now())

  ElMessage.success(`欢迎，${user.name}！`)

  if (user.role === 'host') {
    router.push('/host')
  } else if (user.role === 'security') {
    router.push('/security')
  } else if (user.role === 'admin') {
    router.push('/admin')
  }
}

onMounted(() => {
  authStore.initAuth()
})
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  background: white;
  border-radius: 20px;
  padding: 48px;
  max-width: 900px;
  width: 100%;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.login-title {
  font-size: 36px;
  color: var(--primary-color);
  text-align: center;
  margin-bottom: 12px;
  font-weight: bold;
}

.login-subtitle {
  text-align: center;
  color: var(--text-secondary);
  margin-bottom: 40px;
  font-size: 16px;
}

.role-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 24px;
  margin-bottom: 40px;
}

.role-card {
  background: var(--bg-light);
  border-radius: 12px;
  padding: 32px 24px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid transparent;
}

.role-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.15);
  border-color: var(--secondary-color);
}

.role-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.role-name {
  font-size: 20px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 8px;
}

.role-desc {
  font-size: 14px;
  color: var(--text-secondary);
}

.login-form {
  max-width: 500px;
  margin: 0 auto;
}

.login-form h3 {
  text-align: center;
  color: var(--primary-color);
  margin-bottom: 24px;
  font-size: 20px;
}

.visit-form {
  background: var(--bg-light);
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
  border: 1px solid var(--border-color);
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

.form-control {
  width: 100%;
  padding: 12px;
  border: 1px solid var(--border-color);
  border-radius: 8px;
  font-size: 14px;
}

textarea.form-control {
  resize: vertical;
  min-height: 80px;
}

.btn-block {
  width: 100%;
  margin-bottom: 12px;
}

.btn-secondary {
  background: var(--text-secondary);
  color: white;
}

.btn-secondary:hover {
  background: #5a6a7e;
}
</style>
