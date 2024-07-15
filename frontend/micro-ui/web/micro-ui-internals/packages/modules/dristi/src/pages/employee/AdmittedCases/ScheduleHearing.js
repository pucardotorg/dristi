import React, { useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { modalConfig, scheduleCaseSubmitConfig, selectParticipantConfig } from "../../citizen/FileCase/Config/admissionActionConfig";
import ScheduleAdmission from "../admission/ScheduleAdmission";
import { CloseSvg, Modal } from "@egovernments/digit-ui-react-components";
import AdmissionActionModal from "../admission/AdmissionActionModal";
import { formatDate } from "../../citizen/FileCase/CaseType";
import { DRISTIService } from "../../../services";

const ScheduleHearing = ({ tenantId, setShowModal, caseData, setUpdateCounter, showToast }) => {
  const { t } = useTranslation();
  const [modalInfo, setModalInfo] = useState({ type: "schedule", page: 0 });
  const [selectedChip, setSelectedChip] = useState(null);
  const [scheduleHearingParams, setScheduleHearingParams] = useState({});
  const [submitModalInfo, setSubmitModalInfo] = useState({});

  const updateCaseDetails = async (data = {}) => {
    return DRISTIService.createHearings(
      {
        hearing: {
          tenantId: tenantId,
          filingNumber: [caseData.filingNumber],
          hearingType: data.purpose,
          status: true,
          additionalDetails: {
            participants: Object.values(data.participant)
              .map((val) => Object.keys(val))
              .flat(Infinity)
              .map((name) => {
                return { name: name };
              }),
          },
          startTime: Date.parse(
            `${data.date
              .split(" ")
              .map((date, i) => (i === 0 ? date.slice(0, date.length - 2) : date))
              .join(" ")}`
          ),
          endTime: Date.parse(
            `${data.date
              .split(" ")
              .map((date, i) => (i === 0 ? date.slice(0, date.length - 2) : date))
              .join(" ")}`
          ),
          workflow: {
            action: "CREATE",
            assignes: [],
            comments: "Create new Hearing",
            documents: [{}],
          },
          documents: [],
        },
        tenantId,
      },
      { tenantId: tenantId }
    );
  };

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
        <h3 className="status">{props?.status}</h3>
      </div>
    );
  };

  const setPurposeValue = (value, input) => {
    setScheduleHearingParams({ ...scheduleHearingParams, purpose: value });
  };

  const showCustomDateModal = () => {
    setModalInfo({ ...modalInfo, showDate: true });
  };
  const handleClickDate = (label) => {
    const newSelectedChip = selectedChip === label ? null : label;
    setSelectedChip(newSelectedChip);
    setScheduleHearingParams({
      ...scheduleHearingParams,
      date: newSelectedChip,
    });
  };

  const handleScheduleCase = async (props) => {
    await updateCaseDetails(props).then((res) => {
      setUpdateCounter((prev) => prev + 1);
      setShowModal(false);
      res.responseInfo.status === "successful"
        ? showToast({
            isError: false,
            message: "HEARING_CREATE_SUCCESSFUL",
          })
        : showToast({
            isError: true,
            message: "HEARING_CREATE_UNSUCCESSFUL",
          });
    });
  };

  const complainantFormData = caseData.case.additionalDetails?.complainantDetails?.formdata;
  const respondentFormData = caseData.case.additionalDetails?.respondentDetails?.formdata;

  const updateConfigWithCaseDetails = (config, caseDetails) => {
    const complainantNames = complainantFormData?.map((form) => {
      const firstName = form?.data?.firstName || "";
      const middleName = form?.data?.middleName || "";
      const lastName = form?.data?.lastName || "";
      return `${firstName} ${middleName} ${lastName}`.trim();
    });

    const respondentNames = respondentFormData?.map((form) => {
      const firstName = form?.data?.respondentFirstName || "";
      const lastName = form?.data?.respondentLastName || "";
      return `${firstName} ${lastName}`.trim();
    });

    config.checkBoxes.forEach((checkbox) => {
      if (checkbox.key === "Compliant") {
        checkbox.dependentFields = complainantNames;
      } else if (checkbox.key === "Respondent") {
        checkbox.dependentFields = respondentNames;
      }
    });

    return config;
  };

  const updatedConfig = caseData.case && updateConfigWithCaseDetails(selectParticipantConfig, caseData.case);

  return (
    <React.Fragment>
      <Modal
        headerBarMain={<Heading label={t(modalConfig[2].headModal)} />}
        headerBarEnd={<CloseBtn onClick={() => setShowModal(false)} />}
        hideSubmit={true}
        popupStyles={{ width: "917px" }}
      >
        <AdmissionActionModal
          //   config={modalConfig[2]}
          t={t}
          setShowModal={setShowModal}
          setModalInfo={setModalInfo}
          modalInfo={modalInfo}
          selectedChip={selectedChip}
          setSelectedChip={setSelectedChip}
          showCustomDateModal={showCustomDateModal}
          setPurposeValue={setPurposeValue}
          scheduleHearingParams={scheduleHearingParams}
          setScheduleHearingParam={setScheduleHearingParams}
          submitModalInfo={submitModalInfo}
          handleClickDate={handleClickDate}
          handleScheduleCase={handleScheduleCase}
          handleAdmitCase={() => {}}
          updatedConfig={updatedConfig}
          tenantId={tenantId}
          handleScheduleNextHearing={() => {}}
          disabled={false}
          isCaseAdmitted={true}
        />
      </Modal>
    </React.Fragment>
  );
};

export default ScheduleHearing;
