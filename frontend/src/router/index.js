import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/common/Login.vue'
import VisitorHome from '@/views/visitor/VisitorHome.vue'
import VisitRequest from '@/views/visitor/VisitRequest.vue'
import RequestList from '@/views/visitor/RequestList.vue'
import HostHome from '@/views/host/HostHome.vue'
import PendingApproval from '@/views/host/PendingApproval.vue'
import ApprovedList from '@/views/host/ApprovedList.vue'
import SecurityHome from '@/views/security/SecurityHome.vue'
import SecurityApproval from '@/views/security/SecurityApproval.vue'
import RiskRecords from '@/views/security/RiskRecords.vue'
import Blacklist from '@/views/security/Blacklist.vue'
import AdminHome from '@/views/admin/AdminHome.vue'
import AreaManagement from '@/views/admin/AreaManagement.vue'
import Statistics from '@/views/admin/Statistics.vue'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/visitor',
    name: 'VisitorHome',
    component: VisitorHome,
    meta: { role: 'visitor' }
  },
  {
    path: '/visitor/request',
    name: 'VisitRequest',
    component: VisitRequest,
    meta: { role: 'visitor' }
  },
  {
    path: '/visitor/list',
    name: 'RequestList',
    component: RequestList,
    meta: { role: 'visitor' }
  },
  {
    path: '/host',
    name: 'HostHome',
    component: HostHome,
    meta: { role: 'host' }
  },
  {
    path: '/host/pending',
    name: 'PendingApproval',
    component: PendingApproval,
    meta: { role: 'host' }
  },
  {
    path: '/host/approved',
    name: 'ApprovedList',
    component: ApprovedList,
    meta: { role: 'host' }
  },
  {
    path: '/security',
    name: 'SecurityHome',
    component: SecurityHome,
    meta: { role: 'security' }
  },
  {
    path: '/security/approval',
    name: 'SecurityApproval',
    component: SecurityApproval,
    meta: { role: 'security' }
  },
  {
    path: '/security/records',
    name: 'RiskRecords',
    component: RiskRecords,
    meta: { role: 'security' }
  },
  {
    path: '/security/blacklist',
    name: 'Blacklist',
    component: Blacklist,
    meta: { role: 'security' }
  },
  {
    path: '/admin',
    name: 'AdminHome',
    component: AdminHome,
    meta: { role: 'admin' }
  },
  {
    path: '/admin/areas',
    name: 'AreaManagement',
    component: AreaManagement,
    meta: { role: 'admin' }
  },
  {
    path: '/admin/statistics',
    name: 'Statistics',
    component: Statistics,
    meta: { role: 'admin' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
