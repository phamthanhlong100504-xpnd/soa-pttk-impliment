import React from "react";
import "../styles/booking-sidebar.css";

export function BookingSidebar({ tour, schedules, onBook }) {
  const [selectedScheduleId, setSelectedScheduleId] = React.useState(
    schedules && schedules.length > 0 ? schedules[0].id : ""
  );

  const selectedSchedule = schedules?.find((s) => s.id === selectedScheduleId);

  const handleBooking = () => {
    if (selectedSchedule) {
      onBook(selectedSchedule);
    }
  };

  return (
    <aside className="booking-sidebar">
      <div className="booking-sidebar__card">
        {/* Rating */}
        {tour?.averageRating && (
          <div className="booking-sidebar__rating">
            <span className="booking-sidebar__stars">
              ⭐ {Number(tour.averageRating).toFixed(1)}
            </span>
            <span className="booking-sidebar__reviews">
              ({tour.reviewCount || 0} đánh giá)
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
              : tour?.basePrice
              ? new Intl.NumberFormat("vi-VN", {
                  style: "currency",
                  currency: "VND",
                }).format(tour.basePrice)
              : "Liên hệ"}
          </span>
          <span className="booking-sidebar__price-unit">/khách</span>
        </div>

        {/* Schedule Select */}
        {schedules && schedules.length > 0 ? (
          <div className="booking-sidebar__schedule">
            <label className="booking-sidebar__label">Chọn ngày khởi hành</label>
            <select
              value={selectedScheduleId}
              onChange={(e) => setSelectedScheduleId(e.target.value)}
              className="booking-sidebar__select"
            >
              {schedules.map((schedule) => (
                <option key={schedule.id} value={schedule.id}>
                  {new Date(schedule.start_date).toLocaleDateString("vi-VN")}
                  {schedule.max_participants
                    ? ` (${schedule.max_participants} chỗ)`
                    : ""}
                </option>
              ))}
            </select>
          </div>
        ) : (
          <p className="booking-sidebar__no-schedule">Hiện chưa có lịch khởi hành</p>
        )}

        {/* CTA Button */}
        <button
          className="booking-sidebar__btn"
          onClick={handleBooking}
          disabled={!selectedScheduleId || !schedules?.length}
        >
          Đặt Ngay →
        </button>

        <div className="booking-sidebar__badge">🛡️ Đặt chỗ an toàn & bảo mật</div>
      </div>
    </aside>
  );
}
