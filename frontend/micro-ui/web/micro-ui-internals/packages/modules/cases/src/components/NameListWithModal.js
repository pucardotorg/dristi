import { CloseSvg } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";

const CloseBtn = (props) => {
  return (
    <div onClick={props?.onClick} style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
      <CloseSvg />
    </div>
  );
};
const Heading = (props) => {
  return (
    <div className="evidence-title">
      <h1 className="heading-m">{props.label}</h1>
    </div>
  );
};

const styles = {
  fontFamily: "Roboto",
  fontSize: "16px",
  fontWeight: 700,
  lineHeight: "18.75px",
  textAlign: "left",
  textUnderlinePosition: "from-font",
  textDecorationSkipInk: "none",
  color: "#231F20",
};

const NameListWithModal = ({ t, data, type }) => {
  const [open, setOpen] = useState(false);

  const Modal = window?.Digit?.ComponentRegistryService?.getComponent("Modal");

  const closeModal = () => {
    setOpen(false);
  };

  return (
    <React.Fragment>
      {data?.length > 0 ? (
        <React.Fragment>
          {data?.slice(0, 2)?.map((fullName) => (
            <div className="case-info-value">
              <span>{fullName}</span>
            </div>
          ))}
          {data?.length > 2 && (
            <p
              style={{
                fontFamily: "Roboto",
                fontSize: "12px",
                fontWeight: 300,
                lineHeight: "16px",
                textAlign: "left",
                textDecorationLine: "underline",
                textDecorationStyle: "solid",
                textUnderlinePosition: "from-font",
                textDecorationSkipInk: "none",
                color: "#006FD5",
                margin: "0px",
                cursor: "pointer",
              }}
              onClick={() => setOpen(true)}
            >
              {t("VIEW_ALL_LINK")}
            </p>
          )}
        </React.Fragment>
      ) : (
        <React.Fragment>
          <div className="case-info-value">
            <span>NA</span>
          </div>
        </React.Fragment>
      )}
      {open && (
        <Modal headerBarEnd={<CloseBtn onClick={closeModal} />} formId="modal-action" headerBarMain={<Heading label={t(type)} />}>
          <ul style={{ listStyle: "unset" }}>
            {data?.map((fullName) => (
              <li style={styles}>{fullName}</li>
            ))}
          </ul>
        </Modal>
      )}
    </React.Fragment>
  );
};

export default NameListWithModal;
