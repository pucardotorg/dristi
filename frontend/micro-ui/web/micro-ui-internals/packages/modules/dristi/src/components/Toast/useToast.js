import React, { useCallback, useContext, useState } from "react";

const ToastContext = React.createContext({
  message: "",
  setMessage: () => {},
  type: "",
  setType: () => {},
});

export function ToastProvider({ children }) {
  const [message, setMessage] = useState(null);
  const [type, setType] = useState(null);

  return <ToastContext.Provider value={{ message, setMessage, type, setType }}>{children}</ToastContext.Provider>;
}

export function useToast() {
  const { message, setMessage, type, setType } = useContext(ToastContext);

  const done = useCallback(() => {
    setMessage(null);
    setType(null);
  }, []);

  const toast = useCallback((message, timeout, type) => {
    setMessage(message);
    setType(type);
    setTimeout(() => done(), timeout);
  }, []);

  const success = useCallback((message, timeout = 3000) => {
    toast(message, timeout, "success");
  }, []);
  
  const error = useCallback((message, timeout = 3000) => {
    toast(message, timeout, "error");
  }, []);

  const info = useCallback((message, timeout = 3000) => {
    toast(message, timeout, "info");
  }, []);

  return {
    toastMessage: message,
    toastType: type,
    success,
    error,
    info,
    closeToast: done,
  };
}
