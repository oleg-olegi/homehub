import { FormEvent, useEffect, useMemo, useState } from 'react'
import { createExpense, createIncome, deleteExpense, getExpenses, getExpensesSum } from '../api'
import { CATEGORIES, Category, Expense, ExpensePage } from '../types'
import { hasAdminRole } from '../auth'

function today() {
  return new Date().toISOString().slice(0, 10)
}

function firstDayOfMonth() {
  const d = new Date()
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-01`
}

const categoryLabel: Record<Category, string> = {
  SHOPPING: 'Shopping', PHONE: 'Phone', AUTO: 'Auto', FUN: 'Fun',
  CLOTHES: 'Clothes', HOME: 'Home', OTHER: 'Other', FUEL: 'Fuel',
}

export default function DashboardPage() {
  const [page, setPage] = useState<ExpensePage | null>(null)
  const [sum, setSum] = useState(0)
  const [from, setFrom] = useState(firstDayOfMonth())
  const [to, setTo] = useState(today())
  const [category, setCategory] = useState<Category | ''>('')
  const [month, setMonth] = useState(new Date().getMonth() + 1)
  const [pageNumber, setPageNumber] = useState(0)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [showExpense, setShowExpense] = useState(false)
  const [showIncome, setShowIncome] = useState(false)

  const totalPages = page?.totalPages ?? 0
  const totalElements = page?.totalElements ?? 0
  const admin = hasAdminRole()

  async function loadExpenses(targetPage = pageNumber) {
    setLoading(true)
    setError('')
    try {
      const result = await getExpenses({
        from: from || undefined,
        to: to || undefined,
        category: category || undefined,
        page: targetPage,
        size: 10,
      })
      setPage(result)
    } catch (err: any) {
      setError(err.response?.data?.message ?? 'Could not load expenses')
    } finally {
      setLoading(false)
    }
  }

  async function loadSum() {
    try {
      setSum(await getExpensesSum(month))
    } catch {
      setSum(0)
    }
  }

  useEffect(() => { loadExpenses(0); setPageNumber(0) }, [])
  useEffect(() => { loadSum() }, [month])

  const displayed = useMemo(() => page?.content ?? [], [page])

  async function remove(id: number) {
    if (!confirm('Delete this expense?')) return
    try {
      await deleteExpense(id)
      await loadExpenses(pageNumber)
      await loadSum()
    } catch (err: any) {
      setError(err.response?.data?.message ?? 'Could not delete expense')
    }
  }

  async function submitExpense(data: { amount: number; category: Category; date: string; description?: string }) {
    await createExpense(data)
    setShowExpense(false)
    await loadExpenses(0)
    setPageNumber(0)
    await loadSum()
  }

  async function submitIncome(amount: number) {
    await createIncome(amount)
    setShowIncome(false)
  }

  function applyFilters(e: FormEvent) {
    e.preventDefault()
    setPageNumber(0)
    loadExpenses(0)
  }

  return (
    <main className="page">
      <section className="hero">
        <div>
          <h1>Expenses</h1>
          <p className="muted">Track and filter your household spending.</p>
        </div>
        <div className="actions">
          <button className="secondary" onClick={() => setShowIncome(true)}>+ Income</button>
          <button className="primary" onClick={() => setShowExpense(true)}>+ Expense</button>
        </div>
      </section>

      <section className="stats">
        <div className="stat-card">
          <span>Total for selected month</span>
          <strong>{sum.toFixed(2)}</strong>
          <select value={month} onChange={e => setMonth(Number(e.target.value))}>
            {Array.from({length: 12}, (_, i) => <option key={i + 1} value={i + 1}>{new Date(2026, i, 1).toLocaleString('en', {month: 'long'})}</option>)}
          </select>
        </div>
        <div className="stat-card">
          <span>Expenses in current result</span>
          <strong>{totalElements}</strong>
          <small>matching your filters</small>
        </div>
        <div className="stat-card">
          <span>Current page</span>
          <strong>{totalPages ? pageNumber + 1 : 0} / {totalPages}</strong>
          <small>10 expenses per page</small>
        </div>
      </section>

      <section className="panel">
        <form className="filters" onSubmit={applyFilters}>
          <label>From<input type="date" value={from} onChange={e => setFrom(e.target.value)} /></label>
          <label>To<input type="date" value={to} onChange={e => setTo(e.target.value)} /></label>
          <label>Category
            <select value={category} onChange={e => setCategory(e.target.value as Category | '')}>
              <option value="">All categories</option>
              {CATEGORIES.map(c => <option key={c} value={c}>{categoryLabel[c]}</option>)}
            </select>
          </label>
          <div className="filter-actions">
            <button className="primary" type="submit">Apply filters</button>
            <button className="ghost" type="button" onClick={() => {
              setFrom(''); setTo(''); setCategory(''); setPageNumber(0)
              setTimeout(() => loadExpenses(0), 0)
            }}>Reset</button>
          </div>
        </form>
      </section>

      {error && <div className="error page-error">{error}</div>}

      <section className="panel table-panel">
        <div className="table-header">
          <h2>Expense list</h2>
          {loading && <span className="muted">Loading…</span>}
        </div>

        {!loading && displayed.length === 0 ? (
          <div className="empty">No expenses found for the selected filters.</div>
        ) : (
          <div className="table-wrap">
            <table>
              <thead><tr><th>Date</th><th>Category</th><th>Amount</th>{admin && <th></th>}</tr></thead>
              <tbody>
                {displayed.map((expense: Expense) => (
                  <tr key={expense.id}>
                    <td>{expense.date}</td>
                    <td><span className={`badge badge-${expense.category.toLowerCase()}`}>{categoryLabel[expense.category]}</span></td>
                    <td className="amount">{Number(expense.amount).toFixed(2)}</td>
                    {admin && <td><button className="delete" onClick={() => remove(expense.id)}>Delete</button></td>}
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        <div className="pagination">
          <button className="ghost" disabled={!page || page.first} onClick={() => { const p = pageNumber - 1; setPageNumber(p); loadExpenses(p) }}>Previous</button>
          <span>{totalPages ? `${pageNumber + 1} of ${totalPages}` : '—'}</span>
          <button className="ghost" disabled={!page || page.last} onClick={() => { const p = pageNumber + 1; setPageNumber(p); loadExpenses(p) }}>Next</button>
        </div>
      </section>

      {showExpense && <ExpenseModal onClose={() => setShowExpense(false)} onSubmit={submitExpense} />}
      {showIncome && <IncomeModal onClose={() => setShowIncome(false)} onSubmit={submitIncome} />}
    </main>
  )
}

function ExpenseModal({ onClose, onSubmit }: {
  onClose: () => void
  onSubmit: (data: { amount: number; category: Category; date: string; description?: string }) => Promise<void>
}) {
  const [amount, setAmount] = useState('')
  const [category, setCategory] = useState<Category>('OTHER')
  const [date, setDate] = useState(today())
  const [description, setDescription] = useState('')
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState('')

  async function submit(e: FormEvent) {
    e.preventDefault()
    setError('')
    setSaving(true)
    try {
      await onSubmit({ amount: Number(amount), category, date, description: description || undefined })
    } catch (err: any) {
      setError(err.response?.data?.message ?? 'Could not create expense')
    } finally { setSaving(false) }
  }

  return <div className="modal-backdrop"><form className="modal" onSubmit={submit}>
    <div className="modal-title"><h2>New expense</h2><button type="button" className="close" onClick={onClose}>×</button></div>
    <label>Amount<input type="number" min="0.01" step="0.01" value={amount} onChange={e => setAmount(e.target.value)} required /></label>
    <label>Category<select value={category} onChange={e => setCategory(e.target.value as Category)}>{CATEGORIES.map(c => <option key={c}>{c}</option>)}</select></label>
    <label>Date<input type="date" value={date} onChange={e => setDate(e.target.value)} required /></label>
    <label>Description<input value={description} onChange={e => setDescription(e.target.value)} placeholder="Optional" /></label>
    {error && <div className="error">{error}</div>}
    <div className="modal-actions"><button type="button" className="ghost" onClick={onClose}>Cancel</button><button className="primary" disabled={saving}>{saving ? 'Saving…' : 'Save expense'}</button></div>
  </form></div>
}

function IncomeModal({ onClose, onSubmit }: { onClose: () => void; onSubmit: (amount: number) => Promise<void> }) {
  const [amount, setAmount] = useState('')
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState('')
  async function submit(e: FormEvent) {
    e.preventDefault(); setSaving(true); setError('')
    try { await onSubmit(Number(amount)) } catch (err: any) { setError(err.response?.data?.message ?? 'Could not create income') } finally { setSaving(false) }
  }
  return <div className="modal-backdrop"><form className="modal" onSubmit={submit}>
    <div className="modal-title"><h2>New income</h2><button type="button" className="close" onClick={onClose}>×</button></div>
    <label>Amount<input type="number" min="0.01" step="0.01" value={amount} onChange={e => setAmount(e.target.value)} required /></label>
    {error && <div className="error">{error}</div>}
    <div className="modal-actions"><button type="button" className="ghost" onClick={onClose}>Cancel</button><button className="primary" disabled={saving}>{saving ? 'Saving…' : 'Save income'}</button></div>
  </form></div>
}
