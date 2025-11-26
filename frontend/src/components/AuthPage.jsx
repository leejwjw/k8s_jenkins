import React, { useState } from 'react'
import './AuthPage.css'
import API_BASE_URL from '../config'

function AuthPage({ onLoginSuccess }) {
    const [isLogin, setIsLogin] = useState(true)
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState('')

    const [loginForm, setLoginForm] = useState({
        username: '',
        password: ''
    })

    const [signupForm, setSignupForm] = useState({
        username: '',
        email: '',
        password: '',
        name: ''
    })

    const handleLoginChange = (e) => {
        setLoginForm({
            ...loginForm,
            [e.target.name]: e.target.value
        })
        setError('')
    }

    const handleSignupChange = (e) => {
        setSignupForm({
            ...signupForm,
            [e.target.name]: e.target.value
        })
        setError('')
    }

    const handleLogin = async (e) => {
        e.preventDefault()
        setLoading(true)
        setError('')

        try {
            const response = await fetch(`${API_BASE_URL}/api/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(loginForm)
            })

            if (!response.ok) {
                const errorData = await response.json()
                throw new Error(errorData.message || '로그인에 실패했습니다')
            }

            const data = await response.json()
            localStorage.setItem('token', data.token)
            localStorage.setItem('user', JSON.stringify(data.user))
            onLoginSuccess(data)

        } catch (err) {
            setError(err.message)
        } finally {
            setLoading(false)
        }
    }

    const handleSignup = async (e) => {
        e.preventDefault()
        setLoading(true)
        setError('')

        try {
            const response = await fetch(`${API_BASE_URL}/api/auth/signup`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(signupForm)
            })

            if (!response.ok) {
                const errorData = await response.json()
                throw new Error(errorData.message || '회원가입에 실패했습니다')
            }

            const data = await response.json()
            localStorage.setItem('token', data.token)
            localStorage.setItem('user', JSON.stringify(data.user))
            onLoginSuccess(data)

        } catch (err) {
            setError(err.message)
        } finally {
            setLoading(false)
        }
    }

    return (
        <div className="auth-page">
            <div className="auth-container">
                <h1>l0gin</h1>

                <div className="auth-tabs">
                    <button
                        className={isLogin ? 'active' : ''}
                        onClick={() => {
                            setIsLogin(true)
                            setError('')
                        }}
                    >
                        로그인
                    </button>
                    <button
                        className={!isLogin ? 'active' : ''}
                        onClick={() => {
                            setIsLogin(false)
                            setError('')
                        }}
                    >
                        회원가입
                    </button>
                </div>

                {error && <div className="error">{error}</div>}

                {isLogin ? (
                    <form onSubmit={handleLogin}>
                        <input
                            type="text"
                            name="username"
                            value={loginForm.username}
                            onChange={handleLoginChange}
                            placeholder="사용자명"
                            required
                        />
                        <input
                            type="password"
                            name="password"
                            value={loginForm.password}
                            onChange={handleLoginChange}
                            placeholder="비밀번호"
                            required
                        />
                        <button type="submit" disabled={loading}>
                            {loading ? '로그인 중...' : '로그인'}
                        </button>
                    </form>
                ) : (
                    <form onSubmit={handleSignup}>
                        <input
                            type="text"
                            name="username"
                            value={signupForm.username}
                            onChange={handleSignupChange}
                            placeholder="사용자명"
                            required
                            minLength={3}
                            maxLength={50}
                        />
                        <input
                            type="email"
                            name="email"
                            value={signupForm.email}
                            onChange={handleSignupChange}
                            placeholder="이메일"
                            required
                        />
                        <input
                            type="password"
                            name="password"
                            value={signupForm.password}
                            onChange={handleSignupChange}
                            placeholder="비밀번호 (최소 6자)"
                            required
                            minLength={6}
                        />
                        <input
                            type="text"
                            name="name"
                            value={signupForm.name}
                            onChange={handleSignupChange}
                            placeholder="이름 (선택)"
                        />
                        <button type="submit" disabled={loading}>
                            {loading ? '회원가입 중...' : '회원가입'}
                        </button>
                    </form>
                )}
            </div>
        </div>
    )
}

export default AuthPage
