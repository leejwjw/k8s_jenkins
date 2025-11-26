import React, { useState, useEffect } from 'react'
import AuthPage from './components/AuthPage'
import Dashboard from './components/Dashboard'

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false)
  const [user, setUser] = useState(null)

  useEffect(() => {
    // 페이지 로드 시 토큰 확인
    const token = localStorage.getItem('token')
    const savedUser = localStorage.getItem('user')

    if (token && savedUser) {
      setIsAuthenticated(true)
      setUser(JSON.parse(savedUser))
    }
  }, [])

  const handleLoginSuccess = (data) => {
    setIsAuthenticated(true)
    setUser(data.user)
  }

  const handleLogout = () => {
    setIsAuthenticated(false)
    setUser(null)
  }

  if (isAuthenticated && user) {
    return <Dashboard user={user} onLogout={handleLogout} />
  }

  return <AuthPage onLoginSuccess={handleLoginSuccess} />
}

export default App
