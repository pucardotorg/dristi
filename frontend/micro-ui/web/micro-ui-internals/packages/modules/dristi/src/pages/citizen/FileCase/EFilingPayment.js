import { Banner, Card, CardLabel, CardText, CloseSvg, Modal, TextArea } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import CustomCopyTextDiv from "./admission/CustomCopyTextDiv";
import CustomCaseInfoDiv from "./admission/CustomCaseInfoDiv";
import Button from "../../../components/Button";
import { InfoCard } from "@egovernments/digit-ui-components";
import { Link } from "react-router-dom/cjs/react-router-dom.min";

const mockSubmitModalInfo = {
  header: "CS_HEADER_FOR_E_FILING_PAYMENT",
  subHeader: "CS_SUBHEADER_TEXT_FOR_E_FILING_PAYMENT",
  caseInfo: [
    {
      key: "Case Number",
      value: "FSM-2019-04-23-898898",
    },
  ],
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

function EFilingPayment({ t, setShowModal, header, subHeader, submitModalInfo = mockSubmitModalInfo, amount = 2000 }) {
  const [showPaymentModal, setShowPaymentModal] = useState(false);
  const onCancel = () => {
    setShowPaymentModal(false);
  };
  return (
    <div className="e-filing-payment">
      <Banner
        whichSvg={"tick"}
        successful={true}
        message={submitModalInfo?.header}
        headerStyles={{ fontSize: "32px" }}
        style={{ minWidth: "100%", marginTop: "10px" }}
      ></Banner>
      {submitModalInfo?.subHeader && <CardLabel>{submitModalInfo?.subHeader}</CardLabel>}
      {submitModalInfo?.showTable && <CustomCaseInfoDiv data={submitModalInfo?.caseInfo} />}
      {submitModalInfo?.showCopytext && <CustomCopyTextDiv data={submitModalInfo?.caseInfo} />}
      <div className="button-field" style={{ width: "50%" }}>
        <Button
          variation={"secondary"}
          className={"secondary-button-selector"}
          label={t("CS_PRINT_CASE_FILE")}
          labelClassName={"secondary-label-selector"}
          onButtonClick={() => {}}
        />
        <Button
          className={"tertiary-button-selector"}
          label={t("CS_MAKE_PAYMENT")}
          labelClassName={"tertiary-label-selector"}
          onButtonClick={() => {
            setShowPaymentModal(true);
          }}
        />
      </div>
      {showPaymentModal && (
        <Modal
          headerBarEnd={<CloseBtn onClick={onCancel} />}
          actionSaveLabel={t("CS_PAY_ONLINE")}
          formId="modal-action"
          headerBarMain={<Heading label={t("CS_PAY_TO_FILE_CASE")} />}
        >
          <div>
            <div>
              {`${t("CS_DUE_PAYMENT")}`}
              <span>Rs {amount}/-.</span>
              {`${t("CS_MANDATORY_STEP_TO_FILE_CASE")}`}
            </div>
            <div></div>
            <div>
              <InfoCard
                variant={"default"}
                label={"CS_COMMON_NOTE"}
                style={{ margin: "16px 0 0 0", backgroundColor: "#ECF3FD" }}
                additionalElements={[<Link>{t("CS_LEARN_MORE")}</Link>]}
                inline
                text={"This is an offline step where you can visit your nearest Court to make the payment. "}
                textStyle={{}}
                className={"adhaar-verification-info-card"}
              />
            </div>
          </div>
        </Modal>
      )}
    </div>
  );
}

export default EFilingPayment;
