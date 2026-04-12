import React, { useState, useEffect } from "react";
import { useNavigate, useLocation, useParams } from "react-router-dom";
import { Header } from "../components/Header";
import { Footer } from "../components/Footer";
import "../styles/booking-form.css";

export function BookingForm() {
  const navigate = useNavigate();
  const location = useLocation();
  const { slug } = useParams();

  // Fallback if no state is passed
  const bookingState = location.state || {};
  const { tourName = "Tour", schedule = null, tour = null } = bookingState;
  const mockTour = {
    cover_image_url:
      "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=800&q=80",
    duration_days: 3,
  };

  const [form, setForm] = useState({
    full_name: "",
    phone: "",
    email: "",
    adults: 1,
    children: 0,
  });

  const [errors, setErrors] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false);

  // Redirect if no schedule selected
  useEffect(() => {
    if (!schedule) {
      navigate(`/tour/${slug}`, {
        state: { error: "Please select a schedule first" },
      });
    }
  }, [schedule, slug, navigate]);

  // Calculate pricing
  const adultPrice = schedule?.price || 0;
  const childPrice = adultPrice * 0.7;
  const totalPrice =
    form.adults * adultPrice + form.children * childPrice;

  // Form validation
  const validateForm = () => {
    const newErrors = {};
    if (!form.full_name.trim()) newErrors.full_name = "Name is required";
    if (!form.phone.trim()) newErrors.phone = "Phone is required";
    if (!form.email.trim()) newErrors.email = "Email is required";
    else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email))
      newErrors.email = "Invalid email format";
    if (form.adults < 1) newErrors.adults = "At least 1 adult required";
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validateForm()) return;

    setIsSubmitting(true);
    // Simulate API call delay
    setTimeout(() => {
      const bookingPayload = {
        slug,
        tourName,
        schedule_id: schedule.schedule_id,
        start_date: schedule.start_date,
        full_name: form.full_name,
        phone: form.phone,
        email: form.email,
        adults: form.adults,
        children: form.children,
        adult_price: adultPrice,
        child_price: childPrice,
        total_price: totalPrice,
      };
      navigate("/payment", { state: bookingPayload });
      setIsSubmitting(false);
    }, 500);
  };

  const handleFieldChange = (e) => {
    const { name, value } = e.target;
    const numValue =
      name === "adults" || name === "children" ? parseInt(value) : value;
    setForm({ ...form, [name]: numValue });
    // Clear error on field change
    if (errors[name]) {
      setErrors({ ...errors, [name]: "" });
    }
  };

  const handleBack = () => {
    navigate(`/tour/${slug}`);
  };

  if (!schedule) return null;

  return (
    <div className="booking-form-wrapper">
      <Header />
      <main className="booking-form-main">
        <div className="booking-form-container">
          {/* Header with Back Button */}
          <div className="booking-form-header">
            <button className="back-button" onClick={handleBack}>
              ← Back to Tour
            </button>
            <h1>Complete Your Booking</h1>
          </div>

          <div className="booking-form-content">
            {/* Form Section */}
            <form className="booking-form" onSubmit={handleSubmit}>
              {/* Contact Information */}
              <fieldset className="form-section">
                <legend>👤 Thông tin liên hệ & Đặt chỗ</legend>
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
                  {errors.full_name && (
                    <span className="error-message">
                      {errors.full_name}
                    </span>
                  )}
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
                      placeholder="+84 9 1234 5678"
                      className={errors.phone ? "input-error" : ""}
                    />
                  </div>
                  {errors.phone && (
                    <span className="error-message">{errors.phone}</span>
                  )}
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
                  {errors.email && (
                    <span className="error-message">{errors.email}</span>
                  )}
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
                      {new Date(
                        schedule.start_date
                      ).toLocaleDateString('vi-VN')}
                    </span>
                  </div>
                  <div className="schedule-item">
                    <span className="label">Giá mỗi người lớn:</span>
                    <span className="value">
                      ${adultPrice.toFixed(2)}
                    </span>
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
                      {[1, 2, 3, 4, 5, 6].map((num) => (
                        <option key={num} value={num}>
                          {num} người
                        </option>
                      ))}
                    </select>
                    {errors.adults && (
                      <span className="error-message">
                        {errors.adults}
                      </span>
                    )}
                  </div>

                  <div className="form-group">
                    <label htmlFor="children">Trẻ em (0-12 tuổi)</label>
                    <select
                      id="children"
                      name="children"
                      value={form.children}
                      onChange={handleFieldChange}
                    >
                      {[0, 1, 2, 3, 4, 5].map((num) => (
                        <option key={num} value={num}>
                          {num} trẻ
                        </option>
                      ))}
                    </select>
                  </div>
                </div>
              </fieldset>

              <button
                type="submit"
                className="submit-button"
                disabled={isSubmitting}
              >
                {isSubmitting ? "Đang xử lý..." : "Tiếp tục thanh toán →"}
              </button>
            </form>

            {/* Order Summary Sidebar */}
            <aside className="order-summary">
              <h3>Tóm tắt đơn hàng</h3>
              
              {/* Tour Info */}
              <div className="tour-summary-info">
                {(tour?.cover_image_url || mockTour.cover_image_url) && (
                  <img 
                    src={tour?.cover_image_url || mockTour.cover_image_url} 
                    alt={tourName}
                    className="tour-thumbnail"
                  />
                )}
                <div className="tour-summary-text">
                  <h4>{tourName}</h4>
                  <p className="tour-duration">
                    🕐 {tour?.duration_days || mockTour.duration_days || 'N/A'} ngày
                  </p>
                </div>
              </div>

              {/* Price Breakdown */}
              <div className="price-breakdown">
                <div className="breakdown-item">
                  <span className="label">
                    👥 Người lớn ({form.adults})
                  </span>
                  <span className="price">
                    ${(form.adults * adultPrice).toFixed(2)}
                  </span>
                </div>
                {form.children > 0 && (
                  <div className="breakdown-item">
                    <span className="label">
                      👧 Trẻ em ({form.children})
                    </span>
                    <span className="price">
                      ${(form.children * childPrice).toFixed(2)}
                    </span>
                  </div>
                )}
              </div>

              <div className="summary-divider"></div>

              <div className="summary-total">
                <span>Tổng cộng</span>
                <span className="price">${totalPrice.toFixed(2)}</span>
              </div>
              
              <p className="summary-note">
                💡 Trẻ em được giảm 30% so với giá người lớn
              </p>
            </aside>
          </div>
        </div>
      </main>
      <Footer />
    </div>
  );
}
