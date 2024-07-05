import { InboxSearchComposer } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import { useLocation } from "react-router-dom/cjs/react-router-dom.min";
import { CaseWorkflowState } from "../../../Utils/caseWorkflow";
import { judgeInboxConfig } from "./JudgeInboxConfig";
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
  const location = useLocation();

  return (
    <React.Fragment>
      <div className="home-screen-wrapper" style={{ minHeight: "calc(100vh - 90px)", width: "100%", padding: "30px" }}>
        <div className="header-class" style={{ display: "flex", flexDirection: "column", gap: "8px" }}>
          <div className="header">{t("CS_YOUR_CASE")}</div>
        </div>
        <div className="inbox-search-wrapper">
          <InboxSearchComposer
            customStyle={sectionsParentStyle}
            configs={judgeInboxConfig}
            additionalConfig={{
              resultsTable: {
                onClickRow: (props) => {
                  if (props?.original?.status === CaseWorkflowState.CASE_ADMITTED) {
                    const searchParams = new URLSearchParams();
                    console.log(props.original);
                    // searchParams.set("filingNumber", props.original.filingNumber);
                    // searchParams.set("caseId", props.original.id);
                    // searchParams.set("cnrNumber", props.original.cnrNumber);
                    history.push(
                      `${path}/admitted-case?filingNumber=${props.original.filingNumber}&caseId=${props.original.id}&cnrNumber=${props.original.cnrNumber}&title=${props.original.caseTitle}`
                    );
                  } else if (props?.original?.status === CaseWorkflowState.PENDING_ADMISSION) {
                    const searchParams = new URLSearchParams();
                    console.log(props.original);
                    searchParams.set("filingNumber", props.original.filingNumber);
                    searchParams.set("caseId", props.original.id);
                    history.push(`${path}/admission?${searchParams.toString()}`);
                  } else {
                  }
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
