import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { Header } from "../components/Header";
import { Footer } from "../components/Footer";
import { API_BASE_URL } from "../config/api";
import "../styles/my-bookings.css";

function formatVND(amount) {
  return new Intl.NumberFormat("vi-VN", { style: "currency", currency: "VND" }).format(amount);
}

function formatDate(dateStr) {
  if (!dateStr) return "—";
  return new Date(dateStr).toLocaleDateString("vi-VN", {
    year: "numeric",
    month: "long",
    day: "numeric",
  });
}

const STATUS_LABEL = {
  PENDING: "Chờ xử lý",
  CONFIRMED: "Đã xác nhận",
  CANCELLED: "Đã hủy",
};

const STATUS_CLASS = {
  PENDING: "status--pending",
  CONFIRMED: "status--confirmed",
  CANCELLED: "status--cancelled",
};

export function MyBookings() {
  const navigate = useNavigate();
  const { token, user } = useAuth();
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [pdfLoading, setPdfLoading] = useState({});

  const buildTicketPayload = (b) => ({
    bookingId: b.bookingCode || String(b.id),
    accountId: b.accountId || null,
    tourScheduleId: b.tourScheduleId || null,
    tourName: b.tourName || "",
    quantity: b.quantity || 1,
    confirmedSlots: b.quantity || 1,
    totalPrice: b.totalPrice || 0,
    passengers: (b.passengers || []).map((p) => ({ fullName: p.fullName })),
    optionalServices: (b.optionalServices || []).map((s) => ({
      serviceName: s.serviceName,
      quantity: s.quantity,
      priceType: s.priceType,
    })),
  });

  const handleViewPdf = async (b) => {
    setPdfLoading((prev) => ({ ...prev, [b.id]: "view" }));
    try {
      const res = await fetch(`${API_BASE_URL}/api/v1/documents/booking-tickets/show-ticket`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(buildTicketPayload(b)),
      });
      if (!res.ok) throw new Error("Không thể tạo PDF");
      const blob = await res.blob();
      window.open(URL.createObjectURL(blob), "_blank");
    } catch (err) {
      alert(err.message);
    } finally {
      setPdfLoading((prev) => ({ ...prev, [b.id]: null }));
    }
  };

  const handleDownloadPdf = async (b) => {
    setPdfLoading((prev) => ({ ...prev, [b.id]: "download" }));
    try {
      const res = await fetch(`${API_BASE_URL}/api/v1/documents/booking-tickets/file-pdf-ticket`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(buildTicketPayload(b)),
      });
      if (!res.ok) throw new Error("Không thể tải PDF");
      const blob = await res.blob();
      const url = URL.createObjectURL(blob);
      const a = document.createElement("a");
      a.href = url;
      a.download = `Ve_Tour_${b.bookingCode || b.id}.pdf`;
      a.click();
      URL.revokeObjectURL(url);
    } catch (err) {
      alert(err.message);
    } finally {
      setPdfLoading((prev) => ({ ...prev, [b.id]: null }));
    }
  };

  useEffect(() => {
    if (!token) {
      navigate("/login", { state: { from: "/my-bookings" } });
      return;
    }
    const fetchBookings = async () => {
      setLoading(true);
      setError(null);
      try {
        const params = new URLSearchParams();
        if (user?.id) params.append("user_id", user.id);
        const res = await fetch(`${API_BASE_URL}/api/v1/bookings?${params.toString()}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        if (!res.ok) throw new Error(`Lỗi tải danh sách booking (${res.status})`);
        const data = await res.json();
        setBookings(data.data || data.result || []);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };
    fetchBookings();
  }, [token, user, navigate]);

  return (
    <div className="my-bookings-wrapper">
      <Header />
      <main className="my-bookings-main">
        <div className="my-bookings-container">
          <h1 className="my-bookings-title">Lịch sử đặt tour của tôi</h1>

          {loading && <p className="my-bookings-loading">Đang tải...</p>}

          {error && <p className="my-bookings-error">{error}</p>}

          {!loading && !error && bookings.length === 0 && (
            <div className="my-bookings-empty">
              <p>Bạn chưa có booking nào.</p>
              <button className="btn-primary" onClick={() => navigate("/")}>
                Tìm tour ngay
              </button>
            </div>
          )}

          {bookings.length > 0 && (
            <div className="bookings-list">
              {bookings.map((b) => (
                <div key={b.id} className="booking-card">
                  <div className="booking-card__header">
                    <span className="booking-card__code">#{b.id}</span>
                    <span className={`booking-card__status ${STATUS_CLASS[b.status] || ""}`}>
                      {STATUS_LABEL[b.status] || b.status}
                    </span>
                  </div>
                  <div className="booking-card__body">
                    <div className="booking-card__row">
                      <span className="label">Ngày khởi hành</span>
                      <span className="value">{formatDate(b.startDate || b.start_date)}</span>
                    </div>
                    <div className="booking-card__row">
                      <span className="label">Số khách</span>
                      <span className="value">{b.quantity || "—"}</span>
                    </div>
                    {b.totalPrice != null && (
                      <div className="booking-card__row">
                        <span className="label">Tổng tiền</span>
                        <span className="value total">{formatVND(b.totalPrice)}</span>
                      </div>
                    )}
                  </div>
                  {b.bookingStatus === "CONFIRMED" && (
                    <div className="booking-card__footer">
                      <button
                        className="btn-pdf"
                        onClick={() => handleViewPdf(b)}
                        disabled={!!pdfLoading[b.id]}
                      >
                        {pdfLoading[b.id] === "view" ? "Đang tạo..." : "Xem vé PDF"}
                      </button>
                      <button
                        className="btn-pdf btn-pdf--download"
                        onClick={() => handleDownloadPdf(b)}
                        disabled={!!pdfLoading[b.id]}
                      >
                        {pdfLoading[b.id] === "download" ? "Đang tải..." : "Tải vé PDF"}
                      </button>
                    </div>
                  )}
                </div>
              ))}
            </div>
          )}
        </div>
      </main>
      <Footer />
    </div>
  );
}
