import React from "react";
import { Button, InboxSearchComposer } from "@egovernments/digit-ui-react-components";
import { useHistory } from "react-router-dom";
import { judgeInboxConfig } from "./JudgeInboxConfig";
import { useTranslation } from "react-i18next";
const sectionsParentStyle = {
  height: "50%",
  display: "flex",
  flexDirection: "column",
  gridTemplateColumns: "20% 1fr",
  gap: "1rem",
};
function JudgeScreen({ path }) {
  const { t } = useTranslation();
  const history = useHistory();

  return (
    <React.Fragment>
      <div className="home-screen-wrapper" style={{ minHeight: "calc(100vh - 90px)", width: "100%", padding: "30px" }}>
        <div className="header-class">
          <div className="header">{t("CS_YOUR_CASE")}</div>
        </div>
        <div className="inbox-search-wrapper">
          <InboxSearchComposer
            customStyle={sectionsParentStyle}
            configs={judgeInboxConfig}
            // additionalConfig={{
            //   resultsTable: {
            //     onClickRow: (props) => {
            //       console.log(props);
            //       history.push(`${path}/admission/info`);
            //     },
            //   },
            // }}
            additionalConfig={{
              resultsTable: {
                onClickRow: (row) => {
                  const searchParams = new URLSearchParams();
                  searchParams.set("caseId", row.original.id);
                  history.push(`/admission/case?${searchParams.toString()}`);
                },
              },
            }}
          ></InboxSearchComposer>
        </div>
      </div>
    </React.Fragment>
  );
}

export default JudgeScreen;
