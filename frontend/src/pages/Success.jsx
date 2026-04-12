import React, { useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { Header } from "../components/Header";
import { Footer } from "../components/Footer";
import "../styles/success.css";

export function Success() {
  const navigate = useNavigate();
  const location = useLocation();
  const bookingPayload = location.state || null;

  // Redirect to home if no booking data (refresh scenario)
  useEffect(() => {
    if (!bookingPayload) {
      navigate("/");
    }
  }, [bookingPayload, navigate]);

  if (!bookingPayload) return null;

  const handleBackHome = () => {
    navigate("/");
  };

  const handleNewBooking = () => {
    navigate("/");
  };

  return (
    <div className="success-wrapper">
      <Header />
      <main className="success-main">
        <div className="success-container">
          {/* Success Icon & Heading */}
          <div className="success-header">
            <div className="success-icon">✓</div>
            <h1>Đặt tour thành công!</h1>
            <p className="success-subtitle">
              Email xác nhận sẽ được gửi đến hộp thư của bạn
            </p>
          </div>

          {/* Booking Code */}
          <div className="booking-code-section">
            <p className="code-label">Mã tham chiếu đặt tour</p>
            <div className="booking-code">{bookingPayload.booking_code}</div>
            <p className="code-hint">
              Lưu mã này để kiểm tra trạng thái booking của bạn
            </p>
          </div>

          {/* Receipt Summary */}
          <section className="receipt-summary">
            <h2>Tóm tắt đặt tour</h2>
            <div className="summary-grid">
              {/* Customer Info */}
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

              {/* Tour Details */}
              <div className="summary-card">
                <h3>Thông tin tour</h3>
                <div className="summary-item">
                  <span className="label">Tour:</span>
                  <span className="value">{bookingPayload.tourName}</span>
                </div>
                <div className="summary-item">
                  <span className="label">Ngày khởi hành:</span>
                  <span className="value">
                    {new Date(
                      bookingPayload.start_date
                    ).toLocaleDateString("vi-VN", {
                      weekday: "long",
                      year: "numeric",
                      month: "long",
                      day: "numeric",
                    })}
                  </span>
                </div>
                <div className="summary-item">
                  <span className="label">ID lịch:</span>
                  <span className="value">{bookingPayload.schedule_id}</span>
                </div>
              </div>

              {/* Travelers */}
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

              {/* Payment Details */}
              <div className="summary-card">
                <h3>Chi tiết thanh toán</h3>
                <div className="summary-item">
                  <span className="label">Phương thức:</span>
                  <span className="value">
                    {bookingPayload.payment_method
                      .replace(/_/g, " ")
                      .toUpperCase()}
                  </span>
                </div>
                <div className="summary-item">
                  <span className="label">Giá người lớn:</span>
                  <span className="value">
                    ${bookingPayload.adult_price.toFixed(2)}
                  </span>
                </div>
                {bookingPayload.children > 0 && (
                  <div className="summary-item">
                    <span className="label">Giá trẻ em:</span>
                    <span className="value">
                      ${bookingPayload.child_price.toFixed(2)}
                    </span>
                  </div>
                )}
              </div>

              {/* Total Amount */}
              <div className="summary-card total-card">
                <h3>Số tiền thanh toán</h3>
                <div className="summary-item total">
                  <span className="label">Tổng:</span>
                  <span className="value">
                    ${bookingPayload.total_price.toFixed(2)}
                  </span>
                </div>
              </div>
            </div>
          </section>

          {/* Next Steps */}
          <section className="next-steps">
            <h2>Bước tiếp theo</h2>
            <ol>
              <li>
                Email xác nhận sẽ được gửi đến{" "}
                <strong>{bookingPayload.email}</strong>
              </li>
              <li>Kiểm tra email để xem chi tiết lịch trình và thông tin du lịch</li>
              <li>Liên hệ với đội hỗ trợ nếu bạn có bất kỳ câu hỏi nào</li>
            </ol>
          </section>

          {/* CTA Buttons */}
          <div className="success-actions">
            <button
              className="primary-button"
              onClick={handleBackHome}
            >
              Trở về Trang chủ
            </button>
            <button
              className="secondary-button"
              onClick={handleNewBooking}
            >
              Khám phá thêm tour
            </button>
          </div>

          {/* Contact Info */}
          <section className="contact-support">
            <h3>Cần hỗ trợ?</h3>
            <p>
              Liên hệ đội hỗ trợ của chúng tôi tại{" "}
              <a href="mailto:support@traveltours.com">
                support@traveltours.com
              </a>{" "}
              hoặc gọi 1-800-TOURS-123
            </p>
          </section>
        </div>
      </main>
      <Footer />
    </div>
  );
}
