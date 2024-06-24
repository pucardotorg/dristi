import React from "react";
import { Button, InboxSearchComposer } from "@egovernments/digit-ui-react-components";
import { useHistory } from "react-router-dom";
import { judgeInboxConfig } from "./JudgeInboxConfig";
import { useTranslation } from "react-i18next";
import { useLocation } from "react-router-dom/cjs/react-router-dom.min";
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
      <div className="registration-requests">
        <div className="header-class">
          <div className="header">{t("CS_YOUR_CASE")}</div>
        </div>
        <div className="inbox-search-wrapper">
          <InboxSearchComposer
            customStyle={sectionsParentStyle}
            configs={judgeInboxConfig}
            additionalConfig={{
              resultsTable: {
                onClickRow: (props) => {
                  if (props?.original?.status === "CASE_ADMITTED") {
                    history.push(`${location.pathname}/orders/orders-home?caseId=${props.original.filingNumber}&filingNumber=${"kjfkdkfjslj"}`);
                  } else {
                    const searchParams = new URLSearchParams();
                    searchParams.set("caseId", props.original.id);
                    history.push(`${path}/admission?${searchParams.toString()}`);
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
