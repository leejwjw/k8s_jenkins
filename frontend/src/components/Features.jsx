import React from 'react'

function Features() {
    const features = [
        {
            id: 1,
            icon: '⚡',
            title: '빠른 성능',
            description: 'Vite와 React를 활용한 초고속 개발 환경과 최적화된 빌드 시스템을 제공합니다.'
        },
        {
            id: 2,
            icon: '🎨',
            title: '현대적 디자인',
            description: '글래스모피즘과 그라디언트를 활용한 트렌디한 UI/UX 디자인을 경험하세요.'
        },
        {
            id: 3,
            icon: '🚀',
            title: '쉬운 확장성',
            description: '컴포넌트 기반 아키텍처로 프로젝트를 쉽게 확장하고 유지보수할 수 있습니다.'
        },
        {
            id: 4,
            icon: '💡',
            title: '직관적 사용성',
            description: '사용자 친화적인 인터페이스와 부드러운 애니메이션으로 최상의 경험을 제공합니다.'
        },
        {
            id: 5,
            icon: '🔒',
            title: '안전한 구조',
            description: 'Spring Boot 백엔드와의 완벽한 통합으로 안정적인 서비스를 구축합니다.'
        },
        {
            id: 6,
            icon: '📱',
            title: '반응형 디자인',
            description: '모든 디바이스에서 완벽하게 작동하는 반응형 레이아웃을 제공합니다.'
        }
    ]

    return (
        <div className="features-grid">
            {features.map(feature => (
                <div key={feature.id} className="feature-card">
                    <div className="feature-icon">{feature.icon}</div>
                    <h3 className="feature-title">{feature.title}</h3>
                    <p className="feature-description">{feature.description}</p>
                </div>
            ))}
        </div>
    )
}

export default Features
