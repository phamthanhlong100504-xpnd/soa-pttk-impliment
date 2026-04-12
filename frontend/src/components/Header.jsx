import React from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import "../styles/header.css";

export function Header() {
  const { isLoggedIn, user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  return (
    <header className="header">
      <div className="header__container">
        <div className="logo" onClick={() => navigate("/")}>
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
          {isLoggedIn ? (
            <div className="header__user-info">
              <span className="header__username">👤 {user?.fullName || user?.email}</span>
              <button className="header__logout-btn" onClick={handleLogout}>
                Đăng xuất
              </button>
            </div>
          ) : (
            <button className="header__login-btn" onClick={() => navigate("/login")}>
              Đăng nhập
            </button>
          )}
        </div>
      </div>
    </header>
  );
}
