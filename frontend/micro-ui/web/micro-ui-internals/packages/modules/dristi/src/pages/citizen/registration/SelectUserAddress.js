import { FormComposerV2 } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";

const SelectUserAddress = ({ config, onSelect, t, params }) => {
  const history = useHistory();
  if (!params?.name) {
    history.push("/digit-ui/citizen/dristi/home/login");
  }
  return (
    <React.Fragment>
      <FormComposerV2
        config={config}
        t={t}
        onSubmit={(props) => onSelect(props)}
        noBoxShadow
        inline
        label={"Next"}
        onSecondayActionClick={() => {}}
        headingStyle={{ textAlign: "center" }}
        cardStyle={{ minWidth: "0%", padding: 20, display: "flex", flexDirection: "column" }}
        sectionHeadStyle={{ marginBottom: "20px", fontSize: "40px" }}
        submitInForm
        buttonStyle={{ alignItems: "center" }}
      ></FormComposerV2>
    </React.Fragment>
  );
};

export default SelectUserAddress;
