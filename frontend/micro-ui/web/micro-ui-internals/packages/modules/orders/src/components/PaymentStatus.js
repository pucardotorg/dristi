import React, { useState } from "react";
import { Link, useHistory, useLocation } from "react-router-dom";
import { useTranslation } from "react-i18next";
import {
  Banner,
  Card,
  LinkLabel,
  AddFileFilled,
  ArrowLeftWhite,
  ActionBar,
  SubmitBar,
  DownloadBtnCommon,
} from "@egovernments/digit-ui-react-components";
import { Button } from "@egovernments/digit-ui-components";

const buttonStyle = {
  wrapper: { display: "flex" },
  linkLabel: { display: "flex", marginRight: "3rem" },
  arrow: { marginRight: "8px", marginTop: "3px" },
};

const PaymentStatus = () => {
  const { t } = useTranslation();
  const history = useHistory();
  const queryStrings = Digit.Hooks.useQueryParams();
  const [isResponseSuccess, setIsResponseSuccess] = useState(
    queryStrings?.isSuccess === "true" ? true : queryStrings?.isSuccess === "false" ? false : true
  );
  const { state } = useLocation();

  const navigate = (page) => {
    switch (page) {
      case "home": {
        history.push(`/${window.contextPath}/employee`);
      }
    }
  };

  return (
    <Card>
      <Banner
        successful={isResponseSuccess}
        message={isResponseSuccess ? "Payment Successful" : "Payment Failed"}
        info={`${state?.showID ? t("SUBMISSION_ID") : ""}`}
        whichSvg={`${isResponseSuccess ? "tick" : null}`}
      />
      <div>The summons would be sent to the relevant party.</div>
      <Button
        label={"Download Receipt"}
        variation={"secondary"}
        // onClick={() => handleNavigate("/employee/hearings/adjourn-hearing")}
        // style={{ width: "100%" }}
      />
      <DownloadBtnCommon />

      <Button
        label={"Go to Home"}
        variation={"primary"}
        // onClick={handleEndHearingModal}
        // onClick={() => handleNavigate("/employee/hearings/end-hearing")}
        // style={{ width: "100%" }}
      />
      {/* <div style={buttonStyle?.wrapper}>
        <LinkLabel style={buttonStyle?.linkLabel} onClick={() => navigate("home")}>
          <ArrowLeftWhite fill="#F47738" style={buttonStyle?.arrow} />
          {t("CORE_COMMON_GO_TO_HOME")}
        </LinkLabel>
      </div>
      <ActionBar>
        <Link to={`/${window.contextPath}/employee`}>
          <SubmitBar label={t("CORE_COMMON_GO_TO_HOME")} />
        </Link>
      </ActionBar> */}
    </Card>
  );
};

export default PaymentStatus;
