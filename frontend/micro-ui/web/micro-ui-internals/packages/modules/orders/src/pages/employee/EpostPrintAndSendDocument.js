import React, { useState } from "react";
import ApplicationInfoComponent from "../../components/ApplicationInfoComponent";
import DocumentPrintComponent from "../../components/DocumentPrintComponent";
import { FormComposerV2 } from "@egovernments/digit-ui-react-components";
import { printAndSendDocumentsConfig } from "../../configs/EpostFormConfigs";

const EpostPrintAndSendDocument = ({ t, rowData, form, setTempForm, tempForm, infos, documents, links }) => {
  const config = printAndSendDocumentsConfig;

  const onFormValueChange = (setValue, formData, formState) => {
    if (JSON.stringify(tempForm) !== JSON.stringify(formData) && formData) {
      setTempForm(formData);
    }
  };

  return (
    <div className="epost-print-and-send" style={{ width: "720px" }}>
      <ApplicationInfoComponent infos={infos} links={links} />
      <DocumentPrintComponent documents={documents} />
      <hr className="horizontal-line" />
      <h3 className="speed-post-header">{t("Speed Post Details")}</h3>
      <FormComposerV2
        className="form-print-and-summon"
        key={"printAndSendDocuments"}
        config={config}
        onFormValueChange={onFormValueChange}
        defaultValues={{
          barCode: `${rowData?.original?.trackingNumber || ""}`,
          dateofBooking: `${rowData?.original?.bookingDate}`,
        }}
      />
    </div>
  );
};

export default EpostPrintAndSendDocument;
