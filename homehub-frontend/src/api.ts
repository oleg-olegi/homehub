import axios from 'axios'
import type { Category, ExpensePage, LoginRequest, LoginResponse, RegisterRequest } from './types'

const api = axios.create({
  baseURL: `${import.meta.env.VITE_API_URL}/api/v1`,
  headers: {
    'Content-Type': 'application/json',
  },
})

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('homehub_token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('homehub_token')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  },
)

export async function login(data: LoginRequest) {
  const { data: result } = await api.post<LoginResponse>('/auth/login', data)
  return result
}

export async function register(data: RegisterRequest) {
  return api.post('/auth/register', data)
}

export async function getExpenses(params: {
  from?: string
  to?: string
  category?: Category
  page: number
  size: number
}) {
  const { data } = await api.get<ExpensePage>('/expenses', { params })
  return data
}

export async function getExpensesSum(month: number) {
  const { data } = await api.get<number | string>('/expenses/sum', { params: { month } })
  return Number(data)
}

export async function createExpense(data: {
  amount: number
  category: Category
  date: string
  description?: string
}) {
  return api.post('/expenses/create', data)
}

export async function deleteExpense(id: number) {
  return api.delete(`/expenses/${id}`)
}

export async function createIncome(amount: number) {
  return api.post('/income/create', { amount })
}
