import { CardLabel, CloseSvg, FormComposerV2, LabelFieldPair } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import Button from "./Button";
import InfoCard from "./InfoCard";
import { idProofVerificationConfig } from "../configs/component";
import Modal from "./Modal";

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
  const [{ showModal }, setState] = useState({
    showModal: false,
  });
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
  return (
    <div>
      {inputs?.map((input, index) => {
        let currentValue = (formData && formData[config.key] && formData[config.key][input.name]) || "";
        return (
          <React.Fragment key={index}>
            <LabelFieldPair style={{ width: "100%", display: "flex", alignItem: "center" }}>
              <CardLabel style={{ fontWeight: 700 }} className="card-label-smaller">
                {t(input.label)}
              </CardLabel>
            </LabelFieldPair>
            <div className="button-field" style={{ width: "50%" }}>
              <Button
                variation={"secondary"}
                className={"secondary-button-selector"}
                label={t("VERIFY_AADHAR")}
                labelClassName={"secondary-label-selector"}
              />
              <Button
                className={"tertiary-button-selector"}
                label={t("VERIFY_ID_PROOF")}
                labelClassName={"tertiary-label-selector"}
                onButtonClick={() => {
                  setState((prev) => ({
                    ...prev,
                    showModal: true,
                  }));
                }}
              />
            </div>
            <InfoCard />
            {showModal && (
              <Modal
                headerBarEnd={<CloseBtn onClick={handleCloseModal} isMobileView={true} />}
                // actionCancelLabel={page === 0 ? t("CORE_LOGOUT_CANCEL") : null}
                actionCancelOnSubmit={() => {}}
                actionSaveLabel={t("ADD")}
                actionSaveOnSubmit={() => {}}
                formId="modal-action"
                headerBarMain={<Heading label={t("UPLOAD_ID_PROOF_HEADER")} />}
                submitTextClassName={"verification-button-text-modal"}
              >
                <div>
                  <FormComposerV2
                    config={idProofVerificationConfig}
                    t={t}
                    onSubmit={(props) => {
                      onSubmit(props);
                    }}
                    cardClassName={"form-composer-id-proof-card"}
                    // isDisabled={isDisabled}
                    // defaultValues={{ Terms_Conditions: null }}
                    inline
                    // label={"CS_COMMON_SUBMIT"}
                    headingStyle={{ textAlign: "center" }}
                    cardStyle={{ minWidth: "100%" }}
                    // onFormValueChange={onFormValueChange}
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
