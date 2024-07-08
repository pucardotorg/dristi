import { Banner, CardLabel } from "@egovernments/digit-ui-react-components";
import React from "react";
import CustomCaseInfoDiv from "./CustomCaseInfoDiv";
import CustomCopyTextDiv from "./CustomCopyTextDiv";

function CustomSubmitModal({ t, setShowModal, header, subHeader, submitModalInfo }) {
  return (
    <div>
      <Banner
        whichSvg={"tick"}
        successful={true}
        message={t(submitModalInfo?.header)}
        headerStyles={{ fontSize: "32px" }}
        style={{ minWidth: "100%", marginTop: "10px" }}
      ></Banner>
      {submitModalInfo?.subHeader && <CardLabel>{t(submitModalInfo?.subHeader)}</CardLabel>}
      {submitModalInfo?.showTable && <CustomCaseInfoDiv data={submitModalInfo?.caseInfo} t={t} />}
      {submitModalInfo?.showCopytext && <CustomCopyTextDiv data={submitModalInfo?.caseInfo} t={t} />}
    </div>
  );
}

export default CustomSubmitModal;
