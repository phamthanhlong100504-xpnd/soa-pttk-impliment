import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import { Home } from "./pages/Home";
import { TourDetail } from "./pages/TourDetail";
import { BookingForm } from "./pages/BookingForm";
import { Payment } from "./pages/Payment";
import { Success } from "./pages/Success";
import { Login } from "./pages/Login";
import "./App.css";

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/tour/:slug" element={<TourDetail />} />
          <Route path="/booking/:slug" element={<BookingForm />} />
          <Route path="/payment" element={<Payment />} />
          <Route path="/success" element={<Success />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}
