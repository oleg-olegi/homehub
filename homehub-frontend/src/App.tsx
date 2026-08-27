import { Navigate, Route, Routes, Link, useLocation, useNavigate } from 'react-router-dom'
import { useState } from 'react'
import { getToken, logout } from './auth'
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
import DashboardPage from './pages/DashboardPage'

function Protected() {
  return getToken() ? <DashboardPage /> : <Navigate to="/login" replace />
}

function Layout() {
  const navigate = useNavigate()
  const location = useLocation()
  const [menuOpen, setMenuOpen] = useState(false)

  const doLogout = () => {
    logout()
    navigate('/login')
  }

  if (location.pathname === '/login' || location.pathname === '/register') {
    return null
  }

  return (
    <header className="topbar">
      <Link className="brand" to="/">HomeHub</Link>
      <div className="topbar-actions">
        <button className="icon-button" onClick={() => setMenuOpen(v => !v)}>☰</button>
        {menuOpen && (
          <div className="menu">
            <button onClick={doLogout}>Logout</button>
          </div>
        )}
      </div>
    </header>
  )
}

export default function App() {
  return (
    <>
      <Layout />
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/" element={<Protected />} />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </>
  )
}
