import React from "react";
import "../styles/empty-state.css";

export function EmptyState() {
  return (
    <div className="empty-state">
      <div className="empty-state__icon">🔍</div>
      <h3 className="empty-state__title">Không tìm thấy tour nào</h3>
      <p className="empty-state__text">
        Vui lòng thử với các điều kiện tìm kiếm khác hoặc làm mới trang.
      </p>
    </div>
  );
}
