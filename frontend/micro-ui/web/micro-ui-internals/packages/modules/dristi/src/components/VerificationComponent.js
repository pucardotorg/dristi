import { CardLabel, CloseSvg, FormComposerV2, LabelFieldPair } from "@egovernments/digit-ui-react-components";
import { InfoCard } from "@egovernments/digit-ui-components";
import React, { useCallback, useMemo, useState } from "react";
import Button from "./Button";
import { idProofVerificationConfig } from "../configs/component";
import Modal from "./Modal";
import RenderFileCard from "./RenderFileCard";

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

function VerificationComponent({ t, config, onSelect, formData = {}, errors }) {
  const [{ showModal, verificationType, modalData, isAadharVerified }, setState] = useState({
    showModal: false,
    verificationType: "",
    modalData: {},
    isAadharVerified: false,
  });
  const [isDisabled, setIsDisabled] = useState(false);
  const inputs = useMemo(
    () =>
      config?.populators?.inputs || [
        {
          label: "CS_PIN_LOCATION",
          type: "LocationSearch",
          name: [],
        },
      ],
    [config?.populators?.inputs]
  );

  const handleCloseModal = () => {
    setState((prev) => ({
      ...prev,
      showModal: false,
    }));
  };

  const onFormValueChange = useCallback(
    (setValue, formData, formState) => {
      let isDisabled = false;
      idProofVerificationConfig.forEach((curr) => {
        if (isDisabled) return;
        if (!(curr.body[0].key in formData) || !formData[curr.body[0].key]) {
          return;
        }
        curr.body[0].populators.inputs.forEach((input) => {
          if (isDisabled) return;
          if (Array.isArray(input.name)) return;
          if (
            formData[curr.body[0].key][input.name] &&
            formData[curr.body[0].key][input.name].length > 0 &&
            !["documentUpload", "radioButton"].includes(input.type) &&
            input.validation &&
            !formData[curr.body[0].key][input.name].match(window?.Digit.Utils.getPattern(input.validation.patternType) || input.validation.pattern)
          ) {
            isDisabled = true;
          }
          if (Array.isArray(formData[curr.body[0].key][input.name]) && formData[curr.body[0].key][input.name].length === 0) {
            isDisabled = true;
          }
        });
      });
      if (isDisabled) {
        setIsDisabled(isDisabled);
      } else {
        setIsDisabled(false);
        if (JSON.stringify(formData?.[idProofVerificationConfig?.[0].body[0].key]) !== JSON.stringify(modalData)) {
          setState((prev) => ({
            ...prev,
            modalData: formData?.[idProofVerificationConfig?.[0].body[0].key],
          }));
        }
      }
    },
    [modalData]
  );

  const fileValidator = (file, input) => {
    const maxFileSize = input?.maxFileSize * 1024 * 1024;
    return file.size > maxFileSize ? t(input?.maxFileErrorMessage) : null;
  };

  const handleChange = (file, input, index = Infinity) => {
    setState((prev) => ({
      ...prev,
      showModal: true,
      verificationType: "uploadIdProof",
    }));
  };

  const handleDeleteFile = (input, index) => {
    onSelect(config.key, { ...formData[config.key], [input?.name]: undefined });
  };

  return (
    <div>
      {inputs?.map((input, index) => {
        let currentValue = (formData && formData[config.key] && formData[config.key][input.name]) || "";
        let fileErrors =
          currentValue?.[input.name]?.["ID_Proof"]?.[0]?.[1]?.["file"] &&
          [currentValue?.[input.name]?.["ID_Proof"]?.[0]?.[1]?.["file"]].map((file) =>
            fileValidator(file, idProofVerificationConfig?.[0].body[0]?.populators?.inputs?.[1])
          );
        return (
          <React.Fragment key={index}>
            <LabelFieldPair style={{ width: "100%", display: "flex", alignItem: "center" }}>
              <CardLabel style={{ fontWeight: 700 }} className="card-label-smaller">
                {t(input.label)}
              </CardLabel>
            </LabelFieldPair>
            {!currentValue?.[input.name]?.["ID_Proof"] ? (
              <React.Fragment>
                {!isAadharVerified && (
                  <div className="button-field" style={{ width: "50%" }}>
                    <Button
                      variation={"secondary"}
                      className={"secondary-button-selector"}
                      label={t("VERIFY_AADHAR")}
                      labelClassName={"secondary-label-selector"}
                      onButtonClick={() => {
                        setState((prev) => ({
                          ...prev,
                          isAadharVerified: true,
                        }));
                      }}
                    />
                    <Button
                      className={"tertiary-button-selector"}
                      label={t("VERIFY_ID_PROOF")}
                      labelClassName={"tertiary-label-selector"}
                      onButtonClick={() => {
                        setState((prev) => ({
                          ...prev,
                          showModal: true,
                          verificationType: "uploadIdProof",
                        }));
                      }}
                    />
                  </div>
                )}

                <InfoCard
                  variant={isAadharVerified ? "success" : "default"}
                  label={isAadharVerified ? "CS_AADHAR_VERIFIED" : "CS_COMMON_NOTE"}
                  style={{ margin: "16px 0 0 0", backgroundColor: isAadharVerified ? "#E4F2E4" : "#ECF3FD" }}
                  additionalElements={{}}
                  inline
                  text={
                    isAadharVerified
                      ? "Adhaar verification is instant and can help increase the speed of case filing"
                      : "Your Id was already verified during user registration."
                  }
                  textStyle={{}}
                  className={"adhaar-verification-info-card"}
                />
              </React.Fragment>
            ) : (
              [currentValue?.[input.name]?.["ID_Proof"]?.[0]?.[1]?.["file"]].map((file, index) => (
                <RenderFileCard
                  key={`${input?.name}${index}`}
                  index={index}
                  fileData={file}
                  handleChange={handleChange}
                  handleDeleteFile={handleDeleteFile}
                  t={t}
                  uploadErrorInfo={fileErrors[index]}
                  input={input}
                  isDisabled={true}
                />
              ))
            )}
            {showModal && (
              <Modal
                headerBarEnd={<CloseBtn onClick={handleCloseModal} isMobileView={true} />}
                // actionCancelLabel={page === 0 ? t("CORE_LOGOUT_CANCEL") : null}
                actionCancelOnSubmit={() => { }}
                actionSaveLabel={t("ADD")}
                actionSaveOnSubmit={() => {
                  onSelect(config.key, { ...formData[config.key], [input.name]: { verificationType, [input.name]: modalData } });
                  setState((prev) => ({
                    ...prev,
                    showModal: false,
                    verificationType: "",
                  }));
                }}
                formId="modal-action"
                headerBarMain={<Heading label={t("UPLOAD_ID_PROOF_HEADER")} />}
                submitTextClassName={"verification-button-text-modal"}
                className={"case-types"}
              >
                <div style={{ padding: "16px 24px" }}>
                  <FormComposerV2
                    config={idProofVerificationConfig}
                    t={t}
                    cardClassName={"form-composer-id-proof-card"}
                    inline
                    headingStyle={{ textAlign: "center" }}
                    cardStyle={{ minWidth: "100%" }}
                    onFormValueChange={onFormValueChange}
                  ></FormComposerV2>
                </div>
              </Modal>
            )}
          </React.Fragment>
        );
      })}
    </div>
  );
}

export default VerificationComponent;
