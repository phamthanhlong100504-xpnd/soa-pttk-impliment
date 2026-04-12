import React from "react";
import { useNavigate } from "react-router-dom";
import "../styles/booking-sidebar.css";

export function BookingSidebar({ tour, schedules, onBook }) {
  const [selectedScheduleId, setSelectedScheduleId] = React.useState(
    schedules && schedules.length > 0 ? schedules[0].schedule_id : ""
  );

  const selectedSchedule = schedules?.find(
    (s) => s.schedule_id === selectedScheduleId
  );

  const handleBooking = () => {
    if (selectedSchedule) {
      onBook(selectedSchedule);
    }
  };

  return (
    <aside className="booking-sidebar">
      <div className="booking-sidebar__card">
        {/* Rating */}
        {tour?.rating_score && (
          <div className="booking-sidebar__rating">
            <span className="booking-sidebar__stars">⭐ {tour.rating_score}</span>
            <span className="booking-sidebar__reviews">
              ({tour.rating_count || 0} đánh giá)
            </span>
          </div>
        )}

        {/* Price */}
        <div className="booking-sidebar__price">
          <span className="booking-sidebar__price-label">Giá từ</span>
          <span className="booking-sidebar__price-value">
            {selectedSchedule?.price
              ? new Intl.NumberFormat("vi-VN", {
                  style: "currency",
                  currency: "VND",
                }).format(selectedSchedule.price)
              : "Liên hệ"}
          </span>
          <span className="booking-sidebar__price-unit">/khách</span>
        </div>

        {/* Schedule Select */}
        {schedules && schedules.length > 0 && (
          <div className="booking-sidebar__schedule">
            <label className="booking-sidebar__label">
              Chọn ngày khởi hành
            </label>
            <select
              value={selectedScheduleId}
              onChange={(e) => setSelectedScheduleId(e.target.value)}
              className="booking-sidebar__select"
            >
              {schedules.map((schedule) => (
                <option key={schedule.schedule_id} value={schedule.schedule_id}>
                  {new Date(schedule.start_date).toLocaleDateString("vi-VN")} (
                  {schedule.available_slots || 0} chỗ trống)
                </option>
              ))}
            </select>
          </div>
        )}

        {/* CTA Button */}
        <button
          className="booking-sidebar__btn"
          onClick={handleBooking}
          disabled={!selectedScheduleId}
        >
          Đặt Ngay →
        </button>

        {/* Trust Badge */}
        <div className="booking-sidebar__badge">
          🛡️ Đặt chỗ an toàn & bảo mật
        </div>
      </div>
    </aside>
  );
}
