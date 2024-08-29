import { FormComposerV2 } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";
import { useHistory } from "react-router-dom";
import NextHearingModal from "../../components/NextHearingModal";
import SummaryModal from "../../components/SummaryModal";
import Modal from "@egovernments/digit-ui-module-dristi/src/components/Modal";
import { hearingService } from "../../hooks/services";
import { useTranslation } from "react-i18next";

const AdjournHearing = ({ hearing, updateTranscript, transcriptText, setAdjournHearing, disableTextArea, setTranscriptText }) => {
  const { hearingId } = Digit.Hooks.useQueryParams();
  const [disable, setDisable] = useState(true);
  const [stepper, setStepper] = useState(1);
  const { t } = useTranslation();
  const [reasonFormData, setReasonFormData] = useState({});
  const [transcript, setTranscript] = useState(transcriptText);

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
      body: [
        {
          isMandatory: true,
          type: "dropdown",
          key: "reason",
          label: "PURPOSE_OF_ADJOURNMENT",
          disable: false,
          populators: {
            name: "reason",
            optionsKey: "name",
            error: "CORE_REQUIRED_FIELD_ERROR",
            styles: { maxWidth: "100%" },
            required: true,
            isMandatory: true,
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
            customStyle: { display: "flex", flexDirection: "column", alignItems: "flex-start" },
          },
        },
      ],
    },
  ];

  const handleConfirmationModal = () => {
    setAdjournHearing(false);
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
  const adjournHearing = async (updatedTranscriptText) => {
    try {
      const updatedHearing = structuredClone(hearing);
      updatedHearing.transcript[0] = updatedTranscriptText;
      updatedHearing.workflow = updatedHearing.workflow || {};
      updatedHearing.workflow.action = "CLOSE";
      updatedHearing.additionalDetails = {
        purposeOfAdjournment: {
          reason: reasonFormData.reason.code,
        },
      };
      const response = await hearingService.updateHearings(
        { tenantId: Digit.ULBService.getCurrentTenantId(), hearing: updatedHearing, hearingType: "", status: "" },
        { applicationNumber: "", cnrNumber: "" }
      );
      setTranscriptText(updatedTranscriptText);
      return response;
    } catch (error) {
      console.error("Error Ending hearing:", error);
    }
  };

  return (
    <div>
      {stepper === 1 && (
        <Modal
          popupStyles={{
            width: "40vw",
            minWidth: "600px",
          }}
          headerBarMain={<Heading label={t("ARE_SURE_ADJOURN_HEARING")} />}
          headerBarEnd={
            <h1 style={{ padding: "5px 5x 5x 5x" }}>
              <CloseBtn onClick={handleConfirmationModal} />
            </h1>
          }
          actionSaveLabel={t("ADJOURN_HEARING")}
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
            fieldStyle={{ width: "100%" }}
          ></FormComposerV2>
        </Modal>
      )}
      {stepper === 2 && (
        <SummaryModal
          transcript={transcript}
          setTranscript={setTranscript}
          handleConfirmationModal={handleConfirmationModal}
          hearing={hearing}
          hearingId={hearingId}
          disableTextArea={disableTextArea}
          onSaveSummary={(updatedTranscriptText) => {
            adjournHearing(updatedTranscriptText).then(() => {
              setStepper((stepper) => stepper + 1);
            });
          }}
          onCancel={() => {
            setTranscript(transcriptText);
            setStepper((stepper) => stepper - 1);
          }}
        />
      )}
      {stepper === 3 && (
        <NextHearingModal
          transcript={transcript}
          hearingId={hearingId}
          hearing={hearing}
          stepper={stepper}
          setStepper={setStepper}
          handleConfirmationModal={handleConfirmationModal}
        />
      )}
    </div>
  );
};

export default AdjournHearing;
