import { Banner } from "@egovernments/digit-ui-react-components";
import React from "react";
import Button from "../../../components/Button";
import { useHistory, useLocation } from "react-router-dom/cjs/react-router-dom.min";
import { useTranslation } from "react-i18next";
import CustomCopyTextDiv from "../../../components/CustomCopyTextDiv";

function EFilingPaymentResponse() {
  const history = useHistory();
  const { t } = useTranslation();
  const { state } = useLocation();
  return (
    <div className="user-registration">
      <div className="e-filing-payment" style={{ minHeight: "330px" }}>
        <Banner
          whichSvg={"tick"}
          successful={state?.state?.success ? true : false}
          message={state?.state?.success ? "CS_PAYMENT_SUCCESSFUL" : "CS_PAYMENT_FAILED"}
          headerStyles={{ fontSize: "32px" }}
          style={{ minWidth: "100%", marginTop: "10px" }}
        ></Banner>
        {state?.state?.receiptData && state?.state?.success && (
          <CustomCopyTextDiv
            t={t}
            keyStyle={{ margin: "8px 0px" }}
            valueStyle={{ margin: "8px 0px", fontWeight: 700 }}
            data={state?.state?.receiptData?.caseInfo}
            tableDataClassName={"e-filing-table-data-style"}
            tableValueClassName={"e-filing-table-value-style"}
          />
        )}
        <div className="button-field" style={{ width: "100%", marginTop: 16 }}>
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
