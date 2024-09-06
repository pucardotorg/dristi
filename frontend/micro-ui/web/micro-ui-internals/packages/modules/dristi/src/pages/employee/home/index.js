import { InboxSearchComposer } from "@egovernments/digit-ui-react-components";
import React from "react";
import { scrutinyInboxConfig } from "./scrutinyInboxConfig";
import { useHistory } from "react-router-dom";

const sectionsParentStyle = {
  height: "50%",
  display: "flex",
  flexDirection: "column",
  gridTemplateColumns: "20% 1fr",
  gap: "1rem",
};

function Home() {
  const history = useHistory();
  history.push(`/${window?.contextPath}/employee/home/home-pending-task`);
  return (
    <React.Fragment>
      <div className="scrutiny-inbox-table">
        <div className="inbox-search-wrapper">
          <InboxSearchComposer
            customStyle={sectionsParentStyle}
            configs={scrutinyInboxConfig}
            additionalConfig={{
              resultsTable: {
                onClickRow: (row) => {
                  const searchParams = new URLSearchParams();
                  searchParams.set("caseId", row.original.id);
                  history.push(`case?${searchParams.toString()}`);
                },
              },
            }}
          ></InboxSearchComposer>
        </div>
      </div>
    </React.Fragment>
  );
}

export default Home;
