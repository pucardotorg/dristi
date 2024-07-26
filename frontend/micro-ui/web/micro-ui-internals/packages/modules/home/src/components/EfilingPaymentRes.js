import { Banner, CardLabel } from "@egovernments/digit-ui-react-components";
import React, { useMemo } from "react";
import { useTranslation } from "react-i18next";
import { useHistory, useLocation } from "react-router-dom/cjs/react-router-dom.min";

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

const mockSubmitModalInfo = {
  header: "CS_PAYMENT_SUCCESSFUL",
  subHeader: "CS_PAYMENT_SUCCESSFUL_SUB_TEXT",
  backButtonText: "Back to Home",
  nextButtonText: "Schedule next hearing",
  isArrow: false,
  showTable: true,
};

function EFilingPaymentRes({ setShowModal, header, subHeader, submitModalInfo = mockSubmitModalInfo, amount = 2000 }) {
  const history = useHistory();
  const location = useLocation();
  const { t } = useTranslation();
  const receiptData = location.state.state.receiptData;
  const userInfo = JSON.parse(window.localStorage.getItem("user-info"));
  const Button = window?.Digit?.ComponentRegistryService?.getComponent("Button");
  const CustomCopyTextDiv = window?.Digit?.ComponentRegistryService?.getComponent("CustomCopyTextDiv");
  const SelectCustomNote = window?.Digit?.ComponentRegistryService?.getComponent("SelectCustomNote");

  const userInfoType = useMemo(() => (userInfo?.type === "CITIZEN" ? "citizen" : "employee"), [userInfo]);
  return (
    <div className=" user-registration">
      <div className="e-filing-payment" style={{ minHeight: "100%", height: "100%" }}>
        <Banner
          whichSvg={"tick"}
          successful={true}
          message={t(submitModalInfo?.header)}
          headerStyles={{ fontSize: "32px" }}
          style={{ minWidth: "100%", marginTop: "10px" }}
        ></Banner>
        {submitModalInfo?.subHeader && <CardLabel className={"e-filing-card-label"}>{t(submitModalInfo?.subHeader)}</CardLabel>}
        {receiptData ? (
          <CustomCopyTextDiv
            t={t}
            keyStyle={{ margin: "8px 0px" }}
            valueStyle={{ margin: "8px 0px", fontWeight: 700 }}
            data={receiptData?.caseInfo}
            tableDataClassName={"e-filing-table-data-style"}
            tableValueClassName={"e-filing-table-value-style"}
          />
        ) : (
          <SelectCustomNote t={t} config={customNoteConfig} />
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
              history.push(`/${window?.contextPath}/${userInfoType}/home/home-pending-task`);
            }}
          />
        </div>
      </div>
    </div>
  );
}

export default EFilingPaymentRes;
