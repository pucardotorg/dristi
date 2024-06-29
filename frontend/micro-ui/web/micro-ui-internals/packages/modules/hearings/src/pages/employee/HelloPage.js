import React from "react";
import { useState } from "react";
import { Button } from "@egovernments/digit-ui-react-components";
// import SelectSignature from "../../../../dristi/src/components/SelectSignature";

const HelloPage = () => {
  const [isSignatureAdded, setIsSignatureAdded] = useState(false);
  let formData;
  const buttonStyle = {
    backgroundColor: "#ffffff",
    color: "#c4c4c4",
    border: "1px solid #c4c4c4",
    // borderRadius: "4px",
    padding: "10px 20px",
    fontSize: "15px",
    fontWeight: "900",
    cursor: "pointer",
    boxShadow: "none",
    transition: "all 0.3s ease",
  };

  const successStyle = {
    display: "flex",
    alignItems: "center",
    color: "#008000",
    fontSize: "16px",
    fontWeight: "500",
  };

  const iconStyle = {
    width: "20px",
    height: "20px",
    marginRight: "10px",
  };

  const handleClick = () => {
    console.log("clicked");
    setIsSignatureAdded(true);
  };

  const onSelect = () => {
    console.log("selected");
  };

  return (
    <div>
      <p>Hello There! Wish you best of luck.</p>
      {isSignatureAdded ? (
        <div style={successStyle}>
          <svg
            style={iconStyle}
            onClick={() => {
              setIsSignatureAdded(false);
            }}
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 24 24"
            fill="green"
          >
            <path d="M20.285 2.291a1 1 0 0 0-1.577.21L9.396 17.266 5.315 11.57a1 1 0 1 0-1.63 1.182l4.732 7.103a1 1 0 0 0 1.673-.118L20.81 2.787a1 1 0 0 0-.525-1.496z" />
          </svg>
          Signature Added
        </div>
      ) : (
        <Button label="Add Signature" onButtonClick={handleClick} style={buttonStyle} />

        //  <SelectSignature
        //     onSelect={onSelect}
        //  />

        // <Modal
        //   headerBarEnd={<CloseBtn onClick={handleClick} />}
        //   actionCancelOnSubmit={handleClick}
        //   actionSaveLabel={t("Reschedule All Hearings")}
        //   actionSaveOnSubmit={onSelect}
        //   formId="modal-action"
        //   headerBarMain={<Heading label={t("E-Signature (34)")} />}
        //   // className="pre-hearings"
        //   // popupStyles={popUpStyle}
        // >
          
        // </Modal>
      )}
    </div>
  );
};

export default HelloPage;
