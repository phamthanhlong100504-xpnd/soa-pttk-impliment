import React, { useEffect, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { Header } from "../components/Header";
import { Footer } from "../components/Footer";
import { API_BASE_URL } from "../config/api";
import "../styles/success.css";

function formatVND(amount) {
  return new Intl.NumberFormat("vi-VN", { style: "currency", currency: "VND" }).format(amount);
}

export function Success() {
  const navigate = useNavigate();
  const location = useLocation();
  const { token, user } = useAuth();
  const bookingPayload = location.state || null;
  const [pdfLoading, setPdfLoading] = useState(null); // "view" | "download" | null

  useEffect(() => {
    if (!bookingPayload) {
      navigate("/");
    }
  }, [bookingPayload, navigate]);

  if (!bookingPayload) return null;

  const buildPassengerList = () => {
    const list = [];
    for (let i = 0; i < (bookingPayload.adults || 1); i++) {
      list.push({ fullName: i === 0 ? (bookingPayload.full_name || "") : `Người lớn ${i + 1}` });
    }
    for (let i = 0; i < (bookingPayload.children || 0); i++) {
      list.push({ fullName: `Trẻ em ${i + 1}` });
    }
    return list;
  };

  const buildTicketPayload = () => ({
    bookingId: bookingPayload.booking_code || "",
    accountId: user?.id || null,
    tourScheduleId: bookingPayload.schedule_id || null,
    tourName: bookingPayload.tourName || "",
    quantity: (bookingPayload.adults || 1) + (bookingPayload.children || 0),
    confirmedSlots: (bookingPayload.adults || 1) + (bookingPayload.children || 0),
    totalPrice: bookingPayload.total_price || 0,
    passengers: buildPassengerList(),
    optionalServices: [],
  });

  const handleViewPdf = async () => {
    setPdfLoading("view");
    try {
      const res = await fetch(`${API_BASE_URL}/api/v1/documents/booking-tickets/show-ticket`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          ...(token ? { Authorization: `Bearer ${token}` } : {}),
        },
        body: JSON.stringify(buildTicketPayload()),
      });
      if (!res.ok) throw new Error("Không thể tạo PDF");
      const blob = await res.blob();
      const url = URL.createObjectURL(blob);
      window.open(url, "_blank");
    } catch (err) {
      alert(err.message);
    } finally {
      setPdfLoading(null);
    }
  };

  const handleDownloadPdf = async () => {
    setPdfLoading("download");
    try {
      const res = await fetch(`${API_BASE_URL}/api/v1/documents/booking-tickets/file-pdf-ticket`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          ...(token ? { Authorization: `Bearer ${token}` } : {}),
        },
        body: JSON.stringify(buildTicketPayload()),
      });
      if (!res.ok) throw new Error("Không thể tải PDF");
      const blob = await res.blob();
      const url = URL.createObjectURL(blob);
      const a = document.createElement("a");
      a.href = url;
      a.download = `Ve_Tour_${bookingPayload.booking_code || "ticket"}.pdf`;
      a.click();
      URL.revokeObjectURL(url);
    } catch (err) {
      alert(err.message);
    } finally {
      setPdfLoading(null);
    }
  };

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
            <button
              className="secondary-button"
              onClick={handleViewPdf}
              disabled={pdfLoading !== null}
            >
              {pdfLoading === "view" ? "Đang tạo PDF..." : "Xem vé (PDF)"}
            </button>
            <button
              className="secondary-button"
              onClick={handleDownloadPdf}
              disabled={pdfLoading !== null}
            >
              {pdfLoading === "download" ? "Đang tải..." : "Tải vé về máy"}
            </button>
          </div>
        </div>
      </main>
      <Footer />
    </div>
  );
}
