import CustomCopyTextDiv from "@egovernments/digit-ui-module-dristi/src/components/CustomCopyTextDiv";
import { Banner, Button, DownloadIcon } from "@egovernments/digit-ui-react-components";
import React from "react";

const CustomStepperSuccess = ({ closeButtonAction, t, submissionData }) => {
  return (
    <div className="custom-stepper-modal-success" style={{ padding: "0px 20px" }}>
      <Banner
        whichSvg={"tick"}
        successful={true}
        message={t("You have successfully sent summons via email")}
        headerStyles={{ fontSize: "32px" }}
        style={{ minWidth: "100%", marginTop: "32px", marginBottom: "20px" }}
      ></Banner>
      {<p>Relevant party/ parties will be notified about the document(s) issued.</p>}
      {/* {isPaymentDone && <SelectCustomNote t={t} config={customNoteConfig} />} */}
      <CustomCopyTextDiv
        t={t}
        keyStyle={{ margin: "8px 0px" }}
        valueStyle={{ margin: "8px 0px", fontWeight: 700 }}
        data={submissionData}
        tableDataClassName={"e-filing-table-data-style"}
        tableValueClassName={"e-filing-table-value-style"}
      />
      <div className="action-button-success">
        <Button
          className={"selector-button-border"}
          label={t("Download Document")}
          icon={<DownloadIcon />}
          onButtonClick={() => {
            // closeModal();
            // refreshInbox();
          }}
        />
        <Button
          className={"selector-button-primary"}
          label={"Close"}
          onButtonClick={() => {
            closeButtonAction();
          }}
        >
          {/* <RightArrow /> */}
        </Button>
      </div>
    </div>
  );
};

export default CustomStepperSuccess;
