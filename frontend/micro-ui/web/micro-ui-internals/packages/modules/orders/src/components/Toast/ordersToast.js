import React, { useCallback, useContext, useEffect, useState } from "react";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";

const ToastContext = React.createContext({
  message: "",
  setMessage: () => {},
  type: "",
  setType: () => {},
});

export function ToastProvider({ children }) {
  const [message, setMessage] = useState(null);
  const [type, setType] = useState(null);
  const history = useHistory();
  useEffect(() => {
    const toastInfo = history?.location?.state?.toast;
    const done = () => {
      setMessage(null);
      setType(null);
    };
    if (toastInfo) {
      const { message, type, timeout } = toastInfo;
      setMessage(message);
      setType(type);
      setTimeout(() => done(), timeout);
    }
  }, [history?.location?.state?.toast]);
  return <ToastContext.Provider value={{ message, setMessage, type, setType }}>{children}</ToastContext.Provider>;
}

export function ordersToast() {
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
