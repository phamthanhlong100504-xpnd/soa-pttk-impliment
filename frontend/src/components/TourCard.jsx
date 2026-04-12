import React from "react";
import "../styles/tour-card.css";

function formatVND(amount) {
  return new Intl.NumberFormat("vi-VN", { style: "currency", currency: "VND" }).format(amount);
}

export function TourCard({ tour, onDetail }) {
  return (
    <div className="tour-card">
      <div className="tour-card__image">
        <img
          src={tour.cover_image_url || "https://via.placeholder.com/300x200?text=Tour"}
          alt={tour.name}
        />
        {tour.averageRating && (
          <div className="tour-card__badge">
            ⭐ {Number(tour.averageRating).toFixed(1)}
            <span>({tour.reviewCount || 0})</span>
          </div>
        )}
      </div>

      <div className="tour-card__content">
        <div className="tour-card__meta">
          <span>🕐 {tour.durationDays || 0} ngày</span>
        </div>

        <h3 className="tour-card__title">{tour.name}</h3>

        {tour.description && (
          <p className="tour-card__desc">
            {tour.description.length > 80
              ? tour.description.slice(0, 80) + "..."
              : tour.description}
          </p>
        )}

        <div className="tour-card__divider"></div>

        <div className="tour-card__footer">
          <div className="tour-card__price">
            <span className="tour-card__price-label">Giá từ</span>
            <span className="tour-card__price-value">
              {tour.basePrice ? formatVND(tour.basePrice) : "Liên hệ"}
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
