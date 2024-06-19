import { Button, Header, InboxSearchComposer } from "@egovernments/digit-ui-react-components";
import React, { useState, useEffect } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";

const AdvocatePayment = () => {
  const history = useHistory();
  const handleNavigate = (path) => {
    const contextPath = window?.contextPath || ""; // Adjust as per your context path logic
    history.push(`/${contextPath}${path}`);
  };

  //TODO: not integerated with any api, placeholder screen

  return (
    <div>
      Payment Description Page
      <Button
      label={"Make payment"}
      onButtonClick={() => handleNavigate("/employee/cases/advocate-join-case")}
      >
      </Button>
    </div>
  );
};
export default AdvocatePayment;
