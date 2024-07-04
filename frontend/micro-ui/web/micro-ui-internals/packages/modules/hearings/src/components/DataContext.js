import React, { createContext } from "react";

const DataContext = createContext();

const DataProvider = ({ children }) => {
  const sampleData = {
    hearingDate: "2024-05-09",
    hearingSlot: "10-12",
  };

  return <DataContext.Provider value={sampleData}>{children}</DataContext.Provider>;
};

export { DataProvider, DataContext };
