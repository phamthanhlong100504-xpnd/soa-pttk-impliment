import React from "react";
import "../styles/itinerary.css";

export function Itinerary({ itinerary }) {
  if (!itinerary || itinerary.length === 0) {
    return null;
  }

  return (
    <div className="itinerary">
      <h3 className="itinerary__title">Lịch trình chi tiết</h3>
      <div className="itinerary__timeline">
        {itinerary.map((day, index) => (
          <div key={index} className="itinerary__item">
            <div className="itinerary__marker">
              <span className="itinerary__day">Ngày {day.day_index || index + 1}</span>
            </div>
            <div className="itinerary__content">
              <h4 className="itinerary__day-title">{day.title || `Ngày ${day.day_index || index + 1}`}</h4>
              <p className="itinerary__description">{day.content}</p>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
