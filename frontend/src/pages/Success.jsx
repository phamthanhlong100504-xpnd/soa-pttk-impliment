import React, { useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { Header } from "../components/Header";
import { Footer } from "../components/Footer";
import "../styles/success.css";

function formatVND(amount) {
  return new Intl.NumberFormat("vi-VN", { style: "currency", currency: "VND" }).format(amount);
}

export function Success() {
  const navigate = useNavigate();
  const location = useLocation();
  const bookingPayload = location.state || null;

  useEffect(() => {
    if (!bookingPayload) {
      navigate("/");
    }
  }, [bookingPayload, navigate]);

  if (!bookingPayload) return null;

  return (
    <div className="success-wrapper">
      <Header />
      <main className="success-main">
        <div className="success-container">
          <div className="success-header">
            <div className="success-icon">✓</div>
            <h1>Đặt tour thành công!</h1>
            <p className="success-subtitle">
              Email xác nhận sẽ được gửi đến hộp thư của bạn
            </p>
          </div>

          <div className="booking-code-section">
            <p className="code-label">Mã tham chiếu đặt tour</p>
            <div className="booking-code">{bookingPayload.booking_code}</div>
            <p className="code-hint">Lưu mã này để kiểm tra trạng thái booking</p>
          </div>

          <section className="receipt-summary">
            <h2>Tóm tắt đặt tour</h2>
            <div className="summary-grid">
              <div className="summary-card">
                <h3>Thông tin khách hàng</h3>
                <div className="summary-item">
                  <span className="label">Tên:</span>
                  <span className="value">{bookingPayload.full_name}</span>
                </div>
                <div className="summary-item">
                  <span className="label">Email:</span>
                  <span className="value">{bookingPayload.email}</span>
                </div>
                <div className="summary-item">
                  <span className="label">Số điện thoại:</span>
                  <span className="value">{bookingPayload.phone}</span>
                </div>
              </div>

              <div className="summary-card">
                <h3>Thông tin tour</h3>
                <div className="summary-item">
                  <span className="label">Tour:</span>
                  <span className="value">{bookingPayload.tourName}</span>
                </div>
                <div className="summary-item">
                  <span className="label">Ngày khởi hành:</span>
                  <span className="value">
                    {new Date(bookingPayload.start_date).toLocaleDateString("vi-VN", {
                      weekday: "long",
                      year: "numeric",
                      month: "long",
                      day: "numeric",
                    })}
                  </span>
                </div>
              </div>

              <div className="summary-card">
                <h3>Số lượng khách</h3>
                <div className="summary-item">
                  <span className="label">Người lớn:</span>
                  <span className="value">{bookingPayload.adults}</span>
                </div>
                {bookingPayload.children > 0 && (
                  <div className="summary-item">
                    <span className="label">Trẻ em:</span>
                    <span className="value">{bookingPayload.children}</span>
                  </div>
                )}
              </div>

              <div className="summary-card">
                <h3>Chi tiết thanh toán</h3>
                <div className="summary-item">
                  <span className="label">Phương thức:</span>
                  <span className="value">
                    {bookingPayload.payment_method.replace(/_/g, " ").toUpperCase()}
                  </span>
                </div>
                <div className="summary-item">
                  <span className="label">Giá người lớn:</span>
                  <span className="value">{formatVND(bookingPayload.adult_price)}</span>
                </div>
                {bookingPayload.children > 0 && (
                  <div className="summary-item">
                    <span className="label">Giá trẻ em:</span>
                    <span className="value">{formatVND(bookingPayload.child_price)}</span>
                  </div>
                )}
              </div>

              <div className="summary-card total-card">
                <h3>Số tiền thanh toán</h3>
                <div className="summary-item total">
                  <span className="label">Tổng:</span>
                  <span className="value">{formatVND(bookingPayload.total_price)}</span>
                </div>
              </div>
            </div>
          </section>

          <section className="next-steps">
            <h2>Bước tiếp theo</h2>
            <ol>
              <li>Email xác nhận sẽ được gửi đến <strong>{bookingPayload.email}</strong></li>
              <li>Kiểm tra email để xem chi tiết lịch trình và thông tin du lịch</li>
              <li>Liên hệ hỗ trợ nếu có bất kỳ câu hỏi nào</li>
            </ol>
          </section>

          <div className="success-actions">
            <button className="primary-button" onClick={() => navigate("/")}>
              Trở về Trang chủ
            </button>
            <button className="secondary-button" onClick={() => navigate("/")}>
              Khám phá thêm tour
            </button>
          </div>
        </div>
      </main>
      <Footer />
    </div>
  );
}
