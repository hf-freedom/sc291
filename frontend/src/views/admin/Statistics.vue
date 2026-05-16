<template>
  <div class="page-container">
    <div class="content-wrapper">
      <div class="header">
        <button class="btn btn-back" @click="goBack">← 返回</button>
        <h1 class="page-title">数据统计</h1>
      </div>

      <div class="stats-grid fade-in">
        <div class="stat-card">
          <div class="stat-icon">👥</div>
          <div class="stat-content">
            <div class="stat-value">{{ statistics.todayVisitors }}</div>
            <div class="stat-label">今日访客数</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">⏳</div>
          <div class="stat-content">
            <div class="stat-value">{{ statistics.pendingApprovals }}</div>
            <div class="stat-label">待审批申请</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">🔑</div>
          <div class="stat-content">
            <div class="stat-value">{{ statistics.activePermissions }}</div>
            <div class="stat-label">有效通行权限</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">⚠️</div>
          <div class="stat-content">
            <div class="stat-value">{{ statistics.riskRecords }}</div>
            <div class="stat-label">今日风险记录</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">🚪</div>
          <div class="stat-content">
            <div class="stat-value">{{ statistics.notExitedVisitors }}</div>
            <div class="stat-label">未离场访客</div>
          </div>
        </div>
      </div>

      <div class="card fade-in">
        <h3>实时数据监控</h3>
        <div class="monitor-grid">
          <div class="monitor-item">
            <div class="monitor-label">系统状态</div>
            <div class="monitor-value status-online">● 在线</div>
          </div>
          <div class="monitor-item">
            <div class="monitor-label">定时任务</div>
            <div class="monitor-value">运行中</div>
          </div>
          <div class="monitor-item">
            <div class="monitor-label">最后更新</div>
            <div class="monitor-value">{{ lastUpdate }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import api from '@/services/api'
import { formatDate } from '@/utils/date'

const router = useRouter()
const authStore = useAuthStore()

const statistics = ref({
  todayVisitors: 0,
  pendingApprovals: 0,
  activePermissions: 0,
  riskRecords: 0,
  notExitedVisitors: 0
})

const lastUpdate = ref('')
let updateInterval = null

const goBack = () => {
  router.push('/admin')
}

const loadStatistics = async () => {
  try {
    const response = await api.statistics.get()
    statistics.value = response.data
    lastUpdate.value = formatDate(new Date())
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
  updateInterval = setInterval(loadStatistics, 30000)
})

onUnmounted(() => {
  if (updateInterval) {
    clearInterval(updateInterval)
  }
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

.card {
  background: white;
  border-radius: 16px;
  padding: 32px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.card h3 {
  color: var(--primary-color);
  margin-bottom: 24px;
  font-size: 20px;
}

.monitor-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
}

.monitor-item {
  background: var(--bg-light);
  border-radius: 8px;
  padding: 20px;
  text-align: center;
}

.monitor-label {
  color: var(--text-secondary);
  font-size: 14px;
  margin-bottom: 8px;
}

.monitor-value {
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
}

.status-online {
  color: var(--success-color);
}
</style>
