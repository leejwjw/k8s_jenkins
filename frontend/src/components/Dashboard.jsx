import React, { useState, useEffect } from 'react'
import './Dashboard.css'
import API_BASE_URL from '../config'

function Dashboard({ user, onLogout }) {
    const [users, setUsers] = useState([])
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState('')

    useEffect(() => {
        fetchUsers()
    }, [])

    const fetchUsers = async () => {
        setLoading(true)
        setError('')

        try {
            const token = localStorage.getItem('token')
            const response = await fetch(`${API_BASE_URL}/api/users`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            })

            if (!response.ok) {
                throw new Error('사용자 목록 조회에 실패했습니다')
            }

            const data = await response.json()
            setUsers(data)

        } catch (err) {
            setError(err.message)
        } finally {
            setLoading(false)
        }
    }

    const handleDeleteUser = async (userId) => {
        if (!confirm('정말 이 사용자를 삭제하시겠습니까?')) {
            return
        }

        try {
            const token = localStorage.getItem('token')
            const response = await fetch(`${API_BASE_URL}/api/users/${userId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            })

            if (!response.ok) {
                throw new Error('사용자 삭제에 실패했습니다')
            }

            fetchUsers()

        } catch (err) {
            setError(err.message)
        }
    }

    const handleLogout = () => {
        localStorage.removeItem('token')
        localStorage.removeItem('user')
        onLogout()
    }

    return (
        <div className="dashboard">
            <header className="dashboard-header">
                <h1>l0gin</h1>
                <div className="user-info">
                    <span>{user.username}님</span>
                    <button onClick={handleLogout}>로그아웃</button>
                </div>
            </header>

            <div className="dashboard-content">
                <div className="user-card">
                    <h2>내 정보</h2>
                    <div className="info-row">
                        <span className="label">사용자명:</span>
                        <span>{user.username}</span>
                    </div>
                    <div className="info-row">
                        <span className="label">이메일:</span>
                        <span>{user.email}</span>
                    </div>
                    <div className="info-row">
                        <span className="label">이름:</span>
                        <span>{user.name || '미설정'}</span>
                    </div>
                    <div className="info-row">
                        <span className="label">권한:</span>
                        <span className="badge">{user.role}</span>
                    </div>
                </div>

                <div className="users-card">
                    <div className="card-header">
                        <h2>전체 사용자 목록</h2>
                        <button onClick={fetchUsers} disabled={loading}>
                            {loading ? '로딩 중...' : '새로고침'}
                        </button>
                    </div>

                    {error && <div className="error">{error}</div>}

                    {users.length > 0 ? (
                        <table>
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>사용자명</th>
                                    <th>이메일</th>
                                    <th>이름</th>
                                    <th>권한</th>
                                    <th>작업</th>
                                </tr>
                            </thead>
                            <tbody>
                                {users.map(u => (
                                    <tr key={u.id}>
                                        <td>{u.id}</td>
                                        <td>{u.username}</td>
                                        <td>{u.email}</td>
                                        <td>{u.name || '-'}</td>
                                        <td><span className="badge">{u.role}</span></td>
                                        <td>
                                            <button
                                                onClick={() => handleDeleteUser(u.id)}
                                                className="btn-delete"
                                            >
                                                삭제
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    ) : (
                        <div className="empty-state">
                            {loading ? '로딩 중...' : '사용자가 없습니다'}
                        </div>
                    )}
                </div>
            </div>
        </div>
    )
}

export default Dashboard
