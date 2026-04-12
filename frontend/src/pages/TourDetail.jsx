import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Header } from "../components/Header";
import { Footer } from "../components/Footer";
import { Itinerary } from "../components/Itinerary";
import { IncludedExcluded } from "../components/IncludedExcluded";
import { BookingSidebar } from "../components/BookingSidebar";
import "../styles/tour-detail.css";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";

export function TourDetail() {
  const { slug } = useParams();
  const navigate = useNavigate();
  const [tour, setTour] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchTour = async () => {
      setLoading(true);
      setError(null);
      try {
        // TODO: Restore API call when backend is ready
        // const url = `${API_BASE_URL}/api/v1/tours/${slug}`;
        // const response = await fetch(url);
        // if (!response.ok) {
        //   throw new Error(`Tour not found: ${response.status}`);
        // }
        // const data = await response.json();
        // setTour(data.data || data);

        const mockTour = {
          slug,
          name: "Hà Nội - Sapa 3N2Đ",
          price: 3200000,
          duration_days: 3,
          departures: "Hà Nội",
          rating_score: 4.7,
          rating_count: 128,
          cover_image_url:
            "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=1600&q=80",
          description:
            "Khám phá Sapa với những thửa ruộng bậc thang tuyệt đẹp, bản làng yên bình và khí hậu mát mẻ quanh năm.",
          itinerary: [
            {
              day_index: 1,
              title: "Hà Nội - Sapa",
              content:
                "Khởi hành từ Hà Nội, di chuyển đến Sapa, nhận phòng và khám phá bản Cát Cát.",
            },
            {
              day_index: 2,
              title: "Fansipan - Bản làng",
              content:
                "Chinh phục Fansipan bằng cáp treo, tham quan bản Tả Van và thưởng thức đặc sản địa phương.",
            },
            {
              day_index: 3,
              title: "Sapa - Hà Nội",
              content:
                "Tham quan chợ Sapa, mua sắm đặc sản và trở về Hà Nội.",
            },
          ],
          included_services: [
            "Xe đưa đón theo lịch trình",
            "Khách sạn 3 sao",
            "Hướng dẫn viên",
            "Bữa ăn theo chương trình",
          ],
          excluded_services: [
            "Chi phí cá nhân",
            "Vé cáp treo Fansipan",
            "Đồ uống ngoài chương trình",
          ],
          schedules: [
            {
              schedule_id: "sch_001",
              start_date: "2026-05-05",
              price: 3200000,
              available_slots: 12,
            },
            {
              schedule_id: "sch_002",
              start_date: "2026-05-12",
              price: 3400000,
              available_slots: 8,
            },
            {
              schedule_id: "sch_003",
              start_date: "2026-05-19",
              price: 3500000,
              available_slots: 5,
            },
          ],
        };

        setTour(mockTour);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    if (slug) {
      fetchTour();
    }
  }, [slug]);

  const handleBook = (schedule) => {
    navigate(`/booking/${slug}`, {
      state: {
        tourSlug: slug,
        schedule: schedule,
        tourName: tour?.name,
        tour: tour,
      },
    });
  };

  const handleBack = () => {
    navigate("/");
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
          <button className="tour-detail__back-btn" onClick={handleBack}>
            ← Quay lại tìm kiếm
          </button>
        </div>
        <Footer />
      </div>
    );
  }

  return (
    <div className="tour-detail">
      <Header />

      {/* Hero Section */}
      <div className="tour-detail__hero">
        <img
          src={tour.cover_image_url || "https://via.placeholder.com/1200x400"}
          alt={tour.name}
          className="tour-detail__hero-image"
        />
        <div className="tour-detail__hero-overlay">
          <button className="tour-detail__back-btn" onClick={handleBack}>
            ← Quay lại tìm kiếm
          </button>
          <div className="tour-detail__tags">
            {tour.duration_days && (
              <span className="tour-detail__tag tour-detail__tag--duration">
                🕐 {tour.duration_days} ngày
              </span>
            )}
            {tour.departures && (
              <span className="tour-detail__tag tour-detail__tag--location">
                📍 Khởi hành: {tour.departures}
              </span>
            )}
          </div>
          <h1 className="tour-detail__title">{tour.name}</h1>
        </div>
      </div>

      {/* Main Content */}
      <div className="tour-detail__container">
        <div className="tour-detail__content">
          {/* Highlights */}
          {tour.description && (
            <section className="tour-detail__section">
              <h2 className="tour-detail__section-title">Điểm nổi bật</h2>
              <p className="tour-detail__description">{tour.description}</p>
            </section>
          )}

          {/* Itinerary */}
          {tour.itinerary && (
            <section className="tour-detail__section">
              <Itinerary itinerary={tour.itinerary} />
            </section>
          )}

          {/* Included/Excluded */}
          {(tour.included_services || tour.excluded_services) && (
            <section className="tour-detail__section">
              <IncludedExcluded
                included={tour.included_services}
                excluded={tour.excluded_services}
              />
            </section>
          )}
        </div>

        {/* Sidebar */}
        <BookingSidebar
          tour={tour}
          schedules={tour.schedules || []}
          onBook={handleBook}
        />
      </div>

      <Footer />
    </div>
  );
}
