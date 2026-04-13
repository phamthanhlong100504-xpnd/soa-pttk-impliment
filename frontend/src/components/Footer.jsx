import React from "react";
import "../styles/footer.css";

export function Footer() {
  return (
    <footer className="footer">
      <div className="footer__container">
        <div className="footer__column">
          <div className="footer__brand">
            <span className="logo__icon">✈️</span>
            <span className="footer__title">VietTours</span>
          </div>
          <p className="footer__desc">
            Khám phá những điểm đến tuyệt vời và tạo những kỷ niệm khó quên cùng chúng tôi.
          </p>
        </div>

        <div className="footer__column">
          <h4>Về chúng tôi</h4>
          <ul>
            <li><a href="#">Giới thiệu</a></li>
            <li><a href="#">Tuyển dụng</a></li>
            <li><a href="#">Điều khoản</a></li>
          </ul>
        </div>

        <div className="footer__column">
          <h4>Hỗ trợ</h4>
          <ul>
            <li><a href="#">Trợ giúp</a></li>
            <li><a href="#">Hoàn hủy</a></li>
            <li><a href="#">FAQ</a></li>
          </ul>
        </div>

        <div className="footer__column">
          <h4>Liên hệ</h4>
          <p className="footer__contact">📞 (84) 123-456-789</p>
          <p className="footer__contact">✉️ support@viet-tours.vn</p>
        </div>
      </div>
    </footer>
  );
}
