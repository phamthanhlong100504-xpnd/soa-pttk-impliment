import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { Header } from "../components/Header";
import { Footer } from "../components/Footer";
import { Itinerary } from "../components/Itinerary";
import { BookingSidebar } from "../components/BookingSidebar";
import { API_BASE_URL } from "../config/api";
import "../styles/tour-detail.css";

export function TourDetail() {
  const { slug } = useParams();
  const navigate = useNavigate();
  const { token } = useAuth();

  const [tour, setTour] = useState(null);       // TourResponse
  const [itinerary, setItinerary] = useState([]); // TourItineraryResponse[]
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchTour = async () => {
      setLoading(true);
      setError(null);
      try {
        const headers = { "Content-Type": "application/json" };
        if (token) headers["Authorization"] = `Bearer ${token}`;

        const response = await fetch(`${API_BASE_URL}/api/v1/tours/${slug}`, { headers });

        if (!response.ok) {
          throw new Error(`Tour not found: ${response.status}`);
        }

        const data = await response.json();

        // Response shape: { success, data: { tour: TourResponse, tour_itinerary: [...] } }
        const result = data.data;
        setTour(result.tour);
        setItinerary(result.tour_itinerary || []);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    if (slug) fetchTour();
  }, [slug, token]);

  const handleBook = (schedule) => {
    navigate(`/booking/${slug}`, {
      state: {
        tourSlug: slug,
        schedule,
        tourName: tour?.name,
        tour,
      },
    });
  };

  if (loading) {
    return (
      <div className="tour-detail">
        <Header />
        <div className="tour-detail__loading">Đang tải...</div>
        <Footer />
      </div>
    );
  }

  if (error || !tour) {
    return (
      <div className="tour-detail">
        <Header />
        <div className="tour-detail__error">
          <h2>Tour không tìm thấy</h2>
          <p>{error || "Không thể lấy thông tin tour"}</p>
          <button className="tour-detail__back-btn" onClick={() => navigate("/")}>
            ← Quay lại tìm kiếm
          </button>
        </div>
        <Footer />
      </div>
    );
  }

  // API fields: id, slug, name, description, basePrice, durationDays,
  //             durationNights, averageRating, reviewCount, tour_schedules
  const schedules = tour.tour_schedules || [];

  return (
    <div className="tour-detail">
      <Header />

      {/* Hero Section */}
      <div className="tour-detail__hero">
        <img
          src={tour.cover_image_url || "https://via.placeholder.com/1200x400?text=Tour"}
          alt={tour.name}
          className="tour-detail__hero-image"
        />
        <div className="tour-detail__hero-overlay">
          <button className="tour-detail__back-btn" onClick={() => navigate("/")}>
            ← Quay lại tìm kiếm
          </button>
          <div className="tour-detail__tags">
            {tour.durationDays && (
              <span className="tour-detail__tag tour-detail__tag--duration">
                🕐 {tour.durationDays} ngày {tour.durationNights ? `${tour.durationNights} đêm` : ""}
              </span>
            )}
          </div>
          <h1 className="tour-detail__title">{tour.name}</h1>
        </div>
      </div>

      {/* Main Content */}
      <div className="tour-detail__container">
        <div className="tour-detail__content">
          {/* Description */}
          {tour.description && (
            <section className="tour-detail__section">
              <h2 className="tour-detail__section-title">Điểm nổi bật</h2>
              <p className="tour-detail__description">{tour.description}</p>
            </section>
          )}

          {/* Itinerary */}
          {itinerary.length > 0 && (
            <section className="tour-detail__section">
              <Itinerary itinerary={itinerary} />
            </section>
          )}
        </div>

        {/* Sidebar */}
        <BookingSidebar
          tour={tour}
          schedules={schedules}
          onBook={handleBook}
        />
      </div>

      <Footer />
    </div>
  );
}
