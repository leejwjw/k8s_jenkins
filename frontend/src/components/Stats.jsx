import React, { useState, useEffect } from 'react'

function Stats() {
    const [counts, setCounts] = useState({
        projects: 0,
        users: 0,
        performance: 0
    })

    useEffect(() => {
        const targets = {
            projects: 150,
            users: 10000,
            performance: 99
        }

        const duration = 2000 // 2 seconds
        const steps = 60
        const interval = duration / steps

        let step = 0
        const timer = setInterval(() => {
            step++
            const progress = step / steps

            setCounts({
                projects: Math.floor(targets.projects * progress),
                users: Math.floor(targets.users * progress),
                performance: Math.floor(targets.performance * progress)
            })

            if (step >= steps) {
                setCounts(targets)
                clearInterval(timer)
            }
        }, interval)

        return () => clearInterval(timer)
    }, [])

    return (
        <div className="stats-section">
            <div className="stat-item">
                <span className="stat-value">{counts.projects}+</span>
                <span className="stat-label">프로젝트</span>
            </div>
            <div className="stat-item">
                <span className="stat-value">{counts.users.toLocaleString()}+</span>
                <span className="stat-label">사용자</span>
            </div>
            <div className="stat-item">
                <span className="stat-value">{counts.performance}%</span>
                <span className="stat-label">성능 점수</span>
            </div>
        </div>
    )
}

export default Stats
