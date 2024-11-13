import { Banner, CardLabel } from "@egovernments/digit-ui-react-components";
import React from "react";
import Button from "../../../components/Button";
import { useHistory, useLocation } from "react-router-dom/cjs/react-router-dom.min";
import { useTranslation } from "react-i18next";
import CustomCopyTextDiv from "../../../components/CustomCopyTextDiv";
import SelectCustomNote from "../../../components/SelectCustomNote";

const customNoteConfig = {
  populators: {
    inputs: [
      {
        infoHeader: "CS_COMMON_NOTE",
        infoText: "PAYMENT_FAILED_NOTE_MSG",
        infoTooltipMessage: "CS_NOTE_TOOLTIP_CASE_TYPE",
      },
    ],
  },
};

function EFilingPaymentResponse() {
  const history = useHistory();
  const { t } = useTranslation();
  const { state } = useLocation();
  return (
    <div className="user-registration">
      <div className="e-filing-payment" style={{ minHeight: "100%", height: "100%" }}>
        <Banner
          whichSvg={"tick"}
          successful={state?.state?.success ? true : false}
          message={state?.state?.success ? t("CS_PAYMENT_SUCCESSFUL") : t("CS_PAYMENT_FAILED")}
          headerStyles={{ fontSize: "32px" }}
          style={{ minWidth: "100%", marginTop: "10px" }}
        ></Banner>
        {state?.state?.success && <CardLabel className={"e-filing-card-label"}>{t("CS_PAYMENT_SUCCESSFUL_SUB_TEXT")}</CardLabel>}
        {state?.state?.receiptData && state?.state?.success ? (
          <CustomCopyTextDiv
            t={t}
            keyStyle={{ margin: "8px 0px" }}
            valueStyle={{ margin: "8px 0px", fontWeight: 700 }}
            data={state?.state?.receiptData?.caseInfo}
            tableDataClassName={"e-filing-table-data-style"}
            tableValueClassName={"e-filing-table-value-style"}
          />
        ) : (
          <SelectCustomNote t={t} config={customNoteConfig} />
        )}
        <div className="button-field" style={{ width: "100%", marginTop: 16 }}>
          {/* <Button
            variation={"secondary"}
            className={"secondary-button-selector"}
            label={t("CS_PRINT_RECEIPT")}
            labelClassName={"secondary-label-selector"}
            onButtonClick={() => {}}
          /> */}
          <Button
            className={"tertiary-button-selector"}
            label={t("CS_GO_TO_HOME")}
            labelClassName={"tertiary-label-selector"}
            onButtonClick={() => {
              history.push(`/${window?.contextPath}/employee/dristi/pending-payment-inbox`);
            }}
            style={{ width: "100%" }}
          />
        </div>
      </div>
    </div>
  );
}

export default EFilingPaymentResponse;
