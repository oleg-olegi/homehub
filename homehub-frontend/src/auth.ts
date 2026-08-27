export function getToken() {
  return localStorage.getItem('homehub_token')
}

export function isAuthenticated() {
  return Boolean(getToken())
}

function parseJwt(token: string): Record<string, unknown> | null {
  try {
    const payload = token.split('.')[1]
    const normalized = payload.replace(/-/g, '+').replace(/_/g, '/')
    return JSON.parse(atob(normalized))
  } catch {
    return null
  }
}

export function hasAdminRole() {
  const token = getToken()
  if (!token) return false
  const payload = parseJwt(token)
  if (!payload) return false

  const roles = payload.roles ?? payload.authorities ?? payload.role
  if (Array.isArray(roles)) return roles.includes('ADMIN') || roles.includes('ROLE_ADMIN')
  if (typeof roles === 'string') return roles.includes('ADMIN')
  return false
}

export function logout() {
  localStorage.removeItem('homehub_token')
}
