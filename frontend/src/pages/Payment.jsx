import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { Header } from "../components/Header";
import { Footer } from "../components/Footer";
import { API_BASE_URL } from "../config/api";
import "../styles/payment.css";

function formatVND(amount) {
  return new Intl.NumberFormat("vi-VN", { style: "currency", currency: "VND" }).format(amount);
}

const PAYMENT_METHODS = [
  { id: "credit_card", name: "Thẻ tín dụng / Ghi nợ", icon: "💳", description: "Visa, Mastercard, Amex" },
  { id: "bank_transfer", name: "Chuyển khoản Ngân hàng", icon: "🏦", description: "Chuyển khoản trực tiếp" },
  { id: "vnpay", name: "VNPay", icon: "📱", description: "Ví điện tử VNPay" },
  { id: "momo", name: "MoMo", icon: "🔴", description: "Ví điện tử MoMo" },
];

const POLL_INTERVAL = 2000;
const POLL_MAX_ATTEMPTS = 15;

function buildPassengerRequests(fullName, adults, children) {
  const passengers = [];
  for (let i = 0; i < adults; i++) {
    passengers.push({
      fullName: i === 0 ? fullName : `Người lớn ${i + 1}`,
      dateOfBirth: "1990-01-01",
      passengerType: "ADULT",
    });
  }
  for (let i = 0; i < children; i++) {
    passengers.push({
      fullName: `Trẻ em ${i + 1}`,
      dateOfBirth: "2015-01-01",
      passengerType: "CHILD",
    });
  }
  return passengers;
}

export function Payment() {
  const navigate = useNavigate();
  const location = useLocation();
  const { token, user } = useAuth();
  const bookingPayload = location.state || null;

  const [selectedMethod, setSelectedMethod] = useState("credit_card");
  const [isProcessing, setIsProcessing] = useState(false);
  const [error, setError] = useState(null);

  if (!bookingPayload) {
    navigate("/");
    return null;
  }

  const handlePay = async (e) => {
    e.preventDefault();
    setIsProcessing(true);
    setError(null);

    const idempotencyKey = crypto.randomUUID();

    const body = {
      idempotencyKey,
      tourScheduleId: bookingPayload.schedule_id,
      accountId: user?.id,
      tourName: bookingPayload.tourName,
      quantity: (bookingPayload.adults || 1) + (bookingPayload.children || 0),
      totalPrice: bookingPayload.total_price,
      email: bookingPayload.email,
      passengerRequests: buildPassengerRequests(
        bookingPayload.full_name,
        bookingPayload.adults || 1,
        bookingPayload.children || 0
      ),
      bookingOptionalServiceRequests: [],
    };

    try {
      const res = await fetch(`${API_BASE_URL}/api/v1/booking-tour/booking`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(body),
      });

      if (!res.ok) {
        const err = await res.json().catch(() => ({}));
        throw new Error(err.message || `Lỗi tạo booking (${res.status})`);
      }

      // Demo: bỏ qua polling, navigate thẳng success
      navigate("/success", {
        state: {
          ...bookingPayload,
          booking_code: idempotencyKey,
          payment_method: selectedMethod,
        },
      });
    } catch (err) {
      setError(err.message);
      setIsProcessing(false);
    }
  };

  return (
    <div className="payment-wrapper">
      <Header />
      <main className="payment-main">
        <div className="payment-container">
          <div className="payment-header">
            <button className="back-button" onClick={() => navigate(-1)}>
              ← Quay lại
            </button>
            <h1>Thanh toán</h1>
          </div>

          <div className="payment-content">
            {/* Payment Summary */}
            <section className="payment-summary">
              <h2>Tóm tắt thanh toán</h2>
              <div className="summary-details">
                <div className="summary-row">
                  <span className="label">Tour:</span>
                  <span className="value">{bookingPayload.tourName}</span>
                </div>
                <div className="summary-row">
                  <span className="label">Ngày khởi hành:</span>
                  <span className="value">
                    {new Date(bookingPayload.start_date).toLocaleDateString("vi-VN")}
                  </span>
                </div>
                <div className="summary-row">
                  <span className="label">Số khách:</span>
                  <span className="value">
                    {bookingPayload.adults} người lớn
                    {bookingPayload.children > 0 ? `, ${bookingPayload.children} trẻ em` : ""}
                  </span>
                </div>
                <div className="summary-separator"></div>
                <div className="summary-row total">
                  <span className="label">Tổng cần thanh toán:</span>
                  <span className="value">{formatVND(bookingPayload.total_price)}</span>
                </div>
              </div>
            </section>

            {/* Payment Method */}
            <section className="payment-methods">
              <h2>Chọn phương thức thanh toán</h2>


              {error && <div className="payment-error">{error}</div>}

              <form onSubmit={handlePay}>
                <div className="methods-list">
                  {PAYMENT_METHODS.map((method) => (
                    <label
                      key={method.id}
                      className={`method-option ${selectedMethod === method.id ? "selected" : ""}`}
                    >
                      <input
                        type="radio"
                        name="payment_method"
                        value={method.id}
                        checked={selectedMethod === method.id}
                        onChange={() => setSelectedMethod(method.id)}
                      />
                      <div className="method-content">
                        <span className="method-icon">{method.icon}</span>
                        <div className="method-info">
                          <div className="method-name">{method.name}</div>
                          <div className="method-description">{method.description}</div>
                        </div>
                      </div>
                    </label>
                  ))}
                </div>

                <button type="submit" className="pay-button" disabled={isProcessing}>
                  {isProcessing ? (
                    <><span className="spinner"></span> Đang xử lý...</>
                  ) : (
                    `Xác nhận thanh toán ${formatVND(bookingPayload.total_price)}`
                  )}
                </button>
              </form>
            </section>
          </div>
        </div>
      </main>
      <Footer />
    </div>
  );
}
