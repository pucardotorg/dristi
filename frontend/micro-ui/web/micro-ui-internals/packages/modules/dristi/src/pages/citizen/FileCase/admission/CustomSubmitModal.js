import { Banner, CardLabel } from "@egovernments/digit-ui-react-components";
import React from "react";
import CustomCaseInfoDiv from "./CustomCaseInfoDiv";
import CustomCopyTextDiv from "./CustomCopyTextDiv";

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
  return (
    <div>
      <Banner
        whichSvg={"tick"}
        successful={true}
        message={submitModalInfo?.header}
        headerStyles={{ fontSize: "32px" }}
        style={{ minWidth: "100%", marginTop: "10px" }}
      ></Banner>
      {submitModalInfo?.subHeader && <CardLabel>{t(submitModalInfo?.subHeader)}</CardLabel>}
      {submitModalInfo?.showTable && <CustomCaseInfoDiv data={submitModalInfo?.caseInfo} t={t} />}
      {submitModalInfo?.showCopytext && <CustomCopyTextDiv data={submitModalInfo?.caseInfo} />}
    </div>
  );
}

export default CustomSubmitModal;
