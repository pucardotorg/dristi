import { Banner, Card, CardLabel, CardText, Modal, TextArea } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import CustomCaseInfoDiv from "./CustomCaseInfoDiv";
import CustomCopyTextDiv from "./CustomCopyTextDiv";

function CustomSubmitModal({ t, setShowModal, header, subHeader, submitModalInfo }) {
  return (
    <div>
      <Banner
        whichSvg={"tick"}
        successful={true}
        message={submitModalInfo?.header}
        headerStyles={{ fontSize: "32px" }}
        style={{ minWidth: "100%", marginTop: "10px" }}
      ></Banner>
      {submitModalInfo?.subHeader && <CardLabel>{submitModalInfo?.subHeader}</CardLabel>}
      {submitModalInfo?.showTable && <CustomCaseInfoDiv data={submitModalInfo?.caseInfo} />}
      {submitModalInfo?.showCopytext && <CustomCopyTextDiv data={submitModalInfo?.caseInfo} t={t} />}
    </div>
  );
}

export default CustomSubmitModal;
