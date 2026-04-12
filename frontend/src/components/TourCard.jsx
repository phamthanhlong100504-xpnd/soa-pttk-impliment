import React from "react";
import "../styles/tour-card.css";

export function TourCard({ tour, onDetail }) {
  return (
    <div className="tour-card">
      <div className="tour-card__image">
        <img
          src={tour.cover_image_url || "https://via.placeholder.com/300x200"}
          alt={tour.name}
        />
        {tour.rating_score && (
          <div className="tour-card__badge">
            ⭐ {tour.rating_score}
            <span>({tour.rating_count || 0})</span>
          </div>
        )}
      </div>

      <div className="tour-card__content">
        <div className="tour-card__meta">
          <span>🕐 {tour.duration_days || 0} ngày</span>
          <span>📍 {tour.departures || "N/A"}</span>
        </div>

        <h3 className="tour-card__title">{tour.name}</h3>

        <div className="tour-card__divider"></div>

        <div className="tour-card__footer">
          <div className="tour-card__price">
            <span className="tour-card__price-label">Giá từ</span>
            <span className="tour-card__price-value">
              {tour.price
                ? new Intl.NumberFormat("vi-VN", {
                    style: "currency",
                    currency: "VND",
                  }).format(tour.price)
                : "Liên hệ"}
            </span>
          </div>

          <button
            className="tour-card__btn"
            onClick={() => onDetail(tour.slug || tour.id)}
          >
            Chi tiết
          </button>
        </div>
      </div>
    </div>
  );
}
