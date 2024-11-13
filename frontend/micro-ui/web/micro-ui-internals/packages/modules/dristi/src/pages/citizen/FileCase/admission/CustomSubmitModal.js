import { Banner, CardLabel } from "@egovernments/digit-ui-react-components";
import React from "react";
import CustomCaseInfoDiv from "./CustomCaseInfoDiv";
import CustomCopyTextDiv from "../../../../components/CustomCopyTextDiv";

const mockSubmitModalInfo = {
  header: "The case file has been admitted successfully.",
  subHeader: "CASE_UPDATES_SENT_VIA_SMS_MESSAGE.",
  caseInfo: [
    {
      key: "Case Number",
      value: "FSM-2019-04-23-898898",
    },
    {
      key: "Case Category",
      value: "Criminal",
    },
    {
      key: "Case Type",
      value: "NIA S138",
    },
    {
      key: "Court Name",
      value: "Kerala City Criminal Court",
    },
    {
      key: "Submitted on",
      value: "23 Jan 2024",
    },
  ],
  backButtonText: "Back to Home",
  nextButtonText: "Schedule next hearing",
  isArrow: false,
  showTable: true,
};

function CustomSubmitModal({ t, setShowModal, header, subHeader, submitModalInfo = mockSubmitModalInfo }) {
  const CustomCaseInfoDivCustom = window?.Digit?.ComponentRegistryService?.getComponent("CustomCaseInfoDiv");

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        textAlign: "center",
        padding: "20px", // Add padding if needed to make the content visually centered within the modal
      }}
    >
      <Banner whichSvg={"tick"} successful={true} message={submitModalInfo?.header} headerStyles={{ fontSize: "32px" }} />
      {submitModalInfo?.subHeader && <CardLabel>{t(submitModalInfo?.subHeader)}</CardLabel>}
      {submitModalInfo?.showTable && <CustomCaseInfoDiv data={submitModalInfo?.caseInfo} t={t} />}
      {submitModalInfo?.showCopytext && <CustomCopyTextDiv data={submitModalInfo?.caseInfo} t={t} />}
    </div>
  );
}

export default CustomSubmitModal;
