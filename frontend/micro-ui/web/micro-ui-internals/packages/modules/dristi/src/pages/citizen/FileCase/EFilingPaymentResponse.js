import { Banner, Card, CardLabel, CardText, CloseSvg, Modal, TextArea } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import Button from "../../../components/Button";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";

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
  const [showPaymentModal, setShowPaymentModal] = useState(false);
  const history = useHistory();
  const onCancel = () => {
    setShowPaymentModal(false);
  };
  return (
    <div className=" user-registration">
      <div className="e-filing-payment" style={{ maxHeight: "330px" }}>
        <Banner
          whichSvg={"tick"}
          successful={true}
          message={submitModalInfo?.header}
          headerStyles={{ fontSize: "32px" }}
          style={{ minWidth: "100%", marginTop: "10px" }}
        ></Banner>
        {submitModalInfo?.subHeader && <CardLabel>{submitModalInfo?.subHeader}</CardLabel>}
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
              setShowPaymentModal(true);
              history.push(`/${window?.contextPath}/citizen/dristi/home`);
            }}
          />
        </div>
      </div>
    </div>
  );
}

export default EFilingPaymentResponse;
