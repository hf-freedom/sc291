export const formatDate = (date) => {
  if (!date) return ''
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  return `${year}-${month}-${day} ${hours}:${minutes}`
}

export const formatDateTime = (date) => {
  return formatDate(date)
}

export const getStatusClass = (status) => {
  const classMap = {
    'PENDING': 'tag-pending',
    'APPROVED': 'tag-approved',
    'REJECTED': 'tag-rejected',
    'CANCELLED': 'tag-rejected',
    'EXPIRED': 'tag-expired',
    'ACTIVE': 'tag-active',
    'PENDING_ACTIVATION': 'tag-pending'
  }
  return classMap[status] || 'tag-pending'
}

export const getStatusText = (status) => {
  const textMap = {
    'PENDING': '待审批',
    'APPROVED': '已批准',
    'REJECTED': '已拒绝',
    'CANCELLED': '已取消',
    'EXPIRED': '已过期',
    'ACTIVE': '已激活',
    'PENDING_ACTIVATION': '待激活'
  }
  return textMap[status] || status
}

export const getRiskLevelText = (level) => {
  const textMap = {
    'LOW': '低风险',
    'MEDIUM': '中风险',
    'HIGH': '高风险'
  }
  return textMap[level] || level
}

export const getRiskLevelClass = (level) => {
  const classMap = {
    'LOW': 'tag-approved',
    'MEDIUM': 'tag-warning',
    'HIGH': 'tag-danger'
  }
  return classMap[level] || 'tag-pending'
}
