export const CATEGORIES = [
  'SHOPPING',
  'PHONE',
  'AUTO',
  'FUN',
  'CLOTHES',
  'HOME',
  'OTHER',
  'FUEL',
] as const

export type Category = typeof CATEGORIES[number]

export interface Expense {
  id: number
  amount: number | string
  category: Category
  date: string
}

export interface ExpensePage {
  content: Expense[]
  totalPages: number
  totalElements: number
  number: number
  size: number
  first: boolean
  last: boolean
}

export interface LoginResponse {
  token: string
}

export interface LoginRequest {
  email: string
  password: string
}

export interface RegisterRequest {
  username: string
  email: string
  password: string
}
