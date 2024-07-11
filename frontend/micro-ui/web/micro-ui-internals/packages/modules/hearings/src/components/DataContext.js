import React, { createContext, useState } from "react";

const DataContext = createContext();

const DataProvider = ({ children }) => {
  const [hearingData, setHearingData] = useState({
    fromDate: "",
    toDate: "",
    slot: "",
  });

  const updateHearingData = (data) => {
    setHearingData(data);
  };

  return <DataContext.Provider value={{ hearingData, updateHearingData }}>{children}</DataContext.Provider>;
};

export { DataProvider, DataContext };
