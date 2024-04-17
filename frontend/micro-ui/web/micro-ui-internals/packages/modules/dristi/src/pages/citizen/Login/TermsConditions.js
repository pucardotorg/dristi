import { FormComposerV2 } from "@egovernments/digit-ui-react-components";
import React from "react";

function TermsConditions({ config, t }) {
  return (
    <FormComposerV2
      config={config}
      t={t}
      onSubmit={() => {}}
      noBoxShadow
      inline
      label={"Next"}
      onSecondayActionClick={() => {}}
      description={"Description"}
      headingStyle={{ textAlign: "center" }}
      cardStyle={{ minWidth: "100%", paddingRight: "90vh" }}
      className="employeeForgotPassword"
    ></FormComposerV2>
  );
}

export default TermsConditions;
