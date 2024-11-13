import React, { useEffect, useState } from "react";
import Modal from "./Modal";
import { CardLabel, CloseSvg, LabelFieldPair, TextInput } from "@egovernments/digit-ui-react-components";
import { FormComposerV2, Toast } from "@egovernments/digit-ui-react-components";
import isEqual from "lodash/isEqual";

const CloseBtn = (props) => {
  return (
    <div onClick={props?.onClick} style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
      <CloseSvg />
    </div>
  );
};

const Heading = (props) => {
  return <h1 className="heading-m">{props.label}</h1>;
};

const UploadDocument = ({ config, t, handleCancelUpload, handleUploadProceed, formUploadData, setData }) => {
  const [isDisable, setIsDisable] = useState(true);
  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
    if (!isEqual(formData, formUploadData)) {
      setData(formData);
    }
  };

  useEffect(() => {
    const { SelectUserTypeComponent } = formUploadData || {};

    if (SelectUserTypeComponent?.doc?.length > 0 && SelectUserTypeComponent?.selectIdType?.code === "EVIDENCE") {
      setIsDisable(true);
    } else {
      setIsDisable(false);
    }
  }, [formUploadData]);

  return (
    <Modal
      headerBarEnd={<CloseBtn onClick={handleCancelUpload} isMobileView={true} />}
      headerBarMain={<Heading label={t("Upload Document")} />}
      actionSaveLabel={t("ADD_SIGNATURE")}
      actionSaveOnSubmit={handleUploadProceed}
      isDisabled={!isDisable}
    >
      <div className="advocate-additional-details upload-id">
        <FormComposerV2
          config={config}
          t={t}
          onFormValueChange={onFormValueChange}
          noBoxShadow
          inline
          defaultValues={formUploadData}
        ></FormComposerV2>
      </div>
    </Modal>
  );
};

export default UploadDocument;
