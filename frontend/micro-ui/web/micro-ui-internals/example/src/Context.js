import React, { useState } from "react";
 
export const Context = React.createContext();
export const ContextProvider = ({ children }) => {
    const [showModal, setShowModal] = useState(false);
 
    return (
        <Context.Provider value={{ showModal, setShowModal }}>
            {children}
        </Context.Provider>
    );
};