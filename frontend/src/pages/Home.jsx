import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { Header } from "../components/Header";
import { Footer } from "../components/Footer";
import { SearchFilter } from "../components/SearchFilter";
import { TourCard } from "../components/TourCard";
import { EmptyState } from "../components/EmptyState";
import { API_BASE_URL } from "../config/api";
import "../styles/home.css";

export function Home() {
  const navigate = useNavigate();
  const { token } = useAuth();
  const [tours, setTours] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [searched, setSearched] = useState(false);

  useEffect(() => {
    fetchTours({});
  }, []);

  const fetchTours = async (filters) => {
    setLoading(true);
    setError(null);
    try {
      const params = new URLSearchParams();
      if (filters.q) params.append("q", filters.q);
      if (filters.departures) params.append("departures", filters.departures);
      if (filters.start_date) params.append("start_date", filters.start_date);
      if (filters.duration_days) params.append("duration_days", filters.duration_days);
      if (filters.min_price) params.append("min_price", filters.min_price);
      if (filters.max_price) params.append("max_price", filters.max_price);
      const url = `${API_BASE_URL}/api/v1/tours?${params.toString()}`;
      const headers = { "Content-Type": "application/json" };
      if (token) headers["Authorization"] = `Bearer ${token}`;
      const response = await fetch(url, { headers });
      if (!response.ok) {
        throw new Error(`API error: ${response.status}`);
      }
      const data = await response.json();
      setTours(data.data || data.result || []);
    } catch (err) {
      setError(err.message);
      setTours([]);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async (filters) => {
    setSearched(true);
    await fetchTours(filters);
  };

  const handleDetail = (slug) => {
    navigate(`/tour/${slug}`);
  };

  return (
    <div className="home">
      <Header />

      <SearchFilter onSearch={handleSearch} />

      <section className="home__content">
        <div className="home__container">
          <div className="home__header">
            <h2>Tour Phổ Biến Nhất</h2>
            {searched && <p>Tìm thấy {tours.length} tour</p>}
          </div>

          {loading && <div className="loading">Đang tải...</div>}

          {error && <div className="error">Lỗi: {error}</div>}

          {searched && tours.length === 0 && !loading && <EmptyState />}

          {tours.length > 0 && (
            <div className="tour-grid">
              {tours.map((tour) => (
                <TourCard
                  key={tour.slug || tour.id}
                  tour={tour}
                  onDetail={handleDetail}
                />
              ))}
            </div>
          )}
        </div>
      </section>

      <Footer />
    </div>
  );
}
