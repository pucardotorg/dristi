import { CloseSvg, Modal } from "@egovernments/digit-ui-react-components";
import React, { useMemo } from "react";
import { useHistory } from "react-router-dom";

const Heading = (props) => {
  return (
    <h1 className="heading-m" style={{ marginLeft: "47px" }}>
      {props.label}
    </h1>
  );
};

const CloseBtn = (props) => {
  return (
    <div onClick={props?.onClick} style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
      <CloseSvg />
    </div>
  );
};

function ConfirmCaseDetailsModal({ t, setShowConfirmCaseDetailsModal }) {
  const history = useHistory();
  const userInfo = Digit?.UserService?.getUser()?.info;
  const userInfoType = useMemo(() => (userInfo?.type === "CITIZEN" ? "citizen" : "employee"), [userInfo]);
  const handleOnCancel = () => {
    setShowConfirmCaseDetailsModal(false);
    history.push(`/${window?.contextPath}/${userInfoType}/dristi/landing-page`); // redirect to home page
  };
  const complaintSentText = useMemo(() => {
    return (
      <div style={{ display: "flex", justifyContent: "left", marginLeft: "22px", height: "66px" }}>
        <h3>{t("COMPLAINT_SENT_TO_LITIGANT_FOR_ESIGN")}</h3>
      </div>
    );
  }, []);

  return (
    <Modal
      headerBarMain={<Heading label={t("PLEASE_CONFIRM_CASE_DETAILS")} />}
      headerBarEnd={<CloseBtn onClick={handleOnCancel} />}
      actionSaveLabel={t("CS_CLOSE")}
      actionSaveOnSubmit={handleOnCancel}
      children={complaintSentText}
      popupModuleActionBarStyles={{ display: "flex", justifyContent: "center" }}
      popUpStyleMain={{ zIndex: "1000" }}
    ></Modal>
  );
}

export default ConfirmCaseDetailsModal;
