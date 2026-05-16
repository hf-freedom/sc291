<template>
  <div class="page-container">
    <div class="content-wrapper">
      <div class="header">
        <h1 class="page-title">系统管理</h1>
        <div class="user-info">
          <span>欢迎，{{ authStore.user?.name }}</span>
          <button class="btn btn-sm" @click="handleLogout">退出登录</button>
        </div>
      </div>

      <div class="stats-grid fade-in">
        <div class="stat-card">
          <div class="stat-icon">👥</div>
          <div class="stat-content">
            <div class="stat-value">{{ statistics.todayVisitors }}</div>
            <div class="stat-label">今日访客</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">⏳</div>
          <div class="stat-content">
            <div class="stat-value">{{ statistics.pendingApprovals }}</div>
            <div class="stat-label">待审批</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">🔑</div>
          <div class="stat-content">
            <div class="stat-value">{{ statistics.activePermissions }}</div>
            <div class="stat-label">有效权限</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">⚠️</div>
          <div class="stat-content">
            <div class="stat-value">{{ statistics.riskRecords }}</div>
            <div class="stat-label">风险记录</div>
          </div>
        </div>
      </div>

      <div class="action-cards fade-in">
        <div class="action-card" @click="goTo('/admin/areas')">
          <div class="action-icon">🗺️</div>
          <div class="action-content">
            <h3>区域管理</h3>
            <p>管理园区访问区域</p>
          </div>
        </div>

        <div class="action-card" @click="goTo('/admin/statistics')">
          <div class="action-icon">📊</div>
          <div class="action-content">
            <h3>数据统计</h3>
            <p>查看系统统计数据</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import api from '@/services/api'

const router = useRouter()
const authStore = useAuthStore()

const statistics = ref({
  todayVisitors: 0,
  pendingApprovals: 0,
  activePermissions: 0,
  riskRecords: 0
})

const goTo = (path) => {
  router.push(path)
}

const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}

const loadStatistics = async () => {
  try {
    const response = await api.statistics.get()
    statistics.value = response.data
  } catch (error) {
    console.error('加载统计数据失败', error)
  }
}

onMounted(() => {
  if (!authStore.user || authStore.user.role !== 'admin') {
    router.push('/login')
    return
  }
  loadStatistics()
})
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 40px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 16px;
  color: white;
}

.btn-sm {
  padding: 8px 16px;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border-radius: 6px;
  font-size: 14px;
}

.btn-sm:hover {
  background: rgba(255, 255, 255, 0.3);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 24px;
  margin-bottom: 40px;
}

.stat-card {
  background: white;
  border-radius: 16px;
  padding: 28px;
  display: flex;
  align-items: center;
  gap: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  font-size: 48px;
}

.stat-value {
  font-size: 36px;
  font-weight: bold;
  color: var(--primary-color);
}

.stat-label {
  color: var(--text-secondary);
  font-size: 14px;
}

.action-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 24px;
}

.action-card {
  background: white;
  border-radius: 16px;
  padding: 32px;
  display: flex;
  align-items: center;
  gap: 24px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.action-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.15);
}

.action-icon {
  font-size: 56px;
}

.action-content h3 {
  font-size: 22px;
  color: var(--primary-color);
  margin-bottom: 8px;
}

.action-content p {
  color: var(--text-secondary);
  font-size: 14px;
}
</style>
