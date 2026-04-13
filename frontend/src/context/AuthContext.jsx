import React, { createContext, useContext, useState, useCallback } from "react";
import { API_BASE_URL } from "../config/api";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem("access_token"));
  const [user, setUser] = useState(() => {
    const stored = localStorage.getItem("user");
    return stored ? JSON.parse(stored) : null;
  });

  const login = useCallback(async (username, password) => {
    const res = await fetch(`${API_BASE_URL}/api/v1/auth/log-in`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password }),
    });
    const data = await res.json();
    if (!res.ok || data.code !== 200) {
      throw new Error(data.message || "Đăng nhập thất bại");
    }
    const { token: tokenObj, ...userInfo } = data.result;
    localStorage.setItem("access_token", tokenObj.accessToken);
    localStorage.setItem("user", JSON.stringify(userInfo));
    setToken(tokenObj.accessToken);
    setUser(userInfo);
    return userInfo;
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem("access_token");
    localStorage.removeItem("user");
    setToken(null);
    setUser(null);
  }, []);

  return (
    <AuthContext.Provider value={{ token, user, login, logout, isLoggedIn: !!token }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
