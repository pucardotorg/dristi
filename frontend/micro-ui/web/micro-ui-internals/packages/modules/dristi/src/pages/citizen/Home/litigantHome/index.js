import { Button, InboxSearchComposer } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useHistory } from "react-router-dom";
import { litigantInboxConfig } from "./litigantInboxConfig";
import { useTranslation } from "react-i18next";
import { useRouteMatch } from "react-router-dom/cjs/react-router-dom.min";

const sectionsParentStyle = {
  height: "50%",
  display: "flex",
  flexDirection: "column",
  gridTemplateColumns: "20% 1fr",
  gap: "1rem",
};

function Home() {
  const { t } = useTranslation();
  const history = useHistory();
  const { path } = useRouteMatch();
  return (
    <React.Fragment>
      <div className="home-screen-wrapper" style={{ minHeight: "calc(100vh - 90px)", width: "100%", padding: "30px" }}>
        <div className="header-class">
          <div className="header">{t("CS_YOUR_CASE")}</div>
          <div className="button-field" style={{ width: "50%" }}>
            <Button
              variation={"secondary"}
              className={"secondary-button-selector"}
              label={t("CS_JOIN_CASE")}
              labelClassName={"secondary-label-selector"}
              onButtonClick={() => {}}
            />
            <Button
              className={"tertiary-button-selector"}
              label={t("CS_FILE_CASE")}
              labelClassName={"tertiary-label-selector"}
              onButtonClick={() => {
                history.push("/digit-ui/citizen/dristi/home/file-case");
              }}
            />
          </div>
        </div>
        <div className="inbox-search-wrapper">
          <InboxSearchComposer
            customStyle={sectionsParentStyle}
            configs={litigantInboxConfig}
            additionalConfig={{
              resultsTable: {
                onClickRow: (props) => {
                  console.log(props);
                  history.push(`${path}/file-case/case?caseId=${props?.original?.id}`);
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
