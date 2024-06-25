import { Banner, Card, CardLabel, CardText, CloseSvg, Modal, TextArea } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import Button from "../../../components/Button";
import { useHistory, useLocation } from "react-router-dom/cjs/react-router-dom.min";
import CustomCopyTextDiv from "../../../components/CustomCopyTextDiv";

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

function EFilingPaymentResponse({ t, setShowModal, header, subHeader, submitModalInfo = mockSubmitModalInfo, amount = 2000 }) {
  const history = useHistory();
  const location = useLocation();
  const receiptData = location.state.state.receiptData;
  return (
    <div className=" user-registration">
      <div className="e-filing-payment" style={{ minHeight: "330px" }}>
        <Banner
          whichSvg={"tick"}
          successful={true}
          message={submitModalInfo?.header}
          headerStyles={{ fontSize: "32px" }}
          style={{ minWidth: "100%", marginTop: "10px" }}
        ></Banner>
        {receiptData && (
          <CustomCopyTextDiv
            t={t}
            keyStyle={{ margin: "8px 0px" }}
            valueStyle={{ margin: "8px 0px", fontWeight: 700 }}
            data={receiptData?.caseInfo}
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
              history.push(`/${window?.contextPath}/citizen/dristi/home`);
            }}
          />
        </div>
      </div>
    </div>
  );
}

export default EFilingPaymentResponse;
