import { FormComposerV2 } from "@egovernments/digit-ui-components";
import { Modal } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";
import { useHistory } from "react-router-dom";
import NextHearingModal from "../../components/NextHearingModal";
import SummaryModal from "../../components/SummaryModal";

const AdjournHearing = (props) => {
  const { hearing } = props;
  const { hearingId } = Digit.Hooks.useQueryParams();
  const [disable, setDisable] = useState(true);
  const [stepper, setStepper] = useState(1);
  const [reasonFormData, setReasonFormData] = useState({});

  const history = useHistory();

  const handleNavigate = (path) => {
    const contextPath = window?.contextPath || "";
    history.push(`/${contextPath}${path}`);
  };

  const onSubmit = (data) => {
    setStepper(stepper + 1);
    setDisable(true);
  };

  const config = [
    {
      head: "Purpose of Adjournment",
      body: [
        {
          isMandatory: true,
          type: "dropdown",
          key: "reasons",
          disable: false,
          inline: false,
          populators: {
            name: "reason",
            optionsKey: "name",
            error: "",
            required: true,
            options: [
              {
                code: "Option1",
                name: "Lack of Time",
                isEnabled: true,
              },
              {
                code: "Option2",
                name: "Lack of Witness",
                isEnabled: true,
              },
              {
                code: "Option3",
                name: "Lack of Evidence",
                isEnabled: true,
              },
            ],
            optionsCustomStyle: {
              top: "40px",
            },
          },
        },
      ],
    },
  ];

  const handleConfirmationModal = () => {
    handleNavigate(`/employee/hearings/inside-hearing?hearingId=${hearingId}`);
  };

  const Close = () => (
    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
      <g>
        <path d="M19 6.41L17.59 5L12 10.59L6.41 5L5 6.41L10.59 12L5 17.59L6.41 19L12 13.41L17.59 19L19 17.59L13.41 12L19 6.41Z" fill="#0A0A0A" />
      </g>
    </svg>
  );

  const CloseBtn = (props) => {
    return (
      <div style={{ padding: "10px" }} onClick={props.onClick}>
        <Close />
      </div>
    );
  };

  const Heading = (props) => {
    return <h1 className="heading-m">{props.label}</h1>;
  };
  const onFormValueChange = (setValue, formData, formState) => {
    if (formData?.reason) {
      if (formData.reason !== reasonFormData.reason) {
        setReasonFormData(formData);
      }
      if (formData.reason.code !== "Select a Reason") setDisable(false);
    }
  };

  return (
    <div>
      {stepper === 1 && (
        <Modal
          headerBarMain={<Heading label={"Are you sure you wish to adjourn this hearing?"} />}
          headerBarEnd={<CloseBtn onClick={() => handleNavigate(`/employee/hearings/inside-hearing?hearingId=${hearingId}`)} />}
          actionSaveLabel="Adjourn Hearing"
          actionSaveOnSubmit={onSubmit}
          style={{ marginTop: "5px" }}
          isDisabled={disable}
        >
          <FormComposerV2
            config={config}
            onFormValueChange={onFormValueChange}
            isDisabled={true}
            defaultValues={
              reasonFormData?.reason
                ? {
                    reason: {
                      code: reasonFormData?.reason?.code,
                      name: reasonFormData?.reason?.name,
                      isEnabled: true,
                    },
                  }
                : {
                    reason: {
                      code: "Select a Reason",
                      name: "Select a Reason",
                      isEnabled: true,
                    },
                  }
            }
          ></FormComposerV2>
        </Modal>
      )}
      {stepper === 2 && (
        <SummaryModal handleConfirmationModal={handleConfirmationModal} hearingId={hearingId} stepper={stepper} setStepper={setStepper} />
      )}
      {stepper === 3 && <NextHearingModal hearingId={hearingId} hearing={hearing} stepper={stepper} setStepper={setStepper} />}
    </div>
  );
};

export default AdjournHearing;
