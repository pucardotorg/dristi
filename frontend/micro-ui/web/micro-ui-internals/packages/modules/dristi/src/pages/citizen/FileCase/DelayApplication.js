import React, { useMemo, useState } from "react";
import CustomDragDrop from "../../../components/CustomDragDrop";
import CustomRadioSelector from "../../../components/CustomRadioSelector";
import CustomTextArea from "../../../components/CustomTextArea";
import CustomNote from "../../../components/CustomNote";

function DelayApplication({ t }) {
  const [text, setText] = useState("");
  const [file, setFile] = useState(null);
  const [isBeforeDeadline, setIsBeforeDeadline] = useState(null);

  const fileValidator = (errMsg) => {
    setShowToast({ isError: true, label: t("WBH_BULK_UPLOAD_DOC_VALIDATION_MSG") });
    closeToast();
    setShowBulkUploadModal(false);
  };

  const RadioButtonProps = useMemo(
    () => ({
      options: ["Yes", "No"],
      onSelect: (value) => setIsBeforeDeadline(value),
      selectedOption: ["Yes", "No"].find((i) => i === isBeforeDeadline),
      style: { display: "flex", alignItems: "center", width: "100%", justifyContent: "space-between" },
      radioButtonStyle: { gap: "20px", marginBottom: "0px" },
      selectorStyle: { transform: "scale(0.5)" },
      inputStyle: { marginLeft: "0" },
    }),
    [isBeforeDeadline]
  );

  const handleTextChange = (newText) => {
    setText(newText);
  };

  const handleFileChange = (file) => {
    setFile(file);
  };

  return (
    <div style={{ display: "flex" }}>
      <div style={{ width: "20%", minWidth: "20%" }}> </div>
      <div className="delay-app-parent-container">
        <div className="delay-app-header-div">
          <h1> Delay Application</h1>
          <p>Please provide the necessary details about the cheque(s)</p>
        </div>
        <div className="delay-app-main-div">
          <div className="radio-selector-main-div">
            <CustomRadioSelector t={t} selectionQuestionText={t("CS_DELAY_APPLICATION_WITHIN_TIME_QUESTION")} {...RadioButtonProps} />
          </div>
          {isBeforeDeadline === "No" && (
            <div>
              <CustomTextArea onTextChange={handleTextChange} name={"ReasonText"} rows={"5"} info={"Reason for delay"} className={"textarea-style"} />
              <CustomNote t={t} infoText={"Adding documents such as Delay Condonation Application can help make your case stronger."} />
              <div>
                <CustomDragDrop
                  heading={"Delay Condonation Application"}
                  isOptional={true}
                  note={
                    "extra note for user Adding documents such as Delay Condonation Application can help make your case stronger.Adding documents such as Delay Condonation Application can help make your case stronger."
                  }
                  showInfoTooltip
                  uploadGuidelines={"Upload .pdf or .jpg. Maximum upload size of 50MB"}
                  file={file}
                  t={t}
                  onChange={handleFileChange}
                  fileValidator={fileValidator}
                />
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default DelayApplication;
