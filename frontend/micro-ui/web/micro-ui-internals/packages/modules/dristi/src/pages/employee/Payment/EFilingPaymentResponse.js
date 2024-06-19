import { Banner, Card, CardLabel, CardText, CloseSvg, Modal, TextArea } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import Button from "../../../components/Button";
import { useHistory, useLocation } from "react-router-dom/cjs/react-router-dom.min";
import { useToast } from "../../../components/Toast/useToast";
import { useTranslation } from "react-i18next";

const mockSubmitModalInfo = {
  header: "CS_PAYMENT_SUCCESSFUL",
  backButtonText: "Back to Home",
  nextButtonText: "Schedule next hearing",
  isArrow: false,
  showTable: true,
};

const CloseBtn = (props) => {
  return (
    <div onClick={props?.onClick} style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
      <CloseSvg />
    </div>
  );
};

const Heading = (props) => {
  return <h1 className="heading-m">{props.label}</h1>;
};

function EFilingPaymentResponse({ submitModalInfo = mockSubmitModalInfo }) {
  const history = useHistory();
  const { t } = useTranslation();
  const { state } = useLocation();
  debugger;
  return (
    <div className="user-registration">
      <div className="e-filing-payment" style={{ maxHeight: "330px" }}>
        <Banner
          whichSvg={"tick"}
          successful={state?.state?.success ? true : false}
          message={state?.state?.success ? "CS_PAYMENT_SUCCESSFUL" : "CS_PAYMENT_FAILED"}
          headerStyles={{ fontSize: "32px" }}
          style={{ minWidth: "100%", marginTop: "10px" }}
        ></Banner>
        <div className="button-field" style={{ width: "100%" }}>
          <Button
            variation={"secondary"}
            className={"secondary-button-selector"}
            label={t("CS_PRINT_RECEIPT")}
            labelClassName={"secondary-label-selector"}
            onButtonClick={() => {}}
          />
          <Button
            className={"tertiary-button-selector"}
            label={t("CS_GO_TO_HOME")}
            labelClassName={"tertiary-label-selector"}
            onButtonClick={() => {
              history.push(`/${window?.contextPath}/employee/dristi/pending-payment-inbox`);
            }}
          />
        </div>
      </div>
    </div>
  );
}

export default EFilingPaymentResponse;
