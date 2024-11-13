import { Banner, CardLabel } from "@egovernments/digit-ui-react-components";
import React from "react";
import CustomCaseInfoDiv from "./CustomCaseInfoDiv";
import CustomCopyTextDiv from "./CustomCopyTextDiv";

function CustomSubmitModal({ t, setShowModal, header, subHeader, submitModalInfo }) {
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
      <Banner whichSvg={"tick"} successful={true} message={t(submitModalInfo?.header)} headerStyles={{ fontSize: "32px" }} />
      {submitModalInfo?.subHeader && <CardLabel>{t(submitModalInfo?.subHeader)}</CardLabel>}
      {submitModalInfo?.showTable && <CustomCaseInfoDiv data={submitModalInfo?.caseInfo} t={t} />}
      {submitModalInfo?.showCopytext && (
        <CustomCopyTextDiv textWrapperStyle={{ display: "flex", justifyContent: "start" }} data={submitModalInfo?.caseInfo} t={t} />
      )}
    </div>
  );
}

export default CustomSubmitModal;
