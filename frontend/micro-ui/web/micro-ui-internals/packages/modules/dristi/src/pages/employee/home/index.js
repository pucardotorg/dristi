import { InboxSearchComposer } from "@egovernments/digit-ui-react-components";
import React from "react";
import { scrutinyInboxConfig } from "./scrutinyInboxConfig";

const sectionsParentStyle = {
  height: "50%",
  display: "flex",
  flexDirection: "column",
  gridTemplateColumns: "20% 1fr",
  gap: "1rem",
};

function Home() {
  return (
    <React.Fragment>
      <div className="registration-requests">
        <div className="inbox-search-wrapper">
          <InboxSearchComposer customStyle={sectionsParentStyle} configs={scrutinyInboxConfig}></InboxSearchComposer>
        </div>
      </div>
    </React.Fragment>
  );
}

export default Home;
