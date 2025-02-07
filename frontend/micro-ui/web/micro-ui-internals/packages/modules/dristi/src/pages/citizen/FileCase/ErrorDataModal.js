import { CloseSvg, Modal } from "@egovernments/digit-ui-react-components";
import React, { useMemo } from "react";

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

function ErrorDataModal({ t, setIsSubmitDisabled, showErrorDataModal, setShowErrorDataModal }) {
  const handleOnClose = () => {
    setShowErrorDataModal({ ...showErrorDataModal, show: false, errorData: [] });
  };

  return (
    <Modal
      headerBarMain={<Heading label={t("PLEASE_ENTER_THESE_REMAINING_DETAILS")} />}
      headerBarEnd={<CloseBtn onClick={handleOnClose} />}
      actionSaveLabel={t("CS_CLOSE")}
      actionSaveOnSubmit={handleOnClose}
      popupModuleActionBarStyles={{ display: "flex", justifyContent: "center" }}
      popUpStyleMain={{ zIndex: "1000" }}
    >
      <div>
        {showErrorDataModal?.errorData?.map((data) => {
          return <h1>{`Complainant ${data?.complainant} : ${data?.errorKeys?.join(", ")}`}</h1>;
        })}
      </div>
    </Modal>
  );
}

export default ErrorDataModal;
