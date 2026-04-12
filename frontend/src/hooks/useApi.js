import { useState, useEffect } from "react";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";

export function useApi(url, options = {}) {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!url) return;

    const fetchData = async () => {
      setLoading(true);
      setError(null);
      try {
        // TODO: Restore API call when backend is ready
        // const response = await fetch(`${API_BASE_URL}${url}`, {
        //   headers: { "Content-Type": "application/json" },
        //   ...options,
        // });
        // if (!response.ok) {
        //   throw new Error(`API error: ${response.status}`);
        // }
        // const result = await response.json();
        // setData(result);

        setData(null);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [url]);

  return { data, loading, error };
}
