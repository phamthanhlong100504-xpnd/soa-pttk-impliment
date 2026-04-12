import React from "react";
import "../styles/header.css";

export function Header() {
  return (
    <header className="header">
      <div className="header__container">
        <div className="logo">
          <span className="logo__icon">✈️</span>
          <span className="logo__text">VietTours</span>
        </div>

        <nav className="nav">
          <a href="/" className="nav__link">
            Trang chủ
          </a>
          <a href="#" className="nav__link">
            Điểm đến
          </a>
          <a href="#" className="nav__link">
            Khuyến mãi
          </a>
          <a href="#" className="nav__link">
            Liên hệ
          </a>
        </nav>

        <div className="header__actions">
          <button className="header__user-btn">👤</button>
        </div>
      </div>
    </header>
  );
}
