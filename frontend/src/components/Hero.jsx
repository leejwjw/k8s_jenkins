import React from 'react'

function Hero() {
    return (
        <section className="hero-section">
            <h1 className="hero-title">
                React로 만드는<br />
                현대적인 웹 애플리케이션
            </h1>
            <p className="hero-subtitle">
                아름다운 디자인과 강력한 기능을 결합한 테스트 프로젝트입니다.
                최신 웹 기술을 활용하여 사용자 경험을 극대화합니다.
            </p>
            <div className="button-group">
                <button className="btn btn-primary">
                    <span>시작하기</span>
                    <span>→</span>
                </button>
                <button className="btn btn-secondary">
                    <span>더 알아보기</span>
                </button>
            </div>
        </section>
    )
}

export default Hero
