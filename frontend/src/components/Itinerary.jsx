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
          <div key={day.id || index} className="itinerary__item">
            <div className="itinerary__marker">
              <span className="itinerary__day">Ngày {day.day_number || index + 1}</span>
            </div>
            <div className="itinerary__content">
              <h4 className="itinerary__day-title">
                {day.title || `Ngày ${day.day_number || index + 1}`}
              </h4>
              {day.description && (
                <p className="itinerary__description">{day.description}</p>
              )}
              {day.meals && (
                <p className="itinerary__meals">🍽️ {day.meals}</p>
              )}
              {day.activities && (
                <p className="itinerary__activities">🎯 {day.activities}</p>
              )}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
