import React, { useState } from "react";
import "../styles/search-filter.css";

export function SearchFilter({ onSearch }) {
  const [filters, setFilters] = useState({
    q: "",
    duration_days: "",
    departures: "",
    start_date: "",
    min_price: "",
    max_price: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFilters((prev) => ({ ...prev, [name]: value }));
  };

  const handleSearch = () => {
    onSearch(filters);
  };

  return (
    <div className="search-filter">
      <div className="search-filter__container">
        <h1 className="search-filter__title">Bạn muốn đi đâu?</h1>

        <div className="search-bar">
          <div className="search-bar__input-group">
            <span className="search-bar__icon">🔍</span>
            <input
              type="text"
              name="q"
              placeholder="Tìm kiếm tour, điểm đến..."
              value={filters.q}
              onChange={handleChange}
              className="search-bar__input"
            />
          </div>

          <select
            name="duration_days"
            value={filters.duration_days}
            onChange={handleChange}
            className="search-bar__select"
          >
            <option value="">⏱️ Tất cả ngày</option>
            <option value="3">3 ngày</option>
            <option value="4">4 ngày</option>
            <option value="5">5 ngày</option>
            <option value="7">7 ngày</option>
          </select>

          <button className="search-bar__btn" onClick={handleSearch}>
            Tìm kiếm
          </button>
        </div>

        <div className="search-filter__advanced">
          <div className="filter-group">
            <label>Điểm đi</label>
            <input
              type="text"
              name="departures"
              placeholder="Nơi khởi hành"
              value={filters.departures}
              onChange={handleChange}
            />
          </div>

          <div className="filter-group">
            <label>Ngày khởi hành</label>
            <input
              type="date"
              name="start_date"
              value={filters.start_date}
              onChange={handleChange}
            />
          </div>

          <div className="filter-group">
            <label>Giá từ (VND)</label>
            <input
              type="number"
              name="min_price"
              placeholder="Tối thiểu"
              value={filters.min_price}
              onChange={handleChange}
            />
          </div>

          <div className="filter-group">
            <label>Giá đến (VND)</label>
            <input
              type="number"
              name="max_price"
              placeholder="Tối đa"
              value={filters.max_price}
              onChange={handleChange}
            />
          </div>
        </div>
      </div>
    </div>
  );
}
