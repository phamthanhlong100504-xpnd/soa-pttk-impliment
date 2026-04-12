import React, { useState, useEffect } from "react";
import { useNavigate, useLocation, useParams } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { Header } from "../components/Header";
import { Footer } from "../components/Footer";
import "../styles/booking-form.css";

function formatVND(amount) {
  return new Intl.NumberFormat("vi-VN", { style: "currency", currency: "VND" }).format(amount);
}

export function BookingForm() {
  const navigate = useNavigate();
  const location = useLocation();
  const { slug } = useParams();
  const { token, user } = useAuth();

  // Auth guard — booking requires login
  useEffect(() => {
    if (!token) {
      navigate("/login", { state: { from: `/booking/${slug}` } });
    }
  }, [token, slug, navigate]);

  const bookingState = location.state || {};
  const { tourName = "Tour", schedule = null, tour = null } = bookingState;

  const [form, setForm] = useState({
    full_name: user?.fullName || "",
    phone: "",
    email: user?.email || "",
    adults: 1,
    children: 0,
  });

  const [errors, setErrors] = useState({});

  // Redirect if no schedule selected
  useEffect(() => {
    if (!schedule) {
      navigate(`/tour/${slug}`);
    }
  }, [schedule, slug, navigate]);

  const adultPrice = schedule?.price || 0;
  const childPrice = adultPrice * 0.7;
  const totalPrice = form.adults * adultPrice + form.children * childPrice;

  const validateForm = () => {
    const newErrors = {};
    if (!form.full_name.trim()) newErrors.full_name = "Vui lòng nhập họ tên";
    if (!form.phone.trim()) newErrors.phone = "Vui lòng nhập số điện thoại";
    if (!form.email.trim()) newErrors.email = "Vui lòng nhập email";
    else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email))
      newErrors.email = "Email không đúng định dạng";
    if (form.adults < 1) newErrors.adults = "Cần ít nhất 1 người lớn";
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!validateForm()) return;

    // Mock flow — navigate to Payment without calling API
    navigate("/payment", {
      state: {
        slug,
        tourName,
        schedule_id: schedule.id,
        start_date: schedule.start_date,
        full_name: form.full_name,
        phone: form.phone,
        email: form.email,
        adults: form.adults,
        children: form.children,
        adult_price: adultPrice,
        child_price: childPrice,
        total_price: totalPrice,
      },
    });
  };

  const handleFieldChange = (e) => {
    const { name, value } = e.target;
    const parsed = name === "adults" || name === "children" ? parseInt(value) : value;
    setForm({ ...form, [name]: parsed });
    if (errors[name]) setErrors({ ...errors, [name]: "" });
  };

  const handleBack = () => navigate(`/tour/${slug}`);

  if (!token || !schedule) return null;

  return (
    <div className="booking-form-wrapper">
      <Header />
      <main className="booking-form-main">
        <div className="booking-form-container">
          <div className="booking-form-header">
            <button className="back-button" onClick={handleBack}>
              ← Quay lại tour
            </button>
            <h1>Hoàn tất đặt tour</h1>
          </div>

          <div className="booking-form-content">
            <form className="booking-form" onSubmit={handleSubmit}>
              {/* Contact Information */}
              <fieldset className="form-section">
                <legend>👤 Thông tin liên hệ</legend>

                <div className="form-group">
                  <label htmlFor="full_name">Họ và tên *</label>
                  <div className="input-wrapper">
                    <span className="input-icon">👤</span>
                    <input
                      type="text"
                      id="full_name"
                      name="full_name"
                      value={form.full_name}
                      onChange={handleFieldChange}
                      placeholder="Nguyễn Văn A"
                      className={errors.full_name ? "input-error" : ""}
                    />
                  </div>
                  {errors.full_name && <span className="error-message">{errors.full_name}</span>}
                </div>

                <div className="form-group">
                  <label htmlFor="phone">Số điện thoại *</label>
                  <div className="input-wrapper">
                    <span className="input-icon">📱</span>
                    <input
                      type="tel"
                      id="phone"
                      name="phone"
                      value={form.phone}
                      onChange={handleFieldChange}
                      placeholder="0912345678"
                      className={errors.phone ? "input-error" : ""}
                    />
                  </div>
                  {errors.phone && <span className="error-message">{errors.phone}</span>}
                </div>

                <div className="form-group">
                  <label htmlFor="email">Email *</label>
                  <div className="input-wrapper">
                    <span className="input-icon">✉️</span>
                    <input
                      type="email"
                      id="email"
                      name="email"
                      value={form.email}
                      onChange={handleFieldChange}
                      placeholder="email@example.com"
                      className={errors.email ? "input-error" : ""}
                    />
                  </div>
                  {errors.email && <span className="error-message">{errors.email}</span>}
                </div>
              </fieldset>

              {/* Schedule Information */}
              <fieldset className="form-section">
                <legend>📅 Lựa chọn của bạn</legend>
                <div className="schedule-display">
                  <div className="schedule-item">
                    <span className="label">Tour:</span>
                    <span className="value">{tourName}</span>
                  </div>
                  <div className="schedule-item">
                    <span className="label">Ngày khởi hành:</span>
                    <span className="value">
                      {new Date(schedule.start_date).toLocaleDateString("vi-VN")}
                    </span>
                  </div>
                  <div className="schedule-item">
                    <span className="label">Giá mỗi người lớn:</span>
                    <span className="value">{formatVND(adultPrice)}</span>
                  </div>
                </div>
              </fieldset>

              {/* Guest Count */}
              <fieldset className="form-section">
                <legend>👥 Số lượng khách</legend>
                <div className="traveler-inputs">
                  <div className="form-group">
                    <label htmlFor="adults">Người lớn *</label>
                    <select
                      id="adults"
                      name="adults"
                      value={form.adults}
                      onChange={handleFieldChange}
                      className={errors.adults ? "input-error" : ""}
                    >
                      {[1, 2, 3, 4, 5, 6].map((n) => (
                        <option key={n} value={n}>{n} người</option>
                      ))}
                    </select>
                    {errors.adults && <span className="error-message">{errors.adults}</span>}
                  </div>

                  <div className="form-group">
                    <label htmlFor="children">Trẻ em (0–12 tuổi)</label>
                    <select
                      id="children"
                      name="children"
                      value={form.children}
                      onChange={handleFieldChange}
                    >
                      {[0, 1, 2, 3, 4, 5].map((n) => (
                        <option key={n} value={n}>{n} trẻ</option>
                      ))}
                    </select>
                  </div>
                </div>
              </fieldset>

              <button type="submit" className="submit-button">
                Tiếp tục thanh toán →
              </button>
            </form>

            {/* Order Summary Sidebar */}
            <aside className="order-summary">
              <h3>Tóm tắt đơn hàng</h3>

              {tour?.cover_image_url && (
                <img
                  src={tour.cover_image_url}
                  alt={tourName}
                  className="tour-thumbnail"
                />
              )}

              <div className="tour-summary-text">
                <h4>{tourName}</h4>
                {tour?.duration_days && (
                  <p className="tour-duration">🕐 {tour.duration_days} ngày</p>
                )}
              </div>

              <div className="price-breakdown">
                <div className="breakdown-item">
                  <span className="label">👥 Người lớn ({form.adults})</span>
                  <span className="price">{formatVND(form.adults * adultPrice)}</span>
                </div>
                {form.children > 0 && (
                  <div className="breakdown-item">
                    <span className="label">👧 Trẻ em ({form.children})</span>
                    <span className="price">{formatVND(form.children * childPrice)}</span>
                  </div>
                )}
              </div>

              <div className="summary-divider"></div>

              <div className="summary-total">
                <span>Tổng cộng</span>
                <span className="price">{formatVND(totalPrice)}</span>
              </div>

              <p className="summary-note">💡 Trẻ em được giảm 30% so với giá người lớn</p>
            </aside>
          </div>
        </div>
      </main>
      <Footer />
    </div>
  );
}
