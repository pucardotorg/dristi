import { TextInput } from "@egovernments/digit-ui-react-components";
import { Button } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useHistory } from "react-router-dom";

const AdvocateJoinCase = () => {
  const history = useHistory();
  const handleNavigate = (path) => {
    const contextPath = window?.contextPath || ""; // Adjust as per your context path logic
    history.push(`/${contextPath}${path}`);
  };

  return (
    <div>
      {/* //TODO: enrich the code entered in text field */}
      Enter 6 digit code to join case
      <TextInput></TextInput>
      <Button
                label={"Proceed"}
                onButtonClick={() => handleNavigate("/employee/cases/advocate-join-success")}
            >
            </Button>
    </div>
  );
};
export default AdvocateJoinCase;
