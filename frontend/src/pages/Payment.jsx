import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { Header } from "../components/Header";
import { Footer } from "../components/Footer";
import "../styles/payment.css";

export function Payment() {
  const navigate = useNavigate();
  const location = useLocation();
  const bookingPayload = location.state || null;

  const [selectedMethod, setSelectedMethod] = useState("credit_card");
  const [isProcessing, setIsProcessing] = useState(false);
  const [paymentError, setPaymentError] = useState(null);

  // Redirect if no booking data
  if (!bookingPayload) {
    navigate("/");
    return null;
  }

  const handlePaymentMethodChange = (method) => {
    setSelectedMethod(method);
    setPaymentError(null);
  };

  const handlePay = async (e) => {
    e.preventDefault();
    setIsProcessing(true);
    setPaymentError(null);

    // Simulate payment processing delay
    setTimeout(() => {
      // Simulate success (95% chance)
      const isSuccess = Math.random() < 0.95;

      if (isSuccess) {
        // Generate booking code
        const bookingCode = `BK${Date.now().toString().slice(-8)}`;
        const successPayload = {
          ...bookingPayload,
          booking_code: bookingCode,
          payment_method: selectedMethod,
        };
        navigate("/success", { state: successPayload });
      } else {
        setPaymentError(
          "Payment failed. Please try again or use a different method."
        );
        setIsProcessing(false);
      }
    }, 2000);
  };

  const handleBack = () => {
    navigate(-1);
  };

  return (
    <div className="payment-wrapper">
      <Header />
      <main className="payment-main">
        <div className="payment-container">
          {/* Header with Back Button */}
          <div className="payment-header">
            <button className="back-button" onClick={handleBack}>
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
                    {new Date(
                      bookingPayload.start_date
                    ).toLocaleDateString('vi-VN')}
                  </span>
                </div>
                <div className="summary-row">
                  <span className="label">Số lượng khách:</span>
                  <span className="value">
                    {bookingPayload.adults} Người lớn
                    {bookingPayload.children > 0
                      ? `, ${bookingPayload.children} Trẻ em`
                      : ""}
                  </span>
                </div>
                <div className="summary-separator"></div>
                <div className="summary-row total">
                  <span className="label">Tổng cần thanh toán:</span>
                  <span className="value">
                    ${bookingPayload.total_price.toFixed(2)}
                  </span>
                </div>
              </div>
            </section>

            {/* Payment Method Selection */}
            <section className="payment-methods">
              <h2>Chọn phương thức thanh toán</h2>

              <div className="demo-note">
                <strong>Chế độ Demo:</strong> Đây là luồng thanh toán mô phỏng.
                Sẽ không có yêu cầu thanh toán thực tế.
              </div>

              <div className="methods-list">
                {[
                  {
                    id: "credit_card",
                    name: "Thẻ tín dụng / Ghi nợ",
                    icon: "💳",
                    description: "Visa, Mastercard, Amex",
                  },
                  {
                    id: "debit_card",
                    name: "Thẻ Ghi nợ Ngân hàng",
                    icon: "🏦",
                    description: "Thẻ ghi nợ ngân hàng",
                  },
                  {
                    id: "bank_transfer",
                    name: "Chuyển khoản Ngân hàng",
                    icon: "🔄",
                    description: "Chuyển khoản trực tiếp",
                  },
                  {
                    id: "vnpay",
                    name: "VNPay",
                    icon: "📱",
                    description: "Ví điện tử VNPay",
                  },
                ].map((method) => (
                  <label
                    key={method.id}
                    className={`method-option ${
                      selectedMethod === method.id ? "selected" : ""
                    }`}
                  >
                    <input
                      type="radio"
                      name="payment_method"
                      value={method.id}
                      checked={selectedMethod === method.id}
                      onChange={() => handlePaymentMethodChange(method.id)}
                    />
                    <div className="method-content">
                      <span className="method-icon">{method.icon}</span>
                      <div className="method-info">
                        <div className="method-name">{method.name}</div>
                        <div className="method-description">
                          {method.description}
                        </div>
                      </div>
                    </div>
                  </label>
                ))}
              </div>

              {paymentError && (
                <div className="error-alert">{paymentError}</div>
              )}

              <form onSubmit={handlePay}>
                <button
                  type="submit"
                  className="pay-button"
                  disabled={isProcessing}
                >
                  {isProcessing ? (
                    <>
                      <span className="spinner"></span>
                      Đang xử lý thanh toán...
                    </>
                  ) : (
                    `Thanh toán $${bookingPayload.total_price.toFixed(2)}`
                  )}
                </button>
              </form>
            </section>

            {/* Additional Info */}
            <section className="payment-info">
              <h3>Cần trợ giúp?</h3>
              <ul>
                <li>Liên hệ hỗ trợ: support@traveltours.com</li>
                <li>
                  Trong chế độ demo, không có yêu cầu thanh toán thực tế
                </li>
              </ul>
            </section>
          </div>
        </div>
      </main>
      <Footer />
    </div>
  );
}
