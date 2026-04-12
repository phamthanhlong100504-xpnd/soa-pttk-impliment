import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Header } from "../components/Header";
import { Footer } from "../components/Footer";
import { SearchFilter } from "../components/SearchFilter";
import { TourCard } from "../components/TourCard";
import { EmptyState } from "../components/EmptyState";
import "../styles/home.css";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";

const mockTours = [
  {
    slug: "ha-noi-sapa",
    name: "Hà Nội - Sapa 3N2Đ",
    price: 3200000,
    duration_days: 3,
    departures: "Hà Nội",
    rating_score: 4.7,
    rating_count: 128,
    cover_image_url:
      "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=1200&q=80",
  },
  {
    slug: "da-nang-hoi-an",
    name: "Đà Nẵng - Hội An 4N3Đ",
    price: 4500000,
    duration_days: 4,
    departures: "Đà Nẵng",
    rating_score: 4.9,
    rating_count: 256,
    cover_image_url:
      "https://images.unsplash.com/photo-1470770841072-f978cf4d019e?auto=format&fit=crop&w=1200&q=80",
  },
  {
    slug: "phu-quoc",
    name: "Phú Quốc 5N4Đ",
    price: 6800000,
    duration_days: 5,
    departures: "TP.HCM",
    rating_score: 4.6,
    rating_count: 94,
    cover_image_url:
      "https://images.unsplash.com/photo-1500375592092-40eb2168fd21?auto=format&fit=crop&w=1200&q=80",
  },
];

export function Home() {
  const navigate = useNavigate();
  const [tours, setTours] = useState(mockTours);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [searched, setSearched] = useState(false);

  useEffect(() => {
    setTours(mockTours);
  }, []);

  const handleSearch = async (filters) => {
    setLoading(true);
    setError(null);
    setSearched(true);

    try {
      // TODO: Restore API call when backend is ready
      // const params = new URLSearchParams();
      // if (filters.q) params.append("q", filters.q);
      // if (filters.departures) params.append("departures", filters.departures);
      // if (filters.start_date) params.append("start_date", filters.start_date);
      // if (filters.duration_days) params.append("duration_days", filters.duration_days);
      // if (filters.min_price) params.append("min_price", filters.min_price);
      // if (filters.max_price) params.append("max_price", filters.max_price);
      // const url = `${API_BASE_URL}/api/v1/tours?${params.toString()}`;
      // const response = await fetch(url);
      // if (!response.ok) {
      //   throw new Error(`API error: ${response.status}`);
      // }
      // const data = await response.json();
      // setTours(data.data || []);

      const filtered = mockTours.filter((tour) => {
        if (filters.q) {
          const query = filters.q.toLowerCase();
          if (!tour.name.toLowerCase().includes(query)) return false;
        }

        if (filters.departures) {
          if (tour.departures !== filters.departures) return false;
        }

        if (filters.duration_days) {
          if (Number(tour.duration_days) !== Number(filters.duration_days)) {
            return false;
          }
        }

        if (filters.min_price) {
          if (Number(tour.price) < Number(filters.min_price)) return false;
        }

        if (filters.max_price) {
          if (Number(tour.price) > Number(filters.max_price)) return false;
        }

        return true;
      });

      setTours(filtered);
    } catch (err) {
      setError(err.message);
      setTours([]);
    } finally {
      setLoading(false);
    }
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
