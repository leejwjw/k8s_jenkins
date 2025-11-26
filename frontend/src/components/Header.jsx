import React from 'react'

function Header() {
    return (
        <header className="header">
            <div className="container">
                <div className="header-content">
                    <h1 className="logo">l0gin</h1>
                    <nav>
                        <ul className="nav-links">
                            <li><a href="#home" className="nav-link">홈</a></li>
                            <li><a href="#features" className="nav-link">기능</a></li>
                            <li><a href="#about" className="nav-link">소개</a></li>
                            <li><a href="#contact" className="nav-link">연락</a></li>
                        </ul>
                    </nav>
                </div>
            </div>
        </header>
    )
}

export default Header
