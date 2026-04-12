import React from "react";
import "../styles/included-excluded.css";

export function IncludedExcluded({ included, excluded }) {
  return (
    <div className="included-excluded">
      <div className="included-excluded__column">
        <h4 className="included-excluded__title">
          ✅ Bao gồm
        </h4>
        <ul className="included-excluded__list">
          {included && included.length > 0 ? (
            included.map((item, idx) => (
              <li key={idx} className="included-excluded__item">
                {item}
              </li>
            ))
          ) : (
            <li className="included-excluded__item">Không có thông tin</li>
          )}
        </ul>
      </div>

      <div className="included-excluded__column">
        <h4 className="included-excluded__title">
          ❌ Không bao gồm
        </h4>
        <ul className="included-excluded__list">
          {excluded && excluded.length > 0 ? (
            excluded.map((item, idx) => (
              <li key={idx} className="included-excluded__item">
                {item}
              </li>
            ))
          ) : (
            <li className="included-excluded__item">Không có thông tin</li>
          )}
        </ul>
      </div>
    </div>
  );
}
